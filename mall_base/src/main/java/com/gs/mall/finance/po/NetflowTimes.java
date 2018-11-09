package com.gs.mall.finance.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:19
 */
public class NetflowTimes implements java.io.Serializable{

    /** 流量次数ID */
    private java.lang.Long ntId;

    /** 次数 */
    private java.lang.Integer times;

    /** 所属日期 */
    private java.util.Date createDate;

    public NetflowTimes(){
		
    }

    public void setNtId(java.lang.Long ntId) {
        this.ntId = ntId;
    }

    public java.lang.Long getNtId() {
        return this.ntId;
    }
    public void setTimes(java.lang.Integer times) {
        this.times = times;
    }

    public java.lang.Integer getTimes() {
        return this.times;
    }
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    public java.util.Date getCreateDate() {
        return this.createDate;
    }
}