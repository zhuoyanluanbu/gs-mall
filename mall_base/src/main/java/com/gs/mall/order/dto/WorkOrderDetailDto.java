package com.gs.mall.order.dto;

import java.util.List;


/**
 * Created by chenchuan on 2017/8/31
 */
public class WorkOrderDetailDto extends WorkOrderCommodityDto {

    /**
     * 关联工单
     */
    private List<WorkOrderSimpleDto> linksWorkOrder;

    public List<WorkOrderSimpleDto> getLinksWorkOrder() {
        return linksWorkOrder;
    }

    public void setLinksWorkOrder(List<WorkOrderSimpleDto> linksWorkOrder) {
        this.linksWorkOrder = linksWorkOrder;
    }
}
