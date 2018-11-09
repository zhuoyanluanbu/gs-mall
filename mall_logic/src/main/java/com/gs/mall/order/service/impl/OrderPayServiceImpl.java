package com.gs.mall.order.service.impl;

import com.gs.common.openapi.AlipaySignature;
import com.gs.common.openapi.WatiSignContent;
import com.gs.common.openapi.config.AppSecreConfig;
import com.gs.common.openapi.service.PayApiService;
import com.gs.common.result.ResponseResult;
import com.gs.common.util.StringUtils;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.order.po.Order;
import com.gs.mall.order.service.OrderPayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.util.*;

/**
 * 订单支付实现
 * Created by huangyp on 2017/8/23.
 */
@Service("orderPayService")
public class OrderPayServiceImpl implements OrderPayService {


    private static final Logger logger = Logger.getLogger(OrderPayServiceImpl.class);

    /**
     * 支付超时时间
     */
    @Value("${mall.pay.timeout.minute}")
    private Integer payTimeout;

    /**
     * 支付回调链接
     */
    @Value("${mall.pay.callback.url}")
    private String payCallbackUrl;

    /**
     * 关闭回调链接
     */
    @Value("${mall.close.callback.url}")
    private String closeCallbackUrl;


    @Override
    public ResponseResult createTrade(Order order) {
        Map<String, String> biz = new HashMap<>();
        String out_trade_no = order.getOrderId();
        biz.put("out_trade_no",out_trade_no);  //商户订单号
        biz.put("total_amount", String.valueOf(order.getTotalPrice()));//NumberUtil.amountToLongToString(order.getTotalPrice()));  //订单总金额
        biz.put("buyer_pay_amount", String.valueOf(order.getPayPrice()));//NumberUtil.amountToLongToString(order.getPayPrice()));  //用户实际支付的金额
        biz.put("open_id", order.getOwnerOpenId()); //买家的用户id,和buyer_logon_id不能同时为空
        biz.put("subject", "自营商城-在线支付");  //订单标题
        biz.put("body", "自营商城-在线支付"); //对一笔交易的具体描述信息
        biz.put("timeout_express", payTimeout+"m");  //该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
        biz.put("passback_params", "out_trade_no="+out_trade_no);    //公用回传参数
        //String appId, String appSecretKey, String appPrivateSecret
        try {
            ResponseResult rr = PayApiService.createTrade(getConfig(), biz, payCallbackUrl);
            return rr;
        } catch ( Exception e ) {
            logger.error("积分创建交易失败：", e);
        }
        return ResponseResult.failInstance();
    }

    public AppSecreConfig getConfig() {
        return AppSecreConfig.build(Constant.appID, Constant.appSecretKey, Constant.appPrivateSecret,Constant.openPublicKey);
    }

    @Override
    public ResponseResult closeTrade(String orderId, String tradeNo) {
       /* Map<String, Object> biz = new HashMap<>();
        String out_trade_no = orderId;
        biz.put("out_trade_no", out_trade_no);  //商户订单号
        biz.put("trade_no", tradeNo);  //订单总金额
        biz.put("operator_id", "");*/
        try {
            ResponseResult rr = PayApiService.closeTrade(getConfig(), orderId, tradeNo, closeCallbackUrl);
            return rr;
        } catch ( Exception e ) {
            logger.error( "积分关闭交易失败：" , e );
        }
        return ResponseResult.failInstance();
    }

    @Override
    public ResponseResult queryTrade(String orderId, String tradeNo) {
        try {
            ResponseResult rr = PayApiService.queryTrade(getConfig(), orderId, tradeNo);
            return rr;
        } catch ( Exception e ) {
            logger.error( "积分关闭交易失败：" , e );
        }
        return ResponseResult.failInstance();
    }

    @Override
    public ResponseResult tradeCallbackCheckSign(Map<String, String> filter) {
        String app_id = filter.get("app_id"); //支付系统的外部订单号，就是本地订单ID
        if( ! Constant.appID.equals(app_id) ) {
            return ResponseResult.failInstance();
        }
        //参数集合
        SortedMap<String, String> sortedMap = new TreeMap<>();
        Set<Map.Entry<String, String>> set = filter.entrySet();
        for( Map.Entry<String, String> me : set ) {
            if( "sign".equals(me.getKey()) ) {
                continue;
            }
            String val = "";
            if(!StringUtils.isEmpty(me.getValue())) {
                try {
                    val = URLDecoder.decode(me.getValue(), "utf-8");
                } catch (Exception e) {}
            }
            sortedMap.put(me.getKey(),val);
        }

        String reqSign = filter.get("sign");
        //请求参数按照key=value&key=value方式拼接的未签名原始字符串：
        try {
            String payCallbackSignContent = WatiSignContent.getWaitSignContent(sortedMap);
            logger.debug("######交易回调：待签名内容=" + payCallbackSignContent);
            reqSign = URLDecoder.decode(reqSign, "utf-8");
            boolean check = AlipaySignature.rsa256CheckContent(payCallbackSignContent, reqSign, Constant.openPublicKey, "utf-8");
            logger.debug("######交易回调：验签结果=" + check);
            if( check ) {
                return ResponseResult.successInstance();
            }
        }catch(Exception e){
            logger.error("check sign fail : ",e);
        }
        return ResponseResult.failInstance();
    }
}
