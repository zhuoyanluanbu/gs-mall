package com.gs.mall.enums;

/**
 * 交易类型
 * 类型：1 订单入账  2订单退款  3 转账
 * Created by huangyp on 2017/8/23.
 */
public enum TradeTypeEnum {

    INCOME(1,"入账"),
    REFUND(2,"退款"),
    TRANSFER(3,"转账");//transfer

    private Integer value;

    private String name;

    TradeTypeEnum(Integer value, String name){
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public boolean judge(Integer type) {
        return value.equals(type);
    }

    public static String get(Integer type) {
        for (TradeTypeEnum typeEnum:values()) {
            if(typeEnum.value.equals(type)){
                return typeEnum.name;
            }
        }
        return "";
    }
}
