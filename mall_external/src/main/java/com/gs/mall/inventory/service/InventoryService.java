package com.gs.mall.inventory.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.mall.inventory.cache.InventoryCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by huangyp on 2017/8/22.
 */
@Component("inventoryService")
@Scope()//default "singleton"
public class InventoryService {

    /**
     * spring 线程池
     */
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private Lock lock = new ReentrantLock();

    @Autowired
    private InventoryCache inventoryCache;

    /**
     * 减去库存（在原来库存的基础上减去指定库存，会持久化到数据库）
     * @param array [{id:商品ID,skuId:商品SKUID,num:商品数量}]
     * @return
     */
    public boolean subtract(JSONArray array){
        return inventoryProccess(array,2);
    }


    /**
     * 添加库存（在原来库存的基础上增加指定库存，会持久化到数据库）
     * @param array [{id:商品ID,skuId:商品SKUID,num:商品数量}]
     * @return
     */
    public boolean increase(JSONArray array){
        return inventoryProccess(array,1);
    }


    /**
     * 设置库存（将库存设置为指定的值，不会持久化到数据库）
     * @param array [{id:商品ID,skuId:商品SKUID,num:商品数量}]
     * @return
     */
    public boolean set(JSONArray array){
        return inventoryProccess(array,3);
    }

    /**
     * 库存处理
     * @param array
     * @param type 1 增加 2 减少 3 设置
     * @return
     */
    private boolean inventoryProccess(final JSONArray array,final int type){
        if( array == null || array.isEmpty() ) {
            return false;
        }
        boolean result = true;
        try {
            lock.lock();
//            Future<Boolean> future = threadPoolTaskExecutor.submit(new Callable<Boolean>() {
//                @Override
//                public Boolean call() throws Exception {
                    Boolean b = Boolean.FALSE;
                    try {
                        Map<String, Integer> params = new HashMap<String, Integer>(array.size());
                        for (int i = 0; i < array.size(); ++i) {
                            JSONObject json = array.getJSONObject(i);
                            params.put(inventoryCache.getKey(json.getLong("id"),
                                    json.getString("skuId")), json.getInteger("num"));
                        }
                        switch ( type ) {
                            case 1 : //增加
                                b = inventoryCache.setAndIncrease(params);
                                break;
                            case 2 : //减少库存
                                b = inventoryCache.setAndSubtract(params);
                                break;
                            case 3 :
                                b = inventoryCache.setValues(params);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
//                    return b;
//                }
//            });
            result = b;
            //库存处理成功，更新数据库
            if ( result && type != 3 ) {
                inventoryCache.flushCacheToDB(array);
            }
        } catch (Exception e){
            if( result && type != 3 ) {
                //此处如果是增加则需要变为减少，此处需要取反
                rollback(array, (type == 2));
            }
            result = false;
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return result;
    }

    /**
     * 回滚
     * @param array
     * @param isIncrease 是否是增加 增加 true，减少 false
     */
    private void rollback( JSONArray  array , final boolean isIncrease){
        try{
            lock.lock();
//            threadPoolTaskExecutor.execute(() -> {
                    Boolean b = Boolean.FALSE;
                    try {
                        Map<String, Integer> params = new HashMap<String, Integer>(array.size());
                        for (int i = 0; i < array.size(); ++i) {
                            JSONObject json = array.getJSONObject(i);
                            params.put(inventoryCache.getKey(json.getLong("id"),
                                    json.getString("skuId")), json.getInteger("num"));
                        }
                        if ( isIncrease ) {
                           b = inventoryCache.setAndIncrease(params);
                        } else {
                           b = inventoryCache.setAndSubtract(params);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
//            });
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
