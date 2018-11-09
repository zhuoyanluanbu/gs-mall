package com.gs.mall.partner.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.partner.po.Merchant;
import org.apache.ibatis.annotations.Mapper;

/**
 * Merchant dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:21
 */
@Mapper
public interface MerchantDao extends BaseDao<Merchant,java.lang.Long> {


}
