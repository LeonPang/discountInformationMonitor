package com.pangliang.discountInformationMonitor.crawlThread;

/**
 * Created by Administrator on 2017/6/11 0011.
 */

import com.pangliang.discountInformationMonitor.common.Constants;
import com.pangliang.discountInformationMonitor.common.CrawlQuestStateEnum;
import com.pangliang.discountInformationMonitor.db.po.CrawlQuestPO;
import com.pangliang.discountInformationMonitor.db.po.DiscountInformationPO;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SMZDMMonitorThread  extends Thread
{
    //定时查询是否有对现有关键字的新增修改删除，每个关键字线程都有一个唯一的UUID
    //用这个ID作为KEY
    private Logger logger =  LoggerFactory.getLogger(this.getClass());
    private long crawlQuestID;
    private CrawlQuestPO crawlQuestPO;
    private String url;
    private boolean running = true;
    private Long lastCrawlTime;
    private String html;
    private int currentPageNo = 1;
    private boolean crawlNextPage = true;
    private List<DiscountInformationPO> discountInformationPOList = new LinkedList<>();
    private DiscountInformationPO discountInformationPO = new DiscountInformationPO();
    private long lastCrawlDate = 0L;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Map discountInformationPOMap = new HashMap<Long, List<DiscountInformationPO>>();

    public SMZDMMonitorThread(long crawlQuestID)
    {
        this.crawlQuestID = crawlQuestID;
    }

    public String getHtml(){
        html = "";
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)   //socket超时
                .setConnectTimeout(5000)   //connect超时
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "text/html, */*; q=0.01");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate,sdch");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if(HttpStatus.OK.toString().equals(String.valueOf(response.getStatusLine().getStatusCode()))){
                html = EntityUtils.toString(response.getEntity(), "utf-8");
            }

        } catch (IOException e) {
            logger.error("SMZDMMonitorThread id:" + crawlQuestID + " error:" + e);
            return html;
        }

        return html;
    }

    @Override
    public void run()
    {

        while (running)
        {
            crawlQuestPO = Constants.SMZDMCrawlQuestPOHashMap.get(crawlQuestID);
            if(null == crawlQuestPO || null == crawlQuestPO.getState() ||crawlQuestPO.getState() == CrawlQuestStateEnum.STOPPED.type){
                logger.info("ID为" + crawlQuestID + "的SMZDM爬虫任务已销毁 crawlQuestPO：" + crawlQuestPO);
                running = false;
            }else  if (crawlQuestPO.getState() == CrawlQuestStateEnum.SLEEP.type)
            {
                try
                {
                    logger.info("ID为" + crawlQuestID + "的SMZDM爬虫任务已暂停 开始休眠");
                    Thread.sleep(Constants.crawlsMinSleepTime);
                }
                catch (InterruptedException e)
                {
                }
            }else if (crawlQuestPO.getState() == CrawlQuestStateEnum.RUNNING.type)
            {
                currentPageNo = 1;
                try {
                    //如果是第一次爬则获取今天00:00后数据
                    if(lastCrawlDate == 0){
                        lastCrawlDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()).substring(0,11) + "00:00").getTime();
                    }
                    url = Constants.SMZDMURL + "?c=home&s="+ crawlQuestPO.getSearchContent() +"&order=time&p=" + currentPageNo;
                    getHtml();
                    analyzeHtml();
                    while (crawlNextPage) {
                        url = Constants.SMZDMURL + "?c=home&s="+ crawlQuestPO.getSearchContent() +"&order=time&p=" + ++currentPageNo;
                        getHtml();
                        analyzeHtml();
                    }
                    //如果有需要发送的，首先入库，状态为未发送，这样重启时候邮件线程先去这里处理，打包放到消息队列，入库也放到
                    //阻塞队列处理线程中去
                    if(discountInformationPOList.size() > 0){

                        try {
                            discountInformationPOMap.put(crawlQuestID, discountInformationPOList);
                            Constants.discountInformationQueue.put(discountInformationPOMap);
                            discountInformationPOList.clear();
                            discountInformationPOMap.clear();
                        } catch (InterruptedException e) {
                            logger.error("ID为" + crawlQuestID + "的爬虫discountInformationQueue.put error:" + e);
                        }
                    }
                }catch(Exception e){
                    logger.error("ID为" + crawlQuestID + "的SMZDM爬虫处理爬取到的信息时异常 error:" + e);
                }
                try
                {
                    logger.info("ID为" + crawlQuestID + "的SMZDM爬虫本次任务处理完成 开始休眠");
                    Thread.sleep(Constants.crawlsMinSleepTime);
                }
                catch (InterruptedException e)
                {
                }

            }
        }
    }

    private void analyzeHtml() throws Exception{
        if(!"".equals(html)) {
            Document doc = Jsoup.parse(html);
            Elements items = doc.select("#feed-main-list li");
            //没有这个内容则表示没有相关优惠信息
            //默认是按照时间顺序的，每次获取时间后与上次时间比较，只有比上次时间新的才有效，到小于等于上次时间就停止查询了
            if (null != items && items.size() > 0) {
                for (Element item : items) {
                    String shopName = item.select("span.feed-block-extras span").text();
                    item.select("span.feed-block-extras span").remove();
                    String time = item.select("span.feed-block-extras").text().trim();
                    //日期格式为2016-04-01 表示今年以前
                    long tempDate = 0L;
                    if (time.length() == 10) {
                        tempDate = simpleDateFormat.parse(item.select("span.feed-block-extras").text().trim() + " 00:00").getTime();

                    }else if (time.length() == 11) {
                        //日期格式为06-13 17:43 表示今年内非今天
                        tempDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()).substring(0,5) + time).getTime();
                    }else if (time.length() == 5) {
                        //日期格式为17:43 表示今天
                        tempDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()).substring(0,11) + time).getTime();
                    }
                    if(tempDate <= lastCrawlDate){
                        crawlNextPage = false;
                        break;
                    }
                    //只关心 发现优惠 国内优惠 海淘优惠三种
                    //其实日期判断有没有 -  有几个 - 即可判断
                    if(!"".equals(item.select("span.search-faxian-mark").text()) || !"".equals(item.select("span.search-guonei-mark").text()) || !"".equals(item.select("span.search-haitao-mark").text())){
                        discountInformationPO = new DiscountInformationPO();
                        discountInformationPO.setTitle(item.select("a.feed-nowrap").attr("title"));
                        discountInformationPO.setURL(item.select("a.feed-nowrap").attr("href"));
                        discountInformationPO.setAcquiredDate(new Date().getTime());
                        discountInformationPO.setPrice(item.select("div.z-highlight").text());
                        discountInformationPO.setCrawlQuestID(crawlQuestID);
                        discountInformationPO.setDesc(item.select("div.feed-block-descripe").text());
                        discountInformationPO.setShopName(shopName);
                        //日期格式为2016-04-01 表示今年以前
                        if (item.select("span.feed-block-extras").text().trim().length() == 10) {
                            discountInformationPO.setPublishDate(simpleDateFormat.parse(item.select("span.feed-block-extras").text().trim() + " 00:00:01").getTime());

                        }else if (item.select("span.feed-block-extras").text().trim().length() == 11) {
                            //日期格式为06-13 17:43 表示今年内非今天
                            discountInformationPO.setPublishDate(simpleDateFormat.parse(simpleDateFormat.format(new Date()).substring(0,5) + item.select("span.feed-block-extras").text().trim()).getTime());
                        }else{
                            //日期格式为17:43 表示今天
                            discountInformationPO.setPublishDate(simpleDateFormat.parse(simpleDateFormat.format(new Date()).substring(0,11) + item.select("span.feed-block-extras").text().trim()).getTime());
                        }
                        logger.info("ID为" + crawlQuestID + "的SMZDM爬虫本次任务获取到一条有效信息：" + discountInformationPO);
                        discountInformationPOList.add(discountInformationPO);
                    }
                }
                //意味着不会再爬取了,更新上次爬取时间为当前时间,恢复默认查询第一页
                if(!crawlNextPage){
                    lastCrawlDate = new Date().getTime();
                    currentPageNo = 1;
                }
            }
        }
    }

}



