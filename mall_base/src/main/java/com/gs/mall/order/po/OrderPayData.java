package com.gs.mall.order.po;

/**
 * 订单支付数据
 * Created by huangyp on 2017/8/30.
 */
public class OrderPayData {

    //主键
    private Long opId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 交易号
     */
    private String tradeNo;

    /**
     * 支付数据
     */
    private String payData;

    public Long getOpId() {
        return opId;
    }

    public void setOpId(Long opId) {
        this.opId = opId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getPayData() {
        return payData;
    }

    public void setPayData(String payData) {
        this.payData = payData;
    }
}
