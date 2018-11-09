package com.gs.mall.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.common.util.StringUtils;
import com.gs.mall.base.controller.BaseController;
import com.gs.mall.commodity.dto.CommoditySkuDto;
import com.gs.mall.commodity.service.CommodityService;
import com.gs.mall.common.util.PageUtil;
import com.gs.mall.order.dto.ShoppingCartDto;
import com.gs.mall.order.po.ShoppingCart;
import com.gs.mall.order.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 购物车
 * Created by huangyp on 2017/10/20.
 */
@RestController("shoppingCartController")
@RequestMapping("/app/shoppingCart")
public class ShoppingCartController extends BaseController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CommodityService commodityService;

    /**
     * 查询购物车列表
     *
     * @param params  {"ownerId":"xxxxxxxxxxxxxxx","pageSize":20,"pageNo":1}
     * @param page
     * @param request
     * @return
     * front post
     */
    @RequestMapping(value = "/list")
    public ResponsePageResult list(@RequestParam Map<String, Object> params, PageUtil page, HttpServletRequest request) {
//        if (StringUtils.isEmpty((String) params.get("ownerId"))) {
            params.put("ownerId", getUserInfo(request).getOpenId());
//        }
        if (StringUtils.isEmpty((String) params.get("status"))) {
            params.put("status", 1);
        }
        ResponsePageResult result = shoppingCartService.getListByPage(params, page.getPageNo(), page.getPageSize());
        return result;
    }

    /**
     * 添加到购物车
     *
     * @param cart {
     *             "commodityId":18,
     *             "skuId":"12-86",
     *             "buyNum":1
     *             }
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseResult add(ShoppingCart cart, HttpServletRequest request) {
        ResponseResult result = null;
        if (StringUtils.isEmpty(cart.getOwnerId())) {
            cart.setOwnerId(getUserInfo(request).getOpenId());
        }
        if (cart.getBuyNum() == null || cart.getBuyNum() <= 0) {
            result = ResponseResult.instance(104001);
        } else if (cart.getCommodityId() == null) {
            result = ResponseResult.instance(104002);
        } else if (StringUtils.isEmpty(cart.getSkuId())) {
            result = ResponseResult.instance(104003);
        } else if (StringUtils.isEmpty(cart.getOwnerId())) {
            result = ResponseResult.instance(104004);
        } else {
            Map<String, Object> query = new HashMap<>();
            query.put("commodityId", cart.getCommodityId());
            query.put("ownerId", cart.getOwnerId());
            query.put("skuId", cart.getSkuId());
            query.put("status", 1);
            CommoditySkuDto commoditySkuDto=commodityService.queryByIdAndSkuId(cart.getCommodityId(),cart.getSkuId());
            if(null==commoditySkuDto){
                result = ResponseResult.instance(104006);
            }else{
                PageUtil page = new PageUtil();
                ResponsePageResult pageResult = shoppingCartService.getListByPage(query, page.getPageNo(), page.getPageSize());
                if (null != pageResult) {
                    List<ShoppingCartDto> myCart = (List<ShoppingCartDto>) pageResult.getData();
                    if (myCart != null && !myCart.isEmpty()) {
                        ShoppingCartDto thisItem = myCart.get(0);
                        int nowNum=cart.getBuyNum()+thisItem.getBuyNum();
                        cart.setScId(thisItem.getId());
//                        if(null==commoditySkuDto.getInventory()||0==commoditySkuDto.getInventory()){
//                            result = ResponseResult.instance(104006);
//                            return JSON.toJSONString(result);
//                        }
//                        if(nowNum>commoditySkuDto.getInventory()){
//                            nowNum=commoditySkuDto.getInventory();
//                        }
                        cart.setBuyNum(nowNum);
                    }
                }
                Boolean b  = shoppingCartService.saveOrUpdate(cart);
                result = b ? ResponseResult.successInstance() : ResponseResult.failInstance();
            }

        }
        return result;
    }

    /**
     * 更新购物车
     * @param updateBuyNum : [{"id":1, "buyNum":2}]
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseResult update(String updateBuyNum, HttpServletRequest request) {
        ResponseResult result = null;
        if (StringUtils.isEmpty(updateBuyNum)) {
            result = ResponseResult.instance(104005);
        } else {
            JSONArray jsonArray = JSONArray.parseArray(updateBuyNum);
            JSONArray updArray = new JSONArray(jsonArray.size());
            if(null!=jsonArray){
                //id 查询出ShoppingCart对象
                //commodityId skuId 查库存，验证库存
                for(int i=0;i<jsonArray.size();i++){
                    JSONObject item = jsonArray.getJSONObject(i);
                    JSONObject newItem = new JSONObject();
                    newItem.put("id",item.get("id"));
                    ShoppingCart shoppingCart = shoppingCartService.getById(item.getLong("id"));
                    CommoditySkuDto commoditySkuDto=commodityService.queryByIdAndSkuId(shoppingCart.getCommodityId(),shoppingCart.getSkuId());
                    if(null==commoditySkuDto||null==commoditySkuDto.getInventory()||0==commoditySkuDto.getInventory()){
                        result = ResponseResult.instance(104006);
                        return result;
                    }
                    int nowNum = item.getInteger("buyNum");
//                    if(nowNum>commoditySkuDto.getInventory()){
//                        nowNum=commoditySkuDto.getInventory();
//                    }
                    newItem.put("buyNum",nowNum);
                    updArray.add(i,newItem);
                }
                Map<String,Object> updateJson =new HashMap<>();
                updateJson.put("updateBuyNum",JSONArray.toJSON(updArray));
                result = shoppingCartService.update(JSON.toJSONString(updateJson), getUserInfo(request).getOpenId());
            }

        }
        return result;
    }
    /**
     * 删除购物车
     * @param removeIds : [1,2]
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResponseResult delete(@RequestParam String removeIds,HttpServletRequest request){
        ResponseResult result = null;
        if (StringUtils.isEmpty(removeIds)) {
            result = ResponseResult.instance(104005);
        } else {
            Map<String,Object> updateJson =new HashMap<>();
            updateJson.put("removeIds",removeIds);
            result = shoppingCartService.update(JSON.toJSONString(updateJson), getUserInfo(request).getOpenId());
        }
        return result;
    }

}
