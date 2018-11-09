package com.gs.mall.commodity.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:16
 */
public class CommodityRecommend implements java.io.Serializable{

    /** 推荐商品表ID */
    private java.lang.Integer crId;

    /** 商品ID */
    private java.lang.Long commodityId;

    /** 推荐显示名称 */
    private java.lang.String displayName;

    /** 排序号 */
    private Integer sortNum;

    /** 推荐时间 */
    private java.util.Date createTime;

    public CommodityRecommend(){
		
    }

    public void setCrId(java.lang.Integer crId) {
        this.crId = crId;
    }

    public java.lang.Integer getCrId() {
        return this.crId;
    }
    public void setCommodityId(java.lang.Long commodityId) {
        this.commodityId = commodityId;
    }

    public java.lang.Long getCommodityId() {
        return this.commodityId;
    }
    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    public java.lang.String getDisplayName() {
        return this.displayName;
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
}