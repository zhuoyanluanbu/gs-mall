package com.gs.mall.finance.service.impl;

import java.sql.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.gs.common.cache.JedisCache;
import com.gs.common.result.ResponseResult;
import com.gs.common.util.DateUtil;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.finance.dao.NetflowTimesDao;
import com.gs.mall.finance.dao.TradeAmountDao;
import com.gs.mall.finance.service.FinancePanelService;

/**
 * 财务面板数据 Created by huangyp on 2017/8/29.
 */
@Service("financePanelService")
public class FinancePanelServiceImpl implements FinancePanelService {

	@Resource
	private NetflowTimesDao netflowTimesDao;

	@Resource
	private TradeAmountDao tradeAmountDao;

	@Override
	public ResponseResult getDataByMerchantId(Long merchantId) {
		// 从缓存读取
		JSONObject json = (JSONObject) JedisCache.getRedisObject(Constant.FINANCE_PANEL_CACHE_KEY);
		if (json == null || true) {
			json = new JSONObject();
			java.util.Date d = DateUtil.getYesterday();
			Date yesterday = new Date(d.getTime());
			Date sevenday = new Date(DateUtil.getDateByOffsetDay(d, -7).getTime());
			// 查询昨日记录
			Integer times = netflowTimesDao.queryTimesByDate(yesterday);
			json.put("yesterdayTimes", (times == null) ? 0 : times);
			// 查询7日内记录
			Integer stimes = netflowTimesDao.queryTimesByScope(sevenday, yesterday);
			json.put("sevenDayTimes", (stimes == null) ? 0 : stimes);

			// 查询金额记录
			Integer amount = tradeAmountDao.queryAmountByDate(merchantId, yesterday);
			json.put("yesterdayAmount", (amount == null) ? 0 : amount);
			// 查询7日总金额
			Integer samount = tradeAmountDao.queryAmountByScope(merchantId, sevenday, yesterday);
			json.put("sevenDayAmount", (samount == null) ? 0 : samount);
			// 查询总金额
			Integer totalAmount = tradeAmountDao.queryAmountByScope(merchantId, null, null);
			json.put("totalAmount", (totalAmount == null) ? 0 : totalAmount);

			long l = DateUtil.getToNightZeroMills() - System.currentTimeMillis();
			if (l > 0) {
				l = l / 1000 + 600;
			}
			JedisCache.setRedisObjectExpired(Constant.FINANCE_PANEL_CACHE_KEY, json, (int) l);
		}
		return ResponseResult.successInstance().setData(json);
	}
}
