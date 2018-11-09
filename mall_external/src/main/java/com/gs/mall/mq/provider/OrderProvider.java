package com.gs.mall.mq.provider;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.mq.MQException;
import com.gs.common.mq.MQManager;
import com.gs.common.mq.data.MQMessage;
import com.gs.common.mq.data.MQTypeEnum;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.order.vo.OrderFinanceDetailVo;

/**
 * 订单MQ消息生产者
 * Created by huangyp on 2018/1/11.
 */
@Component
public class OrderProvider {

    private Logger log = LoggerFactory.getLogger(OrderProvider.class);

    /**
     * 订单支付超时时间（分）
     */
    @Value("${mall.pay.timeout.minute}")
    private Integer timeoutExpireMinute;

    /**
     * 订单发货超时时间（天）
     */
    @Value("${mall.deliver.timeout.day}")
    private Integer deliverExpireDay;

    /**
     * 订单发货超时时间（毫秒）
     */
    private long deliverExpireMills = 0;

    /**
     * 订单关闭生产者
     * @param orderId
     * @param createTime
     */
    public void closeOrderProvider( String  orderId , Date  createTime ){
        MQMessage msg = new MQMessage();
        msg.setKey(orderId);
        msg.setTopic(Constant.TOPIC_MALL_ORDER_CLOSE);
        JSONObject json = new JSONObject();
        json.put("orderId", orderId);
        json.put("createTime", createTime.getTime());
        msg.setContent(json.toJSONString());
        long mills = timeoutExpireMinute * 60 * 1000;
//        long mills = 120 * 1000;
        long lessMills = System.currentTimeMillis() - createTime.getTime();
        sendMQMsg(msg, (mills - lessMills));
    }


    /**
     * 订单完成生产者
     * @param orderId
     * @param deliverTime
     */
    public void finishOrderProvider( String orderId, Date deliverTime ){
        deliverTime = deliverTime == null ? new Date() : deliverTime;
        MQMessage msg = new MQMessage();
        msg.setKey(orderId);
        msg.setTopic(Constant.TOPIC_MALL_ORDER_FINISH);
        JSONObject json = new JSONObject();
        json.put("orderId", orderId);
        json.put("deliverTime", deliverTime.getTime());
        msg.setContent(json.toJSONString());
        long mills = getFinishMills();
//        long mills = 120 * 1000;
        long lessMills = System.currentTimeMillis() - deliverTime.getTime();
        sendMQMsg(msg, (mills - lessMills));
    }


    public long getFinishMills() {
        if( deliverExpireMills == 0 ) {
            deliverExpireMills = deliverExpireDay * 24 * 60 * 60 * 1000;
        }
        return deliverExpireMills;
    }
    
    /**
     * 每日消费统计
     * @param purMoney
     * @param countMoney
     */
    public Boolean countMoneyOrderProvider( OrderFinanceDetailVo vo ){
    	try {
	    	MQManager manager = MQManager.getManager();
	    	MQMessage message = new MQMessage();
	    	message.setTopic(Constant.TOPIC_OPERATIONAL_REPORT);  //消费消息
	        message.setSubTopic("default");
	        message.setKey(UUID.randomUUID().toString().replaceAll("-", ""));
		    message.setContent(JSON.toJSONString(vo));
			return manager.sendMessage(message, MQTypeEnum.NSQ);
			
		} catch (MQException e) {
			log.error("发送运营日报消息失败：", e);
		}
    	return false;
    }

    private void sendMQMsg(MQMessage msg, long delayTime) {
        MQManager manager = MQManager.getManager();
        for( int i = 1 ; i <= 3 ; ++ i ) {
            try {
                Boolean b = manager.sendMessage(msg, MQTypeEnum.NSQ, delayTime);
                if ( b ) {
                    log.info("finish order provider : {} ; {}", msg.getContent(), "success");
                    break;
                }
            }catch ( Exception e ) {
                log.error("发送订单完成延迟消息失败：", e);
            }
            try {
                if( i < 3 )
                    Thread.sleep(i * 1000L);
            } catch (Exception e) {
                log.error("finish order provider sleep error : ", e);
            }
        }
    }

}
