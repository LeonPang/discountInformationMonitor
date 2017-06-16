package com.pangliang.discountInformationMonitor.crawlThread;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 庞亮 on 2017/6/16.
 */
public class JD618Thread extends Thread
{
    private Logger logger =  LoggerFactory.getLogger(this.getClass());
    private boolean running = true;
    private String url;
    private String refer;
    private String cookie;
    //yyyy-MM-dd HH:mm:ss
    private String time;
    private String name;

    public JD618Thread(String url,String refer,String cookie,String time,String name){
        this.url = url;
        this.refer = refer;
        this.cookie = cookie;
        this.time = time;
        this.name = name;

    }

    @Override
    public void run()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while (running)
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(time.equals(simpleDateFormat.format(new Date()))){
                for(int i = 0; i < 10; i ++){
                    RequestConfig requestConfig = RequestConfig.custom()
                            .setSocketTimeout(5000)   //socket超时
                            .setConnectTimeout(5000)   //connect超时
                            .build();
                    CloseableHttpClient httpClient = HttpClients.custom()
                            .setDefaultRequestConfig(requestConfig)
                            .build();
                    HttpGet httpGet = new HttpGet(url);
                    httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                    httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch, br");
                    httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
                    httpGet.setHeader("Connection", "keep-alive");
                    httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
                    httpGet.setHeader("Cookie",cookie);
                    httpGet.setHeader("Referer",refer);
                    httpGet.setHeader("Host","coupon.jd.com");
                    httpGet.setHeader("Upgrade-Insecure-Requests","1");
                    try {
                        CloseableHttpResponse response = httpClient.execute(httpGet);
                        if(HttpStatus.OK.toString().equals(String.valueOf(response.getStatusLine().getStatusCode()))){
                            logger.info(EntityUtils.toString(response.getEntity(), "utf-8"));
                        }

                    } catch (IOException e) {
                        logger.error("error:" + e);
                    }
                }


                running = false;
                logger.info(name + "运行结束");
            }

        }
    }


}
