package com.gs.mall.order.service;

import com.gs.mall.order.dto.WorkOrderTableDisplayDto;
import com.gs.mall.order.po.*;
import com.gs.mall.order.service.impl.WorkOrderV2Exception;

import java.util.List;

/**
 * Created by huyoucheng on 2018/10/31.
 */
public interface WorkOrderV2Service {

    Boolean submitWorkOrder(WorkOrderV2 workOrderV2,RefundCommodity refundCommodity,String other) throws WorkOrderV2Exception;

    WorkOrderFlowRec currentWorkOrderByOrderIdOrWoId(String order_id);

    List<WorkOrderFlowRec> allExistWorkOrderFlowRec(String order_idOrWo_id);

    WorkOrderFlowRec confirmOrRollBackCurrentFlowAndCreateNextFlow(WorkOrderFlowRec clientCurFlowRec) throws WorkOrderV2Exception;


    List<WorkOrderFlow> getAllWorkOrderFlowsList();

    WorkOrderFlow saveOrUpdate(WorkOrderFlow workOrderFlow);

    Boolean deleteWorkOrderFlow(int id);


    /*
    * 查询工单
    * */
    List<WorkOrderTableDisplayData> getWorkOrderTableDisplayData(WorkOrderTableDisplayDto workOrderTableDisplayDto);


    /*
    * 关闭工单
    * */
    WorkOrderFlowRec closeWorkOrder(WorkOrderFlowRec workOrderFlowRec);

}
