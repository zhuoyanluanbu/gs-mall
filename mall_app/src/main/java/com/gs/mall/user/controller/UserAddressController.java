package com.gs.mall.user.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.openapi.config.AppSecreConfig;
import com.gs.common.openapi.service.UserApiService;
import com.gs.common.result.ResponseResult;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.util.StringUtil;

/**
 * 用户地址控制层
 * Created by huangyp on 2017/8/21.
 */
@RestController
@RequestMapping("/app/user")
public class UserAddressController {


    //@Autowired
    //private UserAddressService userAddressService;

    /*
     * 保存地址信息
     * @param useraddress
     * @return
     */
    /*@RequestMapping(value="/store", method = RequestMethod.POST)
    public String store(UserAddress useraddress){
        ResponseResult result = userAddressService.saveOrUpdate(useraddress);
        return JSON.toJSONString(result);
    }*/

    /**
     * 获取用户默认地址
     * @param openId
     * @return
     */
    @RequestMapping(value="/getDefaultAddress", method = RequestMethod.GET)
    public ResponseResult getDefaultAddress(@RequestParam String openId){
    	AppSecreConfig config = AppSecreConfig.build(Constant.appID, Constant.appSecretKey, Constant.appPrivateSecret,Constant.openPublicKey);
        ResponseResult result = UserApiService.getDefaultAddress(config, openId);
        Object address = null;
        if (result.getData() != null) {
        	String data = String.valueOf(result.getData());
			if (data.startsWith("[")) {
				address = JSON.parseArray(data);
			}else {
				address = JSON.parseObject(data);
			}
		}
        result = (address == null && result.getCode()!=0) ?
                        ResponseResult.failInstance() : ResponseResult.successInstance().setData(address);
        return result;
    }

    /**
     * 获取所有地址
     * @param openId
     * @return
     */
    @RequestMapping(value="/getAllAddress", method = RequestMethod.GET)
    public ResponseResult getAllAddress(@RequestParam String openId){
        //JSONArray address = UserInfoUtil.getAllAddress(openId);
        AppSecreConfig config = AppSecreConfig.build(Constant.appID, Constant.appSecretKey, Constant.appPrivateSecret);
        ResponseResult result = UserApiService.getAllAddress(config, openId);
        Object address = null;
        if (result.getData() != null) {
        	String data = String.valueOf(result.getData());
			if (data.startsWith("[")) {
				address = JSON.parseArray(data);
			}else {
				address = JSON.parseObject(data);
			}
		}
        result = (address == null) ?
                ResponseResult.failInstance() : result;
        return result;
    }

    /**
     * 编辑地址
     * @param params
     * @return
     */
    @RequestMapping(value="/editAddress", method = RequestMethod.POST)
    public ResponseResult editAddress(@RequestParam String openId, String addressId, @RequestParam Map<String,String> params){
        JSONObject address = new JSONObject(params.size());
        address.putAll(params);
        AppSecreConfig config = AppSecreConfig.build(Constant.appID, Constant.appSecretKey, Constant.appPrivateSecret);
        ResponseResult result = UserApiService.editUserAddress(config, openId, addressId,address);
        //Boolean b = UserInfoUtil.editUserAddress(openId, addressId, address);
        return result;
    }

    /**
     * 删除地址
     * @param openId
     * @param addressId
     * @return
     */
    @RequestMapping(value="/delAddress", method = RequestMethod.POST)
    public ResponseResult delAddress(@RequestParam String openId, @RequestParam String addressId){
        AppSecreConfig config = AppSecreConfig.build(Constant.appID, Constant.appSecretKey, Constant.appPrivateSecret);
        //Boolean b = UserInfoUtil.delUserAddress(openId,addressId);
        ResponseResult delUserAddress = UserApiService.delUserAddress(config, openId, addressId);
        return delUserAddress;
    }

    /**
     * 设置默认地址
     * @param openId
     * @param addressId
     * @return
     */
    @RequestMapping(value="/setDefaultAddr", method = RequestMethod.POST)
    public ResponseResult setDefaultAddr(@RequestParam String openId, @RequestParam String addressId){
        AppSecreConfig config = AppSecreConfig.build(Constant.appID, Constant.appSecretKey, Constant.appPrivateSecret);
        return UserApiService.setDefaultAddr(config,openId,addressId);
    }
}
