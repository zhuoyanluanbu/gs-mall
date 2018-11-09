package com.gs.mall.supplier.controller;

import com.alibaba.fastjson.JSON;
import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.mall.common.util.PageUtil;
import com.gs.mall.partner.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * app供应商管理控制类
 *
 * @auther zhangxinyan
 * @create 2017-08-21 16:13
 */

@RequestMapping("/app/supplier")
@RestController
public class SupplierAppController {

    @Autowired
    private SupplierService supplierService;

    /**
     * 供应商列表分页查询
     * @param params
     * @param page
     * @return
     */
    @RequestMapping("/list")
    public ResponsePageResult list(@RequestParam Map<String,Object> params, PageUtil page){
        params.put("status",1);
        return supplierService.getListByPage(params,page.getPageNo(), page.getPageSize());
    }

    /**
     * 推荐供应商列表
     * @param params
     * @param page
     * @return
     */
    @RequestMapping("/recommendList")
    public ResponseResult recommendList(@RequestParam Map<String,Object> params, PageUtil page){
        return supplierService.recommendList();
    }

    /**
     * 供应商商品分页查询
     * @param params
     * @param page
     * @return
     */
    //@RequestMapping("/supplierCommoditys")
    //public String supplierCommoditys(Map<String,Object> params, PageUtil page){
    //    return JSON.toJSONString(supplierService.recommendList());
    //}

}
