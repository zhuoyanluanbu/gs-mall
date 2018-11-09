package com.gs.mall.order.po;

import com.gs.mall.common.po.MyObject;

/**
 * Created by huyoucheng on 2018/11/5.
 */
public class WorkOrderFlowLink {
    public WorkOrderFlow value;
    public WorkOrderFlowLink pre = null;
    public WorkOrderFlowLink next = null;

    public WorkOrderFlowLink(WorkOrderFlow value) {
        this.value = value;
    }

    public void add(WorkOrderFlowLink workOrderFlowLink){
        WorkOrderFlowLink temp = this;
        while (temp.next != null){
            temp.next.pre = temp;
            temp = temp.next;
        }
        temp.next = workOrderFlowLink;
        temp.next.pre = temp;
    }

}
