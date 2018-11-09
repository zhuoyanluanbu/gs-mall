package com.gs.mall.commodity.dao;

import com.gs.mall.commodity.dto.InventoryDto;
import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.commodity.po.CommodityDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * CommodityDetail dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:16
 */
@Mapper
public interface CommodityDetailDao extends BaseDao<CommodityDetail,java.lang.Long> {


    List<CommodityDetail> queryByCommodityId(Long commodityId);

    CommodityDetail queryByParam(Map<String,Object> params);

    /**
     * 根据商品名称SKU更新库存
     * @param list
     * @return
     */
    int batchUpdateInventory(@Param("list") List<InventoryDto> list);

    /**
     * 逻辑删除商品详情
     * @param CommodityId
     * @return
     */
    int delCommodityDetail(Long CommodityId);
}
