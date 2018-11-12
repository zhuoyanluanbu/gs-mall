package com.gs.mall.order.po;

import com.gs.mall.common.po.MyObject;

import java.util.Comparator;
import java.util.Map;

/**
 * @author:huyoucheng
 * @version:2.0
 * @since:2.0
 * @createTime:2018/10/31 10:51:13
 */
/*
* 工单流程
* */
public class WorkOrderFlow extends MyObject implements Comparator<WorkOrderFlow>{

    public static final String View = "View";//审核
    public static final String BackMail = "BackMail";//寄回快递
    public static final String ReceivedMail = "ReceivedMail";//收到快递
    public static final String FinanceView = "FinanceView";//财务审核
    public static final String Finish = "Finish";//完成
    public static final String Close = "Close";//关闭工单

    private int id = -1;
    private String operation;//操作名，常量定死
    private String operation_id;//操作id，保留字段
    private int sort;//排序
    private String own;//所属部门
    private String remark;//备注
    private int isDel = 0;

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(String operation_id) {
        this.operation_id = operation_id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getOwn() {
        return own;
    }

    public void setOwn(String own) {
        this.own = own;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public WorkOrderFlow() {
    }

    public WorkOrderFlow(int id, String operation, String operation_id, int sort, String own, String remark) {
        this.id = id;
        this.operation = operation;
        this.operation_id = operation_id;
        this.sort = sort;
        this.own = own;
        this.remark = remark;
    }

    @Override
    public int compare(WorkOrderFlow o1, WorkOrderFlow o2) {
        return o2.getSort() - o1.getSort();
    }


    public static class WorkOrderFlowOwner{
        public static final String User = "USER";
        public static final String CustomService = "KF";
        public static final String Finance = "CW";
    }



}
