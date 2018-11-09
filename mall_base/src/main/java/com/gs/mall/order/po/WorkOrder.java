package com.gs.mall.order.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:13
 */
public class WorkOrder implements java.io.Serializable{

    /** 工单号 */
    private java.lang.String woId;

    /** 订单号 */
    private java.lang.String orderId;

    /** 物流ID */
    private java.lang.Long logisticsId;

    /** 商户ID */
    private java.lang.Long merchantId;

    /** 快照表ID */
    private java.lang.Long snapshootId;

    /** 工单类型：1.只退款不退货 2.需要退货退款 */
    private Integer type;

    /** 工单说明 */
    private java.lang.String comment;

    /** 图片地址1 */
    private java.lang.String imgUri1;

    /** 图片地址2 */
    private java.lang.String imgUri2;

    /** 图片地址3 */
    private java.lang.String imgUri3;

    private java.lang.String receiver;

    private java.lang.String receiverTel;

    /** 关闭原因 */
    private java.lang.String closeReason;

    /** 状态：10  申请退货（待商家确认） 20 待用户填写快递 (待商家收货) 30 待退款(已商家收货) 40 已退款（处理完成）50 关闭 */
    private java.lang.Integer status;

    /** 创建时间 */
    private java.util.Date createTime;

    /** 工单金额(分) */
    private java.lang.Integer amount;

    private java.lang.Integer isFreight;

    public WorkOrder(){
		
    }

    public void setWoId(java.lang.String woId) {
        this.woId = woId;
    }

    public java.lang.String getWoId() {
        return this.woId;
    }
    public void setOrderId(java.lang.String orderId) {
        this.orderId = orderId;
    }

    public java.lang.String getOrderId() {
        return this.orderId;
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
    public void setSnapshootId(java.lang.Long snapshootId) {
        this.snapshootId = snapshootId;
    }

    public java.lang.Long getSnapshootId() {
        return this.snapshootId;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }
    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }

    public java.lang.String getComment() {
        return this.comment;
    }
    public void setImgUri1(java.lang.String imgUri1) {
        this.imgUri1 = imgUri1;
    }

    public java.lang.String getImgUri1() {
        return this.imgUri1;
    }
    public void setImgUri2(java.lang.String imgUri2) {
        this.imgUri2 = imgUri2;
    }

    public java.lang.String getImgUri2() {
        return this.imgUri2;
    }
    public void setImgUri3(java.lang.String imgUri3) {
        this.imgUri3 = imgUri3;
    }

    public java.lang.String getImgUri3() {
        return this.imgUri3;
    }
    public void setReceiver(java.lang.String receiver) {
        this.receiver = receiver;
    }

    public java.lang.String getReceiver() {
        return this.receiver;
    }
    public void setReceiverTel(java.lang.String receiverTel) {
        this.receiverTel = receiverTel;
    }

    public java.lang.String getReceiverTel() {
        return this.receiverTel;
    }
    public void setCloseReason(java.lang.String closeReason) {
        this.closeReason = closeReason;
    }

    public java.lang.String getCloseReason() {
        return this.closeReason;
    }
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    public java.lang.Integer getStatus() {
        return this.status;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }
    public void setAmount(java.lang.Integer amount) {
        this.amount = amount;
    }

    public java.lang.Integer getAmount() {
        return this.amount;
    }
    public void setIsFreight(java.lang.Integer isFreight) {
        this.isFreight = isFreight;
    }

    public java.lang.Integer getIsFreight() {
        return this.isFreight;
    }
}