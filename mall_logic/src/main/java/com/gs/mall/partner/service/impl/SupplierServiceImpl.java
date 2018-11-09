package com.gs.mall.partner.service.impl;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.partner.dao.SupplierRecommendDao;
import com.gs.mall.partner.po.Supplier;
import com.gs.mall.partner.dao.SupplierDao;
import com.gs.mall.partner.po.SupplierRecommend;
import com.gs.mall.partner.service.SupplierService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;


/**
 * Supplier service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:21
 */
@Service("supplierService")
public class SupplierServiceImpl extends AbstractBaseService<Supplier,java.lang.String> implements SupplierService {

    @Resource
    private SupplierDao supplierDao;

    @Resource
    private SupplierRecommendDao supplierRecommendDao;
	
    @Override
    protected BaseDao<Supplier,java.lang.String> getBaseDao() {
        return supplierDao;
    }

	

    @Transactional
    @Override
    public Boolean saveOrUpdate(Supplier t) {
        int i = 0;
        //Supplier order = SupplierFactory.getSupplierInstance();		
        if( t.getSupplierId() == null ) {			
				t.setSupplierId(UUID.randomUUID().toString().replaceAll("-",""));
            i = supplierDao.insert(t); 			
        } else {
            i = supplierDao.update(t);
        }
        return (i > 0);
    }



    @Override
    public ResponseResult delSupplier(Supplier supplier) {
        int i = supplierDao.delSupplier(supplier);
        return i > 0 ? ResponseResult.successInstance() : ResponseResult.failInstance();
        
    }

    @Override
    public ResponseResult recommend(SupplierRecommend recommend) {
        int recommended = supplierRecommendDao.isRecommended(recommend.getSupplierId());
        if(recommended > 0){
            SupplierRecommend supplierRecommend = supplierRecommendDao.queryBySupplierId(recommend.getSupplierId());
            if(!recommend.getPrId().equals(supplierRecommend.getPrId())){
                ResponseResult responseResult = ResponseResult.failInstance();
                responseResult.setMessage("供应商已经推荐了");
                return responseResult;
            }
        }
        int i = supplierRecommendDao.update(recommend);
        return i > 0 ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }

    @Override
    public ResponseResult cancelRecommend(SupplierRecommend recommend) {
        int i = supplierRecommendDao.cancelRecommend(recommend);
        return i > 0 ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }

    @Override
    public ResponseResult recommendList() {
        return ResponseResult.successInstance().setData( supplierRecommendDao.list());
    }

    @Override
    public ResponseResult all(Map<String,Object> param) {
        return ResponseResult.successInstance().setData(supplierDao.all(param));
    }

}
