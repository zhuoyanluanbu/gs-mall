package com.gs.mall.common.service;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.po.Category;

import java.util.List;

/**
 * Category service interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:18
 */
public interface CategoryService extends BaseService<Category,java.lang.Long>{

    /**
     * 查询全部商品分类
     * @param status 分类状态，-1删除 1正常，不传查询全部
     * @return
     */
    List<Category> allCategory(Integer status);


    /**
     * 删除商品分类
     * @param categoryId
     * @return
     */
    ResponseResult delCategory(Long categoryId);

    /**
     * 上移商品分类排序
     * @param category
     * @return
     */
    ResponseResult upSort(Category category);

    /**
     * 查询下级分类
     * @param categoryId
     * @return
     */
    List<Category> findByParentId(Long categoryId);

    /**
     * 查询下级分类Id
     * @param categoryId
     * @return
     */
    List<Long> findIdsByParentId(Long categoryId);

}
