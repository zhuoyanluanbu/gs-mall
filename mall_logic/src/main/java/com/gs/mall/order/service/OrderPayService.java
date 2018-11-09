package com.gs.mall.order.service;



import com.gs.common.result.ResponseResult;
import com.gs.mall.order.po.Order;

import java.util.Map;

/**
 * 订单支付服务接口
 * Created by huangyp on 2017/8/23.
 */
public interface OrderPayService {

    /**
     * 创建交易
     * @param order
     * @return
     */
    ResponseResult createTrade(Order order);


    /**
     * 关闭交易
     * @param orderId
     * @param tradeNo
     * @return
     */
    ResponseResult closeTrade(String orderId, String tradeNo);

    /**
     * 查询交易
     * @param orderId
     * @param tradeNo
     * @return
     */
    ResponseResult queryTrade(String orderId, String tradeNo);

    /**
     * 交易回调验签
     * @param params
     * @return
     */
    ResponseResult tradeCallbackCheckSign(Map<String, String> params);

}
