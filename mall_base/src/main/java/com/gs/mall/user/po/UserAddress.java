package com.gs.mall.user.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:22
 */
public class UserAddress implements java.io.Serializable{

    /** 用户ID */
    private java.lang.Long userAddrId;

    /** openID */
    private java.lang.String openId;

    /** 用户名称 */
    private java.lang.String username;

    /** 用户电话 */
    private java.lang.String mobile;

    /** 省ID */
    private java.lang.Integer provinceId;

    /** 省 */
    private java.lang.String province;

    /** 市ID */
    private java.lang.Integer cityId;

    /** 市 */
    private java.lang.String city;

    /** 区、市、县ID */
    private java.lang.Integer areaId;

    /** 区、市、县 */
    private java.lang.String area;

    /** 详细地址 */
    private java.lang.String address;

    /** 创建时间 */
    private java.util.Date createTime;

    public UserAddress(){
		
    }

    public void setUserAddrId(java.lang.Long userAddrId) {
        this.userAddrId = userAddrId;
    }

    public java.lang.Long getUserAddrId() {
        return this.userAddrId;
    }
    public void setOpenId(java.lang.String openId) {
        this.openId = openId;
    }

    public java.lang.String getOpenId() {
        return this.openId;
    }
    public void setUsername(java.lang.String username) {
        this.username = username;
    }

    public java.lang.String getUsername() {
        return this.username;
    }
    public void setMobile(java.lang.String mobile) {
        this.mobile = mobile;
    }

    public java.lang.String getMobile() {
        return this.mobile;
    }
    public void setProvinceId(java.lang.Integer provinceId) {
        this.provinceId = provinceId;
    }

    public java.lang.Integer getProvinceId() {
        return this.provinceId;
    }
    public void setProvince(java.lang.String province) {
        this.province = province;
    }

    public java.lang.String getProvince() {
        return this.province;
    }
    public void setCityId(java.lang.Integer cityId) {
        this.cityId = cityId;
    }

    public java.lang.Integer getCityId() {
        return this.cityId;
    }
    public void setCity(java.lang.String city) {
        this.city = city;
    }

    public java.lang.String getCity() {
        return this.city;
    }
    public void setAreaId(java.lang.Integer areaId) {
        this.areaId = areaId;
    }

    public java.lang.Integer getAreaId() {
        return this.areaId;
    }
    public void setArea(java.lang.String area) {
        this.area = area;
    }

    public java.lang.String getArea() {
        return this.area;
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