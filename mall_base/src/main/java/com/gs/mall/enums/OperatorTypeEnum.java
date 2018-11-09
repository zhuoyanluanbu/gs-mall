package com.gs.mall.enums;

/**
 * 操作人类型枚举
 * 操作人类型：1 用户 2  商户 3 其他
 * Created by huangyp on 2017/8/23.
 */
public enum  OperatorTypeEnum {


    USER(1,"用户"),

    MECHANT(2,"商户"),

    OTHER(3,"其他");

    private Integer value;

    private String name;

    OperatorTypeEnum(Integer value,String name){
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
