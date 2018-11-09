package com.gs.mall.common.service.impl;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.common.po.Banner;
import com.gs.mall.common.dao.BannerDao;
import com.gs.mall.common.service.BannerService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;


/**
 * Banner service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:17
 */
@Service("bannerService")
public class BannerServiceImpl extends AbstractBaseService<Banner,java.lang.Integer> implements BannerService {

    @Resource
    private BannerDao bannerDao;
	
    @Override
    protected BaseDao<Banner,java.lang.Integer> getBaseDao() {
        return bannerDao;
    }

	

    @Transactional
    @Override
    public Boolean saveOrUpdate(Banner t) {
        int i = 0;
        //Banner order = BannerFactory.getBannerInstance();		
        if( t.getBannerId() == null ) {			
            i = bannerDao.insert(t); 			
        } else {
            i = bannerDao.update(t);
        }
        return (i > 0);
    }

    @Override
    public ResponseResult allBanners(Integer status) {
        return ResponseResult.successInstance().setData(bannerDao.allBanners(status));
    }

    @Transactional
    @Override
    public ResponseResult delBanner(Long bannerId) {
        return null;
    }

    @Transactional
    @Override
    public ResponseResult addBanner(Banner banner) {
        int i = bannerDao.maxSortNum();
        i++;
        banner.setSortNum(i);
        banner.setCreateTime(new Date());
        int update = bannerDao.insert(banner);
        return update > 0 ? ResponseResult.successInstance():ResponseResult.failInstance();
    }

    @Transactional
    @Override
    public ResponseResult updateBanner(Banner banner) {
        return null;
    }

}
