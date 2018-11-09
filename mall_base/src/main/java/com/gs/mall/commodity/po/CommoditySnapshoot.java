package com.gs.mall.commodity.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:17
 */
public class CommoditySnapshoot implements java.io.Serializable{

    /** 快照表ID */
    private java.lang.Long snapshootId;

    /** 订单号 */
    private java.lang.String orderId;

    /** 商品ID */
    private java.lang.Long commodityId;

    /** skuid */
    private java.lang.String skuId;

    /** 商品名称 */
    private java.lang.String title;

    /** 商品属性描述 */
    private java.lang.String attributes;

    /** 商品价格(分) */
    private java.lang.Integer price;

    /** 购买数量 */
    private java.lang.Integer num;

    /** 商品条码 */
    private java.lang.String barCode;

    /** 图片uri */
    private java.lang.String imgUri;

    /** 状态：1正常 0退款 */
    private Integer status;

    /** 创建时间 */
    private java.util.Date createTime;

    public CommoditySnapshoot(){
		
    }

    public void setSnapshootId(java.lang.Long snapshootId) {
        this.snapshootId = snapshootId;
    }

    public java.lang.Long getSnapshootId() {
        return this.snapshootId;
    }
    public void setOrderId(java.lang.String orderId) {
        this.orderId = orderId;
    }

    public java.lang.String getOrderId() {
        return this.orderId;
    }
    public void setCommodityId(java.lang.Long commodityId) {
        this.commodityId = commodityId;
    }

    public java.lang.Long getCommodityId() {
        return this.commodityId;
    }
    public void setSkuId(java.lang.String skuId) {
        this.skuId = skuId;
    }

    public java.lang.String getSkuId() {
        return this.skuId;
    }
    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    public java.lang.String getTitle() {
        return this.title;
    }
    public void setAttributes(java.lang.String attributes) {
        this.attributes = attributes;
    }

    public java.lang.String getAttributes() {
        return this.attributes;
    }
    public void setPrice(java.lang.Integer price) {
        this.price = price;
    }

    public java.lang.Integer getPrice() {
        return this.price;
    }
    public void setNum(java.lang.Integer num) {
        this.num = num;
    }

    public java.lang.Integer getNum() {
        return this.num;
    }
    public void setBarCode(java.lang.String barCode) {
        this.barCode = barCode;
    }

    public java.lang.String getBarCode() {
        return this.barCode;
    }
    public void setImgUri(java.lang.String imgUri) {
        this.imgUri = imgUri;
    }

    public java.lang.String getImgUri() {
        return this.imgUri;
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