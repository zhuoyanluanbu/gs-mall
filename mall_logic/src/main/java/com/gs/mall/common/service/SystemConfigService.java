package com.gs.mall.common.service;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.po.SystemConfig;

/**
 * SystemConfig service interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:18
 */
public interface SystemConfigService extends BaseService<SystemConfig,java.lang.Long>{

    /**
     * 获取配置
     * @param key
     * @return
     */
    ResponseResult getSysConfig(String key);

    /**
     * 修改配置
     * @param config
     * @return
     */
    ResponseResult updateSysConfig(SystemConfig config);

}
