package com.gs.mall.order.service;

import com.gs.mall.order.po.RefundCommodity;
import com.gs.mall.order.po.WorkOrderFlow;
import com.gs.mall.order.po.WorkOrderFlowRec;
import com.gs.mall.order.po.WorkOrderV2;
import com.gs.mall.order.service.impl.WorkOrderV2Exception;

import java.util.List;
import java.util.Map;

/**
 * Created by huyoucheng on 2018/10/31.
 */
public interface WorkOrderV2Service {

    Boolean submitWorkOrder(WorkOrderV2 workOrderV2,RefundCommodity refundCommodity);

    WorkOrderFlowRec currentWorkOrderByOrderIdOrWoId(String order_id);

    List<WorkOrderFlowRec> allExistWorkOrderFlowRec(String order_idOrWo_id);

    WorkOrderFlowRec confirmOrRollBackCurrentFlowAndCreateNextFlow(WorkOrderFlowRec clientCurFlowRec) throws WorkOrderV2Exception;


    List<WorkOrderFlow> getAllWorkOrderFlowsList();

    WorkOrderFlow saveOrUpdate(WorkOrderFlow workOrderFlow);

    Boolean deleteWorkOrderFlow(int id);



}
