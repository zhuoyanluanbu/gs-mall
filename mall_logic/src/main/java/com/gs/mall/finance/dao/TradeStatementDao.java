package com.gs.mall.finance.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.finance.po.TradeStatement;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * TradeStatement dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:20
 */
@Mapper
public interface TradeStatementDao extends BaseDao<TradeStatement,java.lang.Long> {

	int queryCountByPage(@Param("params") Map params);
	List<TradeStatement> queryByPage(@Param("params") Map<String,Object> params,
            @Param("startIndex") int startIndex,
            @Param("fetchSize") int fetchSize);


}
