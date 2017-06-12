package com.pangliang.discountInformationMonitor.common;

import com.pangliang.discountInformationMonitor.db.po.CrawlQuestPO;
import com.pangliang.discountInformationMonitor.db.po.DiscountInformationPO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/6/11 0011.
 */
public class Constants {

    //一个用于多个线程共享操作的MAP，KEY是每个搜索的UUID，值是一个字符串

    //另一个控制线程状态的UUID的MAP

    //发送邮件监控线程休眠时间
    //用阻塞队列做
//    public static int sendEmailThreadSleepTime = 300000;

    //爬虫爬取间隔，不能小于默认值300000毫秒
    public static int crawlsMinSleepTime = 300000;

    //目前的爬虫信息，运行中，暂停和销毁状态的都在这里，开机从库里查一次，以后每次成功保存更新到数据库后更新此MAP对应爬虫任务
    //供每个任务的处理线程操作
    public static final ConcurrentMap<Long,CrawlQuestPO> crawlQuestPOHashMap = new ConcurrentHashMap<>();
    //爬取到优惠信息的阻塞队列,大小不限制
    public static final BlockingQueue<Map<Long,List<DiscountInformationPO>>> discountInformationQueue = new LinkedBlockingQueue<Map<Long,List<DiscountInformationPO>>>();


}
