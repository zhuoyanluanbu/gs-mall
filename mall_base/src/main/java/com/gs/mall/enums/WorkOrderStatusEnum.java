package com.gs.mall.enums;

/**
 * 工单状态
 * 10  申请退货（待商家确认） 20 待用户填写快递 (待商家收货) 30 待退款(已商家收货) 40 已退款（处理完成）50 关闭
 * Created by huangyp on 2017/8/28.
 */
public enum WorkOrderStatusEnum {

    APPLY(10,"申请退货"),
    WAITDELIVERY(20,"待发货"),
    WAITREFUND(30,"待退款"),
    SUCCESS(40,"已退款"),
    CLOSE(50,"关闭");

    private Integer value;

    private String name;

    WorkOrderStatusEnum(Integer value, String name){
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public boolean judge(Integer status) {
        return this.value.equals(status);
    }
}
