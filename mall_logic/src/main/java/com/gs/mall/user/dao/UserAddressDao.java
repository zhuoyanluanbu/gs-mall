package com.gs.mall.user.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.user.po.UserAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserAddress dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:22
 */
@Mapper
public interface UserAddressDao extends BaseDao<UserAddress,java.lang.Long> {


}
