package com.gs.mall.hotword.controller;

import com.alibaba.fastjson.JSON;
import com.gs.common.result.ResponseResult;
import com.gs.mall.common.service.HotWordGroupService;
import com.gs.mall.enums.NormalStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * hotword控制类
 *
 * @auther zhangxinyan
 * @create 2017-08-25 15:36
 */

@RestController
@RequestMapping("/app/hotword")
public class HotWordController {

    @Autowired
    private HotWordGroupService hotWordGroupService;


    /**
     * 全部热词
     * @return
     */
    @RequestMapping("/all")
    public ResponseResult all(){
        return hotWordGroupService.all(NormalStatusEnum.NORMAL.getValue());
    }

}
