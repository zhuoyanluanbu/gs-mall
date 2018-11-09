package com.gs.mall.order.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:14
 */
public class ShoppingCart implements java.io.Serializable{

    /** 购物车ID */
    private java.lang.Long scId;

    /** 所有者OPENID */
    private java.lang.String ownerId;

    /** 商品ID */
    private java.lang.Long commodityId;

    /** SKU_ID */
    private java.lang.String skuId;

    /** 购买数量 */
    private java.lang.Integer buyNum;

    /** -1 删除 0 已购买 1 正常 */
    private java.lang.Integer status;

    /** 加入时间 */
    private java.util.Date createTime;

    public ShoppingCart(){
		
    }

    public void setScId(java.lang.Long scId) {
        this.scId = scId;
    }

    public java.lang.Long getScId() {
        return this.scId;
    }
    public void setOwnerId(java.lang.String ownerId) {
        this.ownerId = ownerId;
    }

    public java.lang.String getOwnerId() {
        return this.ownerId;
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
    public void setBuyNum(java.lang.Integer buyNum) {
        this.buyNum = buyNum;
    }

    public java.lang.Integer getBuyNum() {
        return this.buyNum;
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
}