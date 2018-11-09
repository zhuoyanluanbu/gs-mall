package com.gs.mall.common.dao;

import com.gs.mall.common.po.SystemConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * SystemConfig dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:18
 */
@Mapper
public interface SystemConfigDao extends BaseDao<SystemConfig,java.lang.Long> {


    /**
     * 根据key查询
     * @param key
     * @return
     */
    SystemConfig getByKey(String key);

    /**
     * 根据key修改
     * @param systemConfig
     * @return
     */
    int updateByKey(SystemConfig systemConfig);


}
