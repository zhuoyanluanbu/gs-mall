package com.gs.mall.common.po;

import java.util.List;

/**
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:18
 */
public class Category implements java.io.Serializable{

    /** 分类ID */
    private java.lang.Long categoryId;

    /** 父级分类ID */
    private java.lang.Long parentId;

    /** 分类名称 */
    private java.lang.String name;

    /** 状态：-1 删除  1正常 */
    private Integer status;

    /** 是否允许删除 0 不允许 1 允许 */
    private Integer isAllowDel;

    /** 排序值 不能为负数 */
    private Integer sortNum;

    /** 创建时间 */
    private java.util.Date createTime;

    /** 更新时间 */
    private java.util.Date updateTime;

    /** 操作人 */
    private java.lang.String operator;

    /** 操作人ID */
    private java.lang.String operatorId;

    /**
     * 商品总数
     */

    private Integer count;

    private List<Category> child;

    public Category(){
		
    }

    public void setCategoryId(java.lang.Long categoryId) {
        this.categoryId = categoryId;
    }

    public java.lang.Long getCategoryId() {
        return this.categoryId;
    }
    public void setParentId(java.lang.Long parentId) {
        this.parentId = parentId;
    }

    public java.lang.Long getParentId() {
        return this.parentId;
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
    public void setIsAllowDel(Integer isAllowDel) {
        this.isAllowDel = isAllowDel;
    }

    public Integer getIsAllowDel() {
        return this.isAllowDel;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Category> getChild() {
        return child;
    }

    public void setChild(List<Category> child) {
        this.child = child;
    }
}