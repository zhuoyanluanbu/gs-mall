package com.gs.mall.finance.service.impl;

import com.gs.common.cache.JedisCache;
import com.gs.common.util.DateUtil;
import com.gs.common.util.StringUtils;
import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.finance.FinanceFactory;
import com.gs.mall.finance.po.NetflowRecord;
import com.gs.mall.finance.dao.NetflowRecordDao;
import com.gs.mall.finance.service.NetflowRecordService;

import com.gs.mall.user.po.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * NetflowRecord service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:19
 */
@Service("netflowRecordService")
public class NetflowRecordServiceImpl extends AbstractBaseService<NetflowRecord,java.lang.Long> implements NetflowRecordService {

    @Resource
    private NetflowRecordDao netflowRecordDao;
	
    @Override
    protected BaseDao<NetflowRecord,java.lang.Long> getBaseDao() {
        return netflowRecordDao;
    }

	

    @Transactional
    @Override
    public Boolean saveOrUpdate(NetflowRecord t) {
        int i = 0;
        //NetflowRecord order = NetflowRecordFactory.getNetflowRecordInstance();		
        if( t.getNrId() == null ) {			
            i = netflowRecordDao.insert(t); 			
        } else {
            i = netflowRecordDao.update(t);
        }
        return (i > 0);
    }

    @Transactional
    @Override
    public Boolean save(User user, String ip, String deviceInfo) {
        if( user == null || StringUtils.isEmpty(user.getOperatorId()) ) {
            return Boolean.FALSE;
        }
        String key = "GS_Netflow_"+user.getOperatorId();
        String v = (String) JedisCache.getRedisObject(key);
        if( ! StringUtils.isEmpty(v) ) {
            return Boolean.FALSE;
        }
        long l = DateUtil.getToNightZeroMills() - System.currentTimeMillis();
        if( l > 3000 ) { //最少4秒起缓存
            JedisCache.setRedisObjectExpired(key,"1", (int)(l/1000));
        }
        int i = netflowRecordDao.insert(FinanceFactory.getNetflowRecordInstance(user.getOperatorId(),
                user.getOperator(), ip, deviceInfo));
        return (i > 0);
    }
}
