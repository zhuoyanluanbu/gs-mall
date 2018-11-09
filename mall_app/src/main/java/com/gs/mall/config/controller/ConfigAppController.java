package com.gs.mall.config.controller;

import com.alibaba.fastjson.JSON;
import com.gs.common.result.ResponseResult;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * app配置控制类
 */
@RestController
public class ConfigAppController {

    @Autowired
    private SystemConfigService systemConfigService;


    /**
     *查询搜索词
     * 按分类查询传入categoryId
     * 按供应商查询传入supplierId
     * @return
     */
    @RequestMapping("/app/conf/getSearchWord")
    public ResponseResult getSearchWord(){
        return systemConfigService.getSysConfig(Constant.SEARCH_WORD);
    }


    /**
     * 获取AppId
     * 按分类查询传入categoryId
     * 按供应商查询传入supplierId
     * @return
     */
    @RequestMapping("/common/system/getAppId")
    public ResponseResult getAppId(){
        return ResponseResult.successInstance().setData(Constant.appID);
    }

    /**
     * 存活检测
     * @return
     */
    @RequestMapping("/root/ping")
    public String ping(){
        return "200";
    }
}
