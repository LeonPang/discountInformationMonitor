package com.pangliang.discountInformationMonitor;

import com.pangliang.discountInformationMonitor.common.Constants;
import com.pangliang.discountInformationMonitor.common.CrawlQuestStateEnum;
import com.pangliang.discountInformationMonitor.crawlThread.SMZDMMonitorThread;
import com.pangliang.discountInformationMonitor.crawlThread.XIANYUMonitor;
import com.pangliang.discountInformationMonitor.db.po.CrawlQuestPO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiscountInformationMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscountInformationMonitorApplication.class, args);
		CrawlQuestPO crawlQuestPO = new CrawlQuestPO();
//		crawlQuestPO.setId(1);
//		crawlQuestPO.setState(CrawlQuestStateEnum.RUNNING.type);
//		crawlQuestPO.setSearchContent("微波炉");
//		Constants.SMZDMCrawlQuestPOHashMap.put(crawlQuestPO.getId(),crawlQuestPO);
//		SMZDMMonitorThread t = new SMZDMMonitorThread(crawlQuestPO.getId());
//		Thread t1=new Thread(t);
//		t1.start();
//		crawlQuestPO = new CrawlQuestPO();
//		crawlQuestPO.setId(2);
//		crawlQuestPO.setState(CrawlQuestStateEnum.RUNNING.type);
//		crawlQuestPO.setSearchContent("手机");
//		Constants.SMZDMCrawlQuestPOHashMap.put(crawlQuestPO.getId(),crawlQuestPO);
//		t = new SMZDMMonitorThread(crawlQuestPO.getId());
//		t1=new Thread(t);
//		t1.start();
//		crawlQuestPO = new CrawlQuestPO();
//		crawlQuestPO.setId(3);
//		crawlQuestPO.setState(CrawlQuestStateEnum.RUNNING.type);
//		crawlQuestPO.setSearchContent("床");
//		Constants.SMZDMCrawlQuestPOHashMap.put(crawlQuestPO.getId(),crawlQuestPO);
//		t = new SMZDMMonitorThread(crawlQuestPO.getId());
//		t1=new Thread(t);
//		t1.start();
//		crawlQuestPO = new CrawlQuestPO();
//		crawlQuestPO.setId(4);
//		crawlQuestPO.setState(CrawlQuestStateEnum.RUNNING.type);
//		crawlQuestPO.setSearchContent("耳机");
//		Constants.SMZDMCrawlQuestPOHashMap.put(crawlQuestPO.getId(),crawlQuestPO);
//		t = new SMZDMMonitorThread(crawlQuestPO.getId());
//		t1=new Thread(t);
//		t1.start();
//		crawlQuestPO = new CrawlQuestPO();
//		crawlQuestPO.setId(5);
//		crawlQuestPO.setState(CrawlQuestStateEnum.RUNNING.type);
//		crawlQuestPO.setSearchContent("键盘");
//		Constants.SMZDMCrawlQuestPOHashMap.put(crawlQuestPO.getId(),crawlQuestPO);
//		t = new SMZDMMonitorThread(crawlQuestPO.getId());
//		t1=new Thread(t);
//		t1.start();
//		crawlQuestPO = new CrawlQuestPO();
//		crawlQuestPO.setId(6);
//		crawlQuestPO.setState(CrawlQuestStateEnum.RUNNING.type);
//		crawlQuestPO.setSearchContent("显卡");
//		Constants.SMZDMCrawlQuestPOHashMap.put(crawlQuestPO.getId(),crawlQuestPO);
//		t = new SMZDMMonitorThread(crawlQuestPO.getId());
//		t1=new Thread(t);
//		t1.start();
//		crawlQuestPO = new CrawlQuestPO();
//		crawlQuestPO.setId(7);
//		crawlQuestPO.setState(CrawlQuestStateEnum.RUNNING.type);
//		crawlQuestPO.setSearchContent("自行车");
//		Constants.SMZDMCrawlQuestPOHashMap.put(crawlQuestPO.getId(),crawlQuestPO);
//		t = new SMZDMMonitorThread(crawlQuestPO.getId());
//		t1=new Thread(t);
//		t1.start();
//		crawlQuestPO = new CrawlQuestPO();
//		crawlQuestPO.setId(8);
//		crawlQuestPO.setState(CrawlQuestStateEnum.RUNNING.type);
//		crawlQuestPO.setSearchContent("玩具");
//		Constants.SMZDMCrawlQuestPOHashMap.put(crawlQuestPO.getId(),crawlQuestPO);
//		t = new SMZDMMonitorThread(crawlQuestPO.getId());
//		t1=new Thread(t);
//		t1.start();
//		crawlQuestPO = new CrawlQuestPO();
//		crawlQuestPO.setId(9);
//		crawlQuestPO.setState(CrawlQuestStateEnum.RUNNING.type);
//		crawlQuestPO.setSearchContent("乐器");
//		Constants.SMZDMCrawlQuestPOHashMap.put(crawlQuestPO.getId(),crawlQuestPO);
//		t = new SMZDMMonitorThread(crawlQuestPO.getId());
//		t1=new Thread(t);
//		t1.start();
//		crawlQuestPO = new CrawlQuestPO();
//		crawlQuestPO.setId(10);
//		crawlQuestPO.setState(CrawlQuestStateEnum.RUNNING.type);
//		crawlQuestPO.setSearchContent("食品");
//		Constants.SMZDMCrawlQuestPOHashMap.put(crawlQuestPO.getId(),crawlQuestPO);
//		t = new SMZDMMonitorThread(crawlQuestPO.getId());
//		t1=new Thread(t);
//		t1.start();

		//测试咸鱼
		crawlQuestPO.setId(1);
		crawlQuestPO.setState(CrawlQuestStateEnum.RUNNING.type);
		crawlQuestPO.setSearchContent("微波炉");
		Constants.XIANYUCrawlQuestPOHashMap.put(crawlQuestPO.getId(),crawlQuestPO);
		XIANYUMonitor t = new XIANYUMonitor(crawlQuestPO.getId());
		Thread t1=new Thread(t);
		t1.start();
	}
}
