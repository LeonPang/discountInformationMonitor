package com.pangliang.discountInformationMonitor;

import com.pangliang.discountInformationMonitor.common.Constants;
import com.pangliang.discountInformationMonitor.crawlThread.SMZDMMonitorThread;
import com.pangliang.discountInformationMonitor.db.po.CrawlQuestPO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiscountInformationMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscountInformationMonitorApplication.class, args);

		CrawlQuestPO crawlQuestPO = new CrawlQuestPO();
		crawlQuestPO.setId(1);
		Constants.SMZDMCrawlQuestPOHashMap.put(1L,crawlQuestPO);
		SMZDMMonitorThread t = new SMZDMMonitorThread(crawlQuestPO.getId());

		Thread t1=new Thread(t);
		t1.start();
	}
}
