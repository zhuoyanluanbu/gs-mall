package com.gs.mall.common.dao;

import com.gs.mall.common.po.HotWordGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * HotWordGroup dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:18
 */
@Mapper
public interface HotWordGroupDao extends BaseDao<HotWordGroup,java.lang.Integer> {


    List<HotWordGroup> all(Integer status);

    Integer maxSortNum();

}
