package com.gs.mall.supplier.controller;

import com.alibaba.fastjson.JSON;
import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.util.PageUtil;
import com.gs.mall.enums.NormalStatusEnum;
import com.gs.mall.partner.po.Supplier;
import com.gs.mall.partner.po.SupplierRecommend;
import com.gs.mall.partner.service.SupplierService;
import com.gs.mall.user.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 后台供应商管理控制类
 *
 * @auther zhangxinyan
 * @create 2017-08-21 16:13
 */

@RequestMapping("/manage/supplier")
@RestController("webSupplierController")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    /**
     * 供应商列表分页查询
     * @param params
     * @param page
     * @return
     */
    @RequestMapping("/list")
    public ResponsePageResult list(@RequestParam Map<String,Object> params, PageUtil page){
        //params.put("status",1);
        return supplierService.getListByPage(params,page.getPageNo(), page.getPageSize());
    }

    /**
     * 保存供应商
     * @param supplier
     * @return
     */
    @RequestMapping("/addSupplier")
    public ResponseResult addSupplier(Supplier supplier, HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        supplier.setOperator(u.getOperator());
        supplier.setOperatorId(u.getOperatorId());
        supplier.setCreateTime(new Date());
        supplier.setStatus(NormalStatusEnum.NORMAL.getValue());
        return supplierService.saveOrUpdate(supplier)? ResponseResult.successInstance().setData(supplier) : ResponseResult.failInstance();
    }

    /**
     * 修改供应商
     * @param supplier
     * @return
     */
    @RequestMapping("/updateSupplier")
    public ResponseResult updateSupplier(Supplier supplier,HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        supplier.setOperator(u.getOperator());
        supplier.setOperatorId(u.getOperatorId());
        supplier.setUpdateTime(new Date());
        return supplierService.saveOrUpdate(supplier)? ResponseResult.successInstance().setData(supplier) : ResponseResult.failInstance();   
    }

    /**
     * 删除供应商
     * @param supplier
     * @return
     */
    @RequestMapping("/delSupplier")
    public ResponseResult delSupplier(Supplier supplier,HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        supplier.setOperator(u.getOperator());
        supplier.setOperatorId(u.getOperatorId());
        return supplierService.delSupplier(supplier);
    }

    /**
     * 推荐供应商列表
     * @param params
     * @param page
     * @return
     */
    @RequestMapping("/recommendList")
    public ResponseResult recommendList(Map<String,Object> params, PageUtil page){
        return supplierService.recommendList();
    }

    /**
     * 全部供应商
     * @param params
     * @return
     */
    @RequestMapping("/all")
    public String all(@RequestParam Map<String,Object> params){
        params.put("status",NormalStatusEnum.NORMAL.getValue().shortValue());
        System.out.println("params=" + params);
        return JSON.toJSONString(supplierService.all(params));
    }

    /**
     * 推荐供应商
     * @param recommend
     * @return
     */
    @RequestMapping("/recommend")
    public String recommend(SupplierRecommend recommend, HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        //request.setAttribute(Constant.OPERATOR_SESSION_KEY, u);
        recommend.setOperatorId(u.getOperatorId());
        recommend.setOperator(u.getOperator());
        recommend.setUpdateTime(new Date());
        return JSON.toJSONString(supplierService.recommend(recommend));
    }


    /**
     * 取消推荐
     * @param recommend
     * @return
     */
    @RequestMapping("/cancelRecommend")
    public String cancelRecommend(SupplierRecommend recommend,HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        recommend.setOperatorId(u.getOperatorId());
        recommend.setOperator(u.getOperator());
        recommend.setUpdateTime(new Date());
        return JSON.toJSONString(supplierService.cancelRecommend(recommend));
    }


}
