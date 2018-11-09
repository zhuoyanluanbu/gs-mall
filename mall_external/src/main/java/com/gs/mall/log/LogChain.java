package com.gs.mall.log;

import org.springframework.stereotype.Component;


/**
 * log的链条
 * @author liuy
 *
 */
@Component
public class LogChain {

	private static ThreadLocal<LogData> threadLocal = new ThreadLocal<>();
	
	private String buildNewBatchNo() {
		return UuidUtils.uuid32();
	}
	
	public LogData getLogData() {
		LogData logData = threadLocal.get();
		if(logData == null) {
			//初始化链条
			logData = new LogData();
			logData.setRequestId((buildNewBatchNo()));
			threadLocal.set(logData);
		}
		return logData;
	}
}
