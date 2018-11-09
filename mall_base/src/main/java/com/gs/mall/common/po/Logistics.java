package com.gs.mall.common.po;

import java.util.Date;

/**
 * 快递记录
 * @author huangyp
 * @date 2017-8-18
 */
public class Logistics {
    private Long logisticsId;

    private Long userAddrId;

    private String name;

    private String code;

    private String simpleName;

    private Date createTime;

    public Long getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(Long logisticsId) {
        this.logisticsId = logisticsId;
    }

    public Long getUserAddrId() {
        return userAddrId;
    }

    public void setUserAddrId(Long userAddrId) {
        this.userAddrId = userAddrId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }
}