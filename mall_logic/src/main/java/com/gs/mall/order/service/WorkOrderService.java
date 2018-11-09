package com.gs.mall.order.service;

import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.mall.common.po.Logistics;
import com.gs.mall.common.service.BaseService;
import com.gs.mall.order.dto.WorkOrderCommodityDto;
import com.gs.mall.order.dto.WorkOrderDetailDto;
import com.gs.mall.order.po.RefundCommodity;
import com.gs.mall.order.po.WorkOrder;
import com.gs.mall.user.po.User;

import java.util.List;
import java.util.Map;

/**
 * WorkOrder service interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:13
 */
public interface WorkOrderService extends BaseService<WorkOrder,java.lang.String>{

    /**
     * 保存工单
     *
     * @param params
     * @return
     */
    ResponseResult save(Map<String, Object> params, User operator);

    /**
     * 同意发货
     *
     * @param workOrderId
     * @param operator
     * @return
     */
    ResponseResult updateAgreeDelivery(String workOrderId, User operator);


    /**
     * app发货
     *
     * @param workOrderId
     * @param logistics
     * @param operator
     * @return
     */
    ResponseResult updateDelivery(String workOrderId, Logistics logistics, User operator);

    /**
     * 退款完成
     *
     * @param workOrderId 退款订单
     * @param amount      退款金额
     * @param operator    操作人
     * @return
     */
    ResponseResult updateFinish(String workOrderId, Integer amount, User operator);


    /**
     * 关闭(不同意)
     *
     * @param workOrderId
     * @param operator
     * @return
     */
    ResponseResult updateClose(String workOrderId, String reason, User operator);

    /**
     * 查询工单记录
     *
     * @param workOrderId
     * @return
     */
    ResponseResult workOrderLogs(String workOrderId);

    /**
     * 查询退货收货地址
     *
     * @param workOrderId
     * @return
     */
    ResponseResult sendBackAddr(String workOrderId);
    /**
     * 统计未处理的退货申请
     *
     * @param
     * @return
     */
    ResponseResult countTodoNums();

    /**
     * 查询工单关联的商品快照
     * @param workOrder
     * @return
     */
    WorkOrderCommodityDto getWorkOrderSnapshoot(WorkOrder workOrder);

    /**
     * 查询工单详情
     * @param workOrder
     * @return
     */
    WorkOrderDetailDto getWorkOrderDetail(WorkOrder workOrder);

    /**
     * 查询退款商品
     * @param workOrderId
     * @return
     */
    List<RefundCommodity> getRefundCommodities(String workOrderId);

	ResponsePageResult selectBypa(Map<String, Object> params, Integer pageNo, Integer pageSize);

}
