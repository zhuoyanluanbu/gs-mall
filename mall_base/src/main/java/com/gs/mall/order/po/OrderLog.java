package com.gs.mall.order.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:13
 */
public class OrderLog implements java.io.Serializable{

    /** 日志ID */
    private java.lang.Long logId;

    /** 订单号 */
    private java.lang.String orderId;

    /** 订单状态 */
    private java.lang.Integer orderStatus;

    /** 处理内容 */
    private java.lang.String content;

    /** 操作人ID */
    private java.lang.String operatorId;

    /** 操作人 */
    private java.lang.String operator;

    /** 操作人类型：1 用户 2  商户 3 其他 */
    private Integer operatorType;

    /** 操作时间 */
    private java.util.Date operateTime;

    public OrderLog(){
		
    }

    public void setLogId(java.lang.Long logId) {
        this.logId = logId;
    }

    public java.lang.Long getLogId() {
        return this.logId;
    }
    public void setOrderId(java.lang.String orderId) {
        this.orderId = orderId;
    }

    public java.lang.String getOrderId() {
        return this.orderId;
    }
    public void setOrderStatus(java.lang.Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public java.lang.Integer getOrderStatus() {
        return this.orderStatus;
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
}