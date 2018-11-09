package com.gs.mall.common.service.impl;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.common.po.HotWordGroup;
import com.gs.mall.common.dao.HotWordGroupDao;
import com.gs.mall.common.service.HotWordGroupService;

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
@Service("hotWordGroupService")
public class HotWordGroupServiceImpl extends AbstractBaseService<HotWordGroup,java.lang.Integer> implements HotWordGroupService {

    @Resource
    private HotWordGroupDao hotWordGroupDao;
	
    @Override
    protected BaseDao<HotWordGroup,java.lang.Integer> getBaseDao() {
        return hotWordGroupDao;
    }


    @Transactional
    @Override
    public Boolean saveOrUpdate(HotWordGroup t) {
        int i = 0;
        if( t.getGroupId() == null ) {
            int num = hotWordGroupDao.maxSortNum();
            num ++;
            t.setSortNum(num);
            i = getBaseDao().insert(t);
        } else {
            i = getBaseDao().update(t);
        }
        return i > 0 ;
    }

    @Override
    public ResponseResult all(Integer status){
        return ResponseResult.successInstance().setData(hotWordGroupDao.all(status));
    }

    @Transactional
    @Override
    public ResponseResult add(HotWordGroup hot) {
        int i = hotWordGroupDao.maxSortNum();
        i++;
        hot.setSortNum(i);
        int update = hotWordGroupDao.insert(hot);
        return update > 0 ? ResponseResult.successInstance():ResponseResult.failInstance();
    }

    @Transactional
    @Override
    public ResponseResult update(HotWordGroup hot) {
        return getBaseDao().update(hot) == 1 ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }
}
