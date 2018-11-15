package com.gs.mall.order.po;

import com.gs.mall.common.po.MyObject;

/**
 * Created by huyoucheng on 2018/11/12.
 */
public class WorkOrderFlowRecDescription extends MyObject {

    private int status;//状态 0未处理 1通过 2拒绝
    private String description;//描述
    private int displayPlatform;//显示平台
    private String workOrderOperation;//操作名
    private String reason;//原因

    public WorkOrderFlowRecDescription() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisplayPlatform() {
        return displayPlatform;
    }

    public void setDisplayPlatform(int displayPlatform) {
        this.displayPlatform = displayPlatform;
    }

    public String getWorkOrderOperation() {
        return workOrderOperation;
    }

    public void setWorkOrderOperation(String workOrderOperation) {
        this.workOrderOperation = workOrderOperation;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public WorkOrderFlowRecDescription(int status, String description, int displayPlatform, String workOrderOperation, String reason) {
        this.status = status;
        this.description = description;
        this.displayPlatform = displayPlatform;
        this.workOrderOperation = workOrderOperation;
        this.reason = reason;
    }

    //给前端看的状态
    public static WorkOrderFlowRecDescription instanceForApp(WorkOrderFlowRec wofr){
        String operation = wofr.getOperation();
        int status = wofr.getStatus();
        String desc = "";
        if (operation.equalsIgnoreCase(WorkOrderFlow.View)){
            switch (status){
                case 0:desc = "您的售后申请已提交";break;
                case 1:desc = "客服已经受理了您的申请";break;
                case 2:desc = "您的售后申请未通过";break;
            }
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.NoticeBackMail)){
            switch (status){
                case 0:desc = "客服正在处理快递寄回信息";break;
                case 1:desc = "请寄回，快递信息："+wofr.getLogistics();break;
                case 2: desc = "";break;
            }
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.BackMail)){
            switch (status){
                case 0:desc = "等待您退回商品";break;
                case 1:desc = "您已经提交退回商品快递单号，快递信息："+wofr.getLogistics();break;
                case 2: desc = "";break;
            }
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.ReceivedMail)){

        }else if (operation.equalsIgnoreCase(WorkOrderFlow.FinanceView)){
            switch (status){
                case 1:desc = "已完成退款";break;
            }
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.Finish)){
            switch (status){
                case 1:desc = "售后已完成";break;
            }
        }
        return new WorkOrderFlowRecDescription(status,desc,0,operation,wofr.getReason());
    }

    //给管理平台看的状态
    public static WorkOrderFlowRecDescription instanceForManager(WorkOrderFlowRec wofr){
        String operation = wofr.getOperation();
        int status = wofr.getStatus();
        return instanceForManager(operation,status,wofr.getLogistics(),wofr.getReason());
    }


    //给管理平台看的状态
    public static WorkOrderFlowRecDescription instanceForManager(String operation,int status,String logistics,String reason){
        String desc = "";
        if (operation.equalsIgnoreCase(WorkOrderFlow.View)){
            switch (status){
                case 0:desc = "待审核";break;
                case 1:desc = "已受理";break;
                case 2:desc = "驳回";break;
            }
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.NoticeBackMail)){
            switch (status){
                case 0:desc = "待填写寄回的物流信息";break;
                case 1:desc = "寄回的物流信息："+logistics;break;
                case 2: desc = "";break;
            }
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.BackMail)){
            switch (status){
                case 0:desc = "需用户寄回";break;
                case 1:desc = "通知用户寄回";break;
                case 2:desc = "驳回";break;
            }
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.ReceivedMail)){
            switch (status){
                case 0:desc = "监控中";break;
                case 1:desc = "收到用户包裹";break;
                case 2:desc = "驳回";break;
            }
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.FinanceView)){
            switch (status){
                case 0:desc = "转交财务处理";break;
                case 1:desc = "财务已处理";break;
                case 2:desc = "被财务退回";break;
            }
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.Finish)){
            desc = "工单处理完成";
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.Close)){
            desc = "关闭工单";
        }
        return new WorkOrderFlowRecDescription(status,desc,1,operation,reason);
    }


    //给商城看的状态
    public static WorkOrderFlowRecDescription instanceForMall(WorkOrderFlowRec wofr){
        String operation = wofr.getOperation();
        int status = wofr.getStatus();
        String desc = "";
        if (operation.equalsIgnoreCase(WorkOrderFlow.View)){
            switch (status){
                case 0:desc = "待审核";break;
                case 1:desc = "已受理";break;
                case 2:desc = "驳回";break;
            }
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.NoticeBackMail)){
            desc = "等待退回";
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.BackMail)){
            switch (status){
                case 0:desc = "等待退回";break;
                case 1:desc = "退回中";break;
                case 2:desc = "不退了";break;
            }
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.ReceivedMail)){
            desc = "退款中";
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.FinanceView)){
            switch (status){
                case 0:case 2:desc = "退款中";break;
                case 1:desc = "已完成";break;
            }
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.Finish)){
            desc = "已完成";
        }else if (operation.equalsIgnoreCase(WorkOrderFlow.Close)){
            desc = "已关闭";
        }
        return new WorkOrderFlowRecDescription(status,desc,1,operation,wofr.getReason());
    }


}
