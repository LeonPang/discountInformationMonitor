package com.pangliang.discountInformationMonitor.messageHandler;

import com.pangliang.discountInformationMonitor.common.Constants;
import com.pangliang.discountInformationMonitor.db.po.DiscountInformationPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/11 0011.
 */
public class MessageHandlerImpl {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());
    private Map<Long,List<DiscountInformationPO>> discountInformationMap = new HashMap<>();


}
