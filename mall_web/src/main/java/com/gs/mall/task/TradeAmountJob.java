package com.gs.mall.task;

import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gs.common.cache.JedisCache;
import com.gs.common.util.DateUtil;
import com.gs.mall.finance.FinanceFactory;
import com.gs.mall.finance.dao.TradeAmountDao;
import com.gs.mall.finance.dao.TradeStatementDao;
import com.gs.mall.finance.po.TradeAmount;
import com.gs.mall.finance.po.TradeStatement;

import redis.clients.jedis.Jedis;


/**
 * 交易金额统计任务
 * Created by huangyp on 2017/8/29.
 */
@Component("tradeAmountJob")
@Scope
public class TradeAmountJob {


    @Autowired
    private SqlSessionTemplate sqlSession;
    @Autowired
    private TradeAmountDao tradeAmountDao;
    @Autowired
    private TradeStatementDao tradeStatementDao;

    //1天的毫秒数
    private final long dayMills = 86400000L;

    /**
     * 交易金额redis所key
     */
    private final String TRADE_AMOUNT_REDIS_LOCK_Key = "JF_Cache_Trade_Amount_Lock";

    public TradeAmountJob(){

    }

    /**
     * 获得Redis锁
     * @return
     */
    private boolean getRedisLock(){
        Jedis jedis = JedisCache.getJedis();
        if( jedis == null ) {
            return false;
        }
        try {
            long l = jedis.setnx(TRADE_AMOUNT_REDIS_LOCK_Key, "1");
            if( 1 == l ) {
                jedis.expire(TRADE_AMOUNT_REDIS_LOCK_Key, 10);
                 return true;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }finally {
        	JedisCache.closeResource(jedis);
        }
        return false;
    }

    /**
     * 释放Redis锁
     * @return
     */
    private boolean releaseRedisLock(){
        Jedis jedis = JedisCache.getJedis();
        if( jedis == null ) {
            return false;
        }
        try {
            jedis.del(TRADE_AMOUNT_REDIS_LOCK_Key);
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
        	JedisCache.closeResource(jedis);
        }
        return true;
    }

//    @Scheduled(cron="0 0 */1 * * ?")
//    @Scheduled(cron="*/5 * * * * ?")
    public void run(){
        if( ! getRedisLock() ) {
            return;
        }
        try {
            //从数据库获取最新的日期
            Date latestDay = getLatestDay();
            //获得昨天的时间
            Date yestorday = new Date(DateUtil.getYesterday().getTime());
            if (latestDay == null || latestDay.compareTo(yestorday) == -1 ) {
                java.util.Date startDate = (latestDay == null) ? null : new java.util.Date(latestDay.getTime() + dayMills);
                java.util.Date endDate = new java.util.Date(yestorday.getTime() + (dayMills - 1));
//                java.util.Date startDate = DateUtils.string2Date("2018-04-0918:00:00");
//                java.util.Date endDate = DateUtils.getCurrentDate();
                Integer items = getTradeStatementCount(startDate, endDate);
                if (items == null || items == 0) {
                    return;
                }
                int pageSize = 500;
                int pages = (int) Math.ceil(items.doubleValue() / pageSize);

                Map<String, Integer> merchatIdsMap = new HashMap<String, Integer>();
                for (int i = 0; i < pages; ++i) {
                    List<TradeStatement> tses = getTradeStatement(startDate, endDate, (i * pageSize), pageSize);
                    if (tses == null || tses.isEmpty()) continue;
                    tses.forEach(ts -> {
                        String key = getKey(ts);
                        Integer amount = merchatIdsMap.containsKey(key) ? merchatIdsMap.get(key) : 0;
                        amount += ts.getAmount();
                        merchatIdsMap.put(key, amount);
                    });
                }
                Set<Map.Entry<String, Integer>> entries = merchatIdsMap.entrySet();
                List<TradeAmount> list = new ArrayList<TradeAmount>(merchatIdsMap.size());
                entries.forEach(e -> {
                    String[] ars = e.getKey().split("#");
                    list.add(FinanceFactory.getTradeAmountInstance(Long.valueOf(ars[0]), e.getValue(), new Date(DateUtil.parseStringToDate(ars[1]).getTime())));
                });
                batchSaveTradeAmount(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseRedisLock();
        }
    }

    private String getKey(TradeStatement ts){
        StringBuilder sb = new StringBuilder();
        Instant instant = ts.getCreateTime().toInstant();
        ZonedDateTime local = instant.atZone(ZoneId.systemDefault());
        sb.append(ts.getMerchantId()).append("#").append(local.getYear()).append("-");
        sb.append(local.getMonthValue()<10?"0":"").append(local.getMonthValue()).append("-");
        sb.append(local.getDayOfMonth()<10?"0":"").append(local.getDayOfMonth());
        return sb.toString();
    }

    //获得最新日期
    private Date getLatestDay(){
    	return tradeAmountDao.querylatestDate();
//        return sqlSession.selectOne("com.gs.jfmall.finance.dao.TradeAmountDao.querylatestDate");
    }

    //查询账单数量
    private Integer getTradeStatementCount(java.util.Date startDate, java.util.Date endDate){
        Map<String,Object> params = new HashMap<String,Object>(2);
        params.put("startTime", startDate);
        params.put("endTime", endDate);
        return tradeStatementDao.queryCountByPage(params);
//        return sqlSession.selectOne("com.gs.jfmall.finance.dao.TradeStatementDao.queryCountByPage",params);
    }

    //查询账单对象
    private List<TradeStatement> getTradeStatement(java.util.Date startDate, java.util.Date endDate,
                                                      int startIndex, int fetchSize){
        Map<String,Object> params = new HashMap<String,Object>(4);
        params.put("startTime", startDate);
        params.put("endTime", endDate);
        return tradeStatementDao.queryByPage(params, startIndex, fetchSize);
//        return sqlSession.selectList("com.gs.jfmall.finance.dao.TradeStatementDao.queryByPage",params);
    }

    public void batchSaveTradeAmount(List<TradeAmount> list) {
        if(list.size() > 0 ) {
        	tradeAmountDao.batchInsert(list);
//            sqlSession.insert("com.gs.jfmall.finance.dao.TradeAmountDao.batchInsert",list);
        }
    }

}
