package com.gs.mall.common.service;

import com.gs.common.result.ResponsePageResult;
import com.gs.mall.common.dao.BaseDao;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/6.
 */
public abstract class AbstractBaseService<T, C> implements BaseService<T, C> {

    @Override
    public T getById(C id) {
        return getBaseDao().selectById(id);
    }

    @Override
    public ResponsePageResult getListByPage(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        int startIndex = 0;
        int fetchSize = 0;
        if ( pageNo != null && pageSize != null ) {
            startIndex = (pageNo-1) * pageSize;
            fetchSize = pageSize;
        }
        ResponsePageResult rp = ResponsePageResult.empty(pageNo==null?0:pageNo, pageSize==null?0:pageSize);
        int i = getBaseDao().selectCountByParam(params);
        rp.setTotalCount(i);
        if( i > 0 ) {
            rp.setData(getBaseDao().selectByParam(params,startIndex,fetchSize));
        }
        return rp;
    }

    @Transactional
    @Override
    public Boolean removeById(C id) {

        return getBaseDao().deleteById(id) > 0;
    }

    protected abstract BaseDao<T,C> getBaseDao();
}
