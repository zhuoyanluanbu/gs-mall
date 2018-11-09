package com.gs.mall.config;

import com.gs.common.result.ResponseResult;
import com.gs.common.result.StatusCodeConfig;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:12
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 异常处理
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public @ResponseBody
    ResponseResult jsonErrorHandler(Exception e) throws Exception {
        if( e instanceof BindException ) {
            BindException be = (BindException) e;
            if( be.getErrorCount() > 0 ) {
                List<String> list = new ArrayList<String>(be.getErrorCount());
                be.getAllErrors().forEach( er -> {
                    String s = er.getDefaultMessage();
                    list.add(s.matches("^[0-9]+$") ? StatusCodeConfig.getValue(s) : s);
                });
                return ResponseResult.instance(100102).setData(list);
            }
        } else if (e instanceof ConstraintViolationException){
            ConstraintViolationException cve = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> es = cve.getConstraintViolations();
            List<String> list = new ArrayList<String>(es.size());
            es.forEach(cv -> {
                String s = cv.getMessageTemplate();
                list.add(s.matches("^[0-9]+$") ? StatusCodeConfig.getValue(s) : s);
            });
            return ResponseResult.instance(100102).setData(list);
        } else if ( e instanceof NoHandlerFoundException )  {
            return ResponseResult.instance(100101);
        } else {
            e.printStackTrace();
        }
        return ResponseResult.failInstance("系统繁忙");
    }
}
