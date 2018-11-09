package com.gs.mall.order.dto;


import com.gs.mall.common.po.Logistics;
import com.gs.mall.order.po.RefundCommodity;
import com.gs.mall.user.po.UserAddress;

import java.util.List;

/**
 * Created by chenchuan on 2017/8/31
 */
public class WorkOrderCommodityDto extends WorkOrderDto {
    /**
     *订单信息
     */
    private OrderWorkOrderDto order;
    /**
     * 退货商品
     */
    private List<RefundCommodity> refundCommodities;

    /**
     *买家收货地址
     */
    private UserAddress userInfo;

    /**
     *退货物流
     */
    private Logistics logistics;

    public List<RefundCommodity> getRefundCommodities() {
        return refundCommodities;
    }

    public void setRefundCommodities(List<RefundCommodity> refundCommodities) {
        this.refundCommodities = refundCommodities;
    }

    public OrderWorkOrderDto getOrder() {
        return order;
    }

    public void setOrder(OrderWorkOrderDto order) {
        this.order = order;
    }

    public UserAddress getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserAddress userInfo) {
        this.userInfo = userInfo;
    }

    public Logistics getLogistics() {
        return logistics;
    }

    public void setLogistics(Logistics logistics) {
        this.logistics = logistics;
    }
}
