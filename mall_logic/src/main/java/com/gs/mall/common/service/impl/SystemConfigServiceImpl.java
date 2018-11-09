package com.gs.mall.common.service.impl;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.common.po.SystemConfig;
import com.gs.mall.common.dao.SystemConfigDao;
import com.gs.mall.common.service.SystemConfigService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * SystemConfig service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:18
 */
@Service("systemConfigService")
public class SystemConfigServiceImpl extends AbstractBaseService<SystemConfig,java.lang.Long> implements SystemConfigService {

    @Resource
    private SystemConfigDao systemConfigDao;
	
    @Override
    protected BaseDao<SystemConfig,java.lang.Long> getBaseDao() {
        return systemConfigDao;
    }

	

    @Transactional
    @Override
    public Boolean saveOrUpdate(SystemConfig t) {
        int i = 0;
        //SystemConfig order = SystemConfigFactory.getSystemConfigInstance();		
        if( t.getConfigId() == null ) {			
            i = systemConfigDao.insert(t); 			
        } else {
            i = systemConfigDao.update(t);
        }
        return (i > 0);
    }


    @Override
    public ResponseResult getSysConfig(String key) {
        return ResponseResult.successInstance().setData(systemConfigDao.getByKey(key).getConfigValue());
    }

    @Transactional
    @Override
    public ResponseResult updateSysConfig(SystemConfig config) {
        int update = systemConfigDao.updateByKey(config);
        return update > 0 ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }

}
