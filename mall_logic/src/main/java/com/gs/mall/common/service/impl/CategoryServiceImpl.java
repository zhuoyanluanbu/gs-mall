package com.gs.mall.common.service.impl;

import com.gs.common.result.ResponseResult;
import com.gs.mall.commodity.dao.CommodityDao;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.common.po.Category;
import com.gs.mall.common.dao.CategoryDao;
import com.gs.mall.common.service.CategoryService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * Category service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:18
 */
@Service("categoryService")
public class CategoryServiceImpl extends AbstractBaseService<Category,java.lang.Long> implements CategoryService {

    @Resource
    private CategoryDao categoryDao;

    @Resource
    private CommodityDao commodityDao;
	
    @Override
    protected BaseDao<Category,java.lang.Long> getBaseDao() {
        return categoryDao;
    }


    @Override
    public List<Category> allCategory(Integer status) {
    	List<Category> allCategory = categoryDao.allCategory(status);
        allCategory.forEach(c -> {
//        	if (c.getParentId().intValue() == 0) 
        		c.setCount(categoryDao.countCNum(c.getCategoryId()));
        });
        return allCategory;
    }

    @Override
    @Transactional
    public ResponseResult delCategory(Long categoryId) {
        Category category = categoryDao.selectById(categoryId);
        if(1 != category.getIsAllowDel() && category.getCategoryId() == -1){
            ResponseResult responseResult = ResponseResult.failInstance();
            responseResult.setMessage("该分类不能删除");
            return responseResult;
        }
        //将删除的分类的商品移到默认分类中
        Map<String,Object> change = new HashMap<String,Object>() ;
        change.put("fromCategory",categoryId);
        change.put("toCategory", Constant.DEFAULT_CATEGORY);//默认分类
        change.put("toCategoryName", Constant.DEFAULT_CATEGORY_NAME);//默认分类
        int num = commodityDao.changeCommodityCategory(change);
        int i = categoryDao.delCategory(categoryId);
        return ResponseResult.successInstance();
    }

    @Transactional
    @Override
    public ResponseResult upSort(Category category) {
        Category beforeCategory = categoryDao.getBeforeCategory(category);
        if(beforeCategory == null||beforeCategory.getCategoryId()==-1){//第一个或者默认分类不能移动
            return ResponseResult.failInstance("不能继续上移了");
        }
        Category category1 = categoryDao.selectById(category.getCategoryId());
        int sortNum = beforeCategory.getSortNum();
        beforeCategory.setSortNum(category1.getSortNum());//前一个后移
        category1.setSortNum(sortNum);//后一个前移
        categoryDao.update(beforeCategory);
        categoryDao.update(category1);
        return ResponseResult.successInstance();
    }

    @Override
    public List<Category> findByParentId(Long categoryId) {
        return categoryDao.queryByParentId(categoryId);
    }

    @Override
    public List<Long> findIdsByParentId(Long categoryId) {
        List<Long> categoryIds = categoryDao.findIdsByParentId(categoryId);
        return categoryIds!=null&&!categoryIds.isEmpty()?categoryIds:new ArrayList<>();
    }

    @Transactional
    @Override
    public Boolean saveOrUpdate(Category category) {
        int i = 0;
        if( category.getCategoryId() == null ) {
            int num = categoryDao.maxSortNum();
            num ++;
            category.setSortNum(num);
            i = getBaseDao().insert(category);
        } else {
            i = getBaseDao().update(category);
        }
        return i > 0 ;
    }

}
