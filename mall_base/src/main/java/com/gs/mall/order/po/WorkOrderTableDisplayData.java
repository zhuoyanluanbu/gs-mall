package com.gs.mall.order.po;

import com.gs.mall.common.po.MyObject;

/**
 * Created by huyoucheng on 2018/11/12.
 * 后台显示的详情
 */
public class WorkOrderTableDisplayData extends MyObject{

    private String order_id;//订单号
    private String wo_id;//工单号
    private int status;//状态
    private String statusDescription;//状态
    private String operation;//操作时间
    private String operation_time;//操作时间
    private String create_time;//生成时间
    private int num;
    private int refundAmount;//退款金额（分）
    private int refundPoints;//退款积分
    private String refundApplicationInstruction;//退款申请说明
    private String fromApplicationPerson;//申请发起人
    private String fromApplicationPersonId;//申请发起人
    private String operatedPerson;//退款操作人
    private String operatedPersonId;//退款操作人Id
    private String owner;//所有者

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOperation_time() {
        return operation_time;
    }

    public void setOperation_time(String operation_time) {
        this.operation_time = operation_time;
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

    public WorkOrderTableDisplayData() {
    }

    public WorkOrderTableDisplayData(String order_id, String wo_id, int status, String statusDescription,
                                     String operation, String operation_time, String create_time, int num,
                                     int refundAmount, int refundPoints, String refundApplicationInstruction,
                                     String fromApplicationPerson, String fromApplicationPersonId,
                                     String operatedPerson, String operatedPersonId, String owner) {
        this.order_id = order_id;
        this.wo_id = wo_id;
        this.status = status;
        this.statusDescription = statusDescription;
        this.operation = operation;
        this.operation_time = operation_time;
        this.create_time = create_time;
        this.num = num;
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
