package com.gs.mall.order.dto;

import com.gs.mall.common.po.MyObject;

import java.util.Date;

/**
 * Created by huyoucheng on 2018/11/13.
 */
public class WorkOrderTableDisplayDto extends MyObject {

    private int startIndex = 0;
    private int fetchSize = 1;
    private Date startTime;
    private Date endTime;
    private String order_id;
    private String wo_id;
    private String owner;
    private int wo_state;//工单状态

    public WorkOrderTableDisplayDto() {
    }

    public WorkOrderTableDisplayDto(int startIndex, int fetchSize, Date startTime, Date endTime, String order_id, String wo_id, String owner, int wo_state) {
        this.startIndex = startIndex;
        this.fetchSize = fetchSize;
        this.startTime = startTime;
        this.endTime = endTime;
        this.order_id = order_id;
        this.wo_id = wo_id;
        this.owner = owner;
        this.wo_state = wo_state;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public int getWo_state() {
        return wo_state;
    }

    public void setWo_state(int wo_state) {
        this.wo_state = wo_state;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getWo_id() {
        return wo_id;
    }

    public void setWo_id(String wo_id) {
        this.wo_id = wo_id;
    }
}
