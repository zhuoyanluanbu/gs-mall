package com.gs.mall.commodity.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.mall.commodity.dao.CommodityAttributeRefDao;
import com.gs.mall.commodity.dao.CommodityDao;
import com.gs.mall.commodity.dao.CommodityDetailDao;
import com.gs.mall.commodity.dao.CommodityExtendDao;
import com.gs.mall.commodity.dao.CommodityRecommendDao;
import com.gs.mall.commodity.dto.CommoditySkuDto;
import com.gs.mall.commodity.dto.InventoryDto;
import com.gs.mall.commodity.po.Commodity;
import com.gs.mall.commodity.po.CommodityAttributeRef;
import com.gs.mall.commodity.po.CommodityDetail;
import com.gs.mall.commodity.po.CommodityExtend;
import com.gs.mall.commodity.po.CommodityRecommend;
import com.gs.mall.commodity.service.CommodityService;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.dao.AttributeDao;
import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.dao.CategoryDao;
import com.gs.mall.common.po.Attribute;
import com.gs.mall.common.po.Category;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.common.util.PageUtil;
import com.gs.mall.enums.NormalStatusEnum;
import com.gs.mall.enums.SolrEnum;
import com.gs.mall.inventory.service.InventoryService;
import com.gs.mall.solr.bean.CommoditySolrBean;
import com.gs.mall.solr.service.SolrService;
import com.gs.mall.user.po.User;


/**
 * Commodity service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:15
 */
@Service("commodityService")
public class CommodityServiceImpl extends AbstractBaseService<Commodity,java.lang.Long> implements CommodityService {

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private CategoryDao categoryDao;

    @Resource
    private CommodityRecommendDao commodityRecommendDao;

    @Resource
    private CommodityExtendDao commodityExtendDao;

    @Resource
    private CommodityAttributeRefDao commodityAttributeRefDao;

    @Resource
    private CommodityDetailDao commodityDetailDao;

    @Resource
    private AttributeDao attributeDao;

    @Autowired
    private InventoryService inventoryService;

    //solr 服务
    private SolrService solrService;

    @Value("${solr.url}")
    private String solrUrl;

    private Logger log = LoggerFactory.getLogger(CommodityServiceImpl.class);


    public SolrService getSolrService(){
        if( solrService == null ) {
            solrService = new SolrService(solrUrl, SolrEnum.COMMODITY);
        }
        return solrService;
    }


    @Override
    protected BaseDao<Commodity,java.lang.Long> getBaseDao() {
        return commodityDao;
    }

    @Transactional
    @Override
    public Boolean saveOrUpdate(Commodity t) {
        int i = 0;
        //Commodity order = CommodityFactory.getCommodityInstance();		
        if( t.getId() == null ) {			
            i = commodityDao.insert(t); 			
        } else {
            i = commodityDao.update(t);
        }
        return (i > 0);
    }

    @Override
    public ResponsePageResult recommendList(Map<String, Object> params, PageUtil pageUtil) {
        int startIndex = 0;
        int fetchSize = 0;
        int pageNo = 1;
        if (pageUtil != null) {
            startIndex = pageUtil.getStartIndex();
            fetchSize = pageUtil.getPageSize();
            pageNo = pageUtil.getPageNo();
        }
        int count = commodityRecommendDao.recommendCount(params);
        pageUtil.setTotalRows(count);
        if (count <= 0) {
            return ResponsePageResult.successInstance().setPageSize(fetchSize)
                    .setCurrentPageNo(pageNo).setTotalCount(count).createEmptyList();
        }
        List<Map> list = commodityRecommendDao.recommendList(params, startIndex, fetchSize);
        return ResponsePageResult.successInstance().setPageSize(fetchSize)
                .setCurrentPageNo(pageNo).setTotalCount(count).setData(list);
    }

    @Override
    @Transactional
    public ResponseResult addCommodity(Commodity commodity, List<CommodityDetail> commodityDetails, List<CommodityAttributeRef> commodityAttributes) {
        Integer min = commodityDetails.get(0).getSalePrice();
        Integer max = min;
        for (CommodityDetail d : commodityDetails) {
            if (d.getSalePrice().compareTo(min) < 0) {
                min = d.getSalePrice();
            }
            if (d.getSalePrice().compareTo(max) > 0) {
                max = d.getSalePrice();
            }
        }
        commodity.setMaxSalePrice(max);
        commodity.setMinSalePrice(min);
        commodityDao.insert(commodity);

        Long commodityId = commodity.getId();
        log.debug("addCommodity commodityId: {}" ,commodityId);

        for (CommodityAttributeRef a : commodityAttributes) {
            a.setCommodityId(commodityId);
        }

        return ResponseResult.successInstance();
    }

    @Override
    @Transactional
    public ResponseResult addCommodity(Commodity commodity, JSONArray commodityDetails, List<CommodityAttributeRef> commodityAttributes, String content) {
        /**1保存商品*/
        JSONObject det = (JSONObject) commodityDetails.get(0);
        Integer min = det.getInteger("salePrice");
        Integer max = min;
        Integer salePrice = null;
        for (int i = 0; i < commodityDetails.size(); i++) {
            JSONObject d = (JSONObject) commodityDetails.get(i);
            salePrice = d.getInteger("salePrice");
            if(salePrice==null||salePrice<=0){
                return ResponseResult.instance(102051);
            }
            if (salePrice.compareTo(min) < 0) {
                min = d.getInteger("salePrice");
            }
            if (salePrice.compareTo(max) > 0) {
                max = d.getInteger("salePrice");
            }
        }
        commodity.setMaxSalePrice(max);
        commodity.setMinSalePrice(min);
        commodityDao.insert(commodity);

        Long commodityId = commodity.getId();
        log.debug("addCommodity commodityId: {}" ,commodityId);
        CommodityExtend commodityExtend = new CommodityExtend();
        commodityExtend.setContent(content);
        commodityExtend.setType(1);
        commodityExtend.setCommodityId(commodityId);
        commodityExtendDao.insert(commodityExtend);//报存商品扩展

        Map<String, Object> attMap = new HashMap<String, Object>();
        /**2保存商品规格*/
        if (commodityAttributes != null && commodityAttributes.size() != 0) {
            for (int i = 0; i < commodityAttributes.size(); i++) {
                CommodityAttributeRef a = commodityAttributes.get(i);
                a.setCommodityId(commodityId);
                a.setSortNum((i + 1));
                commodityAttributeRefDao.insert(a);
                attMap.put("#" + a.getAttributeId() + "#" + a.getAttributeValue(), a.getAttrRefId());
            }
        }

        /**保存商品详情*/
        for (int i = 0; i < commodityDetails.size(); i++) {//遍历详情
            JSONObject d = (JSONObject) commodityDetails.get(i);
            String skuName = "";
            JSONArray sku = d.getJSONArray("sku");
            StringBuffer skuCode = new StringBuffer();
            if (commodityAttributes == null || commodityAttributes.size() == 0) {//商品没有规格时
                skuCode.append(Constant.SKU_NO_ATTRIBUTE + "-");
            } else {
                for (int j = 0; j < sku.size(); j++) {
                    JSONObject att = (JSONObject) sku.get(j);
                    Object attributeId = att.get("attributeId");
                    Object attributeValue = att.get("attributeValue");
                    skuName += attributeValue + " ";
                    Object attrRefId = attMap.get("#" + attributeId + "#" + attributeValue);
                    skuCode.append(attrRefId);
                    skuCode.append("-");
                }
            }
            CommodityDetail commodityDetail = JSON.toJavaObject(d, CommodityDetail.class);
            commodityDetail.setSkuId(skuCode.substring(0, skuCode.length() - 1));
            commodityDetail.setSkuName(skuName);
            commodityDetail.setCommodityId(commodityId);
            commodityDetail.setTitle(commodity.getTitle());
            commodityDetail.setStatus(NormalStatusEnum.NORMAL.getValue());
            commodityDetail.setCreateTime(new Date());
            //确保规格至少有一张商品图片
            if (StringUtils.isEmpty(commodityDetail.getImgUri())) {
                commodityDetail.setImgUri(commodity.getImgUri1());
            }
            commodityDetailDao.insert(commodityDetail);
        }
        return ResponseResult.successInstance();
    }

    @Override
    @Transactional
    public ResponseResult updateCommodity(Commodity commodity, JSONArray commodityDetails, List<CommodityAttributeRef> commodityAttributes, String content) {
        Commodity old = commodityDao.selectById(commodity.getId());

        /**1保存商品*/
        JSONObject det = (JSONObject) commodityDetails.get(0);
        Integer min = det.getInteger("salePrice");
        Integer max = min;
        Integer salePrice = null;
        for (int i = 0; i < commodityDetails.size(); i++) {
            JSONObject d = (JSONObject) commodityDetails.get(i);
            salePrice = d.getInteger("salePrice");
            if(salePrice==null||salePrice<=0){
                return ResponseResult.instance(102051);
            }
            if (salePrice.compareTo(min) < 0) {
                min = d.getInteger("salePrice");
            }
            if (salePrice.compareTo(max) > 0) {
                max = d.getInteger("salePrice");
            }
        }

        commodity.setMaxSalePrice(max);
        commodity.setMinSalePrice(min);
        commodity.setUpdateTime(new Date());
        commodity.setSupplierId(commodity.getSupplierId() == null ? "" : commodity.getSupplierId());//设置为没有供应商时存放空白串
        commodity.setSupplierName(commodity.getSupplierName() == null ? "" : commodity.getSupplierName());//设置为没有供应商时存放空白串
        commodityDao.update(commodity);

        Commodity commodity2 = commodityDao.selectById(commodity.getId());//查询商品信息用于slor保存
        commodity.setStatus(commodity2.getStatus());

        CommodityExtend commodityExtend = new CommodityExtend();
        commodityExtend.setContent(content);
        commodityExtend.setType(1);
        commodityExtend.setCommodityId(commodity.getId());
        commodityExtendDao.updateExtend(commodity.getId(), content);//修改商品扩展

        Map<String, Object> attMap = new HashMap<String, Object>();
        Long commodityId = commodity.getId();
        log.debug("updateCommodity commodityId: {}" + commodityId);
        /**2保存商品规格*/
        List<CommodityAttributeRef> oldAttributes = old.getAttributeList();
        for (int i = 0; i < commodityAttributes.size(); i++) {
            CommodityAttributeRef a = commodityAttributes.get(i);
            a.setCommodityId(commodityId);
            a.setSortNum((i + 1));
            if (oldAttributes.contains(a)) {//相同的不变
                oldAttributes.remove(a);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("commodityId", a.getCommodityId());
                map.put("attributeValue", a.getAttributeValue());
                map.put("attributeId", a.getAttributeId());
                a = commodityAttributeRefDao.getCommodityAttribute(map);//将原AttrRefId查询出来作为详情的skuId（避免更新是skuId为null）
            } else {//新增
                commodityAttributeRefDao.insert(a);
            }
            //commodityAttributeMapper.insert(a);
            attMap.put("#" + a.getAttributeId() + "#" + a.getAttributeValue(), a.getAttrRefId());
        }
        for (CommodityAttributeRef a : oldAttributes) {//删除
            commodityAttributeRefDao.deleteById(a.getAttrRefId());
        }


        /**保存商品详情*/
        //原详情删除
        commodityDetailDao.delCommodityDetail(commodityId);
        JSONArray inventoryArr = new JSONArray();//商品库存

        for (int i = 0; i < commodityDetails.size(); i++) {//遍历详情
            JSONObject d = (JSONObject) commodityDetails.get(i);
            String skuName = "";
            JSONArray sku = d.getJSONArray("sku");
            StringBuffer skuCode = new StringBuffer();
            JSONObject skuDetail = new JSONObject();//库存详情
            skuDetail.put("id", commodity.getId());
            if(null!=sku){
                for (int j = 0; j < sku.size(); j++) {
                    if (commodityAttributes == null || commodityAttributes.size() == 0) {//商品没有规格时
                        skuCode.append(Constant.SKU_NO_ATTRIBUTE + "-");
                    } else {
                        JSONObject att = (JSONObject) sku.get(j);
                        Object attributeId = att.get("attributeId");
                        Object attributeValue = att.get("attributeValue");
                        skuName += attributeValue + " ";
                        Object attrRefId = attMap.get("#" + attributeId + "#" + attributeValue);
                        skuCode.append(attrRefId);
                        skuCode.append("-");
                    }
                }
            }else{
                skuCode.append(Constant.SKU_NO_ATTRIBUTE + "-");
            }
            CommodityDetail commodityDetail = JSON.toJavaObject(d, CommodityDetail.class);
            commodityDetail.setSkuId(skuCode.substring(0, skuCode.length() - 1));
            commodityDetail.setSkuName(skuName);
            commodityDetail.setCommodityId(commodityId);
            commodityDetail.setTitle(commodity.getTitle());
            commodityDetail.setStatus(NormalStatusEnum.NORMAL.getValue());
            commodityDetail.setCreateTime(new Date());
            //确保规格至少有一张商品图片
            if (StringUtils.isEmpty(commodityDetail.getImgUri())) {
                commodityDetail.setImgUri(commodity.getImgUri1());
            }
            commodityDetailDao.insert(commodityDetail);//新增或修改
            skuDetail.put("skuId", commodityDetail.getSkuId());
            skuDetail.put("num", commodityDetail.getInventory()==null?0:commodityDetail.getInventory());
            inventoryArr.add(skuDetail);
        }
        inventoryService.set(inventoryArr);//更新库存
        return ResponseResult.successInstance();
    }

    @Override
    @Transactional
    public ResponseResult recommend(Commodity commodity, String displayName) {
        int recommended = commodityRecommendDao.isRecommended(commodity.getId());
        if (recommended > 0) {
            return ResponseResult.instance(110100);
        }
        int i = commodityRecommendDao.maxSortNum();
        i++;
        CommodityRecommend commodityRecommend = new CommodityRecommend();
        commodityRecommend.setCommodityId(commodity.getId());
        commodityRecommend.setSortNum(i);
        commodityRecommend.setCreateTime(new Date());
        commodityRecommend.setDisplayName(displayName);
        commodityRecommendDao.insert(commodityRecommend);
        commodity.setIsRecommend(1);
        commodity.setUpdateTime(new Date());
        commodityDao.update(commodity);
        return ResponseResult.successInstance();
    }

    @Override
    @Transactional
    public ResponseResult updRecommend(Commodity commodity, String displayName) {
        CommodityRecommend commodityRecommend = new CommodityRecommend();
        commodityRecommend.setCommodityId(commodity.getId());
        commodityRecommend.setDisplayName(displayName);
        commodityRecommendDao.updateDisplayName(commodityRecommend);
        return ResponseResult.successInstance();
    }

    @Override
    @Transactional
    public ResponseResult unRecommend(Commodity commodity) {
        commodity.setIsRecommend(0);
        commodity.setUpdateTime(new Date());
        commodityDao.update(commodity);
        commodityRecommendDao.delRecommend(commodity.getId());
        return ResponseResult.successInstance();
    }

    @Override
    public ResponseResult pull(Commodity commodity) {
        return null;
    }

    @Override
    public ResponseResult delCommodity(Commodity commodity) {
        return null;
    }

    @Override
    public ResponseResult commodityDetail(Long commodityId) {
        return ResponseResult.successInstance().setData(commodityDao.selectById(commodityId));
    }

    @Override
    @Transactional
    public ResponseResult batchUpdateStatus(String[] idsArr, Map<String, Object> param, User user) {
        param.put("operator", user.getOperator());
        param.put("operatorId", user.getOperatorId());
        Object obj = param.get("toCategoryId");
        if( obj != null ) {
            Category cat = categoryDao.selectById(Long.valueOf(obj.toString()));
            if( cat != null ) {
                param.put("toCategoryName", cat.getName());
            }
        }
        int i = 0;
        if (idsArr != null && idsArr.length > 0) {
            param.put("ids", idsArr);
            i = commodityDao.batchUpdateByIds(param);
        } else {
            i = commodityDao.batchUpdateByParam(param);
            param.remove("status");
            List<Object> ids = commodityDao.querybatchIds(param);
            idsArr = new String[ids.size()];
            for (int j = 0; j < ids.size(); j++) {
                idsArr[j] = ids.get(j).toString();
            }
        }
        ResponseResult r = i > 0 ? ResponseResult.successInstance().setData(idsArr) : ResponseResult.failInstance();
        return r;
    }

    @Override
    public ResponseResult allAttribute(Integer status) {
        return ResponseResult.successInstance().setData(attributeDao.selectAll(status));
    }

    @Override
    public ResponsePageResult serach(String keywords, PageUtil page) {
//        ResponsePageResult pageResult = ResponsePageResult.successInstance();
//        List<Commodity> serach = new ArrayList<>();
//        String querySql= "keywords:*"+keywords +"* AND status:1";
//        Map<String,String> map = new HashMap<>(3);
//        SolrDocumentList list = getSolrService().query( page.getPageNo()-1, page.getPageSize(), querySql, map);
//        if (list == null ||  list.isEmpty() || list.getNumFound() <= 0 ) {
//            pageResult.setData(serach);
//            calculatePage(page, pageResult, 0);
//            return pageResult;
//        }
//        int count = (int) list.getNumFound();
//        StringBuilder ids = new StringBuilder();
//        list.forEach(doc -> {
//            ids.append("'").append((String)doc.get(solrService.getUniqueKey())).append("',");
//        });
//        ids.deleteCharAt( ids.length() - 1 );
//        serach = commodityDao.serach(ids.toString());
//        calculatePage(page, pageResult, serach == null || serach.isEmpty() ? 0 : serach.size() < page.getPageSize() ? serach.size() : count);
//        pageResult.setData(serach);
//        return pageResult;
        
        ResponsePageResult pageResult = ResponsePageResult.successInstance();
        List<Commodity> serach = new ArrayList<>();
        List<CommoditySolrBean> result = null;
        int count = commodityDao.countSerach2(keywords);
        calculatePage(page, pageResult, 
        		result == null || result.isEmpty() ? 0 : result.size() < page.getPageSize() ? result.size() : count);
        pageResult.setData(result);
        
		serach = commodityDao.serach2(keywords,page.getStartIndex(),page.getPageSize());
		pageResult.setData(serach);
        return pageResult;
    }


    private void calculatePage(PageUtil page, ResponsePageResult pageResult, int count) {
        int fetchSize = page.getPageSize();
        int pageNo, lastPage = 1;
        pageNo = page.getPageNo();
        pageResult.setPageSize(page.getPageSize());
        pageResult.setCurrentPageNo(page.getPageNo());
        pageResult.setTotalCount(count);
        if (fetchSize != 0 && count != 0) {
            lastPage = count % fetchSize == 0 ? count / fetchSize : count / fetchSize + 1;
        }
        pageResult.setFirstPage(pageNo == 1);
        pageResult.setLastPageNo(lastPage);
        pageResult.setLastPage(pageNo >= lastPage);
        pageResult.setPrePage(pageResult.isFirstPage() ? 1 : pageNo - 1);
        pageResult.setNextPage(pageResult.isLastPage() ? lastPage : pageNo + 1);
    }


    @Override
    public CommoditySkuDto queryByIdAndSkuId(Long id, String skuId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("id", id);
        params.put("skuId", skuId);
        return commodityDao.queryByIdAndSkuId(params);
    }

    @Transactional
    @Override
    public ResponseResult saveAttribute(String name ,User user) {
    	List<String> names = Arrays.asList(name.split(","));
    	List<String> validate = attributeDao.vadateName(names);
    	if (validate != null && validate.size() > 0) {
    		return ResponseResult.failInstance("以下规格名称已存在:"+validate);
		}
    	List<Attribute> list = new ArrayList<>();
    	int sort = attributeDao.maxSort();
    	for (String na : names) {
    		Attribute attr = new Attribute();
    		sort++;
    		attr.setCreateTime(new Date());
    		attr.setName(na);
    		attr.setOperator(user.getOperator());
    		attr.setOperatorId(user.getOperatorId());
    		attr.setStatus(NormalStatusEnum.NORMAL.getValue());
    		attr.setSortNum(sort);
    		list.add(attr);
		}
		attributeDao.insertBatch(list);
        return ResponseResult.successInstance();
    }

    @Override
    public ResponseResult delAttribute(Long attributeId) {
    	int ref = attributeDao.selectIsRef(attributeId);	//判断是否关联商品
    	if (ref > 0) {
    		return ResponseResult.failInstance("该规格下还有商品");
		}
    	Attribute attr = new Attribute();
    	attr.setAttributeId(attributeId);
    	attr.setStatus(NormalStatusEnum.DELETE.getValue());
        int i = attributeDao.update(attr);
        return i > 0 ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }

    @Override
    public ResponseResult refreshInventory() {
        List<Map> list = commodityDao.getAllInventory();
        int items=0;
        if (null != list && !list.isEmpty() ){
            JSONArray jsonArray = JSON.parseArray(JSONArray.toJSONString(list));
            inventoryService.set(jsonArray);
            items = list.size();
        }
        return ResponseResult.successInstance().setData(items);
    }

    @Override
    public ResponseResult updateInventory(List<InventoryDto> list) {
        if( list == null || list.isEmpty() ) {
            return ResponseResult.instance(100103);
        }
        int i = commodityDetailDao.batchUpdateInventory(list);
        return i > 0 ? ResponseResult.successInstance() : ResponseResult.instance(100104);
    }
    
    @Override
    public ResponsePageResult getCommoditys(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        int startIndex = 0;
        int fetchSize = 0;
        if (pageNo != null && pageSize != null ) {
            startIndex = (pageNo-1) * pageSize;
            fetchSize = pageSize;
        }
        ResponsePageResult rp = ResponsePageResult.empty(pageNo==null?0:pageNo, pageSize==null?0:pageSize);
        int i = commodityDao.selectCountByPa(params);
        rp.setTotalCount(i);
        if( i > 0 ) {
            rp.setData(commodityDao.selectByPa(params,startIndex,fetchSize));
        }
        return rp;
    }


	@Override
	public CommodityRecommend getRecommend(Long id) {
		return commodityRecommendDao.selectByCommdityId(id);
	}

}
