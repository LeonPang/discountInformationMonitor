package com.pangliang.discountInformationMonitor.common;

/**
 * Created by Administrator on 2017/6/11 0011.
 */
public enum CommoditySourceEnum {
    ALL(0,"同时监控SMZDM和咸鱼"),
    SMZDM(1,"只监控SMZDM"),
    XIANYU(2,"只监控咸鱼");

    private int type;
    private String desc;

    private CommoditySourceEnum(int type,String desc){
        this.type = type;
        this.desc = desc;
    }

}
