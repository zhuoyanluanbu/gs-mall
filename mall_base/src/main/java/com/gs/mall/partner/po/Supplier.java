package com.gs.mall.partner.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:21
 */
public class Supplier implements java.io.Serializable{

    /** 商户id */
    private java.lang.String supplierId;

    /** 商户名称 */
    private java.lang.String name;

    /** 品牌名/供应商简称 */
    private java.lang.String shortName;

    /** 简介 */
    private java.lang.String shortIntroduce;

    /** 商户介绍 */
    private java.lang.String introduce;

    /** 状态 */
    private Integer status;

    /** 默认返点 */
    private java.lang.Float rebate;

    /** 价格百分比 */
    private java.lang.Float priceRate;

    /** 背景图 */
    private java.lang.String imgUri;

    /** 创建时间 */
    private java.util.Date createTime;

    /** 操作人ID */
    private java.lang.String operator;

    /** 操作人ID */
    private java.lang.String operatorId;

    /** 操作时间 */
    private java.util.Date updateTime;

    public Supplier(){
		
    }

    public void setSupplierId(java.lang.String supplierId) {
        this.supplierId = supplierId;
    }

    public java.lang.String getSupplierId() {
        return this.supplierId;
    }
    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return this.name;
    }
    public void setShortName(java.lang.String shortName) {
        this.shortName = shortName;
    }

    public java.lang.String getShortName() {
        return this.shortName;
    }
    public void setShortIntroduce(java.lang.String shortIntroduce) {
        this.shortIntroduce = shortIntroduce;
    }

    public java.lang.String getShortIntroduce() {
        return this.shortIntroduce;
    }
    public void setIntroduce(java.lang.String introduce) {
        this.introduce = introduce;
    }

    public java.lang.String getIntroduce() {
        return this.introduce;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
    public void setRebate(java.lang.Float rebate) {
        this.rebate = rebate;
    }

    public java.lang.Float getRebate() {
        return this.rebate;
    }
    public void setPriceRate(java.lang.Float priceRate) {
        this.priceRate = priceRate;
    }

    public java.lang.Float getPriceRate() {
        return this.priceRate;
    }
    public void setImgUri(java.lang.String imgUri) {
        this.imgUri = imgUri;
    }

    public java.lang.String getImgUri() {
        return this.imgUri;
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