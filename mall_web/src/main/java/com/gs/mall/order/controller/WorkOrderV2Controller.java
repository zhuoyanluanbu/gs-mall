package com.gs.mall.order.controller;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.util.StringUtil;
import com.gs.mall.export.ExportEngine;
import com.gs.mall.order.dto.WorkOrderTableDisplayDto;
import com.gs.mall.order.po.*;
import com.gs.mall.order.service.WorkOrderV2Service;
import com.gs.mall.order.service.impl.WorkOrderV2Exception;
import com.gs.mall.params.adapter.TradeDetailDataAdapter;
import com.gs.mall.params.adapter.WorkOrderFlowRecAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huyoucheng on 2018/10/31.
 */
@RestController
@RequestMapping(produces = "application/json;charset=utf-8")
public class WorkOrderV2Controller {

    @Autowired
    WorkOrderV2Service workOrderV2Service;

    /*
    * 提交一个工单
    * @param workOrderClientReq
    * @return
    * */
    @RequestMapping(value = "/app/workOrder/submit",method = RequestMethod.PUT)
    public ResponseResult submitWorkOrderApp(@RequestBody WorkOrderClientReq workOrderClientReq){
        workOrderClientReq.getWorkOrderV2().setFromwhere(0);
        workOrderClientReq.getRefundCommodity().setWoId(workOrderClientReq.getWorkOrderV2().getWo_id());
        try {
            return ResponseResult.successInstance().setData(workOrderV2Service.submitWorkOrder(workOrderClientReq.getWorkOrderV2(),workOrderClientReq.getRefundCommodity(),workOrderClientReq.getOther()));
        } catch (WorkOrderV2Exception e) {
            e.printStackTrace();
            return ResponseResult.instance(e.getCode(),e.getMsg());
        }
    }
    @RequestMapping(value = "/back/workOrder/submit",method = RequestMethod.PUT)
    public ResponseResult submitWorkOrderManager(@RequestBody WorkOrderClientReq workOrderClientReq){
        workOrderClientReq.getWorkOrderV2().setFromwhere(1);
        workOrderClientReq.getRefundCommodity().setWoId(workOrderClientReq.getWorkOrderV2().getWo_id());
        try {
            return ResponseResult.successInstance().setData(workOrderV2Service.submitWorkOrder(workOrderClientReq.getWorkOrderV2(),workOrderClientReq.getRefundCommodity(),workOrderClientReq.getOther()));
        } catch (WorkOrderV2Exception e) {
            e.printStackTrace();
            return ResponseResult.instance(e.getCode(),e.getMsg());
        }
    }

    /*
    * 查看当前步骤
    * @param orderIdOrWoId
    * @return
    * */
    @RequestMapping(value = "/back/workOrder/workOrderFlowRec/current/{orderIdOrWoId}",method = RequestMethod.GET)
    public ResponseResult currentWorkOrderFlowByOrderId(@PathVariable("orderIdOrWoId") String orderIdOrWoId){
        WorkOrderFlowRec wr = workOrderV2Service.currentWorkOrderByOrderIdOrWoId(orderIdOrWoId);
        wr.setDesc(WorkOrderFlowRecDescription.instanceForManager(wr).getDescription());
        return ResponseResult.successInstance().setData(wr);
    }

    /*
    * 获取所有工单的操作记录
    * @param orderIdOrWoId
     * @return
    * */
    @RequestMapping(value = "/back/workOrder/workOrderFlowRecs/{orderIdOrWoId}",method = RequestMethod.GET)
    public ResponseResult workOrderFlowRecs(@PathVariable("orderIdOrWoId") String orderIdOrWoId){
        List<WorkOrderFlowRec> workOrderFlowRecList = workOrderV2Service.allExistWorkOrderFlowRec(orderIdOrWoId);
        workOrderFlowRecList.forEach(wr -> {
            wr.setDesc(WorkOrderFlowRecDescription.instanceForManager(wr).getDescription());
        });
        return ResponseResult.successInstance().setData(workOrderFlowRecList);
    }

    /*
    * 确认当前步骤或者不通过
    * @param passOrNot
    * @return
    * */
    @RequestMapping(value = "/back/workOrder/current/{passOrNot}",method = RequestMethod.POST)
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

    /*
    * 关闭工单
    *
    * */
    @RequestMapping(value = "/back/workOrder/close",method = RequestMethod.POST)
    public ResponseResult currentWorkOrderFlowPassOrNot(@RequestBody WorkOrderFlowRec clientWorkOrderFlowRec){
        try {
            return ResponseResult.successInstance().setData(workOrderV2Service.closeWorkOrder(clientWorkOrderFlowRec));
        } catch (WorkOrderV2Exception e) {
            e.printStackTrace();
            return ResponseResult.instance(e.getCode(),e.getMsg());
        }
    }

    @RequestMapping(value = "/back/workOrder/wordOrderFlows",method = RequestMethod.GET)
    public ResponseResult wordOrderFlows(){
        return ResponseResult.successInstance().setData(workOrderV2Service.getAllWorkOrderFlowsList());
    }

    @RequestMapping(value = "/back/workOrder/wordOrderFlows",method = RequestMethod.POST)
    public ResponseResult wordOrderFlowsInsertOrUpdate(@RequestBody WorkOrderFlow workOrderFlow){
        return ResponseResult.successInstance().setData(workOrderV2Service.saveOrUpdate(workOrderFlow));
    }

    @RequestMapping(value = "/back/workOrder/wordOrderFlows/insetlist",method = RequestMethod.POST)
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


    /*
    * 工单搜索(各个工单的最新状态)
    * @param
    * @return
    * */
    @RequestMapping(value = "/back/workOrder/workOrderFlowRecs/newestState/search",method = RequestMethod.POST)
    public ResponseResult wordOrderFlowsSearchNewestState(@RequestBody WorkOrderTableDisplayDto workOrderTableDisplayDto){
        List<WorkOrderTableDisplayData> workOrderTableDisplayDatas = workOrderV2Service.getWorkOrderTableDisplayDataNewestState(workOrderTableDisplayDto);
        return ResponseResult.successInstance().setData(workOrderTableDisplayDatas);
    }

    /*
    * 工单搜索(各个工单的所有状态)
    * @param
    * @return
    * */
    @RequestMapping(value = "/back/workOrder/workOrderFlowRecs/search",method = RequestMethod.POST)
    public ResponseResult wordOrderFlowsSearch(@RequestBody WorkOrderTableDisplayDto workOrderTableDisplayDto){
        List<WorkOrderTableDisplayData> workOrderTableDisplayDatas = workOrderV2Service.getWorkOrderTableDisplayData(workOrderTableDisplayDto);
        return ResponseResult.successInstance().setData(workOrderTableDisplayDatas);
    }


    /*
    * 后台查看工单当前步骤
    * @param orderIdOrWoId
    * @return
    * */
    @RequestMapping(value = "/back/workOrder/workOrderFlowRecs/description/current/{orderIdOrWoId}",method = RequestMethod.GET)
    public ResponseResult currentWordOrderFlowDescription(@PathVariable("orderIdOrWoId") String orderIdOrWoId){
        List<WorkOrderFlowRec> workOrderFlowRecList = workOrderV2Service.allExistWorkOrderFlowRec(orderIdOrWoId);
        List<WorkOrderFlowRecDescription> wo_descriptions = new ArrayList<>();
        for (WorkOrderFlowRec wofr:workOrderFlowRecList){
            wo_descriptions.add(WorkOrderFlowRecDescription.instanceForManager(wofr));
        }
        return ResponseResult.successInstance().setData(wo_descriptions.get(wo_descriptions.size()-1));
    }

    /*
    * 后台查看工单所有流程描述
    * @param orderIdOrWoId
    * @return
    * */
    @RequestMapping(value = "/back/workOrder/workOrderFlowRecs/description/{orderIdOrWoId}",method = RequestMethod.GET)
    public ResponseResult wordOrderFlowsDescription(@PathVariable("orderIdOrWoId") String orderIdOrWoId){
        List<WorkOrderFlowRec> workOrderFlowRecList = workOrderV2Service.allExistWorkOrderFlowRec(orderIdOrWoId);
        List<WorkOrderFlowRecDescription> wo_descriptions = new ArrayList<>();
        for (WorkOrderFlowRec wofr:workOrderFlowRecList){
            wo_descriptions.add(WorkOrderFlowRecDescription.instanceForManager(wofr));
        }
        return ResponseResult.successInstance().setData(wo_descriptions);
    }


    @Resource(name="excelEngine")
    ExportEngine exportEngine;
    @Autowired
    WorkOrderFlowRecAdapter workOrderFlowRecAdapter;
    /*
    * 后台导出工单
    * @param orderIdOrWoId
    * @return
    * */
    @RequestMapping(value = "/back/workOrder/workOrderFlowRecs/export",method = RequestMethod.POST)
    public void wordOrderFlowsDescription(@RequestBody WorkOrderTableDisplayDto workOrderTableDisplayDto,
                                                    HttpServletResponse response){
        try {

            List<WorkOrderTableDisplayData> workOrderTableDisplayDatas = workOrderV2Service.getWorkOrderTableDisplayData(workOrderTableDisplayDto);
            File file = exportEngine.createExportFile(exportEngine.genFile(workOrderTableDisplayDatas,workOrderFlowRecAdapter));
            FileInputStream in = new FileInputStream(file.getPath());
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            OutputStream out = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while( (len=in.read(bytes)) > 0){
                out.write(bytes,0,len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------
    /*
    * app查看描述
    * @param orderIdOrWoId
    * @return
    * */
    @RequestMapping(value = "/app/workOrder/workOrderFlowRecs/description/{orderIdOrWoId}",method = RequestMethod.GET)
    public ResponseResult wordOrderFlowsDescriptionApp(@PathVariable("orderIdOrWoId") String orderIdOrWoId){
        List<WorkOrderFlowRec> workOrderFlowRecList = workOrderV2Service.allExistWorkOrderFlowRec(orderIdOrWoId);
        List<WorkOrderFlowRecDescription> wo_descriptions = new ArrayList<>();
        for (WorkOrderFlowRec wofr:workOrderFlowRecList){
            WorkOrderFlowRecDescription wofrd = WorkOrderFlowRecDescription.instanceForApp(wofr);
            if (StringUtil.isNotEmpty(wofrd.getDescription()))
                wo_descriptions.add(wofrd);
        }
        return ResponseResult.successInstance().setData(wo_descriptions);
    }

    /*
    * mall查看描述
    * @param orderIdOrWoId
    * @return
    * */
    @RequestMapping(value = "/mall/workOrder/workOrderFlowRecs/description/{orderIdOrWoId}",method = RequestMethod.GET)
    public ResponseResult wordOrderFlowsDescriptionMall(@PathVariable("orderIdOrWoId") String orderIdOrWoId){
        List<WorkOrderFlowRec> workOrderFlowRecList = workOrderV2Service.allExistWorkOrderFlowRec(orderIdOrWoId);
        List<WorkOrderFlowRecDescription> wo_descriptions = new ArrayList<>();
        for (WorkOrderFlowRec wofr:workOrderFlowRecList){
            wo_descriptions.add(WorkOrderFlowRecDescription.instanceForMall(wofr));
        }
        return ResponseResult.successInstance().setData(wo_descriptions);
    }

}

