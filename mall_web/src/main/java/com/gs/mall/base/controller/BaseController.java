package com.gs.mall.base.controller;

import com.gs.mall.common.constant.Constant;
import com.gs.mall.partner.po.Merchant;
import com.gs.mall.user.po.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by huangyp on 2017/8/24.
 */
public class BaseController {

    /**
     * 获得操作人
     * @param request
     * @return
     */
    public User getOperator(HttpServletRequest request) {
        return (User)request.getAttribute(Constant.OPERATOR_SESSION_KEY);
    }

    /**
     * 获得商户
     * @param request
     * @return
     */
    public Merchant getMerchant(HttpServletRequest request) {
        return (Merchant)request.getAttribute(Constant.MERCHANT_SESSION_KEY);
    }



}
