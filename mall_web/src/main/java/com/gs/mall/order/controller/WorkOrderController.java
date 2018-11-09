package com.gs.mall.order.controller;

import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.common.result.StatusCodeConfig;
import com.gs.common.util.DateUtil;
import com.gs.common.util.StringUtils;
import com.gs.mall.base.controller.BaseController;
import com.gs.mall.common.util.PageUtil;
import com.gs.mall.enums.WorkOrderStatusEnum;
import com.gs.mall.order.po.WorkOrder;
import com.gs.mall.order.service.WorkOrderService;
import com.gs.mall.params.common.WorkOrderInput;
import com.gs.mall.params.web.WorkOrderCfmInput;
import com.gs.mall.params.web.WorkOrderPayInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController("manageWorkOrderController")
@RequestMapping("/manage/workOrder")
public class WorkOrderController extends BaseController {

    @Autowired
    WorkOrderService workOrderService;

    @RequestMapping(value = "/todoNums", method = RequestMethod.GET)
    public ResponseResult todoNums() {

        return workOrderService.countTodoNums();
    }

    /**
     * Method:工单列表查询
     * {
     * workOrderId:ID查询
     * status:工单状态
     * }
     * Created by chenchuan
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponsePageResult list(@RequestParam Map<String, Object> params, PageUtil page, HttpServletRequest request) throws ParseException {
        params.put("merchantId", getMerchant(request).getMerchantId());
        String beginDate = (String) params.get("beginDate");
        if( !StringUtils.isEmpty(beginDate) ){
            Date startCreateTime = DateUtil.parseDate(beginDate+ " 00:00:00","yyyy-MM-dd HH:mm:ss");
            if ( startCreateTime == null ) {
                return ResponsePageResult.instance(102010);
            }
            params.put("beginDate",startCreateTime);
        }
        String endDate = (String) params.get("endDate");
        if(!StringUtils.isEmpty(endDate) ){
            Date endCreateTime = DateUtil.parseDate(endDate+ " 23:59:59","yyyy-MM-dd HH:mm:ss");
            if ( endCreateTime == null ) {
                return ResponsePageResult.instance(102010);
            }
            params.put("endDate",endCreateTime);
        }
        if (params.get("status") == null) {
            params.put("status", WorkOrderStatusEnum.APPLY.getValue());
        }
        if(WorkOrderStatusEnum.CLOSE.judge(Integer.valueOf(params.get("status").toString()))){
            params.put("filterReson", StatusCodeConfig.getValue(String.valueOf("103108")));
        }
        ResponsePageResult result = workOrderService.getListByPage(params, page.getPageNo(), page.getPageSize());
        result.setData(((List<WorkOrder>)result.getData()).stream()
                .map(workOrder ->workOrderService.getWorkOrderSnapshoot(workOrder)).collect(toList()));
        return result;
    }
    @RequestMapping(value="/refundCommodity")
    public ResponseResult refundCommodity(String workOrderId){
        return ResponseResult.successInstance().setData(workOrderService.getRefundCommodities(workOrderId));
    }
    /**
     * Method:工单详情
     * Param:{
     * workOrderId:工单ID
     * }
     * Created by chenchuan on 2017/08/28.
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseResult detail(WorkOrderInput input) {
        if (StringUtils.isEmpty(input.getWorkOrderId())) {
            return ResponseResult.instance(103010);
        }
        WorkOrder workOrder = workOrderService.getById(input.getWorkOrderId());
        ResponseResult result = ResponseResult.successInstance();
        result.setData(workOrderService.getWorkOrderDetail(workOrder));
        return result;
    }

    /**
     * Method:后台确认退货申请
     * Param:{
     * workerOrderId:工单ID
     * isPass:是否通过 0|null :驳回 1:同意退货
     * reason:不通过理由
     * }
     * Created by chenchuan on 2017/08/28.
     */
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public ResponseResult confirmRefund(WorkOrderCfmInput cfmInput, HttpServletRequest request) {
        if (StringUtils.isEmpty(cfmInput.getWorkOrderId())) {
            return ResponseResult.instance(103010);
        }
        ResponseResult responseResult = null;
        if (cfmInput.getIsPass() == null || !cfmInput.getIsPass()) {
            if (StringUtils.isEmpty(cfmInput.getReason())) {
                return ResponseResult.instance(103011);
            }
            responseResult = workOrderService.updateClose(cfmInput.getWorkOrderId(), cfmInput.getReason(),getOperator(request));
        } else {
            responseResult = workOrderService.updateAgreeDelivery(cfmInput.getWorkOrderId(), getOperator(request));
        }
        return responseResult;
    }

    /**
     * Method:确认退款
     * Param：{
     * workOrderId:工单ID
     * amount:确认退款金额
     * }
     * Created by chenchuan
     */
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public ResponseResult refundPay(WorkOrderPayInput wpInput, HttpServletRequest request) {
        if (StringUtils.isEmpty(wpInput.getWorkOrderId())) {
            return ResponseResult.instance(103010);
        }
        if (null == wpInput.getAmount()) {
            return ResponseResult.instance(103015);
        }
        return workOrderService.updateFinish(wpInput.getWorkOrderId(), wpInput.getAmount(), getOperator(request));
    }
}
