package com.gs.mall.config;

import com.gs.common.cache.JedisCache;
import com.gs.common.cache.JedisConfig;
import com.gs.common.mq.data.MQConfig;
import com.gs.common.openapi.config.OpenApiConfig;
import com.gs.common.util.StringUtils;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.util.StringUtil;
import com.gs.mall.partner.po.Merchant;
import com.gs.mall.partner.service.MerchantService;
import com.gs.mall.user.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * web 配置
 * Created by huangyp on 2017/9/6.
 */
@Configuration
@ComponentScan("com.gs.*")
@EnableTransactionManagement
public class WebConfigurer extends WebMvcConfigurerAdapter {
	
	Logger logger = LoggerFactory.getLogger(WebConfigurer.class);

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getWebAccessInterceptor()).addPathPatterns("/manage/**").excludePathPatterns("/manage/workOrder/todoNums");
        super.addInterceptors(registry);

    }

    private Map<String, String> getHeaderParams(HttpServletRequest request) {
    	Map<String,String> filter = new HashMap<>();
    	Enumeration<String> headers = request.getHeaderNames();
    	while(headers.hasMoreElements()){
    		String name = headers.nextElement();
//    		System.out.println(name);
    		String data = request.getHeader(name);
    		try {
				data = new String(data.getBytes("iso-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("#WebConfigurer: getHeaderParams() err{}",e);
			}
    		filter.put(name, data);
    	}
		return filter;
	}
    /**
     * 配置拦截器
     * @return
     */
    @Bean
    public HandlerInterceptor getWebAccessInterceptor() {

        return new HandlerInterceptorAdapter() {

            Logger logger = LoggerFactory.getLogger(HandlerInterceptor.class);

            @Autowired
            private MerchantService merchantService;

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//                StringBuffer sb = new StringBuffer("Web接口请求:");
//                sb.append(request.getRequestURI());
//                sb.append(";方法:" + request.getMethod() + " 开始调用。。。。。。 ");
//
//                StringBuffer sb1 = new StringBuffer("Web接口请求参数:");
//                Enumeration paramNames = request.getParameterNames();
//                while (paramNames.hasMoreElements()) {
//                    String paramName = (String) paramNames.nextElement();
//                    String[] paramValues = request.getParameterValues(paramName);
//                    if (paramValues.length == 1) {
//                        String paramValue = paramValues[0];
//                        if (paramValue.length() != 0) {
//                            sb1.append(paramName).append("=").append(paramValue).append("  ");
//                        }
//                    }
//                }
//
//                logger.info(sb.toString());
//                logger.info(sb1.toString());
                Map<String, String> headers = getHeaderParams(request);
                User u = new User();
                u.setOperator(headers.get("Auth-Adminname".toLowerCase()));
                u.setOperatorId(headers.get("Auth-Adminid".toLowerCase()));
                String time = request.getParameter("operatorTime");

                if (StringUtils.isNotEmpty(time) && time.matches("^[0-9]+$")) {
                    u.setOperateTime(new Date(Long.valueOf(time)));
                } else {
                    u.setOperateTime(new Date());
                }
                request.setAttribute(Constant.OPERATOR_SESSION_KEY, u);
                if (logger.isDebugEnabled())
                    logger.debug("current operator" + u.getOperator() + ", operatorId" + u.getOperatorId());
                Merchant merchant = (Merchant) JedisCache.getRedisObject(Constant.MERCHANT_SESSION_KEY);
                if (merchant == null) {
                    merchant = merchantService.getById(Long.valueOf(1L));
                    if (merchant != null) {
                        JedisCache.setRedisObjectExpired(Constant.MERCHANT_SESSION_KEY, merchant, Constant.MERCHANT_CACHE_TIME);
                    }
                }
                if (logger.isDebugEnabled())
                    logger.debug("current merchant name is: " + (merchant == null ? "" : merchant.getName()) + ", id: " + ((merchant == null) ? "" : merchant.getMerchantId()));
                request.setAttribute(Constant.MERCHANT_SESSION_KEY, merchant);
                return true;
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                super.afterCompletion(request, response, handler, ex);
                try {

//                    StringBuffer sb = new StringBuffer("调用完成:");
//                    sb.append(request.getRequestURI());
//                    sb.append(";方法:" + request.getMethod());
//
//                    long reqTime = System.currentTimeMillis();
//                    long respTime = System.currentTimeMillis();
//                    if (null != request.getAttribute("reqTime") && !StringUtils.isEmpty(request.getAttribute("reqTime").toString())) {
//                        reqTime = Long.parseLong(request.getAttribute("reqTime").toString());
//                    }
//                    String ip = StringUtil.getIp(request);
//                    sb.append(";调用时间:=" + (respTime - reqTime) + "=。。。。。。" + "IP：" + ip);
//                    logger.info(sb.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

        /**
         * 配置跨域访问
         * @return
         */
    @Bean
    public Filter getCORSFilter(){
        return new OncePerRequestFilter(){
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                response.setHeader("Access-Control-Allow-Origin", "*"); //允许哪些url可以跨域请求到本域
                response.setHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,OPTIONS"); //允许的请求方法，一般是GET,POST,PUT,DELETE,OPTIONS
                response.setHeader("Access-Control-Allow-Headers","x-requested-with,content-type,sign,timestamp"); //允许哪些请求头可以跨域
                filterChain.doFilter(request, response);
            }
        };
    }
	
	/**
     * 开启方法参数验证
     * 在controller的class上添加@Validated使用
     * @return
     */
    @Bean
    public MethodValidationPostProcessor getMethodValidationPostProcessor(){
        return new MethodValidationPostProcessor();
    }

    /**
     * 缓存配置
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "jedis")
    public JedisConfig getJedisConfig(){
        return new JedisConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "thread.pool")
    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor(){
        return new ThreadPoolTaskExecutor();
    }



    /**
     * MQ配置
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "gs.mq")
    public MQConfig getMQConfig(){
        return new MQConfig();
    }
    
    @Bean
    @ConfigurationProperties(prefix = "openapi")
    public OpenApiConfig getOpenApiConfig(){
        return new OpenApiConfig();
    }
}
