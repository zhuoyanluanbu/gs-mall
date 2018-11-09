package com.gs.mall.order.dto;


import com.gs.mall.order.po.WorkOrder;

/**
 * @Filename com.gs.mall.order.dto.WorkOrderSimpleDto
 * @Description
 * @Version 1.0
 * @Author cdaic
 * @Email cdaic@qq.com
 * @History <li>Author: chenchuan</li>
 * <li>Date: 2017/9/18</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
public class WorkOrderSimpleDto {
    private String woId;
    private Integer status;
    public WorkOrderSimpleDto(){

    }
    public WorkOrderSimpleDto(WorkOrder wo) {
        this.woId = wo.getWoId();
        this.status = wo.getStatus();
    }

    public String getWoId() {
        return woId;
    }

    public void setWoId(String woId) {
        this.woId = woId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
