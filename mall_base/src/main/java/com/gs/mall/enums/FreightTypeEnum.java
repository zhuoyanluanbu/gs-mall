package com.gs.mall.enums;

/**
 * 费用类型
 * Created by huangyp on 2017/8/17.
 */
public enum FreightTypeEnum {

    NORMAL(1,"标准费用"),

    INDEPENDENT(2,"独立费用");//INDEPENDENT

    private Integer value;

    private String name;

    FreightTypeEnum(Integer value,String name){
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
