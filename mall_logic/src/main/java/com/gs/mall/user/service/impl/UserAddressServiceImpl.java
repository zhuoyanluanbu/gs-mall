package com.gs.mall.user.service.impl;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.user.po.UserAddress;
import com.gs.mall.user.dao.UserAddressDao;
import com.gs.mall.user.service.UserAddressService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * UserAddress service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:22
 */
@Service("userAddressService")
public class UserAddressServiceImpl extends AbstractBaseService<UserAddress,java.lang.Long> implements UserAddressService {

    @Resource
    private UserAddressDao userAddressDao;
	
    @Override
    protected BaseDao<UserAddress,java.lang.Long> getBaseDao() {
        return userAddressDao;
    }

	

    @Transactional
    @Override
    public Boolean saveOrUpdate(UserAddress t) {
        int i = 0;
        //UserAddress order = UserAddressFactory.getUserAddressInstance();		
        if( t.getUserAddrId() == null ) {			
            i = userAddressDao.insert(t); 			
        } else {
            i = userAddressDao.update(t);
        }
        return (i > 0);
    }

}
