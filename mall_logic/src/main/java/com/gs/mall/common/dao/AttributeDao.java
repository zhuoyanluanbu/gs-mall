package com.gs.mall.common.dao;

import com.gs.mall.common.po.Attribute;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Attribute dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:17
 */
@Mapper
public interface AttributeDao extends BaseDao<Attribute,java.lang.Long> {


    List<Attribute> selectAll(Integer status);
    
    int selectIsRef(Long attributeId);

    List<String> vadateName(List<String> names);
    
    int insertBatch(List<Attribute> list);
    
    int maxSort();

}
