package com.gs.mall.finance.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.finance.po.TradeDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * TradeDetail dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:20
 */
@Mapper
public interface TradeDetailDao extends BaseDao<TradeDetail,java.lang.String> {

    TradeDetail queryByIdNo(@Param("formId") String formId, @Param("tradeNo") String tradeNo);

    int insertOrUpdate(TradeDetail tradeDetail);

}
