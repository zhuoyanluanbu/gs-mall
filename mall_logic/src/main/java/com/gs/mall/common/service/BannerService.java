package com.gs.mall.common.service;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.po.Banner;

/**
 * Banner service interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:17
 */
public interface BannerService extends BaseService<Banner,java.lang.Integer>{


    /**
     * 全部banner
     * @param status
     * @return
     */
    ResponseResult allBanners(Integer status);

    /**
     * 逻辑删除banner
     * @param bannerId
     * @return
     */
    ResponseResult delBanner(Long bannerId);

    /**
     * 添加
     * @param banner
     * @return
     */
    ResponseResult addBanner(Banner banner);

    /**
     * 修改banner
     * @param banner
     * @return
     */
    ResponseResult updateBanner(Banner banner);

}
