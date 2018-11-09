package com.gs.mall.finance.service.impl;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.finance.po.TradeStatement;
import com.gs.mall.finance.dao.TradeStatementDao;
import com.gs.mall.finance.service.TradeStatementService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * TradeStatement service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:20
 */
@Service("tradeStatementService")
public class TradeStatementServiceImpl extends AbstractBaseService<TradeStatement,java.lang.Long> implements TradeStatementService {

    @Resource
    private TradeStatementDao tradeStatementDao;
	
    @Override
    protected BaseDao<TradeStatement,java.lang.Long> getBaseDao() {
        return tradeStatementDao;
    }

	

    @Transactional
    @Override
    public Boolean saveOrUpdate(TradeStatement t) {
        int i = 0;
        //TradeStatement order = TradeStatementFactory.getTradeStatementInstance();		
        if( t.getTsId() == null ) {			
            i = tradeStatementDao.insert(t); 			
        } else {
            i = tradeStatementDao.update(t);
        }
        return (i > 0);
    }

}
