package com.gs.mall.banner.controller;

import com.alibaba.fastjson.JSON;
import com.gs.common.result.ResponseResult;
import com.gs.mall.common.service.BannerService;
import com.gs.mall.enums.NormalStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * banner控制类
 *
 * @auther zhangxinyan
 * @create 2017-08-25 15:36
 */

@RestController
@RequestMapping("/app/banner")
public class BannerAppController {

    @Autowired
    private BannerService bannerService;


    /**
     * 全部banner
     * @return
     */
    @RequestMapping("/all")
    public ResponseResult all(){
        return bannerService.allBanners(NormalStatusEnum.NORMAL.getValue());
    }

}
