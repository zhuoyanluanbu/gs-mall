package com.gs.mall.finance.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:19
 */
public class TradeAmount implements java.io.Serializable{

    /** 日交易额id */
    private java.lang.Long taId;

    /** 商户ID */
    private java.lang.Long merchantId;

    /** 金额(分) */
    private java.lang.Integer amount;

    /** 所属日期 */
    private java.util.Date createDate;

    public TradeAmount(){
		
    }

    public void setTaId(java.lang.Long taId) {
        this.taId = taId;
    }

    public java.lang.Long getTaId() {
        return this.taId;
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
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    public java.util.Date getCreateDate() {
        return this.createDate;
    }
}