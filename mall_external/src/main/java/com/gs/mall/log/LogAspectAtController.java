package com.gs.mall.log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;

/**
 * 切面日志 ： Controller
 * liuy
 */
@Component
@Aspect
public class LogAspectAtController {

    final static Logger logger = LoggerFactory.getLogger("access");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static List<String> specialList = Arrays.asList("host","content-length","content-type",
    		"user-agent","gate-cluster","source-url-path","x-forwarded-for","accept-encoding","configParam",
    		"connection","cache-control","postman-token","accept","proxy-client-ip","wl-proxy-client-ip",
    		"x-scheme","x-real-ip","accept-language","referer","cookie","x-requested-with");
    @Autowired
    private LogChain logChain;

    public static final String getIpAddr(HttpServletRequest request) {
        String ipString = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getRemoteAddr();
        }
        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ipString.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ipString = str;
                break;
            }
        }
        return ipString;
    }

    /**
     * 一个接入点
     */
    @Pointcut("execution(* com.gs.mall.*.controller..*.*(..)) and " +
    		 "execution(* com.gs.mall.controller..*.*(..)) and " +
    		 "execution(* com.gs.mall.config.GlobalExceptionHandler..(..))")
    public void controllerJointcut() {
    }
    /**
     * @Description: 方法调用前触发
     */
    @Before("controllerJointcut()")
    public void doBeforeInControllerLayer(JoinPoint joinPoint) {
        LogData logData = logChain.getLogData();
        logData.setRequestStart(System.currentTimeMillis());// 记录方法开始执行的时间
    }

    /**
     * @Description: 方法调用后触发
     */
    @After("controllerJointcut()")
    public void doAfterInControllerLayer(JoinPoint joinPoint) {
        LogData logData = logChain.getLogData();
        logData.setRequestEnd(System.currentTimeMillis());// 记录方法执行完成的时间
        this.printOptLog();
    }

    /**
     * @Description: 环绕触发
     */
    @Around("controllerJointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    	logger.info("初始化请求的入参");
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();

        LogData logData = logChain.getLogData();

        // 获取请求地址
        String requestPath = request.getRequestURI();
        logData.setUrl(requestPath);
        logData.setIp(getIpAddr(request));
        logData.setUserAgent(request.getHeader("User-Agent"));
        logData.setMethod(request.getMethod());
//        String queryString = request.getQueryString();
        Map<String,String[]> m = request.getParameterMap();
//        System.out.println("请求参数"+queryString);
//        System.out.println("请求参数2"+JSON.toJSONString(m));
        //过滤输入
        Object inputParamMap = null;
        Map<String,Object> map = null;
        if(urlInPutFilter.contains(requestPath)) {
        	//不打印输入
        	inputParamMap = "request is filtred";
        } else {
        	// 获取输入参数
	        Object[] objs = pjp.getArgs();
	        if(objs!=null&&objs.length!=0){
	            inputParamMap = objs[0];
	        }
	        try {
				if(inputParamMap == null){
					System.out.println("没有获取参数");
				}else{
					if(inputParamMap instanceof HttpServletRequest){
						 HttpServletRequest req = ((HttpServletRequest)inputParamMap);
                         Enumeration enu = req.getParameterNames();
                         map = new HashMap<>();
                         while(enu.hasMoreElements()){
                             String paraName=(String)enu.nextElement();
                             if(specialList.contains(paraName.toLowerCase())){
     			    			continue;
     			    		}
                             map.put(paraName, req.getParameter(paraName));
                         }
//					}else if(inputParamMap instanceof LinkedHashMap){
//						map = (Map<String,Object>)inputParamMap;
					}

					if(map == null){
						map = new HashMap<>();
						//解析请求参数
						map.putAll(getRequestParamMap(m));
					}
					//读取header里的内容

			    	Enumeration<String> headers = request.getHeaderNames();
			    	while(headers.hasMoreElements()){
			    		String name = headers.nextElement();
			    		if(specialList.contains(name.toLowerCase())){
			    			continue;
			    		}
			    		String data = request.getHeader(name);
			    		try {
							data = new String(data.getBytes("iso-8859-1"),"UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
			    		map.put(name, data);
			    	}
//			    	inputParamMap = map;
				}
			} catch (Exception e) {
				logger.warn("解析入参异常",e);
			}
        }

        // 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行
        Map<String, Object> outputParamMap = new HashMap<>();

        Object result = null;
        try {
        	result = pjp.proceed();
		} catch (Throwable e) {
			logData.setException(e.getMessage());
			throw e;  //原路抛出
		} finally {
			//过滤响应
	        if(urlOutPutFilter.contains(requestPath)) {
	        	outputParamMap.put("result", "response is filtered");
	        } else {
	        	outputParamMap.put("result", result);
	        }

	        try {
	        	if(urlInPutFilter.contains(requestPath)){
	        		logData.setRequest(JSON.toJSONString(JSON.toJSON(inputParamMap)));
	        	}else{
	        		logData.setRequest(JSON.toJSONString(map));
	        	}
				logData.setResponse(JSON.toJSONString(outputParamMap));
			} catch (Exception e) {
				logger.warn("打印参数异常",e);
			}
		}

        return result;
    }

    private Map<String,Object> getRequestParamMap(Map<String, String[]> m) {
    	Map<String,Object> map = new HashMap<>();
    	if(m!=null){
    		for(String key : m.keySet()){
    			map.put(key, m.get(key)!=null?m.get(key)[0]:null);
    		}
    	}
		return map;
	}

	private Map<String,Object> getRequestParam(String queryString) {
		Map<String,Object> map = new HashMap<>();
		if(StringUtils.isEmpty(queryString)){
			return map;
		}
		try {
			queryString = URLDecoder.decode(queryString,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if(StringUtils.isEmpty(queryString)){
			return map;
		}
		for(String p : queryString.split("&")){
			String[] arr = p.split("=");
			map.put(arr[0], arr[1]);
		}
		return map;
	}

	/**
     *
     * @Title：printOptLog
     * @Description: 输出日志
     */
    private void printOptLog() {
    	try {

    	    LogData logData = logChain.getLogData();

//    	    if("/merchant/merchant/order/unReadList".equalsIgnoreCase(logData.getUrl())) {
//    	        return;
//    	    }

    		StringBuilder output = new StringBuilder();
            output.append("\nip:\t\t\t").append(logData.getIp());
            output.append("\nurl:\t\t").append(logData.getUrl());
            output.append("\ntime:\t\t").append(sdf.format(logData.getRequestStart()));
            output.append("\ncost:\t\t").append((logData.getRequestEnd() - logData.getRequestStart())).append("ms");
            output.append("\nrequest:\t").append(logData.getRequest());
            output.append("\nresponse:\t").append(logData.getResponse());
            logger.info(output.toString());
    	} catch(Exception e) {
    	}
    }

    /*
     * 过滤请求
     * 不打印请求的参数
     */
    private final static List<String> urlInPutFilter = Arrays.asList(
//    		"/merchant/merchant/file/imgUpload", // 图片的上传(统一入口)
//    		"/merchant/merchant/order/unReadList"// 商户未读订单查询(一个非常平凡的结果)
    );

    /*
     * 过滤响应
     * 不打印响应信息
     */
    private final static List<String> urlOutPutFilter = Arrays.asList(
//            "/merchant/merchant/order/unReadList"// 商户未读订单查询(一个非常平凡的结果)
	);
}
