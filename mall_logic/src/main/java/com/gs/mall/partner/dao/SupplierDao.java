package com.gs.mall.partner.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.partner.po.Supplier;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Supplier dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:21
 */
@Mapper
public interface SupplierDao extends BaseDao<Supplier,java.lang.String> {

    /**
     * 逻辑删除供应商
     * @param supplier
     * @return
     */
    int delSupplier(Supplier supplier);

    /**
     * 全部供应商
     * @param param
     * @return
     */
    List<Supplier> all(Map<String,Object> param);

}
