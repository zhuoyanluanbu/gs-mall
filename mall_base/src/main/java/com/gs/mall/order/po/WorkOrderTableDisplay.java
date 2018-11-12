package com.gs.mall.order.po;

import com.gs.mall.common.po.MyObject;

/**
 * Created by huyoucheng on 2018/11/12.
 * 后台显示的详情
 */
public class WorkOrderTableDisplay extends MyObject{

    private String order_id;//订单号
    private String wo_id;//工单号
    private int status;//状态
    private String timeFormat;//时间
    private int refundAmount;//退款金额（分）
    private int refundPoints;//退款积分
    private String refundApplicationInstruction;//退款申请说明
    private String fromApplicationPerson;//申请发起人
    private String fromApplicationPersonId;//申请发起人
    private String operatedPerson;//退款操作人
    private String operatedPersonId;//退款操作人Id
    private String owner;//所有者

    public String getFromApplicationPersonId() {
        return fromApplicationPersonId;
    }

    public void setFromApplicationPersonId(String fromApplicationPersonId) {
        this.fromApplicationPersonId = fromApplicationPersonId;
    }

    public String getOperatedPersonId() {
        return operatedPersonId;
    }

    public void setOperatedPersonId(String operatedPersonId) {
        this.operatedPersonId = operatedPersonId;
    }

    public String getWo_id() {
        return wo_id;
    }

    public void setWo_id(String wo_id) {
        this.wo_id = wo_id;
    }

    public int getRefundPoints() {
        return refundPoints;
    }

    public void setRefundPoints(int refundPoints) {
        this.refundPoints = refundPoints;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundApplicationInstruction() {
        return refundApplicationInstruction;
    }

    public void setRefundApplicationInstruction(String refundApplicationInstruction) {
        this.refundApplicationInstruction = refundApplicationInstruction;
    }

    public String getFromApplicationPerson() {
        return fromApplicationPerson;
    }

    public void setFromApplicationPerson(String fromApplicationPerson) {
        this.fromApplicationPerson = fromApplicationPerson;
    }

    public String getOperatedPerson() {
        return operatedPerson;
    }

    public void setOperatedPerson(String operatedPerson) {
        this.operatedPerson = operatedPerson;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public WorkOrderTableDisplay() {
    }

    public WorkOrderTableDisplay(String order_id, String wo_id, int status, String timeFormat, int refundAmount, int refundPoints, String refundApplicationInstruction, String fromApplicationPerson, String fromApplicationPersonId, String operatedPerson, String operatedPersonId, String owner) {
        this.order_id = order_id;
        this.wo_id = wo_id;
        this.status = status;
        this.timeFormat = timeFormat;
        this.refundAmount = refundAmount;
        this.refundPoints = refundPoints;
        this.refundApplicationInstruction = refundApplicationInstruction;
        this.fromApplicationPerson = fromApplicationPerson;
        this.fromApplicationPersonId = fromApplicationPersonId;
        this.operatedPerson = operatedPerson;
        this.operatedPersonId = operatedPersonId;
        this.owner = owner;
    }
}
