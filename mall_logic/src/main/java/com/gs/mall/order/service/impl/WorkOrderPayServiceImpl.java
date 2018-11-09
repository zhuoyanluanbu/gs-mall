package com.gs.mall.order.service.impl;

import com.gs.common.openapi.config.AppSecreConfig;

import com.gs.common.openapi.service.PayApiService;
import com.gs.common.result.ResponseResult;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.order.po.WorkOrder;
import com.gs.mall.order.service.WorkOrderPayService;
import com.gs.mall.user.po.User;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Service;

/**
 * 工单退款支付实现
 * Created by chenchuan on 2017/8/30
 */
@Service("workOrderPayService")
public class WorkOrderPayServiceImpl implements WorkOrderPayService {

    private static final Logger logger = Logger.getLogger(WorkOrderPayServiceImpl.class);

    @Override
    public ResponseResult refundTrade(WorkOrder workOrder, User operator) {
        ResponseResult result = ResponseResult.successInstance();
        try {
/*            Map<String, String> biz = new HashMap<>();
            String out_trade_no = workOrder.getOrderId();
            biz.put("out_trade_no", out_trade_no);  //订单支付退款，传支付订单的订单号
            biz.put("refund_amount", String.valueOf(workOrder.getAmount()));//退款金额
            biz.put("refund_reason", "自营商城正常退款");
            biz.put("out_request_no", workOrder.getWoId());//工单号作为多次退款的唯一标识
            biz.put("operator_id", operator.getOperatorId());*/
            AppSecreConfig config = AppSecreConfig.build(Constant.appID, Constant.appSecretKey, Constant.appPrivateSecret,Constant.openPublicKey);
            result = PayApiService.refundTrade(config,  workOrder.getOrderId(), workOrder.getWoId(), workOrder.getAmount(), operator.getOperatorId());
        } catch (Exception e) {
            logger.error("退款支付失败", e);
            result.setCode(-1);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
