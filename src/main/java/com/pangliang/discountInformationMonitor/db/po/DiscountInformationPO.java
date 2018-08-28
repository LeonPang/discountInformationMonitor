package com.pangliang.discountInformationMonitor.db.po;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Administrator on 2017/6/11 0011.
 */
//@Entity
public class DiscountInformationPO {

//    //自身ID
//    @Id
//    @GeneratedValue
//    private long id;
    //所属爬虫任务ID 这个任务内容内容爬取到结果调用显示和发送邮件之前都会去库里取最新的过来
    private long crawlQuestID;
    //商品在SMZDM或者咸鱼的URL
    private String URL;
    //发布时间
    private Long publishDate;
    //爬取时间
    private Long acquiredDate;
    //价格
    private String price;
    //描述
    private String desc;
    //标题
    private String title;
    //卖场
    private String shopName;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public long getCrawlQuestID() {
        return crawlQuestID;
    }

    public void setCrawlQuestID(long crawlQuestID) {
        this.crawlQuestID = crawlQuestID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Long getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Long publishDate) {
        this.publishDate = publishDate;
    }

    public Long getAcquiredDate() {
        return acquiredDate;
    }

    public void setAcquiredDate(Long acquiredDate) {
        this.acquiredDate = acquiredDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
