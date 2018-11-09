package com.gs.mall.banner.controller;

import com.alibaba.fastjson.JSON;
import com.gs.common.result.ResponseResult;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.po.Banner;
import com.gs.mall.common.service.BannerService;
import com.gs.mall.enums.NormalStatusEnum;
import com.gs.mall.user.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * banner控制类
 *
 * @auther zhangxinyan
 * @create 2017-08-25 15:36
 */

@RestController
@RequestMapping("/manage/banner")
public class BannerController {

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


    /**
     * 删除banner
     * @param banner
     * @return
     */
    @RequestMapping("/delBanner")
    public ResponseResult delBanner(Banner banner, HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        banner.setOperatorId(u.getOperatorId());
        banner.setOperator(u.getOperator());
        banner.setStatus(NormalStatusEnum.DELETE.getValue());
        return bannerService.saveOrUpdate(banner) ? ResponseResult.successInstance().setData(banner) : ResponseResult.failInstance();
    }

    /**
     * 添加banner
     * @param b
     * @return
     */
    @RequestMapping("/addBanner")
    public ResponseResult addCategory(Banner b,HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        b.setOperatorId(u.getOperatorId());
        b.setOperator(u.getOperator());
        b.setStatus(NormalStatusEnum.NORMAL.getValue());
        return bannerService.addBanner(b);
    }

    /**
     * 修改banner
     * @param b
     * @return
     */
    @RequestMapping("/updateBanner")
    public ResponseResult updateBanner(Banner b,HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        b.setOperator(u.getOperator());
        b.setOperatorId(u.getOperatorId());
        if (b.getLinkUrl() ==null) {
			b.setLinkUrl("");
		}
        return bannerService.saveOrUpdate(b) ? ResponseResult.successInstance().setData(b) : ResponseResult.failInstance();
    }
}
