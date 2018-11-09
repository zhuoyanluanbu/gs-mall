package com.gs.mall.order.service;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.service.BaseService;
import com.gs.mall.order.po.ShoppingCart;

/**
 * ShoppingCart service interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:14
 */
public interface ShoppingCartService extends BaseService<ShoppingCart,java.lang.Long>{


    /**
     * 更新json
     * @param updateJson
     * @return
     */
    ResponseResult update(String updateJson , String ownerId);

}
