package com.gs.mall.consumer.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.mq.consumer.adapter.NSQConsumerAdapter;
import com.gs.common.mq.data.MQMessage;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.order.service.OrderService;
import com.gs.mall.user.po.User;
import org.apache.solr.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单关闭消费
 * Created by huangyp on 2018/1/11
 */
@Component
public class OrderCloseConsumer extends NSQConsumerAdapter {

    @Autowired
    private OrderService orderService;

    @Override
    public String getTopic() {
        return Constant.TOPIC_MALL_ORDER_CLOSE;
    }

    @Override
    public Boolean consumer(MQMessage mqMessage) {
        if( StringUtils.isEmpty(mqMessage.getContent()) ) {
            return Boolean.TRUE;
        }
        JSONObject jsonObject = JSON.parseObject(mqMessage.getContent());
        try {
            User u = User.getSystemUser();
            orderService.updateClose(jsonObject.getString("orderId"), u);
            log.info("关闭订单 :"+jsonObject);
            return Boolean.TRUE;
        } catch ( Exception e ) {
            log.error("完成订单失败：", e);
        }
        return Boolean.FALSE;
    }
}
