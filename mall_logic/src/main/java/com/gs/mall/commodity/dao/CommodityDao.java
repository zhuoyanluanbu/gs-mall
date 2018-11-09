package com.gs.mall.commodity.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gs.mall.commodity.dto.CommoditySkuDto;
import com.gs.mall.commodity.po.Commodity;
import com.gs.mall.common.dao.BaseDao;

/**
 * Commodity dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:15
 */
@Mapper
public interface CommodityDao extends BaseDao<Commodity,java.lang.Long> {

    /**
     * 变更商品分类
     * @param param {fromCategory:原商品分类,toCategory:更改后商品分类}
     * @param
     * @return
     */
    int changeCommodityCategory(Map<String, Object> param);

    /**
     * 根据id批量修改商品状态
     * @param params
     * @return
     */
    int batchUpdateByIds(@Param("params") Map<String,Object> params);


    /**
     * 根据查询条件批修改商品状态
     * @param param
     * @return
     */
    int batchUpdateByParam(@Param("params") Map<String,Object> param);


    /**
     * 根据查询条件返回全部id数组
     * @param param
     * @return
     */
    List<Object> querybatchIds(@Param("params") Map<String,Object> param);

    /**
     * 批量更累计销量和最后购买时间
     * @param list
     * @return
     */
    int batchUpdateSaleTotalLastBuy(@Param("list") List<Map<String,Object>> list);

    /**
     * 根据id搜索
     * @param ids
     * @return
     */
    List<Commodity> serach(String ids);



    /**
     * 查询商品及规格信息
     * @return
     */
    CommoditySkuDto queryByIdAndSkuId(@Param("params") Map<String, Object> params);

    /**
     * 查询全部商品库存
     * @return
     */
    List<Map> getAllInventory();
    
    List<Commodity> selectByPa(@Param("params") Map<String,Object> params,
            @Param("startIndex") int startIndex,
            @Param("fetchSize") int fetchSize);
    
    int selectCountByPa(@Param("params") Map<String,Object> params);
    
    /**
     * 根据关键字搜索
     * @param keyWords
     * @param startIndex
     * @param fetchSize
     * @return
     */
    List<Commodity> serach2(@Param("keyWords")String keyWords,
    		@Param("startIndex")int startIndex,@Param("fetchSize")int fetchSize);
    
    int countSerach2(@Param("keyWords")String keyWords);
    
}
