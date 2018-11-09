package com.gs.mall.common.dao;

import com.gs.mall.common.po.Banner;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Banner dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:17
 */
@Mapper
public interface BannerDao extends BaseDao<Banner,java.lang.Integer> {

    List<Banner> allBanners(Integer value);


    int maxSortNum();

}
