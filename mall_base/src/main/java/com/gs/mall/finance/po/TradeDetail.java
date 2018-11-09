package com.gs.mall.finance.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:20
 */
public class TradeDetail implements java.io.Serializable{

    /** 交易明细ID */
    private java.lang.String tdId;

    /** 单据号（订单号、工单号） */
    private java.lang.String formId;

    /** 商家ID */
    private java.lang.Long merchantId;

    /** 交易号 */
    private java.lang.String tradeNo;

    /** 交易名称 */
    private java.lang.String name;

    /** 付款人ID */
    private java.lang.String payerId;

    /** 付款人 */
    private java.lang.String payer;

    /** 类型：1 订单入账  2订单退款  3 转账 */
    private Integer type;

    /** 金额(分) */
    private java.lang.Integer amount;

    /** 收款人ID */
    private java.lang.String payeeId;

    /** 收款人 */
    private java.lang.String payee;

    /** 状态：0 创建 1成功 2关闭 */
    private Integer status;

    /** 交易状态 */
    private java.lang.String tradeStatus;

    /** 创建时间 */
    private java.util.Date createTime;

    public TradeDetail(){
		
    }

    public void setTdId(java.lang.String tdId) {
        this.tdId = tdId;
    }

    public java.lang.String getTdId() {
        return this.tdId;
    }
    public void setFormId(java.lang.String formId) {
        this.formId = formId;
    }

    public java.lang.String getFormId() {
        return this.formId;
    }
    public void setMerchantId(java.lang.Long merchantId) {
        this.merchantId = merchantId;
    }

    public java.lang.Long getMerchantId() {
        return this.merchantId;
    }
    public void setTradeNo(java.lang.String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public java.lang.String getTradeNo() {
        return this.tradeNo;
    }
    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return this.name;
    }
    public void setPayerId(java.lang.String payerId) {
        this.payerId = payerId;
    }

    public java.lang.String getPayerId() {
        return this.payerId;
    }
    public void setPayer(java.lang.String payer) {
        this.payer = payer;
    }

    public java.lang.String getPayer() {
        return this.payer;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }
    public void setAmount(java.lang.Integer amount) {
        this.amount = amount;
    }

    public java.lang.Integer getAmount() {
        return this.amount;
    }
    public void setPayeeId(java.lang.String payeeId) {
        this.payeeId = payeeId;
    }

    public java.lang.String getPayeeId() {
        return this.payeeId;
    }
    public void setPayee(java.lang.String payee) {
        this.payee = payee;
    }

    public java.lang.String getPayee() {
        return this.payee;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
    public void setTradeStatus(java.lang.String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public java.lang.String getTradeStatus() {
        return this.tradeStatus;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }
}