package com.gs.mall.config.controller;

import com.alibaba.fastjson.JSON;


import com.gs.common.result.ResponseResult;
import com.gs.mall.commodity.service.CommodityService;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.po.SystemConfig;
import com.gs.mall.common.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 后台配置控制类
 */
@RestController
public class ConfigController {

    @Autowired
    private SystemConfigService systemConfigService;
    
    @Autowired
    private CommodityService commodityService;


    //private static final Logger logger = Logger.getLogger(ConfigController.class);


    /**
     *分页查询商品,
     * 按分类查询传入categoryId
     * 按供应商查询传入supplierId
     * @return
     */
    @RequestMapping("/manage/conf/getSearchWord")
    public ResponseResult getSearchWord(){
        return systemConfigService.getSysConfig(Constant.SEARCH_WORD);
    }

    /**
     * 修改搜索词
     * @param searchWord
     * @return
     * 参数：
     * attributes:[{attributeId:规格id,attributeValue:规格值}]
     * details[{sku:[{attributeId:规格id,attributeValue:规格值}],img:图片,inventory:库存,barCode:条码,salePrice:售价}]
     */
    @RequestMapping("/manage/conf/updSearchWord")
    public ResponseResult updateSearchWord(@RequestParam String searchWord,HttpServletRequest request){
        SystemConfig c = new SystemConfig();
        c.setConfigKey(Constant.SEARCH_WORD);
        c.setConfigValue(searchWord);
        return systemConfigService.updateSysConfig(c);
    }

    /**
     * 刷新库存缓存
     * @return
     */
    @RequestMapping("/manage/conf/refreshInventory")
    public ResponseResult refreshInventory(){
        return commodityService.refreshInventory();
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
