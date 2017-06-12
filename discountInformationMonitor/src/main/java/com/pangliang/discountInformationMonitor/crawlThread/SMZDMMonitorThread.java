package com.pangliang.discountInformationMonitor.crawlThread;

/**
 * Created by Administrator on 2017/6/11 0011.
 */
import com.pangliang.discountInformationMonitor.common.Constants;
import com.pangliang.discountInformationMonitor.common.CrawlQuestStateEnum;
import com.pangliang.discountInformationMonitor.db.po.CrawlQuestPO;
import com.pangliang.discountInformationMonitor.db.repository.CrawlQuestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private boolean running = true;

    @Override
    public void run()
    {
        while (running)
        {
            crawlQuestPO = Constants.crawlQuestPOHashMap.get(crawlQuestID);
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
                //1爬取  用哪种方式呢？不追求效率，所以用调用浏览器内核的方式？


                //2如果成功对结果处理

                //如果有需要发送的，首先入库，状态为未发送，这样重启时候邮件线程先去这里处理，打包放到消息队列，入库也放到
                //阻塞队列处理线程中去


            }
        }
    }
}



