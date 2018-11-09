package com.gs.mall.log;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志数据
 *
 * //一次request记录的log
	{
		requestId : "请求ID",
		requestStart : 请求时间,
		requestEnd : 请求结束时间,
		cost : 请求耗时,
		request : "请求body",
		response : "响应body",
		url : "访问URL",
		ip : "127.0.0.1",
		user-agent : "Mozil-2.001",
		OS : "Windows",
		serverInf : [
			{
				infSignature : 接口方法签名,
				args : 方法参数,
				returnV : 方法返回值,
				exception : 异常信息,
				startTime : 请求时间,
				endTime : 响应时间,
				cost : 耗时
			}
		]
	}
 * @author liuy
 */
public class LogData {
	
	private String requestId; //请求ID
	private long requestStart;//请求时间
	private long requestEnd;//请求结束时间
	private String request;//请求body
	private String response;//响应body
	private String url;
	private String ip;
	private String userAgent;//
	private String method;
	private String exception;
	
	private List<LogNode> serverInf;
	
	public void addNode(LogNode node) {
		if(getServerInf() == null) {
			setServerInf(new ArrayList<>());
		}
		getServerInf().add(node);
	}
	
	public long getCost() {
		return requestEnd - requestStart;
	}
	
	public String getRequestId() {
		return requestId == null ? "" : requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public long getRequestStart() {
		return requestStart;
	}

	public void setRequestStart(long requestStart) {
		this.requestStart = requestStart;
	}

	public long getRequestEnd() {
		return requestEnd;
	}

	public void setRequestEnd(long requestEnd) {
		this.requestEnd = requestEnd;
	}

	public String getRequest() {
		return request == null ? "" : request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response == null ? "" : response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getUrl() {
		return url == null ? "" : url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIp() {
		return ip == null ? "" : ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserAgent() {
		return userAgent == null ? "" : userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public List<LogNode> getServerInf() {
		return serverInf;
	}

	public void setServerInf(List<LogNode> serverInf) {
		this.serverInf = serverInf;
	}

	public String getMethod() {
		return method == null ? "" : method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getException() {
		return exception == null ? "" : exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	
}
