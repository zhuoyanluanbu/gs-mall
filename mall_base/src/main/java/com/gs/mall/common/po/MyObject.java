package com.gs.mall.common.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huyoucheng on 2018/10/31.
 */
public class MyObject implements Serializable {

    public String toString(){
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

    public static  <T> T toBean(String jsonStr,Class<T> t){
        return JSON.parseObject(jsonStr,new TypeReference<T>(){});
    }

    public JSONObject toJsonObject(){
        return JSON.parseObject(this.toString());
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        JSONObject jsonObject = this.toJsonObject();
        for(Map.Entry<String,Object> entry: jsonObject.entrySet()){
            map.put(entry.getKey(),entry.getValue());
        }
        if (map.size()<1) return null;
        return map;
    }

}
