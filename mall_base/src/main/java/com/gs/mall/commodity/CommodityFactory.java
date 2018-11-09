package com.gs.mall.commodity;

import com.gs.mall.commodity.po.CommodityDetail;
import com.gs.mall.commodity.po.CommoditySnapshoot;
import com.gs.mall.enums.NormalStatusEnum;

import java.util.Date;

/**
 * 商品工厂类
 * Created by huangyp on 2017/8/21.
 */
public final class CommodityFactory {

    /**
     * 获得快照实例
     * @return
     */
    public static CommoditySnapshoot getSnapshootInstance(CommodityDetail detail){
        CommoditySnapshoot commod = new CommoditySnapshoot();
        commod.setStatus(NormalStatusEnum.NORMAL.getValue());
        commod.setCreateTime(new Date());
        commod.setAttributes(detail.getSkuName());
        commod.setImgUri(detail.getImgUri());
        commod.setCommodityId(detail.getCommodityId());
        commod.setSkuId(detail.getSkuId());
        commod.setTitle(detail.getTitle());
        commod.setBarCode(detail.getBarCode());
        return commod;
    }
}
