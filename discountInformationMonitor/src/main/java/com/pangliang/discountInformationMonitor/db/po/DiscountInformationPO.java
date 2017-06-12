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
public class DiscountInformationPO {

    //自身ID
    @Id
    @GeneratedValue
    private long id;
    //所属爬虫任务ID 这个任务内容内容爬取到结果调用显示和发送邮件之前都会去库里取最新的过来
    private int crawlQuestID;
    //商品在SMZDM或者咸鱼的URL
    private int URL;
    //获取到的时间
    private String acquiredtime;
    //
    private String price;



    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
