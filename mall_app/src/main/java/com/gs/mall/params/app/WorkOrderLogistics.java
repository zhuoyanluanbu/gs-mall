package com.gs.mall.params.app;

import com.gs.mall.common.po.Logistics;
import com.gs.mall.params.common.WorkOrderInput;

import java.util.Date;

/**
 * Created by chenchuan on 2017/8/29
 */
public class WorkOrderLogistics extends WorkOrderInput {
    String name;
    String simpleName;
    String code;
    Long userAddressId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(Long userAddressId) {
        this.userAddressId = userAddressId;
    }

    public Logistics genLogistis() {
        Logistics lgs = new Logistics();
        lgs.setCode(getCode());
        lgs.setSimpleName(getSimpleName());
        lgs.setName(getName());
        lgs.setCreateTime(new Date());
        return  lgs;
    }
}
