package com.gs.mall.common.service;


import com.gs.common.result.ResponsePageResult;

import java.util.Map;

/**
 * Created by huangyp on 2017/9/6.
 */
public interface BaseService<T,C> {

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    T getById(C id);

    /**
     * 分页查询对象列表
     * @param params
     * @param pageNo
     * @param pageSize
     * @return
     */
    ResponsePageResult getListByPage(Map<String, Object> params, Integer pageNo, Integer pageSize);

    /**
     * 通过id删除
     * @param id
     * @return
     */
    Boolean removeById(C id);
	
	/**
     * 保存或更新
     * @param t
     * @return
     */	
	Boolean saveOrUpdate(T t);
}
