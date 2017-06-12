package com.pangliang.discountInformationMonitor.common;

/**
 * Created by Administrator on 2017/6/11 0011.
 */
public class Constants {

    //一个用于多个线程共享操作的MAP，KEY是每个搜索的UUID，值是一个字符串

    //另一个控制线程状态的UUID的MAP

    //发送邮件监控线程休眠时间
    public static int sendEmailThreadSleepTime = 300000;

    //爬虫爬取间隔，不能小于默认值300000毫秒
    public static int crawlsMinSleepTime = 300000;



}
