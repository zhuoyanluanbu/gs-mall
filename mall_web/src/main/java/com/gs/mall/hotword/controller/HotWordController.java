package com.gs.mall.hotword.controller;

import com.alibaba.fastjson.JSON;
import com.gs.common.result.ResponseResult;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.po.HotWordGroup;
import com.gs.mall.common.service.HotWordGroupService;
import com.gs.mall.enums.NormalStatusEnum;
import com.gs.mall.user.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * hotword控制类
 *
 * @auther zhangxinyan
 * @create 2017-08-25 15:36
 */

@RestController
@RequestMapping("/manage/hotword")
public class HotWordController {

    @Autowired
    private HotWordGroupService hotWordGroupService;


    /**
     * 全部banner
     * @return
     */
    @RequestMapping("/all")
    public ResponseResult all(){
        return hotWordGroupService.all(NormalStatusEnum.NORMAL.getValue());
    }


    /**
     * 新增热词
     * @param hot
     * @param request
     * @return
     */
    @RequestMapping("/add")
    public ResponseResult add(HotWordGroup hot, HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        hot.setOperatorId(u.getOperatorId());
        hot.setOperator(u.getOperator());
        hot.setStatus(NormalStatusEnum.NORMAL.getValue());
        hot.setUpdateTime(new Date());
        return hotWordGroupService.add(hot);
    }

    /**
     * 修改热词
     * @param hot
     * @param request
     * @return
     */
    @RequestMapping("/update")
    public ResponseResult update(HotWordGroup hot,HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        hot.setOperatorId(u.getOperatorId());
        hot.setOperator(u.getOperator());
        hot.setStatus(NormalStatusEnum.NORMAL.getValue());
        hot.setUpdateTime(new Date());
        return hotWordGroupService.saveOrUpdate(hot) ? ResponseResult.successInstance().setData(hot) : ResponseResult.failInstance();
    }

    /**
     * 删除热词
     * @param hot
     * @param request
     * @return
     */
    @RequestMapping("/del")
    public ResponseResult del(HotWordGroup hot,HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        hot.setOperatorId(u.getOperatorId());
        hot.setOperator(u.getOperator());
        hot.setStatus(NormalStatusEnum.DELETE.getValue());
        hot.setUpdateTime(new Date());
        return hotWordGroupService.saveOrUpdate(hot) ? ResponseResult.successInstance().setData(hot) : ResponseResult.failInstance();
    }


}
