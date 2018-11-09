package com.gs.mall.common.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:17
 */
public class Attribute implements java.io.Serializable{

    /** 规格ID */
    private java.lang.Long attributeId;

    /** 规格名称 */
    private java.lang.String name;

    /** 状态：1正常 0 停用 -1 删除 */
    private Integer status;

    /** 排序值 不能为负 */
    private Integer sortNum;

    /** 创建时间 */
    private java.util.Date createTime;

    /** 操作人 */
    private java.lang.String operator;

    /** 操作人ID */
    private java.lang.String operatorId;

    public Attribute(){
		
    }

    public void setAttributeId(java.lang.Long attributeId) {
        this.attributeId = attributeId;
    }

    public java.lang.Long getAttributeId() {
        return this.attributeId;
    }
    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return this.name;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public Integer getSortNum() {
        return this.sortNum;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }
    public void setOperator(java.lang.String operator) {
        this.operator = operator;
    }

    public java.lang.String getOperator() {
        return this.operator;
    }
    public void setOperatorId(java.lang.String operatorId) {
        this.operatorId = operatorId;
    }

    public java.lang.String getOperatorId() {
        return this.operatorId;
    }
}