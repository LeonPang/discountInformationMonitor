package com.pangliang.discountInformationMonitor.crawlThread;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pangliang.discountInformationMonitor.Utils.DateUtils;
import com.pangliang.discountInformationMonitor.Utils.UrlUtil;
import com.pangliang.discountInformationMonitor.common.Constants;
import com.pangliang.discountInformationMonitor.common.CrawlQuestStateEnum;
import com.pangliang.discountInformationMonitor.db.po.CrawlQuestPO;
import com.pangliang.discountInformationMonitor.db.po.DiscountInformationPO;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/6/11 0011.
 */
@Deprecated
public class XIANYUMonitor extends Thread
{
    //定时查询是否有对现有关键字的新增修改删除，每个关键字线程都有一个唯一的UUID
    //用这个ID作为KEY
    private Logger logger =  LoggerFactory.getLogger(this.getClass());
    private long crawlQuestID;
    private CrawlQuestPO crawlQuestPO;
    private String url;
    private boolean running = true;
    private Long lastCrawlTime;
    private String jsonStr;
    private int currentPageNo = 1;
    private boolean crawlNextPage = true;
    private List<DiscountInformationPO> discountInformationPOList = new LinkedList<>();
    private DiscountInformationPO discountInformationPO = new DiscountInformationPO();
    private long lastCrawlDate = 0L;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat simpleDateFormatYMD = new SimpleDateFormat("yyyy.MM.dd 00:00:01");
    private Map discountInformationPOMap = new HashMap<Long, List<DiscountInformationPO>>();

    public XIANYUMonitor(long crawlQuestID)
    {
        this.crawlQuestID = crawlQuestID;
    }

    public String getJson(){
//        Connection connect = Jsoup.connect(url);
//        try {
//            Connection.Response response = connect.execute();
//            System.out.println(response.body());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        jsonStr = "";
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)   //socket超时
                .setConnectTimeout(5000)   //connect超时
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

//        创建HttpClientBuilder
//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
//         依次是目标请求地址，端口号,协议类型
//        HttpHost target = new HttpHost(url, 443,"https");
//        HttpHost target = new HttpHost(url);
//         依次是代理地址，代理端口号，协议类型
//        HttpHost proxy = new HttpHost("113.238.13.103", 80);
//        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();


        HttpGet httpGet = new HttpGet(url);
//        httpGet.setConfig(config);
        httpGet.setHeader("Accept", "text/html, */*; q=0.01");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate,sdch");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
//            CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
            System.out.println(String.valueOf(response.getStatusLine().getStatusCode()));
            if(HttpStatus.OK.toString().equals(String.valueOf(response.getStatusLine().getStatusCode()))){
                jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
            }

        } catch (IOException e) {
            logger.error("XIANYUMonitorThread id:" + crawlQuestID + " error:" + e);
            return jsonStr;
        }

        return jsonStr;
    }

    @Override
    public void run()
    {

        while (running)
        {
            crawlQuestPO = Constants.XIANYUCrawlQuestPOHashMap.get(crawlQuestID);
            if(null == crawlQuestPO || null == crawlQuestPO.getState() ||crawlQuestPO.getState() == CrawlQuestStateEnum.STOPPED.type){
                logger.info("ID为" + crawlQuestID + "的XIANYU爬虫任务已销毁 crawlQuestPO：" + crawlQuestPO);
                running = false;
            }else  if (crawlQuestPO.getState() == CrawlQuestStateEnum.SLEEP.type)
            {
                try
                {
                    logger.info("ID为" + crawlQuestID + "的XIANYU爬虫任务已暂停 开始休眠");
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
                    //&divisionId=350500
                    //&start=222&end=22222
                    url = Constants.XYWaterFallURL + currentPageNo + "&_ksTS=" + new Date().getTime() + "_" + (new Random().nextInt(500) + 1) + "&callback=&stype=1&st_edtime=1&q=" + UrlUtil.getURLEncoderString(crawlQuestPO.getSearchContent()) + "&ist=1";
                    getJson();
                    analyzeJson();
                    while (crawlNextPage) {
                        try
                        {
                            logger.info("此处为调用加载信息，注意要休眠不然很快就会被封掉IP 暂定20S");
                            Thread.sleep(20000);
                        }
                        catch (InterruptedException e)
                        {
                        }

                        url = Constants.XYWaterFallURL + ++currentPageNo + "&_ksTS=" + new Date().getTime() + "_" + (new Random().nextInt(500) + 1) + "&callback=&stype=1&st_edtime=1&q=" + UrlUtil.getURLEncoderString(crawlQuestPO.getSearchContent()) + "&ist=1";
                        getJson();
                        analyzeJson();
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
                            logger.error("ID为" + crawlQuestID + "的XIANYU爬虫discountInformationQueue.put error:" + e);
                        }
                    }
                }catch(Exception e){
                    logger.error("ID为" + crawlQuestID + "的XIANYU爬虫处理爬取到的信息时异常 error:" + e);
                }
                try
                {
                    logger.info("ID为" + crawlQuestID + "的XIANYU爬虫本次任务处理完成 开始休眠");
                    Thread.sleep(Constants.XIANYUCrawlsMinSleepTime);
                }
                catch (InterruptedException e)
                {
                }

            }
        }
    }

    private void analyzeJson() throws Exception{
        //每5分钟爬一次
        //第一次默认爬当天0点以后的，所以当发现遇到1天前的停止
        //第二次开始更新了5分钟，所以爬到5分钟之前停止
        if(!"".equals(jsonStr) && !jsonStr.contains("anti_Spider")) {
            JsonParser parse = new JsonParser();  //创建json解析器
            String jsonStrS = jsonStr.replace("\n","").replace("\t","").replace("\r","");
            jsonStrS = jsonStrS.substring(1,jsonStrS.length()-1);
            JsonObject jsonObject = (JsonObject)parse.parse(jsonStrS);
            String numFound = jsonObject.get("numFound").getAsString();
            String currPage = jsonObject.get("currPage").getAsString();
            String totalPage = jsonObject.get("totalPage").getAsString();
            //ershou和idle都是一个集合，我只看idle,每次最多返回20个
//            JsonObject ershouCount = jsonObject.get("ershouCount").getAsJsonObject();
            String idleCount = jsonObject.get("idleCount").getAsString();
//            JsonObject ershou = jsonObject.get("ershou").getAsJsonObject();
            JsonArray idle = jsonObject.get("idle").getAsJsonArray();
            if("0".equals(numFound)){
                crawlNextPage = false;
                lastCrawlDate = new Date().getTime();
                currentPageNo = 1;
                return;
            }
            for (int i = 0; i < idle.size(); i++) {
                JsonObject tmp = idle.get(i).getAsJsonObject();
                JsonObject user = tmp.get("user").getAsJsonObject();
                JsonObject item = tmp.get("item").getAsJsonObject();
                discountInformationPO = new DiscountInformationPO();
                discountInformationPO.setAcquiredDate(new Date().getTime());
                discountInformationPO.setCrawlQuestID(crawlQuestID);
                discountInformationPO.setDesc(item.get("describe").getAsString());
                discountInformationPO.setPrice(item.get("price").getAsString());
                //发布日期的格式是X天/小时/分钟前和2017.05.21，转换成当前时间
                String publishTime = item.get("publishTime").getAsString();
                Date publishDate = new Date();
                if(publishTime.contains("天")){
                    publishDate = DateUtils.addDay(new Date(),Integer.parseInt(publishTime.substring(0,publishTime.indexOf("天"))));
                    System.out.println(simpleDateFormat.format(publishDate));
                }else if(publishTime.contains("小时")){
                    publishDate = DateUtils.addHour(new Date(),Integer.parseInt(publishTime.substring(0,publishTime.indexOf("小时"))));
                    System.out.println(simpleDateFormat.format(publishDate));
                }else if(publishTime.contains("分钟")){
                    publishDate = DateUtils.addMinute(new Date(),Integer.parseInt(publishTime.substring(0,publishTime.indexOf("分钟"))));
                    System.out.println(simpleDateFormat.format(publishDate));
                }else{
                    publishDate = simpleDateFormatYMD.parse(publishTime);
                    System.out.println(simpleDateFormat.format(publishDate));
                }
                discountInformationPO.setPublishDate(publishDate.getTime());
                //TITLE取回来了没展示，在具体页面展示了
                discountInformationPO.setTitle(item.get("title").getAsString().replace("<font color=red>","").replace("</font>",""));
                discountInformationPO.setURL(item.get("itemUrl").getAsString());
                if(discountInformationPO.getPublishDate() <= lastCrawlDate){
                    crawlNextPage = false;
                    break;
                }else{
                    discountInformationPOList.add(discountInformationPO);
                }
            }
            //意味着不会再爬取了,更新上次爬取时间为当前时间,恢复默认查询第一页
            if(!crawlNextPage){
                lastCrawlDate = new Date().getTime();
                currentPageNo = 1;
            }
        }else{
            if(!"".equals(jsonStr) && jsonStr.contains("anti_Spider")){
                logger.info("被封了");
            }
            crawlNextPage = false;
            lastCrawlDate = new Date().getTime();
            currentPageNo = 1;
        }
    }

}


