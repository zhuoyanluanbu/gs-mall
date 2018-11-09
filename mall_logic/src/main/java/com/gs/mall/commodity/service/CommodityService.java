package com.gs.mall.commodity.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.mall.commodity.dto.CommoditySkuDto;
import com.gs.mall.commodity.dto.InventoryDto;
import com.gs.mall.commodity.po.Commodity;
import com.gs.mall.commodity.po.CommodityAttributeRef;
import com.gs.mall.commodity.po.CommodityDetail;
import com.gs.mall.commodity.po.CommodityRecommend;
import com.gs.mall.common.service.BaseService;
import com.gs.mall.common.util.PageUtil;
import com.gs.mall.user.po.User;

/**
 * Commodity service interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:15
 */
public interface CommodityService extends BaseService<Commodity,java.lang.Long>{


    /**
     * 推荐商品分页查询
     * @param params
     * @param pageUtil
     * @return
     */
     ResponsePageResult recommendList(Map<String, Object> params, PageUtil pageUtil);

    /**
     * 保存商品
     * @param commodity 商品
     * @param commodityAttributes 商品详情
     * @param commodityAttributes 商品规格
     * @return
     */
     ResponseResult addCommodity(Commodity commodity, List<CommodityDetail> commodityDetails, List<CommodityAttributeRef> commodityAttributes);

    /**
     * 添加商品
     * @param commodity 商品
     * @param commodityAttributes 商品详情
     * @param commodityAttributes 商品规格
     * @param content 商品扩展内容
     * @return
     */
     ResponseResult addCommodity(Commodity commodity, JSONArray commodityDetails, List<CommodityAttributeRef> commodityAttributes, String content);


    /**
     * 修改商品
     * @param commodity 商品
     * @param commodityAttributes 商品详情
     * @param commodityAttributes 商品规格
     * @return
     */
     ResponseResult updateCommodity(Commodity commodity, JSONArray commodityDetails, List<CommodityAttributeRef> commodityAttributes,String content);



    /**
     * 商品设为推荐
     * @param commodity
     * @param  displayName
     * @return
     */
     ResponseResult recommend(Commodity commodity,String displayName);

    /**
     * 修改商品推荐名称
     * @param commodity
     * @param displayName
     * @return
     */
     ResponseResult updRecommend(Commodity commodity,String displayName);

    /**
     * 取消商品推荐
     * @param commodity
     * @return
     */
     ResponseResult unRecommend(Commodity commodity);


    /**
     * 商品下架
     * @param commodity
     * @return
     */
     ResponseResult pull(Commodity commodity);


    /**
     * 商品删除
     * @param commodity
     * @return
     */
     ResponseResult delCommodity(Commodity commodity);

    /**商品详情
     * @param commodityId
     * @return
     */
     ResponseResult commodityDetail(Long commodityId);


    /**
     * @param idsArr
     * @param param
     * @param user
     * @return
     */
     ResponseResult  batchUpdateStatus(String[] idsArr, Map<String,Object> param, User user);


    /**
     * 全部规格
     * @param status
     * @return
     */
     ResponseResult allAttribute(Integer status);

    /**
     * 查询
     * @param keywords
     * @return
     */
     ResponsePageResult serach(String keywords,PageUtil page);

    /**
     * 查询商品及其规格信息
     * @param id
     * @param skuId
     * @return
     */
     CommoditySkuDto queryByIdAndSkuId(Long id, String skuId);

	/**
	 * 新增规格
	 * @param attribute
	 * @return
	 */
	 ResponseResult saveAttribute(String name ,User user);

    /**
     * 删除规格
     * @param attributeId
     * @return
     */
     ResponseResult delAttribute(Long attributeId);

    /**
     * 刷新库存
     * @return
     */
     ResponseResult refreshInventory();

    /**
     * 更新库存
     * @param list
     * @return
     */
     ResponseResult updateInventory(List<InventoryDto> list);
     
     /**
      * 分页查询商品
      */
     ResponsePageResult getCommoditys(Map<String, Object> params, Integer pageNo, Integer pageSize);

	CommodityRecommend getRecommend(Long id);
	
}
