package com.gs.mall.common.service;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.po.HotWordGroup;

/**
 * HotWordGroup service interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:18
 */
public interface HotWordGroupService extends BaseService<HotWordGroup,java.lang.Integer>{

    /**
     * 全部热词
     * @param status
     * @return
     */
    ResponseResult all(Integer status);

    /**
     * 新增热词组
     * @param hot
     * @return
     */
    ResponseResult add(HotWordGroup hot);

    /**
     * 修改
     * @param hot
     * @return
     */
    ResponseResult update(HotWordGroup hot);

}
