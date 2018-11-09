package com.gs.mall.order.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.order.po.WorkOrderFlow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huyoucheng on 2018/10/31.
 */
@Mapper
public interface WorkOrderFlowDao extends BaseDao<WorkOrderFlow,Integer> {
    List<WorkOrderFlow> getAllWordOrderFlows();
}
