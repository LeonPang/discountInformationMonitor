package com.pangliang.discountInformationMonitor.db.po;

import com.pangliang.discountInformationMonitor.common.Constants;
import com.pangliang.discountInformationMonitor.common.CrawlQuestStateEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

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
    //商品类型
    private String searchContent;

    //价格区间等等

    //频道，地区，商城等等筛选信息先不做 后面补充



    //搜索源 CommoditySourceEnum 当都有时会在两个集合中都生成任务
    private int commoditySource;
    //
    private Date createTime;
    //
    private Date lastUpdateTime;
    //监控间隔 不能小于Constants.crawlsMinSleepTime
    private int interval = Constants.crawlsMinSleepTime;
    //状态 CrawlQuestStateEnum
    private Integer state = CrawlQuestStateEnum.RUNNING.type;

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

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public int getCommoditySource() {
        return commoditySource;
    }

    public void setCommoditySource(int commoditySource) {
        this.commoditySource = commoditySource;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
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
