package com.pangliang.discountInformationMonitor.db.po;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
    private Integer state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityModel() {
        return commodityModel;
    }

    public void setCommodityModel(String commodityModel) {
        this.commodityModel = commodityModel;
    }

    public String getCommodityBrand() {
        return commodityBrand;
    }

    public void setCommodityBrand(String commodityBrand) {
        this.commodityBrand = commodityBrand;
    }

    public int getCommoditySource() {
        return commoditySource;
    }

    public void setCommoditySource(int commoditySource) {
        this.commoditySource = commoditySource;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(int lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
