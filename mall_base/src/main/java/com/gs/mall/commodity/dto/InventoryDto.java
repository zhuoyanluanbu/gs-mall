package com.gs.mall.commodity.dto;

/**
 * Created by Administrator on 2017/12/22.
 */
public class InventoryDto {

    /**
     * 商品ID
     */
    private Long commodityId;

    /**
     * skuId
     */
    private String skuId;

    /**
     * 库存Id
     */
    private Integer inventory;

    /**
     * 库存变更时间戳
     */
    private Long timestamp;

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
