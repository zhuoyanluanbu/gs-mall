package com.gs.mall.common.po;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:18
 */
public class SystemConfig implements java.io.Serializable{

    /** 系统配置 */
    private java.lang.Long configId;

    /** 系统配置key */
    private java.lang.String configKey;

    /** 系统配置值 */
    private java.lang.String configValue;

    /** 描述 */
    private java.lang.String description;

    /** 状态：1正常 -1 删除 */
    private Integer status;

    /** 创建时间 */
    private java.util.Date createTime;

    /** 更新时间 */
    private java.util.Date updateTime;

    /** 操作人ID */
    private java.lang.String operator;

    /** 操作人ID */
    private java.lang.String operatorId;

    public SystemConfig(){
		
    }

    public void setConfigId(java.lang.Long configId) {
        this.configId = configId;
    }

    public java.lang.Long getConfigId() {
        return this.configId;
    }
    public void setConfigKey(java.lang.String configKey) {
        this.configKey = configKey;
    }

    public java.lang.String getConfigKey() {
        return this.configKey;
    }
    public void setConfigValue(java.lang.String configValue) {
        this.configValue = configValue;
    }

    public java.lang.String getConfigValue() {
        return this.configValue;
    }
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getDescription() {
        return this.description;
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
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    public java.util.Date getUpdateTime() {
        return this.updateTime;
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