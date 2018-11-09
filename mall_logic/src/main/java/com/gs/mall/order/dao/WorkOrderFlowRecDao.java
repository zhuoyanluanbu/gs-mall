package com.gs.mall.order.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.order.po.WorkOrderFlowRec;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by huyoucheng on 2018/11/1.
 */
@Mapper
public interface WorkOrderFlowRecDao extends BaseDao<WorkOrderFlowRec,Integer> {

    WorkOrderFlowRec currentWorkOrderByWoId(@Param("wo_id") String wo_id);

}
