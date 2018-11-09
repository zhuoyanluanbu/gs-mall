package com.gs.mall.solr.bean;

import java.io.Serializable;

import org.apache.solr.client.solrj.beans.Field;


/**
 * 商品solr bean
 * @author wangbo
 *
 */
public class CommoditySolrBean implements Serializable{
	
	private static final long serialVersionUID = 4930214910051359434L;
	@Field("commodityId")
	private java.lang.String id;
	@Field("title")
	private java.lang.String title;
	@Field("subTitle")
	private java.lang.String subTitle;
	@Field("category")
	private java.lang.String category;
	@Field("supplier")
	private java.lang.String supplier;
	@Field("status")
	private java.lang.String status;
	@Field("skuName")
	private java.lang.String skuName;
	@Field("barCode")
	private java.lang.String barCode;
	@Field("categoryId")
	private java.lang.String categoryId;
	@Field("minSalePrice")
	private java.lang.Integer minSalePrice;
	@Field("maxSalePrice")
	private java.lang.Integer maxSalePrice;
	@Field("imgUrl")
	private java.lang.String imgUri1;
	@Field("saleTotal")
	private java.lang.Integer saleTotal;
	@Field("commentScore")
	private java.lang.Integer commentScore;
	@Field("createTime")
	private java.lang.Integer createTime;
	@Field("merchantId")
	private java.lang.String merchantId;
	@Field("supplierId")
	private java.lang.String supplierId;
	@Field("displayName")
	private java.lang.String commodityName;
	
	public java.lang.String getCommodityName() {
		return commodityName;
	}
	public void setCommodityName(java.lang.String commodityName) {
		this.commodityName = commodityName;
	}
	public java.lang.String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(java.lang.String supplierId) {
		this.supplierId = supplierId;
	}
	public java.lang.String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(java.lang.String merchantId) {
		this.merchantId = merchantId;
	}
	public java.lang.String getId() {
		return id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getTitle() {
		return title;
	}
	public void setTitle(java.lang.String title) {
		this.title = title;
	}
	public java.lang.String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(java.lang.String subTitle) {
		this.subTitle = subTitle;
	}
	public java.lang.String getCategory() {
		return category;
	}
	public void setCategory(java.lang.String category) {
		this.category = category;
	}
	public java.lang.String getSupplier() {
		return supplier;
	}
	public void setSupplier(java.lang.String supplier) {
		this.supplier = supplier;
	}
	public java.lang.String getStatus() {
		return status;
	}
	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	public java.lang.String getSkuName() {
		return skuName;
	}
	public void setSkuName(java.lang.String skuName) {
		this.skuName = skuName;
	}
	public java.lang.String getBarCode() {
		return barCode;
	}
	public void setBarCode(java.lang.String barCode) {
		this.barCode = barCode;
	}
	public java.lang.String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(java.lang.String categoryId) {
		this.categoryId = categoryId;
	}
	public java.lang.String getImgUri1() {
		return imgUri1;
	}
	public void setImgUri1(java.lang.String imgUri1) {
		this.imgUri1 = imgUri1;
	}
	public java.lang.Integer getMinSalePrice() {
		return minSalePrice;
	}
	public void setMinSalePrice(java.lang.Integer minSalePrice) {
		this.minSalePrice = minSalePrice;
	}
	public java.lang.Integer getMaxSalePrice() {
		return maxSalePrice;
	}
	public void setMaxSalePrice(java.lang.Integer maxSalePrice) {
		this.maxSalePrice = maxSalePrice;
	}
	public java.lang.Integer getSaleTotal() {
		return saleTotal;
	}
	public void setSaleTotal(java.lang.Integer saleTotal) {
		this.saleTotal = saleTotal;
	}
	public java.lang.Integer getCommentScore() {
		return commentScore;
	}
	public void setCommentScore(java.lang.Integer commentScore) {
		this.commentScore = commentScore;
	}
	public java.lang.Integer getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.lang.Integer createTime) {
		this.createTime = createTime;
	}
	
	


	
}
