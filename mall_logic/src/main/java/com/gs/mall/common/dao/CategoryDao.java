package com.gs.mall.common.dao;

import com.gs.mall.common.po.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Category dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:18
 */
@Mapper
public interface CategoryDao extends BaseDao<Category,java.lang.Long> {


    /**
     * 逻辑删除商品分类
     * @param id
     * @return
     */
    int delCategory(Long id);
    /**
     * 查询全部商品分类
     * @param status -1 删除 -正常，null全部
     * @return
     */
    List<Category> allCategory(Integer status);
    
    /**
     * 统计父类节点商品数
     * @param categoryId
     * @return
     */
	int countCNum(@Param("parentId") Long parentId);

    /**
     * 上移分类
     * @param category
     * @return
     */
    //int upCategory(Category category);

    /**
     * 查询前一个商品分类
     * @param category
     * @return
     */
    Category getBeforeCategory(Category category);

    int maxSortNum();

    List<Category> queryByParentId(@Param("parentId") Long parentId);

    List<Long> findIdsByParentId(@Param("parentId") Long parentId);

}
