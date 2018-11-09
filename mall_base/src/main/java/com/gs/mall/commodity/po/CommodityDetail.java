package com.gs.mall.commodity.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:16
 */
public class CommodityDetail implements java.io.Serializable{

    /** 商品明细ID */
    private java.lang.Long detailId;

    /** 商品ID */
    private java.lang.Long commodityId;

    /** 商品标题 */
    private java.lang.String title;

    /** skuID */
    private java.lang.String skuId;

    /** sku名称 */
    private java.lang.String skuName;

    /** 图片uri */
    private java.lang.String imgUri;

    /** 售价（分） */
    private java.lang.Integer salePrice;

    /** 供应价格（分） */
    private java.lang.Integer supplyPrice;

    /** 返点 */
    private java.lang.Float rebate;

    /** 库存 */
    private java.lang.Integer inventory;

    /** 商品条码 */
    private java.lang.String barCode;

    /** 状态：1 正常 0 停用 -1 删除 */
    private Integer status;

    /** 创建时间 */
    private java.util.Date createTime;

    public CommodityDetail(){
		
    }

    public void setDetailId(java.lang.Long detailId) {
        this.detailId = detailId;
    }

    public java.lang.Long getDetailId() {
        return this.detailId;
    }
    public void setCommodityId(java.lang.Long commodityId) {
        this.commodityId = commodityId;
    }

    public java.lang.Long getCommodityId() {
        return this.commodityId;
    }
    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    public java.lang.String getTitle() {
        return this.title;
    }
    public void setSkuId(java.lang.String skuId) {
        this.skuId = skuId;
    }

    public java.lang.String getSkuId() {
        return this.skuId;
    }
    public void setSkuName(java.lang.String skuName) {
        this.skuName = skuName;
    }

    public java.lang.String getSkuName() {
        return this.skuName;
    }
    public void setImgUri(java.lang.String imgUri) {
        this.imgUri = imgUri;
    }

    public java.lang.String getImgUri() {
        return this.imgUri;
    }
    public void setSalePrice(java.lang.Integer salePrice) {
        this.salePrice = salePrice;
    }

    public java.lang.Integer getSalePrice() {
        return this.salePrice;
    }
    public void setSupplyPrice(java.lang.Integer supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public java.lang.Integer getSupplyPrice() {
        return this.supplyPrice;
    }
    public void setRebate(java.lang.Float rebate) {
        this.rebate = rebate;
    }

    public java.lang.Float getRebate() {
        return this.rebate;
    }
    public void setInventory(java.lang.Integer inventory) {
        this.inventory = inventory;
    }

    public java.lang.Integer getInventory() {
        return this.inventory;
    }
    public void setBarCode(java.lang.String barCode) {
        this.barCode = barCode;
    }

    public java.lang.String getBarCode() {
        return this.barCode;
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