package com.gs.mall.common.service.impl;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.dao.LogisticsDao;
import com.gs.mall.common.po.Logistics;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.common.service.LogisticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * HotWordGroup service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:18
 */
@Service("logisticsService")
public class LogisticsServiceImpl extends AbstractBaseService<Logistics,Long> implements LogisticsService {

    @Resource
    private LogisticsDao logisticsDao;

    @Override
    protected BaseDao<Logistics,Long> getBaseDao() {
        return logisticsDao;
    }


    @Transactional
    @Override
    public Boolean saveOrUpdate(Logistics t) {
        int i = 0;
        if( t.getLogisticsId() == null ) {
            i = getBaseDao().insert(t);
        } else {
            i = getBaseDao().update(t);
        }
        return i > 0 ;
    }


}
