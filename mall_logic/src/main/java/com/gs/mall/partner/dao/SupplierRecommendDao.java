package com.gs.mall.partner.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.partner.po.SupplierRecommend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * SupplierRecommend dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:21
 */
@Mapper
public interface SupplierRecommendDao extends BaseDao<SupplierRecommend,java.lang.Integer> {

    /**
     *  推荐列表
     * @return
     */
    List<Map<String,Object>> list();

    /**
     * 取消推荐
     * @param recommend
     * @return
     */
    int cancelRecommend(SupplierRecommend recommend);

    /**
     * 供应商是否推荐
     * @param supplierId
     * @return
     */
    int isRecommended(String supplierId);


    SupplierRecommend queryBySupplierId(@Param("supplierId") String supplierId);

}
