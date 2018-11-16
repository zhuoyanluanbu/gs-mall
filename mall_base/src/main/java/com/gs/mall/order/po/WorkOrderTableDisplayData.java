package com.gs.mall.order.po;

import com.gs.mall.common.po.MyObject;

import java.util.Date;

/**
 * Created by huyoucheng on 2018/11/12.
 * 后台显示的详情
 */
public class WorkOrderTableDisplayData extends WorkOrderFlowRec{

    private String order_id;//订单号
    private int num;
    private int refundAmount;//退款金额（分）
    private int refundPoints;//退款积分
    private String owner;//所有者


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public int getRefundPoints() {
        return refundPoints;
    }

    public void setRefundPoints(int refundPoints) {
        this.refundPoints = refundPoints;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public WorkOrderTableDisplayData(){}

    public WorkOrderTableDisplayData(String order_id, int num, int refundAmount, int refundPoints, String owner) {
        this.order_id = order_id;
        this.num = num;
        this.refundAmount = refundAmount;
        this.refundPoints = refundPoints;
        this.owner = owner;
    }

    public WorkOrderTableDisplayData(int id, String wo_id, String operator_id, String operator, String operation, Date operation_time, int operation_from, String logistics, String pre_operation, String next_operation, int status, String reason, String remark, Date create_time, String order_id, int num, int refundAmount, int refundPoints, String owner) {
        super(id, wo_id, operator_id, operator, operation, operation_time, operation_from, logistics, pre_operation, next_operation, status, reason, remark, create_time);
        this.order_id = order_id;
        this.num = num;
        this.refundAmount = refundAmount;
        this.refundPoints = refundPoints;
        this.owner = owner;
    }

    public WorkOrderTableDisplayData(int id, String wo_id, String operator_id, String operator, String operation, Date operation_time, int operation_from, String logistics, String pre_operation, String next_operation, int status, String reason, String remark, String desc, Date create_time, String from_operator, String from_operator_id, String order_id, int num, int refundAmount, int refundPoints, String owner) {
        super(id, wo_id, operator_id, operator, operation, operation_time, operation_from, logistics, pre_operation, next_operation, status, reason, remark, desc, create_time, from_operator, from_operator_id);
        this.order_id = order_id;
        this.num = num;
        this.refundAmount = refundAmount;
        this.refundPoints = refundPoints;
        this.owner = owner;
    }
}
