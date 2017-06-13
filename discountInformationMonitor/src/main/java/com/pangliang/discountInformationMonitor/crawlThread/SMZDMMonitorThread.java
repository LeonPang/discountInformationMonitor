package com.pangliang.discountInformationMonitor.crawlThread;

/**
 * Created by Administrator on 2017/6/11 0011.
 */

import com.pangliang.discountInformationMonitor.common.Constants;
import com.pangliang.discountInformationMonitor.common.CrawlQuestStateEnum;
import com.pangliang.discountInformationMonitor.db.po.CrawlQuestPO;
import org.apache.http.HttpEntity;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SMZDMMonitorThread  extends Thread
{
    //定时查询是否有对现有关键字的新增修改删除，每个关键字线程都有一个唯一的UUID
    //用这个ID作为KEY
    private Logger logger =  LoggerFactory.getLogger(this.getClass());
    private long crawlQuestID;
    private CrawlQuestPO crawlQuestPO;
    public SMZDMMonitorThread(long crawlQuestID)
    {
       this.crawlQuestID = crawlQuestID;
    }
//    private HttpClient httpClient;
    private String url;
    private boolean running = true;

    @Override
    public void run()
    {
        while (running)
        {
            crawlQuestPO = Constants.SMZDMCrawlQuestPOHashMap.get(crawlQuestID);
            if(null == crawlQuestPO || null == crawlQuestPO.getState() ||crawlQuestPO.getState() == CrawlQuestStateEnum.STOPPED.type){
                logger.info("ID为" + crawlQuestID + "的爬虫任务已销毁 crawlQuestPO：" + crawlQuestPO);
                running = false;
            }else  if (crawlQuestPO.getState() == CrawlQuestStateEnum.SLEEP.type)
            {
                try
                {
                    logger.info("ID为" + crawlQuestID + "的爬虫任务已暂停 开始休眠");
                    Thread.sleep(Constants.crawlsMinSleepTime);
                }
                catch (InterruptedException e)
                {
                }
            }else if (crawlQuestPO.getState() == CrawlQuestStateEnum.RUNNING.type)
            {
                //1爬取
//                if(null == httpClient){
//                    httpClient = new HttpClient();
//                    // 设置HttpClient的连接管理对象，设置 HTTP连接超时5s
//                    httpClient.getHttpConnectionManager().getParams()
//                            .setConnectionTimeout(5000);
//                }
                //2拼凑URL
                url = Constants.SMZDMURL + "?c=home&s=手机";
//                url = Constants.SMZDMURL + "?c=home&s=IBM";
                String html = "";
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
                }

                if(!"".equals(html)) {
                    Document doc = Jsoup.parse(html);
                    Elements items = doc.select("#feed-main-list li");
                    if(null != items && items.size() > 0){
                        for (Element item : items) {
                            System.out.println(item.select("a.feed-nowrap").attr("title"));
                            System.out.println(item.select("a.feed-nowrap").attr("href"));
                            System.out.println(item.select("div.z-highlight").html());

                        }
                    }

                    //2如果成功对结果处理

                    //如果有需要发送的，首先入库，状态为未发送，这样重启时候邮件线程先去这里处理，打包放到消息队列，入库也放到
                    //阻塞队列处理线程中去

                }
            }
        }
    }
    private static String readResponse(HttpEntity resEntity, String charset) {
        StringBuffer res = new StringBuffer();
        BufferedReader reader = null;
        try {
            if (resEntity == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(
                    resEntity.getContent(), charset));
            String line = null;

            while ((line = reader.readLine()) != null) {
                res.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
        return res.toString();
    }
}



