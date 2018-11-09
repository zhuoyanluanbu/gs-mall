package com.gs.mall.order.service;

import com.gs.common.result.ResponseResult;
import com.gs.mall.order.po.WorkOrder;
import com.gs.mall.user.po.User;

/**
 * 工单退款处理支付接口
 * Created by chenchuan on 2017/8/30
 */
public interface WorkOrderPayService {
    /**
     * 退款支付
     *
     * @param workOrder
     * @return
     */
    ResponseResult refundTrade(WorkOrder workOrder, User operator);

}
