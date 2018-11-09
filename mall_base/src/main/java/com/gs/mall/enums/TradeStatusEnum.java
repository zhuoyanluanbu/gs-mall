package com.gs.mall.enums;

/**
 * 交易状态枚举
 * Created by huangyp on 2017/8/23.
 */
public enum TradeStatusEnum {

    CREATE(0,"创建"),
    SUCCESS(1,"成功"),
    CLOSE(2,"关闭");

    private Integer value;

    private String name;

    TradeStatusEnum(Integer value, String name){
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
