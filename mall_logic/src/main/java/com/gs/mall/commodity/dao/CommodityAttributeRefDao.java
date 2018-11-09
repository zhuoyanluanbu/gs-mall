package com.gs.mall.commodity.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.commodity.po.CommodityAttributeRef;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * CommodityAttributeRef dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:15
 */
@Mapper
public interface CommodityAttributeRefDao extends BaseDao<CommodityAttributeRef,java.lang.Long> {

    /**
     * 根据commodityId attributeId attributeValue 查询商品规格
     * @return
     */
    CommodityAttributeRef getCommodityAttribute(Map<String,Object> map);

}
