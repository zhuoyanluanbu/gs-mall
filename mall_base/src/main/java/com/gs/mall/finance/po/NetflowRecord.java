package com.gs.mall.finance.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:19
 */
public class NetflowRecord implements java.io.Serializable{

    /** 流量记录ID */
    private java.lang.Long nrId;

    /** 用户ID */
    private java.lang.String openId;

    /** 用户名称 */
    private java.lang.String userName;

    /** 用户ID */
    private java.lang.String ip;

    /** 设备信息 */
    private java.lang.String deviceInfo;

    /** 访问时间 */
    private java.util.Date createTime;

    public NetflowRecord(){
		
    }

    public void setNrId(java.lang.Long nrId) {
        this.nrId = nrId;
    }

    public java.lang.Long getNrId() {
        return this.nrId;
    }
    public void setOpenId(java.lang.String openId) {
        this.openId = openId;
    }

    public java.lang.String getOpenId() {
        return this.openId;
    }
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    public java.lang.String getUserName() {
        return this.userName;
    }
    public void setIp(java.lang.String ip) {
        this.ip = ip;
    }

    public java.lang.String getIp() {
        return this.ip;
    }
    public void setDeviceInfo(java.lang.String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public java.lang.String getDeviceInfo() {
        return this.deviceInfo;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }
}