package com.gs.mall.finance.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gs.common.result.ResponseResult;
import com.gs.mall.base.controller.BaseController;
import com.gs.mall.finance.service.FinancePanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 财务面板控制层
 * Created by huangyp on 2017/8/29.
 */
@RestController
@RequestMapping("/manage/financePanel")
public class FinancePanelController extends BaseController {

    @Autowired
    private FinancePanelService financePanelService;

    /**
     * 显示数据
     * @return
     */
    @RequestMapping("/showData")
    public ResponseResult showData(HttpServletRequest request){
        Long lo = this.getMerchant(request).getMerchantId();
        return financePanelService.getDataByMerchantId(lo);
    }

}
