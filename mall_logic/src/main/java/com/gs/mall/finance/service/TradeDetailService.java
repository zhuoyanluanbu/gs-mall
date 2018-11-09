package com.gs.mall.finance.service;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.service.BaseService;
import com.gs.mall.finance.po.TradeDetail;

/**
 * TradeDetail service interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:20
 */
public interface TradeDetailService extends BaseService<TradeDetail,java.lang.String>{



    ResponseResult getByIdNo(String formId, String tradeNo);
}
