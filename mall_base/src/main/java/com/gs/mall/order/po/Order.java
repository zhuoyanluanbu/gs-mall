package com.gs.mall.order.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:12
 */
public class Order implements java.io.Serializable{

    /** 订单号 */
    private java.lang.String orderId;

    /** 用户地址ID */
    private java.lang.Long userAddrId;

    /** 物流ID */
    private java.lang.Long logisticsId;

    /** 商户ID */
    private java.lang.Long merchantId;

    /** 所有人ID */
    private java.lang.String ownerOpenId;

    /** 商品数 */
    private Integer commodityNum;

    /** 商品类型：1 实物商品（物流发货） 2 虚拟商品（无需物流） 3 电子卡券（无需物流） */
    private Integer commodityType;

    /** 订单金额（分） */
    private java.lang.Integer totalPrice;

    /** 支付金额（分） */
    private java.lang.Integer payPrice;

    /** 运费金额（分） */
    private java.lang.Integer freightPrice;

    /** 优惠金额（分） */
    private java.lang.Integer discountPrice;

    /** 优惠券码 */
    private java.lang.String discountCode;

    /** 优惠券名称 */
    private java.lang.String discountName;

    /** 订单状态：1000 下单成功（待付款）  1100 付款成功（待发货） 1200 已发货（待完成） 1300  已完成 1400 订单关闭 */
    private java.lang.Integer status;

    /** 支付状态：0 未支付  1 已支付  2 已退款 */
    private Integer payStatus;

    private java.lang.String refundDetail;

    private java.lang.Integer refundStatus;

    /** 创建时间 */
    private java.util.Date createTime;

    /** 付款时间 */
    private java.util.Date payTime;

    /** 发货时间 */
    private java.util.Date deliverTime;

    /** 完成时间 */
    private java.util.Date finishTime;

    public Order(){
		
    }

    public void setOrderId(java.lang.String orderId) {
        this.orderId = orderId;
    }

    public java.lang.String getOrderId() {
        return this.orderId;
    }
    public void setUserAddrId(java.lang.Long userAddrId) {
        this.userAddrId = userAddrId;
    }

    public java.lang.Long getUserAddrId() {
        return this.userAddrId;
    }
    public void setLogisticsId(java.lang.Long logisticsId) {
        this.logisticsId = logisticsId;
    }

    public java.lang.Long getLogisticsId() {
        return this.logisticsId;
    }
    public void setMerchantId(java.lang.Long merchantId) {
        this.merchantId = merchantId;
    }

    public java.lang.Long getMerchantId() {
        return this.merchantId;
    }
    public void setOwnerOpenId(java.lang.String ownerOpenId) {
        this.ownerOpenId = ownerOpenId;
    }

    public java.lang.String getOwnerOpenId() {
        return this.ownerOpenId;
    }
    public void setCommodityNum(Integer commodityNum) {
        this.commodityNum = commodityNum;
    }

    public Integer getCommodityNum() {
        return this.commodityNum;
    }
    public void setCommodityType(Integer commodityType) {
        this.commodityType = commodityType;
    }

    public Integer getCommodityType() {
        return this.commodityType;
    }
    public void setTotalPrice(java.lang.Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public java.lang.Integer getTotalPrice() {
        return this.totalPrice;
    }
    public void setPayPrice(java.lang.Integer payPrice) {
        this.payPrice = payPrice;
    }

    public java.lang.Integer getPayPrice() {
        return this.payPrice;
    }
    public void setFreightPrice(java.lang.Integer freightPrice) {
        this.freightPrice = freightPrice;
    }

    public java.lang.Integer getFreightPrice() {
        return this.freightPrice;
    }
    public void setDiscountPrice(java.lang.Integer discountPrice) {
        this.discountPrice = discountPrice;
    }

    public java.lang.Integer getDiscountPrice() {
        return this.discountPrice;
    }
    public void setDiscountCode(java.lang.String discountCode) {
        this.discountCode = discountCode;
    }

    public java.lang.String getDiscountCode() {
        return this.discountCode;
    }
    public void setDiscountName(java.lang.String discountName) {
        this.discountName = discountName;
    }

    public java.lang.String getDiscountName() {
        return this.discountName;
    }
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    public java.lang.Integer getStatus() {
        return this.status;
    }
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getPayStatus() {
        return this.payStatus;
    }
    public void setRefundDetail(java.lang.String refundDetail) {
        this.refundDetail = refundDetail;
    }

    public java.lang.String getRefundDetail() {
        return this.refundDetail;
    }
    public void setRefundStatus(java.lang.Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public java.lang.Integer getRefundStatus() {
        return this.refundStatus;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }
    public void setPayTime(java.util.Date payTime) {
        this.payTime = payTime;
    }

    public java.util.Date getPayTime() {
        return this.payTime;
    }
    public void setDeliverTime(java.util.Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    public java.util.Date getDeliverTime() {
        return this.deliverTime;
    }
    public void setFinishTime(java.util.Date finishTime) {
        this.finishTime = finishTime;
    }

    public java.util.Date getFinishTime() {
        return this.finishTime;
    }
}