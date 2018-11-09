package com.gs.mall.order.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Filename com.gs.mall.order.dto.OrderWorkOrderDto
 * @Description
 * @Version 1.0
 * @Author cdaic
 * @Email cdaic@qq.com
 * @History <li>Author: chenchuan</li>
 * <li>Date: 2017/9/19</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
public class OrderWorkOrderDto {
    //订单ID
    private String orderId;

    //下单时间
    private Date createTime;

    //支付时间
    private Date payTime;

    //发货时间
    private Date deliverTime;

    //商品数
    private Integer commodityNum;

    //订单金额
    private Integer totalPrice;

    //运费
    private Integer freightPrice;

    //优惠金额
    private Integer discountPrice;

    //状态
    private Integer status;

    //支付状态
    private Short payStatus;

    //商品列表
    private List<Map<String,Object>> commodities;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    public Integer getCommodityNum() {
        return commodityNum;
    }

    public void setCommodityNum(Integer commodityNum) {
        this.commodityNum = commodityNum;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getFreightPrice() {
        return freightPrice;
    }

    public void setFreightPrice(Integer freightPrice) {
        this.freightPrice = freightPrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Integer discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Short getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Short payStatus) {
        this.payStatus = payStatus;
    }

    public List<Map<String, Object>> getCommodities() {
        return commodities;
    }

    public void setCommodities(List<Map<String, Object>> commodities) {
        this.commodities = commodities;
    }
}
