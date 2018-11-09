package com.gs.mall.order.service.impl;

/**
 * Created by huyoucheng on 2018/11/8.
 */
public class WorkOrderV2Exception extends Exception {

    private int code;
    private String msg;

    public WorkOrderV2Exception(Item item) {
        this.code = item.code;
        this.msg = item.msg;
    }

    public WorkOrderV2Exception(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public WorkOrderV2Exception(String message, int code, String msg) {
        super(message);
        this.code = code;
        this.msg = msg;
    }

    public WorkOrderV2Exception(String message, Throwable cause, int code, String msg) {
        super(message, cause);
        this.code = code;
        this.msg = msg;
    }

    public WorkOrderV2Exception(Throwable cause, int code, String msg) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    public WorkOrderV2Exception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code, String msg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



    public enum Item{
        WorkOrderFinished(40001,"当前工单已经完成"),
        OperatorNull(40002,"请填写操作者id或者操作者"),
        LogisticsError(400003,"物流信息错误");

        private int code;
        private String msg;

        Item(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

}
