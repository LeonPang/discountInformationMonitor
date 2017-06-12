package com.pangliang.discountInformationMonitor.db.po;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Administrator on 2017/6/11 0011.
 */
@Entity
public class EmailInfoPO {
    //自身ID
    @Id
    @GeneratedValue
    private long id;
    //发送邮箱
    private String sendEmail;
    //第三方码
    private String privateCode;
    //接收邮箱，可配置多个用 ; 分隔 第一个为接收人其他为抄送人
    private String receiveEmails;
}
