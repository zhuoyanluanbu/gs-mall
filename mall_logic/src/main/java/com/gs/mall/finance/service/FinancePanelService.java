package com.gs.mall.finance.service;

import com.gs.common.result.ResponseResult;

/**
 * 财务面板信息
 * Created by huangyp on 2017/8/29.
 */
public interface FinancePanelService {

    /**
     * 获得商家财务数据
     * @param merchantId
     * @return
     */
    ResponseResult getDataByMerchantId(Long merchantId);

}
