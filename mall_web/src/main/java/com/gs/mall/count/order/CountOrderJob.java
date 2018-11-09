package com.gs.mall.count.order;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.gs.common.util.DateUtil;
import com.gs.mall.commodity.service.CommodityService;
import com.gs.mall.mq.provider.OrderProvider;
import com.gs.mall.order.service.OrderService;
import com.gs.mall.order.vo.OrderFinanceDetailVo;


@Component
@Scope
public class CountOrderJob {
	
	private Logger log = LoggerFactory.getLogger(CountOrderJob.class);
	
    private Lock lock = new ReentrantLock();
    
    @Autowired
    private OrderProvider orderProvider;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommodityService commodityService;
    
//    @Scheduled(cron="*/5 * * * * ?")
//    @Scheduled(cron="58 59 0-23 * * ?")
    public void cronJob(){
    	try {
    		lock.lock();
    		commodityService.refreshInventory();
    		doJob();
		} catch (Exception e) {
			log.error("定时统计销售数据发送MQ异常:{}",e);
		}finally {
			lock.unlock();
		}
    }

    private void doJob() throws Exception{
    	boolean result = false;
    	String startTime = DateUtil.getDaySetHour(new Date(),0);
		String endTime = DateUtil.getDaySetHour(new Date(),24);
		OrderFinanceDetailVo vo = orderService.countConsume(startTime, endTime);
		for (int i = 1; i <= 3; i++) {
			result = orderProvider.countMoneyOrderProvider(vo);
			if (result) {
				log.info("定时统计销售数据发送MQ成功,body:{}",JSON.toJSONString(vo));
				break;
			}else {
				Thread.sleep(i*1000);
			}
		}
    }

}
