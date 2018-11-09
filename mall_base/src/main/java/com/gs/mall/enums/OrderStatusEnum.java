package com.gs.mall.enums;

/**
 *
 * 订单状态：1000 下单成功（待付款）  1100 付款成功（待发货） 1200 已发货（待收货） 1300  已完成 1400 订单关闭
 * Created by huangyp on 2017/8/21.
 */
public enum OrderStatusEnum {

    WAITPAYMENT(1000,"待付款"),
    WAITDELIVERY(1100,"待发货"),
    WAITRECEIPT(1200,"待收货"),
    SUCCESS(1300,"已完成"),
    CLOSE(1400,"已关闭");

    private Integer value;

    private String name;

    OrderStatusEnum(Integer value, String name){
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
