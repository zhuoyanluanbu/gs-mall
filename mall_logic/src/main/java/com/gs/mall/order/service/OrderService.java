package com.gs.mall.order.service;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.po.Logistics;
import com.gs.mall.common.service.BaseService;
import com.gs.mall.order.dto.OrderDto;
import com.gs.mall.order.po.Order;
import com.gs.mall.order.po.OrderPayData;
import com.gs.mall.order.vo.OrderFinanceDetailVo;
import com.gs.mall.user.po.User;

import java.util.Date;
import java.util.Map;

/**
 * Order service interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:12
 */
public interface OrderService extends BaseService<Order,java.lang.String>{



    /**
     * 保存订单
     * @param params
     * @return
     */
    ResponseResult save(Map<String,String> params);

    /**
     * 支付成功
     * @param orderId 订单号（必填）
     * @param payTime 支付时间（可选，默认为当前系统时间）
     * @return
     */
    ResponseResult updatePaySuccess(String orderId, Date payTime, User operator);

    /**
     * 发货完成
     * @param orderId 订单号（必填）
     * @param operator 操作人（必填）
     * @param logistics 物流信息
     * @return
     */
    ResponseResult updateDeliver(String orderId, Date deliverTime,User operator, Logistics logistics);

    /**
     * 订单完成
     * @param orderId 订单号（必填）
     * @param finishTime 完成时间（可选，默认为当前系统时间）
     * @param operator 操作人（必填）
     * @return
     */
    ResponseResult updateFinish(String orderId, Date finishTime, User operator);

    /**
     * 订单关闭
     * @param orderId 订单号（必填）
     * @param operator 操作人（必填）
     * @return
     */
    ResponseResult updateClose(String orderId,User operator);

    /**
     * 查询支付数据
     * @param orderId
     * @return
     */
    OrderPayData getPayDataByOrderId(String orderId);

    /**
     * 通过ID查询
     * @param orderId
     * @param openId
     * @return
     */
    OrderDto getOrderDtoByOrderId(String orderId, String openId);

	OrderFinanceDetailVo countConsume(String startTime, String endTime);
}
