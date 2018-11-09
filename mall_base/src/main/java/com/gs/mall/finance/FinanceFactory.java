package com.gs.mall.finance;

import com.gs.common.util.IdentityUtil;
import com.gs.mall.enums.NormalStatusEnum;
import com.gs.mall.finance.po.*;
import com.gs.mall.order.po.Order;
import com.gs.mall.order.po.WorkOrder;

import java.util.Date;
import java.util.Map;

/**
 * 财务工厂类
 * Created by huangyp on 2017/8/23.
 */
public final class FinanceFactory {


    /**
     * 创建交易明细实例
     * @param params
     * @return
     */
    public static TradeDetail getTradeDetailInstance(Map<String,String> params){
        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setTdId(IdentityUtil.getNumberIdentity(20));
        tradeDetail.setFormId(params.get("out_trade_no"));
        tradeDetail.setTradeNo(params.get("trade_no"));
        tradeDetail.setCreateTime(new Date());
        return tradeDetail;
    }

    /**
     * 创建对账单实例
     * @param order
     * @return
     */
    public static TradeStatement getTradeStatementInstance(Order order){
        TradeStatement ts = new TradeStatement();
        ts.setAmount(order.getTotalPrice());
        ts.setMerchantId(order.getMerchantId());
        ts.setOrderId(order.getOrderId());
        //ts.setSupplierId();
        ts.setStatus(NormalStatusEnum.NORMAL.getValue());
        ts.setCreateTime(new Date());
        return ts;
    }

    /**
     * 创建访问记录实例
     * @param userOpenId
     * @param userName
     * @param ip
     * @param deviceInfo
     * @return
     */
    public static NetflowRecord getNetflowRecordInstance(String userOpenId,
                                                         String userName,
                                                         String ip, String deviceInfo){
        NetflowRecord nr = new NetflowRecord();
        nr.setOpenId(userOpenId);
        nr.setUserName(userName);
        nr.setIp(ip);
        nr.setDeviceInfo(deviceInfo);
        nr.setCreateTime(new Date());
        return nr;
    }

    /**
     * 创建流量访问次数实例
     * @param times
     * @param date
     * @return
     */
    public static NetflowTimes getNetflowTimesInstance(Integer times, java.sql.Date date){
        NetflowTimes nt = new NetflowTimes();
        nt.setTimes(times);
        nt.setCreateDate(date);
        return nt;
    }

    /**
     * 创建交易金额实例
     * @param
     * @param amount
     * @param date
     * @return
     */
    public static TradeAmount getTradeAmountInstance(Long merchantId, Integer amount, java.sql.Date date){
        TradeAmount nt = new TradeAmount();
        nt.setMerchantId(merchantId);
        nt.setAmount(amount);
        nt.setCreateDate(date);
        return nt;
    }

    public static TradeDetail getTradeDetailFromWorkOrder(WorkOrder workOrder){
        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setTdId(IdentityUtil.getNumberIdentity(20));
        tradeDetail.setFormId(workOrder.getWoId());
        tradeDetail.setCreateTime(new Date());
        return tradeDetail;
    }
}
