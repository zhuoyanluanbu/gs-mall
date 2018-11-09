package com.gs.mall.category.controller;

import com.alibaba.fastjson.JSON;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.po.Category;
import com.gs.mall.common.service.CategoryService;
import com.gs.mall.enums.NormalStatusEnum;
import com.gs.mall.user.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品分类公共
 */
@RequestMapping("/manage/category")
@RestController
public class CategoryCommonController {


    @Autowired
    private CategoryService categoryService;

    /**
     * 查询全部商品分类层级
     * @param params stauts状态 为空时查全部
     * @return
     */
    @RequestMapping("/all")
    public ResponseResult all(Map<String,Object> params){
        List<Category> categories = categoryService.allCategory(NormalStatusEnum.NORMAL.getValue());
        return ResponseResult.successInstance().setData(categories);
    }

    /**
     * 删除分类
     * @param categoryId
     * @return
     */
    @RequestMapping("/delCategory")
    public ResponseResult delCategory(Long categoryId){
        if ( categoryId == null ) {
            ResponseResult rr = ResponseResult.instance(105001);
            return rr;
        }
        return categoryService.delCategory(categoryId);
    }

    /**
     * 添加商品分类
     * @param c
     * @return
     */
    @RequestMapping("/addCategory")
    public ResponseResult addCategory(Category c,HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        c.setOperatorId(u.getOperatorId());
        c.setOperator(u.getOperator());
        c.setCreateTime(new Date());
        c.setIsAllowDel(1);
        if(null ==c.getParentId()||c.getParentId() == 0){
            c.setParentId(0l);

        }
        c.setStatus(NormalStatusEnum.NORMAL.getValue());
        return categoryService.saveOrUpdate(c)?ResponseResult.successInstance().setData(c):ResponseResult.failInstance();
    }

    /**
     * 修改商品分类
     * @param c
     * @return
     */
    @RequestMapping("/updateCategory")
    public ResponseResult updateCategory(Category c,HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        Category category = categoryService.getById(c.getCategoryId());
        category.setName(c.getName());
        category.setOperator(u.getOperator());
        category.setOperatorId(u.getOperatorId());
        category.setUpdateTime(new Date());
        return categoryService.saveOrUpdate(category)?ResponseResult.successInstance().setData(c):ResponseResult.failInstance();
    }

    /**
     * 上移商品分类
     * @param c
     * @return
     */
    @RequestMapping("/up")
    public ResponseResult up(Category c,HttpServletRequest request){
        return categoryService.upSort(c);
    }

}
