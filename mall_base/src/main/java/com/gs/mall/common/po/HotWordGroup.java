package com.gs.mall.common.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:18
 */
public class HotWordGroup implements java.io.Serializable{

    /** 热搜词组ID */
    private java.lang.Integer groupId;

    /** 热搜词名称 */
    private java.lang.String groupName;

    private java.lang.String hotWord;

    /** 状态（1 正常  -1 删除） */
    private java.lang.Integer status;

    /** 排序值 */
    private Integer sortNum;

    /** 操作人ID */
    private java.lang.String operator;

    /** 操作人ID */
    private java.lang.String operatorId;

    /** 操作时间 */
    private java.util.Date updateTime;

    public HotWordGroup(){
		
    }

    public void setGroupId(java.lang.Integer groupId) {
        this.groupId = groupId;
    }

    public java.lang.Integer getGroupId() {
        return this.groupId;
    }
    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }

    public java.lang.String getGroupName() {
        return this.groupName;
    }
    public void setHotWord(java.lang.String hotWord) {
        this.hotWord = hotWord;
    }

    public java.lang.String getHotWord() {
        return this.hotWord;
    }
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    public java.lang.Integer getStatus() {
        return this.status;
    }
    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public Integer getSortNum() {
        return this.sortNum;
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
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    public java.util.Date getUpdateTime() {
        return this.updateTime;
    }
}