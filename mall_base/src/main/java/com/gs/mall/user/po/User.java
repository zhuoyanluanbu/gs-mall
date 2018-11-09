package com.gs.mall.user.po;

import java.util.Date;

/**
 * Created by huangyp on 2016/10/28.
 */
public class User {

    /**
     * 操作人ID
     */
    protected String operatorId;

    /**
     * 操作人
     */
    protected String operator;

    /**
     * 操作时间
     */
    protected Date operateTime;



    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    /**
     * 创建系统用户
     * @return
     */
    public static User getSystemUser(){
        User u = new User();
        u.setOperator("系统");
        u.setOperatorId("-1");
        u.setOperateTime(new Date());
        return u;
    }
}
