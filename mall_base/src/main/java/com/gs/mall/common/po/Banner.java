package com.gs.mall.common.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:17
 */
public class Banner implements java.io.Serializable{

    /** banner主键 */
    private java.lang.Integer bannerId;

    /** 图片地址 */
    private java.lang.String imgUri;

    /** 链接地址 */
    private java.lang.String linkUrl;

    /** 状态：1 正常  0 暂存 -1 删除 */
    private Integer status;

    /** 排序值：显示时降序排列 */
    private Integer sortNum;

    /** 创建时间 */
    private java.util.Date createTime;

    private java.lang.String bannerText;

    /** 操作人 */
    private java.lang.String operator;

    /** 操作人ID */
    private java.lang.String operatorId;

    public Banner(){
		
    }

    public void setBannerId(java.lang.Integer bannerId) {
        this.bannerId = bannerId;
    }

    public java.lang.Integer getBannerId() {
        return this.bannerId;
    }
    public void setImgUri(java.lang.String imgUri) {
        this.imgUri = imgUri;
    }

    public java.lang.String getImgUri() {
        return this.imgUri;
    }
    public void setLinkUrl(java.lang.String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public java.lang.String getLinkUrl() {
        return this.linkUrl;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
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
    public void setBannerText(java.lang.String bannerText) {
        this.bannerText = bannerText;
    }

    public java.lang.String getBannerText() {
        return this.bannerText;
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
}