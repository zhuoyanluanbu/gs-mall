package com.gs.mall.finance.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:20
 */
public class TradeStatement implements java.io.Serializable{

    /** 对账单ID */
    private java.lang.Long tsId;

    /** 订单号 */
    private java.lang.String orderId;

    /** 商户ID */
    private java.lang.Long merchantId;

    /** 金额(分) */
    private java.lang.Integer amount;

    /** 供应商ID */
    private java.lang.String supplierId;

    /** 状态（1 正常，-1  删除） */
    private Integer status;

    /** 创建时间 */
    private java.util.Date createTime;

    public TradeStatement(){
		
    }

    public void setTsId(java.lang.Long tsId) {
        this.tsId = tsId;
    }

    public java.lang.Long getTsId() {
        return this.tsId;
    }
    public void setOrderId(java.lang.String orderId) {
        this.orderId = orderId;
    }

    public java.lang.String getOrderId() {
        return this.orderId;
    }
    public void setMerchantId(java.lang.Long merchantId) {
        this.merchantId = merchantId;
    }

    public java.lang.Long getMerchantId() {
        return this.merchantId;
    }
    public void setAmount(java.lang.Integer amount) {
        this.amount = amount;
    }

    public java.lang.Integer getAmount() {
        return this.amount;
    }
    public void setSupplierId(java.lang.String supplierId) {
        this.supplierId = supplierId;
    }

    public java.lang.String getSupplierId() {
        return this.supplierId;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }
}