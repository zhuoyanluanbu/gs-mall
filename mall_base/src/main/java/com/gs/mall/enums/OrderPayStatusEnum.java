package com.gs.mall.enums;

/**
 * 支付状态：0 未支付  1 已支付  2 已退款
 * Created by huangyp on 2017/8/21.
 */
public enum OrderPayStatusEnum {

    NONPAYMENT(0,"未支付"),

    PAYMENTED(1,"已支付"),

    REFUNDED(2,"已退款") ;

    private Integer value;

    private String name;

    OrderPayStatusEnum(Integer value, String name){
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
