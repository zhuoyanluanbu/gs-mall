package com.gs.mall.order;



import com.alibaba.fastjson.JSONObject;
import com.gs.common.util.IdentityUtil;
import com.gs.common.util.StringUtils;
import com.gs.mall.enums.OrderPayStatusEnum;
import com.gs.mall.enums.OrderStatusEnum;
import com.gs.mall.order.po.*;

import java.util.Date;
import java.util.Map;

/**
 * 订单工厂类
 * Created by huangyp on 2017/8/21.
 */
public final class OrderFactory {

    /**
     * 获得订单实例
     * @return
     */
    public static Order getOrderInstance(){
        Order order = getEmptyOrderInstance();
        order.setOrderId(IdentityUtil.getIdentity("G"));
        order.setStatus(OrderStatusEnum.WAITPAYMENT.getValue());
        order.setPayStatus(OrderPayStatusEnum.NONPAYMENT.getValue());
        order.setRefundStatus(0);
        order.setCreateTime(new Date());
        return order;
    }

    public static Order getEmptyOrderInstance(){
        return  new Order();
    }


    /**
     * 根据参数返回实例
     * @param params
     * @return
     */
    public static Order getOrderInstance(Map<String,String> params){
        Order order = getOrderInstance();
        /*
         * userAddrId 用户地址ID,
         * openId 用户OPENID,
         * totalPrice 订单总金额,
         * freightPrice 运费,
         * discountPrice 优惠金额,
         * discountCode 优惠码,
         * discountName 优惠券名称,
         */
        String val = params.get("userAddrId");
        if( StringUtils.isNotEmpty(val) ) {
            order.setUserAddrId(Long.valueOf(val));
        }

        val = params.get("openId");
        if( StringUtils.isNotEmpty(val) ) {
            order.setOwnerOpenId(val);
        }

        val = params.get("totalPrice");
        if( StringUtils.isNotEmpty(val) ) {
            //order.setTotalPrice(NumberUtil.reserve2Decimals(new BigDecimal(val)));
            order.setTotalPrice(Integer.valueOf(val));
            order.setPayPrice(order.getTotalPrice());
        } else {
            order.setTotalPrice(0);
        }

        val = params.get("freightPrice");
        if( StringUtils.isNotEmpty(val) ) {
            //order.setFreightPrice(NumberUtil.reserve2Decimals(new BigDecimal(val)));
            order.setFreightPrice(Integer.valueOf(val));
        } else {
            order.setFreightPrice(0);
        }

        val = params.get("discountPrice");
        if( StringUtils.isNotEmpty(val) ) {
            //order.setDiscountPrice(NumberUtil.reserve2Decimals(new BigDecimal(val)));
            order.setDiscountPrice(Integer.valueOf(val));
        } else {
            order.setDiscountPrice(0);
        }

        val = params.get("discountCode");
        if( StringUtils.isNotEmpty(val) ) {
            order.setDiscountCode(val);
        }

        val = params.get("discountName");
        if( StringUtils.isNotEmpty(val) ) {
            order.setDiscountName(val);
        }
        //BigDecimal price = order.getTotalPrice();
        //price = price.add(order.getFreightPrice()).subtract(order.getDiscountPrice());
        //order.setPayPrice(NumberUtil.reserve2Decimals(price));
        return order;
    }

    /**
     * 创建订单日志实例
     * @param order
     * @return
     */
    public static OrderLog getOrderLogInstance(Order order){
        OrderLog orderLog = new OrderLog();
        //orderLog.setContent();
        orderLog.setOrderId(order.getOrderId());
        orderLog.setOrderStatus(order.getStatus());
        orderLog.setOperateTime(new Date());
        return orderLog;
    }

    /**
     * 创建工单实例
     * @param order
     * @return
     */
    public static WorkOrderLog getWorkOrderLogInstance(WorkOrder order) {
        WorkOrderLog log = new WorkOrderLog();
        log.setWoId(order.getWoId());
        log.setStatus(order.getStatus());
        /*log.setContent(order.);
        log.setOperatorId();
        log.setOperator();
        log.setOperatorType();*/
        log.setOperateTime(new Date());
        return log;
    }

    /**
     * 获得工单实例
     * @param params
     * @return
     */
    public static WorkOrder getWorkOrderInstance(Map<String,Object> params) {
        WorkOrder order = new WorkOrder();
        order.setWoId(IdentityUtil.getIdentity("R"));
        order.setComment((String)params.get("comment"));
        order.setOrderId((String)params.get("orderId"));
        order.setType(Integer.valueOf(params.get("type").toString()));
        order.setImgUri1((String) params.get("imgUri1"));
        order.setImgUri2((String) params.get("imgUri2"));
        order.setImgUri3((String) params.get("imgUri3"));
        order.setCreateTime(new Date());
        order.setIsFreight(0);
        return order;
    }

    /**
     * 订单支付数据
     * @param payData
     * @return
     */
    public static OrderPayData getOrderPayDataInstance(JSONObject payData){
        OrderPayData opayData = new OrderPayData();
        opayData.setOrderId(payData.getString("out_trade_no"));
        opayData.setTradeNo(payData.getString("trade_no"));
        opayData.setPayData(payData.getString("payData"));
        return opayData;
    }
}
