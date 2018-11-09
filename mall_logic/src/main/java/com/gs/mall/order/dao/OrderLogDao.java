package com.gs.mall.order.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.order.po.OrderLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * OrderLog dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:13
 */
@Mapper
public interface OrderLogDao extends BaseDao<OrderLog,java.lang.Long> {

    List<OrderLog> queryByOrderId(String orderId);

}
