package com.gs.mall.finance.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.finance.po.TransferRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * TransferRecord dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:20
 */
@Mapper
public interface TransferRecordDao extends BaseDao<TransferRecord,java.lang.Long> {


}
