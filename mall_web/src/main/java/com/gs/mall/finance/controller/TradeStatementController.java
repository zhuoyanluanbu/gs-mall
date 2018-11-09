package com.gs.mall.finance.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.common.util.DateUtil;
import com.gs.common.util.StringUtils;
import com.gs.mall.base.controller.BaseController;
import com.gs.mall.common.util.PageUtil;
import com.gs.mall.export.ExportEngine;
import com.gs.mall.finance.po.TradeStatement;
import com.gs.mall.finance.service.TradeStatementService;
import com.gs.mall.oss.OSSFileTemp;
import com.gs.mall.oss.OssResult;
import com.gs.mall.params.adapter.TradeSeatementDataAdapter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易对账单
 * Created by huangyp on 2017/8/26.
 */
@RestController
@RequestMapping("/manage/tradeStatement")
public class TradeStatementController extends BaseController {

    Logger logger = Logger.getLogger(TradeStatementController.class);

    @Autowired
    private TradeStatementService tradeStatementService;

    @Autowired
    TradeSeatementDataAdapter tradeSeatementDataAdapter;

    @Resource(name = "excelEngine")
    ExportEngine exportEngine;

    @Autowired
    OSSFileTemp ossFileTemp;

    @Value("${oss.test.env}")
    Boolean testEnv;


    /**
     * 查询列表
     *
     * @return
     */
    @RequestMapping("/list")
    public ResponsePageResult list(@RequestParam Map<String, Object> params, PageUtil pageUtil, HttpServletRequest request) {
        params.put("merchantId", getMerchant(request).getMerchantId());
        if (parseQueryDate(params)) 
        	return ResponsePageResult.instance(102010);
        return tradeStatementService.getListByPage(params, pageUtil.getPageNo(), pageUtil.getPageSize());
    }

    /**
     * Method: 导出交易明细
     * {
     * formId:订单号、工单号
     * begineDate:开始时间
     * endDate:结束时间
     * type:1：入账2：退款3：转账
     * }
     * Created by chenchuan
     */
    @RequestMapping("/export")
    public ResponseResult export(@RequestParam Map<String, Object> params, PageUtil pageUtil, HttpServletRequest request) {
        params.put("merchantId", getMerchant(request).getMerchantId());
        if (parseQueryDate(params)) 
        	return ResponseResult.instance(102010);
        ResponsePageResult result = tradeStatementService.getListByPage(params, pageUtil.getPageNo(), pageUtil.getPageSize());
        List<TradeStatement> originalData = (List<TradeStatement>) result.getData();
        Map<String,String> url = new HashMap<>(1);
        try {
            File file = exportEngine.createExportFile(exportEngine.genFile(originalData,tradeSeatementDataAdapter));
            /*if(testEnv){
                String host = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
                String path = file.getPath();
                url.put("url",host+"/"+path.substring((path.contains("/")?path.lastIndexOf("/"):path.lastIndexOf("\\"))+1));
            }else{
                OssResult ossResult = ossFileTemp.uploadFileTemp(file,ossFileTemp.mall_key());
                url.put("url",ossResult.getUrl());
            }*/
            url.put("fileName",file.getName());
        } catch (Exception e) {
            ResponseResult responseResult = ResponseResult.failInstance();
            responseResult.setMessage("导出文件失败：" + e.getMessage());
            return responseResult;
        }
        JSONObject response = JSON.parseObject(toJsonString(ResponseResult.successInstance().setData(url)));
        response.remove("data");
        response.put("url",url.get("fileName"));
        return ResponseResult.successInstance().setData(response);
    }

    private boolean parseQueryDate(@RequestParam Map<String, Object> params) {
        String beginDate = (String) params.get("beginDate");
        if (StringUtils.isNotEmpty(beginDate)) {
            Date startCreateTime = DateUtil.parseStringToDate(beginDate);
            if (startCreateTime == null) {
                return true;
            }
            params.put("startCreateTime", startCreateTime);
            params.remove("beginDate");
        }
        String endDate = (String) params.get("endDate");
        if (StringUtils.isNotEmpty(endDate)) {
            Date endCreateTime = null;
            try {
                endCreateTime = DateUtil.parseDate(endDate+" 23:59:59","yyyy-MM-dd HH:mm:ss");
            } catch (ParseException e) {
                logger.error("交易对账单查询结束日期转换失败",e);
            }
            if (endCreateTime == null) {
                return true;
            }
            params.put("endCreateTime", endCreateTime);
            params.remove("endDate");
        }
        return false;
    }
    
    public String toJsonString(Object obj) {
        return JSON.toJSONString( obj ,
                SerializerFeature.WriteDateUseDateFormat);
    }
}
