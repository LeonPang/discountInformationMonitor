package com.pangliang.discountInformationMonitor.db.po;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Administrator on 2017/6/11 0011.
 */
@Entity
public class CrawlQuestPO {
    //每个任务一个独立ID
    @Id
    @GeneratedValue
    private long id;
    //商品
    private String commodityName;
    //商品型号
    private String commodityModel;
    //商品品牌
    private String commodityBrand;
    //搜索源 CommoditySourceEnum
    private int commoditySource;
    //
    private int createTime;
    //
    private int lastUpdateTime;
    //监控间隔 不能小于Constants.crawlsMinSleepTime
    private int interval;
    //状态 CrawlQuestStateEnum
    private int state;

}
