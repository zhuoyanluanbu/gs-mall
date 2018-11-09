package com.gs.mall.finance.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:20
 */
public class TransferRecord implements java.io.Serializable{

    /** 转账记录ID */
    private java.lang.Long transferId;

    /** 订单号 */
    private java.lang.String orderId;

    /** 转账金额(分) */
    private java.lang.Integer amount;

    /** 转账备注 */
    private java.lang.String remark;

    /** 状态：0待支付  1成功 2失败 */
    private Integer status;

    /** 创建时间 */
    private java.util.Date createTime;

    public TransferRecord(){
		
    }

    public void setTransferId(java.lang.Long transferId) {
        this.transferId = transferId;
    }

    public java.lang.Long getTransferId() {
        return this.transferId;
    }
    public void setOrderId(java.lang.String orderId) {
        this.orderId = orderId;
    }

    public java.lang.String getOrderId() {
        return this.orderId;
    }
    public void setAmount(java.lang.Integer amount) {
        this.amount = amount;
    }

    public java.lang.Integer getAmount() {
        return this.amount;
    }
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }

    public java.lang.String getRemark() {
        return this.remark;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }
}