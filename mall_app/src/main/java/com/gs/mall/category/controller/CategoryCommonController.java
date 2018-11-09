package com.gs.mall.category.controller;

import com.alibaba.fastjson.JSON;
import com.gs.common.result.ResponseResult;
import com.gs.mall.common.po.Category;
import com.gs.mall.common.service.CategoryService;
import com.gs.mall.enums.NormalStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品分类公共
 */
@RequestMapping("/app/category")
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
//        categories =categories.stream().filter(category -> -1!=category.getCategoryId()).collect(Collectors.toList());
        return ResponseResult.successInstance().setData(categories);
    }
}
