package com.gs.mall.order.po;

import com.gs.mall.common.po.MyObject;

import java.io.File;
import java.util.Date;
import java.util.List;


/**
 * Created by huyoucheng on 2018/11/1.
 */
public class WorkOrderFlowRec extends MyObject {

    public static final int NotDeal = 0;
    public static final int Pass = 1;
    public static final int NotPass = 2;

    private int id;
    private String wo_id;
    private String operator_id;//操作员id 可以使用户openid，客服人员id
    private String operator;//操作者
    private String operation;//操作名
    private Date operation_time;//操作时间
    private int operation_from;//0客户端 1后台
    private String logistics;//物流 如"顺丰速递:9001010101010929"
    private String pre_operation;//上一操作名
    private String next_operation;//下一操作名
    private int status;//0未处理 1同意 2拒绝
    private String reason;//如果未通过，这里是原因；通过也可以编写原因
    private String remark;
    private String desc;//描述
    private Date create_time;
    private String from_operator;
    private String from_operator_id;

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getFrom_operator() {
        return from_operator;
    }

    public void setFrom_operator(String from_operator) {
        this.from_operator = from_operator;
    }

    public String getFrom_operator_id() {
        return from_operator_id;
    }

    public void setFrom_operator_id(String from_operator_id) {
        this.from_operator_id = from_operator_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWo_id() {
        return wo_id;
    }

    public void setWo_id(String wo_id) {
        this.wo_id = wo_id;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getOperation_time() {
        return operation_time;
    }

    public void setOperation_time(Date operation_time) {
        this.operation_time = operation_time;
    }

    public int getOperation_from() {
        return operation_from;
    }

    public void setOperation_from(int operation_from) {
        this.operation_from = operation_from;
    }

    public String getLogistics() {
        return logistics;
    }

    public void setLogistics(String logistics) {
        this.logistics = logistics;
    }

    public String getPre_operation() {
        return pre_operation;
    }

    public void setPre_operation(String pre_operation) {
        this.pre_operation = pre_operation;
    }

    public String getNext_operation() {
        return next_operation;
    }

    public void setNext_operation(String next_operation) {
        this.next_operation = next_operation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public WorkOrderFlowRec() {
    }

    public WorkOrderFlowRec(int id, String wo_id, String operator_id, String operator, String operation, Date operation_time, int operation_from, String logistics, String pre_operation, String next_operation, int status, String reason, String remark) {
        this.id = id;
        this.wo_id = wo_id;
        this.operator_id = operator_id;
        this.operator = operator;
        this.operation = operation;
        this.operation_time = operation_time;
        this.operation_from = operation_from;
        this.logistics = logistics;
        this.pre_operation = pre_operation;
        this.next_operation = next_operation;
        this.status = status;
        this.reason = reason;
        this.remark = remark;
    }

    public WorkOrderFlowRec(int id, String wo_id, String operator_id, String operator, String operation, Date operation_time, int operation_from, String logistics, String pre_operation, String next_operation, int status, String reason, String remark, String desc, Date create_time, String from_operator, String from_operator_id) {
        this.id = id;
        this.wo_id = wo_id;
        this.operator_id = operator_id;
        this.operator = operator;
        this.operation = operation;
        this.operation_time = operation_time;
        this.operation_from = operation_from;
        this.logistics = logistics;
        this.pre_operation = pre_operation;
        this.next_operation = next_operation;
        this.status = status;
        this.reason = reason;
        this.remark = remark;
        this.desc = desc;
        this.create_time = create_time;
        this.from_operator = from_operator;
        this.from_operator_id = from_operator_id;
    }

}
