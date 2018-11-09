package com.gs.mall.order.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单对象dto
 * Created by huangyp on 2017/8/24.
 */
public class OrderDto {

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
    
    //商品数
    private Integer commodityType;

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

    //退货状态
    private Integer refundStatus;

    //退货情况
    private String refundDetail;

    //商品列表
    private List<Map<String,Object>> commodities;

    //物流信息
    private Map<String,Object> logistics;

    //用户信息
    private Map<String,Object> userInfo;

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

	public Integer getCommodityType() {
		return commodityType;
	}

	public void setCommodityType(Integer commodityType) {
		this.commodityType = commodityType;
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

    public List<Map<String, Object>> getCommodities() {
        return commodities;
    }

    public void setCommodities(List<Map<String, Object>> commodities) {
        this.commodities = commodities;
    }

    public Map<String, Object> getLogistics() {
        return logistics;
    }

    public void setLogistics(Map<String, Object> logistics) {
        this.logistics = logistics;
    }

    public Map<String, Object> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Map<String, Object> userInfo) {
        this.userInfo = userInfo;
    }

    public Short getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Short payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundDetail() {
        return refundDetail;
    }

    public void setRefundDetail(String refundDetail) {
        this.refundDetail = refundDetail;
    }
}

