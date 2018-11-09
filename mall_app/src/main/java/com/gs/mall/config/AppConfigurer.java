package com.gs.mall.config;

import com.alibaba.fastjson.JSON;
import com.gs.common.cache.JedisCache;
import com.gs.common.cache.JedisConfig;
import com.gs.common.mq.data.MQConfig;
import com.gs.common.openapi.config.AppSecreConfig;
import com.gs.common.openapi.config.OpenApiConfig;
import com.gs.common.openapi.model.UserInfo;
import com.gs.common.openapi.service.UserApiService;
import com.gs.common.result.ResponseResult;
import com.gs.common.solr.config.SolrConfig;
import com.gs.common.util.StringUtils;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.util.StringUtil;
import com.gs.mall.finance.service.NetflowRecordService;
import com.gs.mall.user.po.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Enumeration;

/**
 * web 配置
 * Created by huangyp on 2017/9/6.
 */
@Configuration
@ComponentScan("com.gs.*")
@EnableTransactionManagement
public class AppConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getWebAccessInterceptor()).addPathPatterns("/app/**");
        super.addInterceptors(registry);

    }

    /**
     * 配置拦截器
     * @return
     */
    @Bean
    public HandlerInterceptor getWebAccessInterceptor(){

        return new HandlerInterceptorAdapter(){
            
            private final Logger logger = Logger.getLogger(HandlerInterceptorAdapter.class);

            @Autowired
            private ThreadPoolTaskExecutor threadPoolTaskExecutor;

            @Value("${mall.app.entrance.uri}")
            private String appEntranceUri;

            @Autowired
            private NetflowRecordService netflowRecordService;

            /**
             * 验证openId是否为空
             * @param request
             * @param response
             * @param handler
             * @return
             * @throws Exception
             */
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//                StringBuffer sb = new StringBuffer("App接口请求:");
//                sb.append(request.getRequestURI());
//                sb.append(";方法:" + request.getMethod()+" 开始调用。。。。。。 ");
//
//                StringBuffer sb1 = new StringBuffer("App接口请求参数:");
//                Enumeration paramNames = request.getParameterNames();
//                while (paramNames.hasMoreElements()) {
//                    String paramName = (String) paramNames.nextElement();
//
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

                String openId = request.getParameter("openId");
                //UserInfoUtil
                if( StringUtils.isEmpty(openId) ) {
                    openId = request.getHeader("openId");
                    if (StringUtils.isEmpty(openId)) {
                    	openId = request.getParameter("open_id");
					}
                    if( StringUtils.isEmpty(openId) ) {
                        response.setHeader("Content-Type","application/json;charset=UTF-8");
                        response.getWriter().write(JSON.toJSONString(ResponseResult.instance(101004)));
                        return false;
                    }
                }
                UserInfo userInfo = (UserInfo) JedisCache.getRedisObject(Constant.APP_USER_CACHE_PREFIX+openId);
                if( userInfo == null ) {
                    AppSecreConfig config = AppSecreConfig.build(Constant.appID, Constant.appSecretKey, Constant.appPrivateSecret);
                    ResponseResult rr = UserApiService.getUserInfo(config, openId);
                    if( rr.getCode() != 0 || rr.getData() == null ) {
                        response.setHeader("Content-Type","application/json;charset=UTF-8");
                        response.getWriter().write(JSON.toJSONString(ResponseResult.instance(101005)));
                        return false;
                    }
                    userInfo = (UserInfo) rr.getData();
                    JedisCache.setRedisObjectExpired(Constant.APP_USER_CACHE_PREFIX+openId, userInfo, Constant.APP_USER_CACHE_EXPIRE);//缓存2小时
                }
                request.setAttribute(Constant.APP_USER_SESSION_KEY, userInfo);
                if( appEntranceUri.equals(request.getRequestURI()) ) {
                    final UserInfo temp = userInfo;
                    final String ip = StringUtil.getIp(request);
                    final String deviceInfo = StringUtils.isEmpty(request.getParameter("deviceInfo")) ? request.getHeader("User-Agent") : request.getParameter("deviceInfo");
                    threadPoolTaskExecutor.execute(() -> {
                        User user = new User();
                        user.setOperatorId(temp.getOpenId());
                        user.setOperator(temp.getUserName());
                        netflowRecordService.save(user,ip,deviceInfo);
                    });
                }
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
//                    if(null!=request.getAttribute("reqTime")&&!StringUtils.isEmpty(request.getAttribute("reqTime").toString())){
//                        reqTime=Long.parseLong(	request.getAttribute("reqTime").toString());
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
    
    @Bean
    @ConfigurationProperties(prefix = "solr")
    public SolrConfig getSolrConfig(){
    	SolrConfig solrConfig = new SolrConfig();
        return solrConfig;
    }
}
