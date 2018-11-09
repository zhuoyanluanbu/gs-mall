package com.gs.mall.finance.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.finance.po.NetflowRecord;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * NetflowRecord dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:19
 */
@Mapper
public interface NetflowRecordDao extends BaseDao<NetflowRecord,java.lang.Long> {

	Integer queryCountByPage(@Param("params") Map<String, Object> params);
	
	List<NetflowRecord> queryByPage(@Param("params") Map<String,Object> params,
            @Param("startIndex") int startIndex,
            @Param("fetchSize") int fetchSize);


}
