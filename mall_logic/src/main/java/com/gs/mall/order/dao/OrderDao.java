package com.gs.mall.order.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.order.dto.OrderDto;
import com.gs.mall.order.po.Order;
import com.gs.mall.order.po.OrderPayData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Order dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:12
 */
@Mapper
public interface OrderDao extends BaseDao<Order,java.lang.String> {



    List<Map<String,String>> queryOrderTimeByStatus(Map<String,Integer> params);

    /**
     * 保存订单支付数据
     * @param payData
     * @return
     */
    Integer inertOrderPayData(OrderPayData payData);

    /**
     * 查询支付数据
     * @param orderId
     * @return
     */
    OrderPayData queryPayDataByOrderId(String orderId);


    /**
     * 查询orderDTO数据
     * @param orderId
     * @return
     */
    OrderDto queryOrderDtoByOrderId(@Param("orderId") String orderId, @Param("openId") String openId);


    /**
     * 修改状态
     * @param orderId
     * @param oldStatus
     * @param newStatus
     * @return
     */
    Integer updateStatus(@Param("orderId") String orderId,
                         @Param("oldStatus") Integer oldStatus,
                         @Param("newStatus") Integer newStatus,
                         @Param("finishTime") Date finishTime);

    int countConsume(@Param("startTime") String startTime,@Param("endTime") String endTime);
}
