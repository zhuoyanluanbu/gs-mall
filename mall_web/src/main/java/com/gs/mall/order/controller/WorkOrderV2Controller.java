package com.gs.mall.order.controller;

import com.alibaba.fastjson.JSON;
import com.gs.common.result.ResponseResult;
import com.gs.mall.order.po.*;
import com.gs.mall.order.service.WorkOrderV2Service;
import com.gs.mall.order.service.impl.WorkOrderV2Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by huyoucheng on 2018/10/31.
 */
@RestController
@RequestMapping(value = "/back/workOrder",produces = "application/json;charset=utf-8")
public class WorkOrderV2Controller {

    @Autowired
    WorkOrderV2Service workOrderV2Service;

    /*
    * 提交一个工单
    * */
    @RequestMapping(value = "/submit",method = RequestMethod.PUT)
    public ResponseResult submitWorkOrder(@RequestBody WorkOrderClientReq workOrderClientReq){
        return ResponseResult.successInstance().setData(workOrderV2Service.submitWorkOrder(workOrderClientReq.getWorkOrderV2(),workOrderClientReq.getRefundCommodity()));
    }

    /*
    * 查看当前步骤
    * */
    @RequestMapping(value = "/current/{orderIdOrWoId}",method = RequestMethod.GET)
    public ResponseResult currentWorkOrderFlowByOrderId(@PathVariable("orderIdOrWoId") String orderIdOrWoId){
        return ResponseResult.successInstance().setData(workOrderV2Service.currentWorkOrderByOrderIdOrWoId(orderIdOrWoId));
    }

    /*
    * 确认当前步骤或者不通过
    * */
    @RequestMapping(value = "/current/{passOrNot}",method = RequestMethod.POST)
    public ResponseResult currentWorkOrderFlowPassOrNot(@PathVariable("passOrNot") int passOrNot,@RequestBody WorkOrderFlowRec clientWorkOrderFlowRec){
        //1通过 2驳回
        clientWorkOrderFlowRec.setStatus(passOrNot);
//        clientWorkOrderFlowRec.setOperation_from(1);//后台操作设置成1;
        try {
            return ResponseResult.successInstance().setData(workOrderV2Service.confirmOrRollBackCurrentFlowAndCreateNextFlow(clientWorkOrderFlowRec));
        }catch (WorkOrderV2Exception e){
            return ResponseResult.instance(e.getCode(),e.getMsg());
        }
    }





    @RequestMapping(value = "/wordOrderFlows",method = RequestMethod.GET)
    public ResponseResult wordOrderFlows(){
        return ResponseResult.successInstance().setData(workOrderV2Service.getAllWorkOrderFlowsList());
    }

    @RequestMapping(value = "/wordOrderFlows",method = RequestMethod.POST)
    public ResponseResult wordOrderFlowsInsertOrUpdate(@RequestBody WorkOrderFlow workOrderFlow){
        return ResponseResult.successInstance().setData(workOrderV2Service.saveOrUpdate(workOrderFlow));
    }

    @RequestMapping(value = "/wordOrderFlows/insetlist",method = RequestMethod.POST)
    public ResponseResult wordOrderFlowsInsertOrUpdate(@RequestBody List<WorkOrderFlow> workOrderFlows){
        int[] i = {1};
        workOrderFlows.forEach(w -> {
            w.setSort(i[0]);
            w.setId(-1);
            w = workOrderV2Service.saveOrUpdate(w);
            i[0]++;
        });
        return ResponseResult.successInstance().setData(workOrderFlows);
    }

}
