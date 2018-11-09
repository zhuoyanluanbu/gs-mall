package com.gs.mall.enums;

/**
 * 商品类型
 * Created by huangyp on 2017/8/17.
 * 1 实物商品（物流发货） 2 虚拟商品（无需物流） 3 电子卡券（无需物流）
 */
public enum CommodityTypeEnum {

    PHYSICAL(1,"实体商品"),

    FICTITIOUS(2,"虚拟商品"),

    ELECTRONICCARD(3,"电子卡券");

    private Integer value;

    private String name;

    CommodityTypeEnum(Integer value,String name){
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

