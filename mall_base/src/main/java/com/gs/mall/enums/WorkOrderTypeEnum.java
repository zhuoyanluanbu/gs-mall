package com.gs.mall.enums;

import com.gs.mall.order.po.WorkOrder;

/**
 * 订单
 * Created by huangyp on 2017/8/28.
 */
public enum WorkOrderTypeEnum {

    ONLYREFUND(1,"只退款不退货"),

    REFUNDANDGOODS(2,"退款退货");

    private Integer value;

    private String name;

    WorkOrderTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /**
     * Method:通过value判断枚举类型
     * Created by chenchuan
     */
    public boolean judge(WorkOrder workOrder) {
        return this.value==workOrder.getType();
    }
}
