package com.gs.mall.commodity.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.commodity.po.CommoditySnapshoot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * CommoditySnapshoot dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:17
 */
@Mapper
public interface CommoditySnapshootDao extends BaseDao<CommoditySnapshoot,java.lang.Long> {



    List<CommoditySnapshoot> queryByOrderId(String orderId);


    /**
     * 批量插入
     * @param list
     * @return
     */
    int batchInsert(@Param("list") List<CommoditySnapshoot> list);
}
