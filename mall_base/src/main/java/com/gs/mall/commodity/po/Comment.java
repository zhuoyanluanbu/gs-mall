package com.gs.mall.commodity.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:15
 */
public class Comment implements java.io.Serializable{

    /** 评论ID */
    private java.lang.Long commentId;

    /** 商品ID */
    private java.lang.Long commodityId;

    /** 商品快照ID */
    private java.lang.Long snapshootId;

    /** sku名称 */
    private java.lang.String skuName;

    /** 用户openID */
    private java.lang.String openId;

    /** 评论人 */
    private java.lang.String commentor;

    private java.lang.String commentorImg;

    /** 评论内容 */
    private java.lang.String commentContent;

    /** 评分：-1 不参数与评论  0 差评 1 好评 */
    private Integer score;

    /** 评论时间 */
    private java.util.Date commentTime;

    /** 图片1 */
    private java.lang.String imgUri1;

    /** 图片2 */
    private java.lang.String imgUri2;

    /** img_uri2 */
    private java.lang.String imgUri3;

    /** 回复人 */
    private java.lang.String replyer;

    /** 回复内容 */
    private java.lang.String replyContent;

    /** 回复时间 */
    private java.util.Date replyTime;

    public Comment(){
		
    }

    public void setCommentId(java.lang.Long commentId) {
        this.commentId = commentId;
    }

    public java.lang.Long getCommentId() {
        return this.commentId;
    }
    public void setCommodityId(java.lang.Long commodityId) {
        this.commodityId = commodityId;
    }

    public java.lang.Long getCommodityId() {
        return this.commodityId;
    }
    public void setSnapshootId(java.lang.Long snapshootId) {
        this.snapshootId = snapshootId;
    }

    public java.lang.Long getSnapshootId() {
        return this.snapshootId;
    }
    public void setSkuName(java.lang.String skuName) {
        this.skuName = skuName;
    }

    public java.lang.String getSkuName() {
        return this.skuName;
    }
    public void setOpenId(java.lang.String openId) {
        this.openId = openId;
    }

    public java.lang.String getOpenId() {
        return this.openId;
    }
    public void setCommentor(java.lang.String commentor) {
        this.commentor = commentor;
    }

    public java.lang.String getCommentor() {
        return this.commentor;
    }
    public void setCommentorImg(java.lang.String commentorImg) {
        this.commentorImg = commentorImg;
    }

    public java.lang.String getCommentorImg() {
        return this.commentorImg;
    }
    public void setCommentContent(java.lang.String commentContent) {
        this.commentContent = commentContent;
    }

    public java.lang.String getCommentContent() {
        return this.commentContent;
    }
    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getScore() {
        return this.score;
    }
    public void setCommentTime(java.util.Date commentTime) {
        this.commentTime = commentTime;
    }

    public java.util.Date getCommentTime() {
        return this.commentTime;
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
    public void setReplyer(java.lang.String replyer) {
        this.replyer = replyer;
    }

    public java.lang.String getReplyer() {
        return this.replyer;
    }
    public void setReplyContent(java.lang.String replyContent) {
        this.replyContent = replyContent;
    }

    public java.lang.String getReplyContent() {
        return this.replyContent;
    }
    public void setReplyTime(java.util.Date replyTime) {
        this.replyTime = replyTime;
    }

    public java.util.Date getReplyTime() {
        return this.replyTime;
    }
}