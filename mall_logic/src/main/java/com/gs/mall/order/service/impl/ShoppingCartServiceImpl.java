package com.gs.mall.order.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.result.ResponseResult;
import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.order.po.ShoppingCart;
import com.gs.mall.order.dao.ShoppingCartDao;
import com.gs.mall.order.service.ShoppingCartService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * ShoppingCart service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:14
 */
@Service("shoppingCartService")
public class ShoppingCartServiceImpl extends AbstractBaseService<ShoppingCart,java.lang.Long> implements ShoppingCartService {

    private Logger logger = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    @Resource
    private ShoppingCartDao shoppingCartDao;
	
    @Override
    protected BaseDao<ShoppingCart,java.lang.Long> getBaseDao() {
        return shoppingCartDao;
    }

	

    @Transactional
    @Override
    public Boolean saveOrUpdate(ShoppingCart t) {
        int i = 0;
        //ShoppingCart order = ShoppingCartFactory.getShoppingCartInstance();
        if( t.getScId() == null ) {
            if( t.getCreateTime() == null ){
                t.setCreateTime(new Date());
            }
            t.setStatus(1);
            i = shoppingCartDao.insert(t);
        } else {
            i = shoppingCartDao.update(t);
        }
        return (i > 0);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public ResponseResult update(String updateJson, String ownerId) {
        JSONObject json = null;
        try {
            json = JSONObject.parseObject(updateJson);
        } catch ( Exception e ) {
            logger.error("解析json字符串失败：", e );
        }
        int i = 0;

        if( json.containsKey("updateBuyNum") ) {
            JSONArray jsonArray = json.getJSONArray("updateBuyNum");
            if( jsonArray != null && jsonArray.size() > 0 ) {
                List<ShoppingCart> list = new ArrayList<ShoppingCart>(jsonArray.size());
                for ( int a = 0 ; a < jsonArray.size() ; ++ a ) {
                    JSONObject js = jsonArray.getJSONObject(a);
                    ShoppingCart sc = new ShoppingCart();
                    sc.setScId(js.getLong("id"));
                    sc.setOwnerId(ownerId);
                    Integer bn = js.getInteger("buyNum");
                    if ( bn == null || bn <= 0 ) {
                        return ResponseResult.instance(104001);
                    }
                    sc.setBuyNum(bn);
                    list.add(sc);
                }
                i = shoppingCartDao.batchUpdate(list);
            }
        }

        if( json.containsKey("removeIds") ) {
            JSONArray jsonArray = json.getJSONArray("removeIds");
            if( jsonArray != null && jsonArray.size() > 0 ) {
                //List<Object> list = jsonArray.subList( 0 , jsonArray.size());
                Long[] ids = new Long[jsonArray.size()];
                for (int j =0;j<jsonArray.size();j++){
                    ids[j] = Long.valueOf(jsonArray.get(j).toString());
                }
                i = shoppingCartDao.batchDelete(ids, ownerId);
            }
        }

        return i > 0 ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }
}
