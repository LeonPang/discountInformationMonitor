package com.pangliang.discountInformationMonitor.common;

/**
 * Created by Administrator on 2017/6/11 0011.
 */
public enum CrawlQuestStateEnum {
    RUNNING(1,"工作"),
    SLEEP(2,"休眠"),
    STOPPED(3,"注销");

    public int type;
    public String desc;

    private CrawlQuestStateEnum(int type,String desc){
        this.type = type;
        this.desc = desc;
    }


}
