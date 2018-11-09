package com.gs.mall.partner.service.impl;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.partner.po.Merchant;
import com.gs.mall.partner.dao.MerchantDao;
import com.gs.mall.partner.service.MerchantService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Merchant service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:21
 */
@Service("merchantService")
public class MerchantServiceImpl extends AbstractBaseService<Merchant,java.lang.Long> implements MerchantService {

    @Resource
    private MerchantDao merchantDao;
	
    @Override
    protected BaseDao<Merchant,java.lang.Long> getBaseDao() {
        return merchantDao;
    }

	

    @Transactional
    @Override
    public Boolean saveOrUpdate(Merchant t) {
        int i = 0;
        //Merchant order = MerchantFactory.getMerchantInstance();		
        if( t.getMerchantId() == null ) {			
            i = merchantDao.insert(t); 			
        } else {
            i = merchantDao.update(t);
        }
        return (i > 0);
    }

}
