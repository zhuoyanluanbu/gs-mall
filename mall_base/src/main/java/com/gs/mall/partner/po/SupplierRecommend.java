package com.gs.mall.partner.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:21
 */
public class SupplierRecommend implements java.io.Serializable{

    /** 推荐商户ID */
    private java.lang.Integer prId;

    /** 商户id */
    private java.lang.String supplierId;

    /** 供应商名称 */
    private java.lang.String merchantName;

    /** 推荐图链接 */
    private java.lang.String imgUrl;

    /** 排序号 */
    private Integer sortNum;

    /** 推荐时间 */
    private java.util.Date createTime;

    /** 操作人ID */
    private java.lang.String operator;

    /** 操作人ID */
    private java.lang.String operatorId;

    /** 操作时间 */
    private java.util.Date updateTime;

    public SupplierRecommend(){
		
    }

    public void setPrId(java.lang.Integer prId) {
        this.prId = prId;
    }

    public java.lang.Integer getPrId() {
        return this.prId;
    }
    public void setSupplierId(java.lang.String supplierId) {
        this.supplierId = supplierId;
    }

    public java.lang.String getSupplierId() {
        return this.supplierId;
    }
    public void setMerchantName(java.lang.String merchantName) {
        this.merchantName = merchantName;
    }

    public java.lang.String getMerchantName() {
        return this.merchantName;
    }
    public void setImgUrl(java.lang.String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public java.lang.String getImgUrl() {
        return this.imgUrl;
    }
    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public Integer getSortNum() {
        return this.sortNum;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }
    public void setOperator(java.lang.String operator) {
        this.operator = operator;
    }

    public java.lang.String getOperator() {
        return this.operator;
    }
    public void setOperatorId(java.lang.String operatorId) {
        this.operatorId = operatorId;
    }

    public java.lang.String getOperatorId() {
        return this.operatorId;
    }
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    public java.util.Date getUpdateTime() {
        return this.updateTime;
    }
}