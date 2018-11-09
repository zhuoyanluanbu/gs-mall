package com.gs.mall.partner.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:21
 */
public class Merchant implements java.io.Serializable{

    /** 商户ID */
    private java.lang.Long merchantId;

    /** OPEN_ID */
    private java.lang.String openId;

    /** 商户名称 */
    private java.lang.String name;

    /** 是否自营（1是 0否） */
    private Integer isSelfManage;

    /** 商户简介 */
    private java.lang.String introduction;

    /** 联系人 */
    private java.lang.String contact;

    /** 联系电话 */
    private java.lang.String contactPhone;

    /** 联系人地址 */
    private java.lang.String address;

    /** 操作时间 */
    private java.util.Date createTime;

    public Merchant(){
		
    }

    public void setMerchantId(java.lang.Long merchantId) {
        this.merchantId = merchantId;
    }

    public java.lang.Long getMerchantId() {
        return this.merchantId;
    }
    public void setOpenId(java.lang.String openId) {
        this.openId = openId;
    }

    public java.lang.String getOpenId() {
        return this.openId;
    }
    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return this.name;
    }
    public void setIsSelfManage(Integer isSelfManage) {
        this.isSelfManage = isSelfManage;
    }

    public Integer getIsSelfManage() {
        return this.isSelfManage;
    }
    public void setIntroduction(java.lang.String introduction) {
        this.introduction = introduction;
    }

    public java.lang.String getIntroduction() {
        return this.introduction;
    }
    public void setContact(java.lang.String contact) {
        this.contact = contact;
    }

    public java.lang.String getContact() {
        return this.contact;
    }
    public void setContactPhone(java.lang.String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public java.lang.String getContactPhone() {
        return this.contactPhone;
    }
    public void setAddress(java.lang.String address) {
        this.address = address;
    }

    public java.lang.String getAddress() {
        return this.address;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }
}