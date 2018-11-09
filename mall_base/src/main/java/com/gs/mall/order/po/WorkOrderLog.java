package com.gs.mall.order.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:13
 */
public class WorkOrderLog implements java.io.Serializable{

    /** 工单日志ID */
    private java.lang.Long logId;

    /** 工单号 */
    private java.lang.String woId;

    /** 工单状态 */
    private java.lang.Integer status;

    /** 内容 */
    private java.lang.String content;

    /** 操作人ID */
    private java.lang.String operatorId;

    /** 操作人名称 */
    private java.lang.String operator;

    /** 操作人类型：1 用户 2  商户 3 其他 */
    private Integer operatorType;

    /** 操作时间 */
    private java.util.Date operateTime;

    /** 物流信息Id */
    private java.lang.Long logisticsId;

    public WorkOrderLog(){
		
    }

    public void setLogId(java.lang.Long logId) {
        this.logId = logId;
    }

    public java.lang.Long getLogId() {
        return this.logId;
    }
    public void setWoId(java.lang.String woId) {
        this.woId = woId;
    }

    public java.lang.String getWoId() {
        return this.woId;
    }
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    public java.lang.Integer getStatus() {
        return this.status;
    }
    public void setContent(java.lang.String content) {
        this.content = content;
    }

    public java.lang.String getContent() {
        return this.content;
    }
    public void setOperatorId(java.lang.String operatorId) {
        this.operatorId = operatorId;
    }

    public java.lang.String getOperatorId() {
        return this.operatorId;
    }
    public void setOperator(java.lang.String operator) {
        this.operator = operator;
    }

    public java.lang.String getOperator() {
        return this.operator;
    }
    public void setOperatorType(Integer operatorType) {
        this.operatorType = operatorType;
    }

    public Integer getOperatorType() {
        return this.operatorType;
    }
    public void setOperateTime(java.util.Date operateTime) {
        this.operateTime = operateTime;
    }

    public java.util.Date getOperateTime() {
        return this.operateTime;
    }
    public void setLogisticsId(java.lang.Long logisticsId) {
        this.logisticsId = logisticsId;
    }

    public java.lang.Long getLogisticsId() {
        return this.logisticsId;
    }
}