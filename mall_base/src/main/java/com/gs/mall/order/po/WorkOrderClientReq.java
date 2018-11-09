package com.gs.mall.order.po;

import com.gs.mall.common.po.MyObject;

/**
 * Created by huyoucheng on 2018/11/5.
 */
public class WorkOrderClientReq extends MyObject {

    private WorkOrderV2 workOrderV2;
    private RefundCommodity refundCommodity;

    public WorkOrderClientReq() {
    }

    public WorkOrderClientReq(WorkOrderV2 workOrderV2, RefundCommodity refundCommodity) {
        this.workOrderV2 = workOrderV2;
        this.refundCommodity = refundCommodity;
    }

    public WorkOrderV2 getWorkOrderV2() {
        return workOrderV2;
    }

    public void setWorkOrderV2(WorkOrderV2 workOrderV2) {
        this.workOrderV2 = workOrderV2;
    }

    public RefundCommodity getRefundCommodity() {
        return refundCommodity;
    }

    public void setRefundCommodity(RefundCommodity refundCommodity) {
        this.refundCommodity = refundCommodity;
    }
}
