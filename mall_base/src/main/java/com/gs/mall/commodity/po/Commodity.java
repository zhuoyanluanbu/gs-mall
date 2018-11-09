package com.gs.mall.commodity.po;

import java.util.List;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:15
 */
public class Commodity implements java.io.Serializable{

    /** 商品ID */
    private java.lang.Long id;

    /** 分类ID */
    private java.lang.Long categoryId;

    /** 商户ID */
    private java.lang.Long merchantId;

    /** 供应商id */
    private java.lang.String supplierId;

    /** 供应商名称 */
    private java.lang.String supplierName;

    /** 商品标题 */
    private java.lang.String title;

    /** 商品副标题 */
    private java.lang.String subTitle;

    /** 分类名称 */
    private java.lang.String categoryName;

    /** 商品类型：1 实物商品（物流发货） 2 虚拟商品（无需物流） 3 电子卡券（无需物流） */
    private Integer type;

    /** 运费（分） */
    private java.lang.Integer freight;

    /** 运费类型：1 普通费用 2 独立费用 */
    private Integer freightType;

    /** 最小售价（分） */
    private java.lang.Integer minSalePrice;

    /** 最大售价（分） */
    private java.lang.Integer maxSalePrice;

    /** 状态：-1 删除 0.下架 1 上架 */
    private Integer status;

    /** 是否推荐：0 否 1 是 */
    private Integer isRecommend;

    /** 商品图片链接1 */
    private java.lang.String imgUri1;

    /** 商品图片链接2 */
    private java.lang.String imgUri2;

    /** 商品图片链接3 */
    private java.lang.String imgUri3;

    /** 商品图片链接4 */
    private java.lang.String imgUri4;

    /** 商品图片链接5 */
    private java.lang.String imgUri5;

    /** 累计销量 */
    private java.lang.Integer saleTotal;

    /** 累计评分 */
    private java.math.BigDecimal commentScore;

    /** 累计评论 */
    private java.lang.Integer commentTotal;

    /** 最后购买时间 */
    private java.util.Date lastBuy;

    /** 创建时间 */
    private java.util.Date createTime;

    /** 更新时间 */
    private java.util.Date updateTime;

    /** 操作人 */
    private java.lang.String operator;

    /** 操作人ID */
    private java.lang.String operatorId;

    private String supplierImg;
    
    /**
     * 商品属性
     */
    private List<CommodityAttributeRef> attributeList;

    /**
     * 商品详情
     */
    private List<CommodityDetail> detailList;

    /**
     * 商品扩展
     */
    private CommodityExtend commodityExtend;


    /**
     * 商品评价
     */
    private List<Comment> comments;


    public Commodity(){

    }

	public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getId() {
        return this.id;
    }
    public void setCategoryId(java.lang.Long categoryId) {
        this.categoryId = categoryId;
    }

    public java.lang.Long getCategoryId() {
        return this.categoryId;
    }
    public void setMerchantId(java.lang.Long merchantId) {
        this.merchantId = merchantId;
    }

    public java.lang.Long getMerchantId() {
        return this.merchantId;
    }
    public void setSupplierId(java.lang.String supplierId) {
        this.supplierId = supplierId;
    }

    public java.lang.String getSupplierId() {
        return this.supplierId;
    }
    public void setSupplierName(java.lang.String supplierName) {
        this.supplierName = supplierName;
    }

    public java.lang.String getSupplierName() {
        return this.supplierName;
    }
    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    public java.lang.String getTitle() {
        return this.title;
    }
    public void setSubTitle(java.lang.String subTitle) {
        this.subTitle = subTitle;
    }

    public java.lang.String getSubTitle() {
        return this.subTitle;
    }
    public void setCategoryName(java.lang.String categoryName) {
        this.categoryName = categoryName;
    }

    public java.lang.String getCategoryName() {
        return this.categoryName;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }
    public void setFreight(java.lang.Integer freight) {
        this.freight = freight;
    }

    public java.lang.Integer getFreight() {
        return this.freight;
    }
    public void setFreightType(Integer freightType) {
        this.freightType = freightType;
    }

    public Integer getFreightType() {
        return this.freightType;
    }
    public void setMinSalePrice(java.lang.Integer minSalePrice) {
        this.minSalePrice = minSalePrice;
    }

    public java.lang.Integer getMinSalePrice() {
        return this.minSalePrice;
    }
    public void setMaxSalePrice(java.lang.Integer maxSalePrice) {
        this.maxSalePrice = maxSalePrice;
    }

    public java.lang.Integer getMaxSalePrice() {
        return this.maxSalePrice;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Integer getIsRecommend() {
        return this.isRecommend;
    }
    public void setImgUri1(java.lang.String imgUri1) {
        this.imgUri1 = imgUri1;
    }

    public java.lang.String getImgUri1() {
        return this.imgUri1;
    }
    public void setImgUri2(java.lang.String imgUri2) {
        this.imgUri2 = imgUri2;
    }

    public java.lang.String getImgUri2() {
        return this.imgUri2;
    }
    public void setImgUri3(java.lang.String imgUri3) {
        this.imgUri3 = imgUri3;
    }

    public java.lang.String getImgUri3() {
        return this.imgUri3;
    }
    public void setImgUri4(java.lang.String imgUri4) {
        this.imgUri4 = imgUri4;
    }

    public java.lang.String getImgUri4() {
        return this.imgUri4;
    }
    public void setImgUri5(java.lang.String imgUri5) {
        this.imgUri5 = imgUri5;
    }

    public java.lang.String getImgUri5() {
        return this.imgUri5;
    }
    public void setSaleTotal(java.lang.Integer saleTotal) {
        this.saleTotal = saleTotal;
    }

    public java.lang.Integer getSaleTotal() {
        return this.saleTotal;
    }
    public void setCommentScore(java.math.BigDecimal commentScore) {
        this.commentScore = commentScore;
    }

    public java.math.BigDecimal getCommentScore() {
        return this.commentScore;
    }
    public void setCommentTotal(java.lang.Integer commentTotal) {
        this.commentTotal = commentTotal;
    }

    public java.lang.Integer getCommentTotal() {
        return this.commentTotal;
    }
    public void setLastBuy(java.util.Date lastBuy) {
        this.lastBuy = lastBuy;
    }

    public java.util.Date getLastBuy() {
        return this.lastBuy;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    public java.util.Date getUpdateTime() {
        return this.updateTime;
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

    public List<CommodityAttributeRef> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<CommodityAttributeRef> attributeList) {
        this.attributeList = attributeList;
    }

    public List<CommodityDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<CommodityDetail> detailList) {
        this.detailList = detailList;
    }

    public CommodityExtend getCommodityExtend() {
        return commodityExtend;
    }

    public void setCommodityExtend(CommodityExtend commodityExtend) {
        this.commodityExtend = commodityExtend;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getSupplierImg() {
        return supplierImg;
    }

    public void setSupplierImg(String supplierImg) {
        this.supplierImg = supplierImg;
    }
}