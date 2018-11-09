package com.gs.mall.order.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.order.po.RefundCommodity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * RefundCommodity dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:14
 */
@Mapper
public interface RefundCommodityDao extends BaseDao<RefundCommodity,java.lang.Long> {


    List<RefundCommodity> queryByWorkOrderId(@Param("woId") String woId);

}
