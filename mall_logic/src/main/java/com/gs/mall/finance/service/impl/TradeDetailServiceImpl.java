package com.gs.mall.finance.service.impl;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.finance.po.TradeDetail;
import com.gs.mall.finance.dao.TradeDetailDao;
import com.gs.mall.finance.service.TradeDetailService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;


/**
 * TradeDetail service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:20
 */
@Service("tradeDetailService")
public class TradeDetailServiceImpl extends AbstractBaseService<TradeDetail,java.lang.String> implements TradeDetailService {

    @Resource
    private TradeDetailDao tradeDetailDao;
	
    @Override
    protected BaseDao<TradeDetail,java.lang.String> getBaseDao() {
        return tradeDetailDao;
    }

	

    @Transactional
    @Override
    public Boolean saveOrUpdate(TradeDetail t) {
        int i = 0;
        //TradeDetail order = TradeDetailFactory.getTradeDetailInstance();		
        if( t.getTdId() == null ) {			
				t.setTdId(UUID.randomUUID().toString().replaceAll("-",""));
            i = tradeDetailDao.insert(t); 			
        } else {
            i = tradeDetailDao.update(t);
        }
        return (i > 0);
    }


    @Override
    public ResponseResult getByIdNo(String formId, String tradeNo) {
        TradeDetail tradeDetail = tradeDetailDao.queryByIdNo(formId, tradeNo);
        return ResponseResult.successInstance().setData(tradeDetail);
    }



}
