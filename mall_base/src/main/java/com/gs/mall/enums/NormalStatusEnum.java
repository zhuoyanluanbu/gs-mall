package com.gs.mall.enums;

/**
 * 通用状态值
 * Created by huangyp on 2017/8/17.
 */
public enum NormalStatusEnum {

    DELETE(-1,"删除"),

    DISABLE(0,"停用"),

    NORMAL(1,"正常");

    private Integer value;

    private String name;

    NormalStatusEnum(Integer value,String name){
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
