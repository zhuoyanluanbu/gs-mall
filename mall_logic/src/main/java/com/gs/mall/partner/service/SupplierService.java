package com.gs.mall.partner.service;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.service.BaseService;
import com.gs.mall.partner.po.Supplier;
import com.gs.mall.partner.po.SupplierRecommend;

import java.util.Map;

/**
 * Supplier service interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:21
 */
public interface SupplierService extends BaseService<Supplier,java.lang.String>{


    /**
     * 删除供应商
     * @param supplier
     * @return
     */
    ResponseResult delSupplier(Supplier supplier);

    /**
     * 设为推荐供应商
     * @param recommend
     * @return
     */
    ResponseResult recommend(SupplierRecommend recommend);

    /**
     * 取消推荐
     * @param recommend
     * @return
     */
    ResponseResult cancelRecommend(SupplierRecommend recommend);

    /**
     * 推荐供应商列表
     * @return
     */
    ResponseResult recommendList();


    /**
     * 全部供应商
     * @param param
     * @return
     */
    ResponseResult all(Map<String,Object> param);

}
