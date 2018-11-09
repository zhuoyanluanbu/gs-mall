package com.gs.mall.params.web;


import com.gs.mall.params.common.WorkOrderInput;

/**
 * 确认退货工单入参
 * Created by chenchuan on 2017/8/28
 */
public class WorkOrderCfmInput  extends WorkOrderInput {
    Boolean isPass;
    String reason;

    public Boolean getIsPass() {
        return isPass;
    }

    public void setIsPass(Boolean pass) {
        isPass = pass;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
