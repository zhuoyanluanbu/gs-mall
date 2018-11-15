package com.gs.mall.common.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 基础dao接口
 * @author huangyp
 * @since 2017/9/5.
 * @version 1.0
 */
public interface BaseDao<T,C> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    T selectById(C id);

    /**
     * 分页查询
     * @param params
     * @param startIndex
     * @param fetchSize
     * @return
     */
    List<T> selectByParam(@Param("params") Map<String,Object> params,
                          @Param("startIndex") int startIndex,
                          @Param("fetchSize") int fetchSize);

    /**
     * 根据条件查询参数
     * @param params
     * @return
     */
    int selectCountByParam(@Param("params") Map<String,Object> params);

    /**
     * 添加对象
     * @param t
     * @return
     */
    int insert(T t);

    /**
     * 修改对象
     * @param t
     * @return
     */
    int update(T t);

    /**
     * 通过Id删除
     * @param id
     * @return
     */
    int deleteById(C id);
}
