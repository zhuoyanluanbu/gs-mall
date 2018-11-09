package com.gs.mall.order.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.order.po.WorkOrder;
import com.gs.mall.order.po.WorkOrderLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * WorkOrderLog dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:13
 */
@Mapper
public interface WorkOrderLogDao extends BaseDao<WorkOrderLog,java.lang.Long> {

    List<WorkOrder> queryByWorkId(String workId);
}
