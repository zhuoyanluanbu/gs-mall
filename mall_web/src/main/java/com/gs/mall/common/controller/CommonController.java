package com.gs.mall.common.controller;


import com.gs.common.result.ResponseResult;
import com.gs.mall.common.constant.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公共控制层
 * Created by huangyp on 2018/1/12.
 */
@RestController
public class CommonController {


    @RequestMapping(value = "/common/system/getAppId", method = RequestMethod.GET)
    public ResponseResult getAppId(){
        return ResponseResult.successInstance().setData(Constant.appID);
    }
    
    @Value("${export.file.path}")
    String exportFilePath;

    private Logger logger = LoggerFactory.getLogger(CommonController.class);



    /**
     * 文件下载
     *
     * @param fileName
     * @param response
     */
    @RequestMapping(value = "/common/system/fileDownLoad", method = RequestMethod.GET)
    public void fileDownLoad(@NotBlank(message = "105002") String fileName,
                             HttpServletRequest request,
                             HttpServletResponse response) {

        File file = new File(exportFilePath + "/" + fileName);
        if (!file.exists()) {
            String s = "{\"code\":105003,\"message\":\"this file is not exists.\",\"timestamp\":" + System.currentTimeMillis() + "}";
            response.setContentType("application/json;charset=utf-8");
            try (PrintWriter pw = response.getWriter()) {
                pw.write(s);
                pw.flush();
            } catch (IOException e) {
                logger.error("下载文件返回错误异常：", e);
            }
            return;
        }
        //根据浏览器类型处理文件名称
        response.setContentType("application/octet-stream;charset=utf-8"); //设置输出二进制文件
        String agent= request.getHeader("USER-AGENT").toLowerCase();
        if (agent.indexOf("msie") > -1) {
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        } else {
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        }
        try ( InputStream in = new FileInputStream(file) ) {
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[1024];
            int len = 0;
            while ( (len = in.read(b)) > 0 ) {
                out.write(b, 0, len);
            }
            out.flush();
        } catch ( IOException ie ) {
            logger.error("下载文件出现异常：", ie);
        }
    }


}
