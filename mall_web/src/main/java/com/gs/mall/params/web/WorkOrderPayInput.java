package com.gs.mall.params.web;


import com.gs.mall.params.common.WorkOrderInput;

/**
 * Created by chenchuan on 2017/8/30
 */
public class WorkOrderPayInput extends WorkOrderInput {
    Integer amount;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
