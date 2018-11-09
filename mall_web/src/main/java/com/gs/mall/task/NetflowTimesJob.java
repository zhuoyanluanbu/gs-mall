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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gs.common.cache.JedisCache;
import com.gs.common.util.DateUtil;
import com.gs.mall.finance.FinanceFactory;
import com.gs.mall.finance.dao.NetflowRecordDao;
import com.gs.mall.finance.dao.NetflowTimesDao;
import com.gs.mall.finance.po.NetflowRecord;
import com.gs.mall.finance.po.NetflowTimes;

import redis.clients.jedis.Jedis;

/**
 * 流量次数统计任务
 * Created by huangyp on 2017/8/29.
 */
@EnableScheduling
@Component("netflowTimesJob")
public class NetflowTimesJob {

    //1天的毫秒数
    private final long dayMills = 86400000L;


    private final String NETFLOW_REDIS_LOCK_Key = "Mall_Cache_NetFlow_Lock";

    @Autowired
    private SqlSessionTemplate sqlSession;
    @Autowired
    private NetflowTimesDao netflowTimesDao;
    @Autowired
    private NetflowRecordDao netflowRecordDao;

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
            long l = jedis.setnx(NETFLOW_REDIS_LOCK_Key, "1");
            if( 1 == l ) {
                jedis.expire(NETFLOW_REDIS_LOCK_Key, 10);
            }
            return ( 1 == l );
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
            jedis.del(NETFLOW_REDIS_LOCK_Key);
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
            if (latestDay == null || latestDay.compareTo(yestorday) == -1) {
                java.util.Date startDate = (latestDay == null) ? null : new java.util.Date(latestDay.getTime() + dayMills);
                java.util.Date endDate = new java.util.Date(yestorday.getTime() + (dayMills - 1));
                Integer items = getNetflowRecordCount(startDate, endDate);
                if (items == null || items == 0) {
                    return;
                }
                int pageSize = 500;
                int pages = (int) Math.ceil(items.doubleValue() / pageSize);

                Map<String, Integer> dateMap = new HashMap<String, Integer>();
                for (int i = 0; i < pages; ++i) {
                    List<NetflowRecord> tses = getNetflowRecord(startDate, endDate, (i * pageSize), pageSize);
                    if (tses == null || tses.isEmpty()) continue;
                    tses.forEach(ts -> {
                        String key = getKey(ts);
                        Integer times = dateMap.containsKey(key) ? dateMap.get(key) : 0;
                        ++times;
                        dateMap.put(key, times);
                    });
                }
                Set<Map.Entry<String, Integer>> entries = dateMap.entrySet();
                List<NetflowTimes> list = new ArrayList<NetflowTimes>(dateMap.size());
                entries.forEach(e -> {
                    list.add(FinanceFactory.getNetflowTimesInstance(e.getValue(), new Date(DateUtil.parseStringToDate(e.getKey()).getTime())));
                });
                batchSaveNetflowTimes(list);
            }
        } catch ( Exception e ){
            e.printStackTrace();
        } finally {
           this.releaseRedisLock();
        }
    }

    private String getKey(NetflowRecord ts){
        StringBuilder sb = new StringBuilder();
        Instant instant = ts.getCreateTime().toInstant();
        ZonedDateTime local = instant.atZone(ZoneId.systemDefault());
        sb.append(local.getYear()).append("-");
        sb.append(local.getMonthValue()<10?"0":"").append(local.getMonthValue()).append("-");
        sb.append(local.getDayOfMonth()<10?"0":"").append(local.getDayOfMonth());
        return sb.toString();
    }

    //获得最新日期
    private Date getLatestDay(){
    	return netflowTimesDao.querylatestDate();
//        return sqlSession.selectOne("com.gs.jfmall.finance.dao.NetflowTimesDao.querylatestDate");
    }

    //查询流量记录数
    private Integer getNetflowRecordCount(java.util.Date startDate, java.util.Date endDate){
        Map<String,Object> params = new HashMap<String,Object>(2);
        params.put("startTime", startDate);
        params.put("endTime", endDate);
        return netflowRecordDao.queryCountByPage(params);
//        return sqlSession.selectOne("com.gs.jfmall.finance.dao.NetflowRecordDao.queryCountByPage",params);
    }

    //查询流量记录
    private List<NetflowRecord> getNetflowRecord(java.util.Date startDate,
                                                 java.util.Date endDate,
                                                 int startIndex, int fetchSize){
        Map<String,Object> params = new HashMap<String,Object>(4);
        params.put("startTime", startDate);
        params.put("endTime", endDate);
        params.put("startIndex", startIndex);
        params.put("fetchSize", fetchSize);
        return netflowRecordDao.queryByPage(params,startIndex,fetchSize);
//        return sqlSession.selectList("com.gs.jfmall.finance.dao.NetflowRecordDao.queryByPage",params);
    }

    private void batchSaveNetflowTimes(List<NetflowTimes> list) {
        if(list.size() > 0 ) {
        	netflowTimesDao.batchInsert(list);
//            sqlSession.insert("com.gs.jfmall.finance.dao.NetflowTimesDao.batchInsert",list);
        }
    }


}
