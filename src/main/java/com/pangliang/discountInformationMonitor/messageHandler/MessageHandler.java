package com.pangliang.discountInformationMonitor.messageHandler;

/**
 * Created by Administrator on 2017/6/11 0011.
 */
public interface MessageHandler {

    /**
     * 1保存查询到的优惠信息到数据库，按批存储，按批推送到邮件系统
     * 2将保存成功的信息存入邮件系统（会返回保存成功的ID，这个ID也给邮件系统，因为这是要更新的）
     * 因为邮件是按照批次发的，所以消息也是按批存储的
     */
    void handleMessage();
}
