package com.gs.mall.order.controller;

import com.alibaba.fastjson.JSON;
import com.gs.common.openapi.config.AppSecreConfig;
import com.gs.common.openapi.model.UserInfo;
import com.gs.common.openapi.service.UserApiService;
import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.common.util.DateUtil;
import com.gs.common.util.StringUtils;
import com.gs.mall.base.controller.BaseController;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.po.Logistics;
import com.gs.mall.common.service.LogisticsService;
import com.gs.mall.common.util.PageUtil;
import com.gs.mall.order.po.Order;
import com.gs.mall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台订单管理
 * Created by huangyp on 2017/8/24.
 */
@RestController("manageOrderController")
@RequestMapping("/manage/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private LogisticsService logisticsService;
    
    private long dayTimeMills = 86400000L;

    /**
     * 商户订单查询
     * @param requset
     * @return
     */
    @RequestMapping("/list")
    public ResponsePageResult list(@RequestParam Map<String,Object> params, PageUtil pageUtil, HttpServletRequest requset){
        params.put("merchantId", getMerchant(requset).getMerchantId());
        /*if( params.get("status") == null ) {
            params.put("status", OrderStatusEnum.WAITDELIVERY.getValue());
        }*/
        String bcd = (String) params.remove("beginCreateDate");
        if( ! StringUtils.isEmpty(bcd) ) {
            Date date = DateUtil.parseStringToDate(bcd);
            if(date == null ) {
                return ResponsePageResult.instance(102010);
            } else {
                params.put("beginCreateDate",date);
            }
        }
        String ecd = (String) params.remove("endCreateDate");
        if( ! StringUtils.isEmpty(ecd) ) {
            Date date2 = DateUtil.parseStringToDate(ecd);
            if(date2 == null ) {
                return ResponsePageResult.instance(102010);
            } else {
                long l = (date2.getTime()+dayTimeMills-1);
                params.put("endCreateDate", new Date(l));
            }
        }
        return orderService.getListByPage(params, pageUtil.getPageNo(), pageUtil.getPageSize());
    }

    /**
     * 后台发货
     * @return
     */
    @RequestMapping(value="/deliverOrder", method = RequestMethod.POST)
    public ResponseResult deliverOrder(String orderId, Logistics logistics, HttpServletRequest request) {
        if( StringUtils.isEmpty(logistics.getSimpleName()) ||
                StringUtils.isEmpty(logistics.getName()) ||
                StringUtils.isEmpty(logistics.getCode())) {
            return ResponseResult.instance(101001);
        }
        ResponseResult responseResult = null;
        if( logistics.getLogisticsId() == null || logistics.getLogisticsId() <= 0  ) {
            if( StringUtils.isEmpty(orderId) ) {
                return ResponseResult.instance(101001);
            }
            responseResult = orderService.updateDeliver(orderId,new Date(),getOperator(request),logistics);
        } else {
            responseResult = logisticsService.saveOrUpdate(logistics) ? ResponseResult.successInstance() : ResponseResult.failInstance();
        }
        return responseResult;
    }
    
    
    @RequestMapping(value="/userinfo", method = RequestMethod.GET)
    public ResponseResult deliverOrder(String orderId) {
    	if (StringUtils.isEmpty(orderId) ) {
			return ResponseResult.failInstance("输入正确的订单号");
		}
    	String[] split = orderId.split(",");
    	List<Map<String, Object>> result = new ArrayList<>();
    	for (String id : split) {
    		Order order = orderService.getById(id);
    		if (order == null ) {
    			return ResponseResult.failInstance("订单查询为空");
    		}
    		AppSecreConfig config = AppSecreConfig.build(Constant.appID, Constant.appSecretKey, Constant.appPrivateSecret);
    		ResponseResult rr = UserApiService.getUserInfo(config, order.getOwnerOpenId());
    		if (rr.getCode() != 0 || rr.getData() == null ) {
    			return rr;
    		}
    		UserInfo userInfo = (UserInfo) rr.getData();
    		Map<String, Object> res = new HashMap<>();
    		res.put("订单号", order.getOrderId());
    		res.put("付款人姓名", userInfo.getUserName());
    		res.put("付款人手机号", userInfo.getUserMobile());
    		res.put("付款时间", order.getPayTime());
    		result.add(res);
		}
    	return ResponseResult.successInstance().setData(result);
    }

}
