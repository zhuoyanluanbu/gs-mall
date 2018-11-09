package com.gs.mall.order.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.order.po.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ShoppingCart dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:14
 */
@Mapper
public interface ShoppingCartDao extends BaseDao<ShoppingCart,java.lang.Long> {

    int batchDelete(@Param("ids") Long[] ids,
                    @Param("ownerId") String ownerId);


    /**
     * 批量更新
     * @param list
     * @return
     */
    int batchUpdate(@Param("list")List<ShoppingCart> list);
}
