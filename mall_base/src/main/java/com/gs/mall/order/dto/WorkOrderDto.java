package com.gs.mall.order.dto;

import java.util.Date;

/**
 * Created by chenchuan on 2017/9/1
 */
public class WorkOrderDto {
    private String woId;

    private String orderId;

    private Integer type;
    /**
     * 退款金额
     */
    private Integer amount;

    private String comment;

    private String imgUri1;

    private String imgUri2;

    private String imgUri3;

    private String closeReason;

    private Integer status;

    private Date createTime;

    private Integer isFreight=0;

    public String getWoId() {
        return woId;
    }

    public void setWoId(String woId) {
        this.woId = woId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImgUri1() {
        return imgUri1;
    }

    public void setImgUri1(String imgUri1) {
        this.imgUri1 = imgUri1;
    }

    public String getImgUri2() {
        return imgUri2;
    }

    public void setImgUri2(String imgUri2) {
        this.imgUri2 = imgUri2;
    }

    public String getImgUri3() {
        return imgUri3;
    }

    public void setImgUri3(String imgUri3) {
        this.imgUri3 = imgUri3;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsFreight() {
        return isFreight;
    }

    public void setIsFreight(Integer isFreight) {
        this.isFreight = isFreight;
    }
}
