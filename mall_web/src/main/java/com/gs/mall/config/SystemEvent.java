package com.gs.mall.config;

import com.gs.common.mq.MQManager;
import com.gs.common.mq.data.MQTypeEnum;
import com.gs.mall.consumer.order.OrderCloseConsumer;
import com.gs.mall.consumer.order.OrderFinishConsumer;
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
    private OrderCloseConsumer orderCloseConsumer;

    @Autowired
    private OrderFinishConsumer orderFinishConsumer;

    @Autowired
    private InventoryCache inventoryCache;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if( event.getApplicationContext().getParent() == null ){
            //注册消费者
            MQManager messager = MQManager.getManager();
            messager.registConsumer(orderCloseConsumer, MQTypeEnum.NSQ); //用户固守消息发送
            messager.registConsumer(orderFinishConsumer, MQTypeEnum.NSQ); //用户立言消息发送

            inventoryCache.setCacheDefault();
        }
    }


}
