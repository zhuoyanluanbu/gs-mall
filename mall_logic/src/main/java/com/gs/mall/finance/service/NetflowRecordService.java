package com.gs.mall.finance.service;

import com.gs.mall.common.service.BaseService;
import com.gs.mall.finance.po.NetflowRecord;
import com.gs.mall.user.po.User;

/**
 * NetflowRecord service interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:19
 */
public interface NetflowRecordService extends BaseService<NetflowRecord,java.lang.Long>{


    /**
     * 保存记录
     * @param user 用户
     * @param ip 访问ID
     * @param deviceInfo 设备信息
     * @return
     */
    Boolean save(User user, String ip, String deviceInfo);

}
