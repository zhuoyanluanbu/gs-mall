package com.gs.mall.enums;

/**
 * @auther zhangxinyan
 * @create 2017-09-01 14:07
 */

public enum SolrEnum {
    COMMODITY(1,"商品");
    private Integer value;

    private String name;

    SolrEnum(Integer value,String name ){
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
