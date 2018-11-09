package com.gs.mall.base.controller;


import com.gs.common.openapi.model.UserInfo;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.user.po.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by huangyp on 2017/8/25.
 */
public class BaseController {
    public UserInfo getUserInfo(HttpServletRequest request){
        return (UserInfo) request.getAttribute(Constant.APP_USER_SESSION_KEY);
    }


    public User getOperator(HttpServletRequest request){
        UserInfo user = (UserInfo) request.getAttribute(Constant.APP_USER_SESSION_KEY);
        User u = new User();
        u.setOperatorId(user.getOpenId());
        u.setOperator(user.getUserName());
        return u;
    }
}
