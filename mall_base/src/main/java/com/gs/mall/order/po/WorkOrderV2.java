package com.gs.mall.order.po;

import com.gs.mall.common.po.MyObject;

import java.util.Date;

/**
 * Created by huyoucheng on 2018/11/1.
 */
public class WorkOrderV2 extends MyObject {

    public static final int WaitViewPass = 10;//申请退货（待商家确认)
    public static final int WaitRefund = 20;//待退款(处理退款中)
    public static final int HaveRefunded = 30;//已退款
    public static final int HaveClosed = 40;//关闭

    private int id;
    private String order_id;//订单id
    private String wo_id;//工单id
    private String wo_reason;//提起工单的原因
    private String fromid;//来源id 可以使用户的id
    private String fromname;//来源名 比如用户名
    private String fromphone;//联系方式
    private int fromwhere;//0用户提起工单 1后台提起工单
    private Date createtime = new Date();//创建时间
    private int isrefund;//是否退款退积分
    private String imgurl0;//图片
    private String imgurl1;
    private String imgurl2;
    private int state;//状态
    private String remark;//备注
    private int isDel;//是否后台删除

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getWo_reason() {
        return wo_reason;
    }

    public void setWo_reason(String wo_reason) {
        this.wo_reason = wo_reason;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getFromphone() {
        return fromphone;
    }

    public void setFromphone(String fromphone) {
        this.fromphone = fromphone;
    }

    public int getFromwhere() {
        return fromwhere;
    }

    public void setFromwhere(int fromwhere) {
        this.fromwhere = fromwhere;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public int getIsrefund() {
        return isrefund;
    }

    public void setIsrefund(int isrefund) {
        this.isrefund = isrefund;
    }

    public String getImgurl0() {
        return imgurl0;
    }

    public void setImgurl0(String imgurl0) {
        this.imgurl0 = imgurl0;
    }

    public String getImgurl1() {
        return imgurl1;
    }

    public void setImgurl1(String imgurl1) {
        this.imgurl1 = imgurl1;
    }

    public String getImgurl2() {
        return imgurl2;
    }

    public void setImgurl2(String imgurl2) {
        this.imgurl2 = imgurl2;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public WorkOrderV2() {
    }

    public WorkOrderV2(int id, String order_id, String wo_id, String wo_reason, String fromid, String fromname, String fromphone, int fromwhere, Date createtime, int isrefund, String imgurl0, String imgurl1, String imgurl2, int state, String remark, int isDel) {
        this.id = id;
        this.order_id = order_id;
        this.wo_id = wo_id;
        this.wo_reason = wo_reason;
        this.fromid = fromid;
        this.fromname = fromname;
        this.fromphone = fromphone;
        this.fromwhere = fromwhere;
        this.createtime = createtime;
        this.isrefund = isrefund;
        this.imgurl0 = imgurl0;
        this.imgurl1 = imgurl1;
        this.imgurl2 = imgurl2;
        this.state = state;
        this.remark = remark;
        this.isDel = isDel;
    }

    public WorkOrderV2(String wo_id,int state) {
        this.state = state;
        this.wo_id = wo_id;
    }
}
