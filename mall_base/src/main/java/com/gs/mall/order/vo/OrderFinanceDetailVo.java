package com.gs.mall.order.vo;

public class OrderFinanceDetailVo {
	private String reportDay;//统计日期,固定8位字符串，yyyyMMdd
	private String reportTime;//统计截止时间，yyyy-MM-dd HH:mm:ss(假如同一天有多条消息，财务系统根据此字段判断，每天的数据只记录最新的一条)
	private String fromName="消费合作社";//数据来源系统的名称，如：消费合作社、严选
	private String fromType;// 数据来源分类，1：在线商城；2：备付金门店；3：后结算门店；4：第三方介入频道
	private String merchantType;//1-备付金，2后结算
	private Long consumeMoney;//当日消费统计，单位分订单金额
	private Long purMoney;//采购成本，单位分
	private String note;//备注
	public String getReportDay() {
		return reportDay;
	}
	public void setReportDay(String reportDay) {
		this.reportDay = reportDay;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getFromType() {
		return fromType;
	}
	public void setFromType(String fromType) {
		this.fromType = fromType;
	}
	public String getMerchantType() {
		return merchantType;
	}
	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}
	public Long getConsumeMoney() {
		return consumeMoney;
	}
	public void setConsumeMoney(Long consumeMoney) {
		this.consumeMoney = consumeMoney;
	}
	public Long getPurMoney() {
		return purMoney;
	}
	public void setPurMoney(Long purMoney) {
		this.purMoney = purMoney;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
