package com.gs.mall.commodity.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:16
 */
public class CommodityExtend implements java.io.Serializable{

    /** 商品扩展ID */
    private java.lang.Long extendId;

    /** 商品ID */
    private java.lang.Long commodityId;

    /** 扩展类型：1 商品描述  */
    private Integer type;

    /** 扩展内容 */
    private java.lang.String content;

    public CommodityExtend(){
		
    }

    public void setExtendId(java.lang.Long extendId) {
        this.extendId = extendId;
    }

    public java.lang.Long getExtendId() {
        return this.extendId;
    }
    public void setCommodityId(java.lang.Long commodityId) {
        this.commodityId = commodityId;
    }

    public java.lang.Long getCommodityId() {
        return this.commodityId;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }
    public void setContent(java.lang.String content) {
        this.content = content;
    }

    public java.lang.String getContent() {
        return this.content;
    }
}