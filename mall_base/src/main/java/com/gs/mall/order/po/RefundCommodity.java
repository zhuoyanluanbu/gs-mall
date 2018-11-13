package com.gs.mall.order.po;

import com.gs.mall.common.po.MyObject;


/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:14
 */
public class RefundCommodity extends MyObject{

    /** 主键 */
    private java.lang.Long rcId;

    /** 工单号 */
    private java.lang.String woId;

    /** 快照表ID */
    private java.lang.Long snapshootId;

    /** 商品id */
    private java.lang.Long commodityId;

    /** sku_id */
    private java.lang.String skuId;

    /** 商品名称 */
    private java.lang.String title;

    /** 商品属性描述 */
    private java.lang.String attributes;

    /** 商品价格 */
    private java.lang.Integer price;

    /** 数量 */
    private java.lang.Integer num;

    /** 创建时间 */
    private java.util.Date createTime;

    private java.lang.String imgUri;

    public RefundCommodity(){
		
    }


    public RefundCommodity(Long snapshootId,Integer num){
        this.snapshootId = snapshootId;
        this.num = num;
    }

    public void setRcId(java.lang.Long rcId) {
        this.rcId = rcId;
    }

    public java.lang.Long getRcId() {
        return this.rcId;
    }
    public void setWoId(java.lang.String woId) {
        this.woId = woId;
    }

    public java.lang.String getWoId() {
        return this.woId;
    }
    public void setSnapshootId(java.lang.Long snapshootId) {
        this.snapshootId = snapshootId;
    }

    public java.lang.Long getSnapshootId() {
        return this.snapshootId;
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
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }
    public void setImgUri(java.lang.String imgUri) {
        this.imgUri = imgUri;
    }

    public java.lang.String getImgUri() {
        return this.imgUri;
    }

    public static void main(String[] args) {
        System.out.println(new RefundCommodity().toString());
    }
}