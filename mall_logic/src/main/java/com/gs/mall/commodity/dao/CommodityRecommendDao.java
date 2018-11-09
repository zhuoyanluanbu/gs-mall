package com.gs.mall.commodity.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.commodity.po.CommodityRecommend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * CommodityRecommend dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:16
 */
@Mapper
public interface CommodityRecommendDao extends BaseDao<CommodityRecommend,java.lang.Integer> {


    /**
     * 推荐商品分页查询
     * @return
     */
    List<Map> recommendList(@Param("params") Map<String, Object> params,
                            @Param("startIndex") int startIndex,
                            @Param("fetchSize") int fetchSize);


    /**
     * 商品分页总数
     * @return
     */
    int recommendCount(@Param("params") Map<String, Object> params);

    /**
     * 当前推荐商品最大排序号
     * @return
     */
    short maxSortNum();

    /**
     * 删除商品推荐
     * @param commodityId
     * @return
     */
    int delRecommend(Long commodityId);

    /**
     * 修改商品推荐名称
     * @param recommend
     * @return
     */
    int updateDisplayName(CommodityRecommend recommend);

    /**
     * 商品是否推荐
     * @param commodityId
     * @return
     */
    int isRecommended(Long commodityId);


	/**
     * 商品是否推荐
     * @param commodityId
     * @return
     */
    CommodityRecommend selectByCommdityId(Long commodityId);
}
