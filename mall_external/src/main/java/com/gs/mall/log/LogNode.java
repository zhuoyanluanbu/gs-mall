package com.gs.mall.log;

/**
 * 日志节点 
 * @author liuy
 *
 */
public class LogNode {
	
	private String moduleNo;//模块号
	
	private String invokeSignature; //接口署名(哪个类的哪个方法)
	
	private String batchNo; //批次  一次接口调用(包含多次thrift服务、包含子线程的异步调用)只有一个批次号
	
	private String infNo;//本次接口号
	
	private String previousInfNo;//前一次接口号
	
	private String requestArgs;//请求参数
	
	private String returnValue;//返回信息
	
	private String exceptionMessage;//异常信息
	
	private int isAynTask; //是异步任务 1
	
	private long startTimeMillis = 0; // 开始时间
    private long endTimeMillis = 0; // 结束时间
    public long getCost() {
    	return endTimeMillis - startTimeMillis;
    }

	public String getModuleNo() {
		return moduleNo == null ? "" : moduleNo;
	}

	public void setModuleNo(String moduleNo) {
		this.moduleNo = moduleNo;
	}

	public String getInvokeSignature() {
		return invokeSignature == null ? "" : invokeSignature;
	}

	public void setInvokeSignature(String invokeSignature) {
		this.invokeSignature = invokeSignature;
	}

	public String getBatchNo() {
		return batchNo == null ? "" : batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getInfNo() {
		return infNo == null ? "" : infNo;
	}

	public void setInfNo(String infNo) {
		this.infNo = infNo;
	}

	public String getPreviousInfNo() {
		return previousInfNo == null ? "" : previousInfNo;
	}

	public void setPreviousInfNo(String previousInfNo) {
		this.previousInfNo = previousInfNo;
	}

	public String getRequestArgs() {
		return requestArgs == null ? "" : requestArgs;
	}

	public void setRequestArgs(String requestArgs) {
		this.requestArgs = requestArgs;
	}

	public String getReturnValue() {
		return returnValue == null ? "" : returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public String getExceptionMessage() {
		return exceptionMessage == null ? "" : exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public int getIsAynTask() {
		return isAynTask;
	}

	public void setIsAynTask(int isAynTask) {
		this.isAynTask = isAynTask;
	}

	public long getStartTimeMillis() {
		return startTimeMillis;
	}

	public void setStartTimeMillis(long startTimeMillis) {
		this.startTimeMillis = startTimeMillis;
	}

	public long getEndTimeMillis() {
		return endTimeMillis;
	}

	public void setEndTimeMillis(long endTimeMillis) {
		this.endTimeMillis = endTimeMillis;
	}

	@Override
	public String toString() {
		return "LogNode [moduleNo=" + moduleNo + ", invokeSignature=" + invokeSignature + ", batchNo=" + batchNo
				+ ", infNo=" + infNo + ", previousInfNo=" + previousInfNo + ", requestArgs=" + requestArgs
				+ ", returnValue=" + returnValue + ", exceptionMessage=" + exceptionMessage + ", isAynTask=" + isAynTask
				+ ", startTimeMillis=" + startTimeMillis + ", endTimeMillis=" + endTimeMillis + "]";
	}
}
