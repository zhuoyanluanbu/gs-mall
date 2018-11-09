package com.gs.mall.order.controller;

import com.alibaba.fastjson.JSON;
import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.common.result.StatusCodeConfig;
import com.gs.common.util.StringUtils;
import com.gs.mall.base.controller.BaseController;
import com.gs.mall.common.util.PageUtil;
import com.gs.mall.enums.WorkOrderTypeEnum;
import com.gs.mall.order.po.WorkOrder;
import com.gs.mall.order.service.WorkOrderService;
import com.gs.mall.params.app.WorkOrderLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * 工单控制类
 * Created by huangyp on 2017/8/28.
 */
@RestController("appRestController")
@RequestMapping("/app/workOrder")
public class WorkOrderController extends BaseController {

    @Autowired
    private WorkOrderService workOrderService;

    /**
     * Method:查询用户退货申请记录
     * param{
     * openId:用户标识
     * }
     * Created by chenchuan
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponsePageResult list(@RequestParam Map<String, Object> params, PageUtil page, HttpServletRequest request) {
        if ( StringUtils.isEmpty((String) params.get("openId")) ) {
            params.put("openId", getUserInfo(request).getOpenId());
        }
        ResponsePageResult result = workOrderService.selectBypa(params, page.getPageNo(), page.getPageSize());
        if(result.getData()!=null){
            result.setData(((List<WorkOrder>) result.getData()).stream()
                    .map(workOrder -> workOrderService.getWorkOrderSnapshoot(workOrder)).collect(toList()));
        }

        return result;
    }

    /**
     * 提交退款
     *
     * @param params{ openId //用户openId
     *                orderId //订单Id
     *                commodities[
     *                {snapshootId:15,num:1},
     *                {snapshootId:10,num:2}
     *                ] //退款商品信息
     *                type   //退款申请类型：1：只退款不退货；2：退货退款
     *                comment //退款原因
     *                imgUri1  //退款凭证照片，最多上传三张
     *                imgUri2  //申请类型为2，需要退货的情况，至少有一张凭证照片
     *                imgUri3
     *                }
     * @param requset
     * @return
     */
    @RequestMapping(value = "/submitRefund", method = RequestMethod.POST)
    public ResponseResult submitRefund(@RequestParam Map<String, Object> params, HttpServletRequest requset) {
        if (StringUtils.isEmpty((String) params.get("orderId"))) {
            return ResponseResult.instance(103001);
        } else if (params.get("commodities") == null) {
            return ResponseResult.instance(103002);
        }
        String typeStr = (String) params.get("type");
        Short type = null;
        if (StringUtils.isEmpty(typeStr)) {
            return ResponseResult.instance(103003);
        } else {
            type = Short.valueOf(typeStr);
            if (type < WorkOrderTypeEnum.ONLYREFUND.getValue() ||
                    type > WorkOrderTypeEnum.REFUNDANDGOODS.getValue()) {
                return ResponseResult.instance(103003);
            }
        }
        if (WorkOrderTypeEnum.REFUNDANDGOODS.getValue().equals(type)
                && StringUtils.isEmpty((String) params.get("imgUri1")) &&
                StringUtils.isEmpty((String) params.get("imgUri2")) &&
                StringUtils.isEmpty((String) params.get("imgUri3"))) {
            return ResponseResult.instance(103004);
        }
        if (StringUtils.isEmpty((String) params.get("comment"))) {
            return ResponseResult.instance(103005);
        }
        return workOrderService.save(params, this.getOperator(requset));
    }

    /**
     * Method:退货接收地址
     * Param：{
     * workOrderId:工单ID
     * }
     * Created by chenchuan
     */
    @RequestMapping(value = "/sendDackAddress", method = RequestMethod.GET)
    public ResponseResult sendBackAddress(String workOrderId) {
        return workOrderService.sendBackAddr(workOrderId);
    }

    /**
     * Method:提交/更新物流信息
     * Param：{
     * name：物流名称
     * simpleName：名称拼音
     * code：快递单号
     * userAddressId:寄回地址id,默认为0->自营商城地址ID
     * }
     * Created by chenchuan
     */
    @RequestMapping(value = "/updateDelivery", method = RequestMethod.POST)
    public ResponseResult updateDelivery(WorkOrderLogistics input, HttpServletRequest request) {
        if (StringUtils.isEmpty(input.getWorkOrderId())) {
            return ResponseResult.instance(103010);
        } else if (StringUtils.isEmpty(input.getName()) || StringUtils.isEmpty(input.getSimpleName())) {
            return ResponseResult.instance(103013);
        } else if (StringUtils.isEmpty(input.getCode())) {
            return ResponseResult.instance(103014);
        }
        return workOrderService.updateDelivery(input.getWorkOrderId(), input.genLogistis(), getOperator(request));
    }

    /**
     * Method:查看工单操作记录
     * Param：{
     * workOrderId:工单ID
     * }
     * Created by chenchuan
     */
    @RequestMapping(value = "/trackLog", method = RequestMethod.GET)
    public ResponseResult trackLog(String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            return ResponseResult.instance(103010);
        }
        return workOrderService.workOrderLogs(workOrderId);
    }

    /**
     * Method:取消退款申请
     * Param：{
     * workOrderId:工单ID
     * }
     * Created by chenchuan
     */
    @RequestMapping(value = "/cancelRefund", method = RequestMethod.POST)
    public ResponseResult cancelRefund(String workOrderId, HttpServletRequest request) {
        if (StringUtils.isEmpty(workOrderId)) {
            return ResponseResult.instance(103010);
        }
        return workOrderService.updateClose(workOrderId,
                StatusCodeConfig.getValue("103108"), this.getOperator(request));
    }

    /**
     * Method:查看工单关闭原因
     * Param:{
     * workOrderId:工单ID
     * }
     * Created by chenchuan
     */
    @RequestMapping(value = "/closeReason", method = RequestMethod.GET)
    public ResponseResult closeReason(String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            return ResponseResult.instance(103010);
        }
        WorkOrder workOrder = workOrderService.getById(workOrderId);
        ResponseResult rslt = ResponseResult.successInstance();
        rslt.setData(workOrder.getCloseReason());
        return rslt;
    }

}
