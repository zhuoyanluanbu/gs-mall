package com.gs.mall.commodity.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:15
 */
public class CommodityAttributeRef implements java.io.Serializable{

    /** 商品规格ID */
    private java.lang.Long attrRefId;

    /** 商品ID */
    private java.lang.Long commodityId;

    /** 规格ID */
    private java.lang.Long attributeId;

    /** 规格值 */
    private java.lang.String attributeValue;

    /** 排序值 */
    private Integer sortNum;

    private String attributeName;

    public CommodityAttributeRef(){
		
    }

    public void setAttrRefId(java.lang.Long attrRefId) {
        this.attrRefId = attrRefId;
    }

    public java.lang.Long getAttrRefId() {
        return this.attrRefId;
    }
    public void setCommodityId(java.lang.Long commodityId) {
        this.commodityId = commodityId;
    }

    public java.lang.Long getCommodityId() {
        return this.commodityId;
    }
    public void setAttributeId(java.lang.Long attributeId) {
        this.attributeId = attributeId;
    }

    public java.lang.Long getAttributeId() {
        return this.attributeId;
    }
    public void setAttributeValue(java.lang.String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public java.lang.String getAttributeValue() {
        return this.attributeValue;
    }
    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public Integer getSortNum() {
        return this.sortNum;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}