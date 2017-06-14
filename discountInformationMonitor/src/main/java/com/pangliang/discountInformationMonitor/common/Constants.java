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
    public static final ConcurrentMap<Long,CrawlQuestPO> SMZDMCrawlQuestPOHashMap = new ConcurrentHashMap<>();
    //供每个任务的处理线程操作
    public static final ConcurrentMap<Long,CrawlQuestPO> XIANYUCrawlQuestPOHashMap = new ConcurrentHashMap<>();
    //爬取到优惠信息的阻塞队列,大小不限制
    public static final BlockingQueue<Map<Long,List<DiscountInformationPO>>> discountInformationQueue = new LinkedBlockingQueue<Map<Long,List<DiscountInformationPO>>>();

    public static final String SMZDMURL = "http://search.smzdm.com/";//http://search.smzdm.com/?c=home&s=%E6%89%8B%E6%9C%BA
    public static final String XYURL = "https://s.2.taobao.com/list/list.htm?spm=2007.1000337.6.2.WJ2qSw";//https://s.2.taobao.com/list/list.htm?q=%CA%D6%BB%FA&search_type=item&app=shopsearch
    //注意这个wp参数，很奇怪，第一页会有2 3，第二页是5,6，第三页是8，9
    //每页加载后会有一个div.pagination 显示有多少页
    //没值时返回这个
    //jsonp316({ "numFound":85,	"currPage":5,	"totalPage":5,	"ershouCount":0,	"idleCount":0,	"ershou":[	], "idle":[	] })
    //他也是默认这个页标下会有2个链接，滚动时候触发，然后看取回的内容
    //jsonp167({
    //"numFound":144013,	"currPage":2,	"totalPage":7201,	"ershouCount":0,	"idleCount":20,	"ershou":[		],
    //每次返回的这个信息会告诉你一共有多少页，不需要再调用页面了，调用这个即可！
    //这个返回的实际比页面显示的全
    //https://s.2.taobao.com/list/waterfall/waterfall.htm?wp=6&_ksTS=1497411857220_134&callback=jsonp135&stype=1&st_trust=1&q=px130&ist=1
    //https://s.2.taobao.com/list/waterfall/waterfall.htm?wp=5&_ksTS=1497411903046_166&callback=jsonp167&stype=1&st_trust=1&page=2&q=PX130&ist=1
    //callback=jsonp111111111111111111111 数字随意给页面用的
    //时间戳除了当前时间还要给个随机数
    //STYPE之类目前看都没啥用
    //callback= 空 这样去掉前后的()就是标准JSON了
    public static final String  XYWaterFallURL = "https://s.2.taobao.com/list/waterfall/waterfall.htm?wp=";

}
