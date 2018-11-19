package com.gs.mall.order.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.order.po.WorkOrderV2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by huyoucheng on 2018/11/1.
 */
@Mapper
public interface WorkOrderV2Dao extends BaseDao<WorkOrderV2,Integer>{
    WorkOrderV2 selectByWoId(@Param("wo_id") String wo_id);
}
