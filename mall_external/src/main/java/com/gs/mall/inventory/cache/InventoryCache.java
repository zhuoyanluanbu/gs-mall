package com.gs.mall.inventory.cache;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.cache.JedisCache;
import com.gs.mall.commodity.dto.InventoryDto;
import com.gs.mall.commodity.po.CommodityDetail;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huangyp on 2017/8/22.
 */
@Component("inventoryCache")
@Scope()
public class InventoryCache {

    //库存操作重试次数
    @Value("${mall.inventory.retry.time}")
    private Integer retryTimes;

    //库存缓存key
    private final String CACHE_IN_REDIS_KEY = "Cache_MallCommodity_Inventory_Map";

    //库存缓存所key
    private final String LOCK_IN_REDIS_KEY = "Cache_MallCommodity_Inventory_Lock";

    //字符标记
    private final String CHAR_FLAG = "#";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    
    private Logger log = LoggerFactory.getLogger(InventoryCache.class);

    public InventoryCache(){
       //setCacheDefault();
    }

  //设置默认缓存
    public void setCacheDefault(){
        /*Jedis jedis = JedisCache.getJedis();
        if( jedis != null ) {
            jedis.hmset(CACHE_IN_REDIS_KEY, new ConcurrentHashMap<String, String>());
        }
        JedisCache.closeResource(jedis);*/
        Map<String,String> defaultMap = new ConcurrentHashMap<String, String>(1);
        defaultMap.put("default","0");
        setCacheValue(defaultMap, null);
    }

    //设置指定值
    private void setCacheValue(Map<String,String> cacheValue, Jedis jds){
        Jedis jedis = (jds == null) ? JedisCache.getJedis() : jds;
        try {
            if (jedis != null) {
                jedis.hmset(CACHE_IN_REDIS_KEY, cacheValue);
            }
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            if( jds == null )
                JedisCache.closeResource(jedis);
        }
    }

    /**
     * 获得Redis锁
     * @return
     */
    private boolean getRedisLock(Map<String,Integer> maps){
       Jedis jedis = JedisCache.getJedis();
       if( jedis == null ) {
           return false;
       }
       try {
    	   	boolean bool = false;
    	   	String[] key = maps.keySet().toArray(new String[maps.size()]);
            for( int i = 1 ; i <= retryTimes; ++ i ) {
            	
            	for (int j = 0; j < key.length; j++) {
            		long l = jedis.setnx(LOCK_IN_REDIS_KEY+key[j], "1");
            		if( 1 != l ) {
            			//jedis.expire(LOCK_IN_REDIS_KEY, 10);
            			return false;
            		}else {
            			jedis.expire(LOCK_IN_REDIS_KEY+key[j], 10);
            			bool = true;
            		}
				}
            	if (bool) {
					return bool;
				}
            	
            	try {
            		Thread.sleep(500);
            	} catch (InterruptedException e) {
            		e.printStackTrace();
            	}
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
    private boolean releaseRedisLock(Map<String,Integer> maps){
        Jedis jedis = JedisCache.getJedis();
        if( jedis == null ) {
            return false;
        }
        String[] key = maps.keySet().toArray(new String[maps.size()]);
        try {
        	for (int i = 0; i < key.length; i++) {
        		Long del = jedis.del(LOCK_IN_REDIS_KEY+key[i]);
//        		System.out.println(del);
			}
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            JedisCache.closeResource(jedis);
        }
        return true;
    }

    //获得库存
    public List<Integer> getValue(Jedis jedis,String ...key) {
        List<String> values =  jedis.hmget(CACHE_IN_REDIS_KEY, key);
        final List<Integer> newVal = new ArrayList<Integer>(values.size());
        if( values != null && ! values.isEmpty() ) {
            values.forEach(val -> newVal.add((val==null)?null:Integer.valueOf(val)));
        }
        for( int i = 0, size = newVal.size(); i < size ; ++ i ) {
            String k = key[i];
            Integer v = newVal.get(i);
            if ( v == null ) { //表示从缓存未获得，从数据库中在获取一次
                String[] ka = k.split(CHAR_FLAG);
                v = getFormDB(Long.valueOf(ka[0]),ka[1]);
                newVal.remove(i);
                newVal.add(i, v);
            }
        }
        return newVal;
    }
    
    //获得库存
    public List<Integer> getValue(Object ...key) {
    	Jedis jedis = JedisCache.getJedis();
    	try {
	        Map<String, String> inventoryMap = jedis.hgetAll(CACHE_IN_REDIS_KEY);
	        final List<Integer> newVal = new ArrayList<Integer>();
	        
	        if( inventoryMap != null) {
	        	for( int i = 0, size = key.length; i < size ; ++ i ) {
	        		for (Map.Entry<String, String> m : inventoryMap.entrySet()) {
	        			if (m.getKey().equals(key[i])) {
	        				newVal.add(m.getValue()==null?null:Integer.valueOf(m.getValue()));
						}
	        		}
				}
	        }
	        for( int i = 0, size = key.length; i < size ; ++ i ) {
	            String k = (String)key[i];
	            Integer v = null;
	            try {
	            	v = newVal.get(i);
	            	//缓存取出数据不一致可能导致索引越界
				} catch (Exception e) {
					log.error("购物车列表获得库存缓存异常{}",e);
				}
	            if ( v == null ) { //表示从缓存未获得，从数据库中在获取一次
	                String[] ka = k.split(CHAR_FLAG);
	                v = getFormDB(Long.valueOf(ka[0]),ka[1]);
	                newVal.remove(i);
	                newVal.add(i, v);
	            }
	        }
	        return newVal;
    	}finally {
            JedisCache.closeResource(jedis);
        }    
    }

    /**
     * 根据指定key获得对应库存列表
     * @param key
     * @return
     */
    public List<Integer> getValuesByKey(String ...key) {
        if( key == null || key.length == 0 ) {
            return null;
        }
        Jedis j = JedisCache.getJedis();
        try {
            return getValue(j,key);
        }finally {
            JedisCache.closeResource(j);
        }
    }

    /**
     * 设置和递增
     * @param maps
     * @return
     */
    public Boolean setAndIncrease(Map<String,Integer> maps){
        if( maps == null || maps.isEmpty() ) {
            return Boolean.FALSE;
        }
        //阻塞获取锁
        if( ! getRedisLock(maps) ) {
             return Boolean.FALSE;
        }
        Jedis j = JedisCache.getJedis();
        try {
            String[] key = maps.keySet().toArray(new String[maps.size()]);
            List<Integer> values = getValue(j, key);
            Map<String,String> newMaps = new HashMap<String,String>(key.length);
            for( int i = 0, size = values.size(); i < size ; ++ i ) {
                String k = key[i];
                Integer v = values.get(i);
                if ( v == null ) { //表示从缓存未获得，从数据库中在获取一次
                    String[] ka = k.split(CHAR_FLAG);
                    v = getFormDB(Long.valueOf(ka[0]),ka[1]);
                }
                v = ( v==null ? maps.get(k) : v+maps.get(k) );
                newMaps.put(k, String.valueOf(v));
            }
            setCacheValue(newMaps,j);
            return Boolean.TRUE;
        } finally {
            JedisCache.closeResource(j);
            releaseRedisLock(maps);
        }
    }

    /**
     * 设置值
     * @param maps
     * @return
     */
    public Boolean setValues(Map<String,Integer> maps){
        if( maps == null || maps.isEmpty() ) {
            return Boolean.FALSE;
        }
        //阻塞获取锁
        if( ! getRedisLock(maps) ) {
            return Boolean.FALSE;
        }
        Jedis j = JedisCache.getJedis();
        try {
            Set<Map.Entry<String,Integer>> sets = maps.entrySet();
            Map<String,String> newMaps = new HashMap<String,String>(maps.size());
            for( Map.Entry<String,Integer> entry : sets ) {
                newMaps.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            setCacheValue(newMaps,j);
            return Boolean.TRUE;
        } finally {
            JedisCache.closeResource(j);
            releaseRedisLock(maps);
        }
    }

    /**
     * 设置和递减
     * @param maps
     * @return
     */
    public Boolean setAndSubtract(Map<String,Integer> maps){
        if( maps == null || maps.isEmpty() ) {
            return Boolean.FALSE;
        }
        //阻塞获取锁
        if( ! getRedisLock(maps) ) {
            return Boolean.FALSE;
        }
        Jedis j = JedisCache.getJedis();
        try {
            String[] key = maps.keySet().toArray(new String[maps.size()]);
            List<Integer> values = getValue(j, key);
            Map<String,String> newMaps = new HashMap<String,String>(key.length);
            for( int i = 0, size = values.size(); i < size ; ++ i ) {
                String k = key[i];
                Integer v = values.get(i);
                if ( v == null ) { //表示从缓存未获得，从数据库中在获取一次
                    String[] ka = k.split(CHAR_FLAG);
                    v = getFormDB(Long.valueOf(ka[0]),ka[1]);
                }
                //库存不足
                if( v == null || (v - maps.get(k)) < 0 ) {
                    return Boolean.FALSE;
                }
                newMaps.put(k, String.valueOf( ( v - maps.get(k) )));
            }
            setCacheValue(newMaps,j);
            return Boolean.TRUE;
        } finally {
            JedisCache.closeResource(j);
            releaseRedisLock(maps);
        }
    }

    //获得key
    public String getKey(Long commodityId, String skuId){
        return commodityId+CHAR_FLAG+skuId;
    }


    private Integer getFormDB(Long commodityId, String skuId){
        Map<String,Object> param = new HashMap<String,Object>(2);
        param.put("commodityId", commodityId);
        param.put("skuId", skuId);
        CommodityDetail detail = sqlSessionTemplate.selectOne("com.gs.mall.commodity.dao.CommodityDetailDao.queryByParam", param);
        return detail == null ? null : detail.getInventory();
    }

    public Boolean flushCacheToDB(JSONArray arry) {
        List<InventoryDto> list = new ArrayList<InventoryDto>(arry.size());
        String[] key = new String[arry.size()];
        for ( int i = 0 ; i < arry.size() ; ++ i ) {
            JSONObject json = arry.getJSONObject(i);
            Long commodityId = json.getLongValue("id");
            String skuId = json.getString("skuId");
            key[i] = getKey(commodityId,skuId);
        }
        List<Integer> values = getValuesByKey(key);
        for( int i = 0; i < values.size() ; ++ i ) {
            if( values.get(i) == null ) {
                continue;
            }
            InventoryDto dto = new InventoryDto();
            String[] keys = key[i].split(CHAR_FLAG);
            //param.put("commodityId", Long.parseLong(keys[0]));
            dto.setCommodityId(Long.parseLong(keys[0]));
            //param.put("skuId", keys[1]);
            dto.setSkuId(keys[1]);
            //param.put("inventory", values.get(i)==null?0:values.get(i));
            dto.setInventory(values.get(i)==null?0:values.get(i));
            //param.put("timestamp", System.currentTimeMillis());
            dto.setTimestamp(System.currentTimeMillis());
            list.add(dto);
        }
        /*MQMessage msg = new MQMessage();
        msg.setContent(JSONObject.toJSONString(list));
        msg.setTopic(Constant.JF_MALL_INVENTORY_UPDATE_TOPIC);
        msg.setKey(IdentityUtil.getIdentity());
        try {
            Boolean b = MQManager.getManager().sendMessage(msg, MQTypeEnum.NSQ);
            return b;
        } catch ( Exception  e ){
            e.printStackTrace();
        }*/
        try {
            int update = sqlSessionTemplate.update("com.gs.mall.commodity.dao.CommodityDetailDao.batchUpdateInventory", list);
            return update > 0;
        }catch(Exception e){
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

}
