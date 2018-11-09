package com.gs.mall.config;

import com.gs.mall.inventory.cache.InventoryCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @description 监听系统事件ContextRefreshedEvent
 * @author huangyp
 * @date n 2017/9/27.
 */
@Component
public class SystemEvent implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private InventoryCache inventoryCache;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if( event.getApplicationContext().getParent() == null ){
            inventoryCache.setCacheDefault();
        }
    }


}
