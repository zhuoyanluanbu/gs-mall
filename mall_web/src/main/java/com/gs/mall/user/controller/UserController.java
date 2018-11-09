package com.gs.mall.user.controller;

import com.alibaba.fastjson.JSON;
import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.mall.common.util.PageUtil;
import com.gs.mall.order.service.OrderService;
import com.gs.mall.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by huangyp on 2017/8/17.
 */
@RequestMapping("/manage/user")
@RestController
public class UserController {

    @Autowired
    private UserAddressService userService;


    @Autowired
    private OrderService orderService;

    /**
     * 用户列表
     * @param params
     * @param page
     * @return
     */
    @RequestMapping("/list")
    public ResponsePageResult list(Map<String,Object> params, PageUtil page){
        return userService.getListByPage(params,page.getPageNo(), page.getPageSize());
    }

    @RequestMapping("/test")
    public ResponseResult test(Map<String,Object> params, PageUtil page){
        return ResponseResult.successInstance().setData(orderService.saveOrUpdate(null));
    }
}
