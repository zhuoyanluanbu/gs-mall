package com.gs.mall.commodity.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.commodity.po.CommodityExtend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * CommodityExtend dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:16
 */
@Mapper
public interface CommodityExtendDao extends BaseDao<CommodityExtend,java.lang.Long> {

    CommodityExtend queryByCommodityId(Long commodityId);

    /**
     * 根据商品id修改商品扩展
     * @param commodityId
     * @return
     */
    int updateExtend(@Param("commodityId") Long commodityId, @Param("content") String content);

}
