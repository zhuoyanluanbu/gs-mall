package com.gs.mall.order.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.order.po.WorkOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * WorkOrder dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:13
 */
@Mapper
public interface WorkOrderDao extends BaseDao<WorkOrder,java.lang.String> {

    List<WorkOrder> query(@Param("params") Map<String, Object> params);

    List<WorkOrder> queryLink(@Param("params")Map<String, Object> params);

	List<WorkOrder> selectByPa(@Param("params")Map<String, Object> params, @Param("startIndex") int startIndex,
            @Param("fetchSize") int fetchSize);

	int selectCountByPa(@Param("params")Map<String, Object> params);

}
