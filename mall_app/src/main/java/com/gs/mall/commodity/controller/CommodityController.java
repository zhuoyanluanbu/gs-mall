package com.gs.mall.commodity.controller;

import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.openapi.config.AppSecreConfig;
import com.gs.common.openapi.model.UserInfo;
import com.gs.common.openapi.service.UserApiService;
import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.common.solr.SolrManager;
import com.gs.common.solr.data.QueryParam;
import com.gs.common.solr.data.SolrDataPage;
import com.gs.common.solr.data.SolrTypeEnum;
import com.gs.common.solr.service.SolrService;
import com.gs.common.util.StringUtils;
import com.gs.mall.base.controller.BaseController;
import com.gs.mall.commodity.po.Comment;
import com.gs.mall.commodity.po.Commodity;
import com.gs.mall.commodity.po.CommodityAttributeRef;
import com.gs.mall.commodity.service.CommentService;
import com.gs.mall.commodity.service.CommodityService;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.po.Category;
import com.gs.mall.common.service.CategoryService;
import com.gs.mall.common.util.PageUtil;
import com.gs.mall.enums.NormalStatusEnum;
import com.gs.mall.partner.po.Supplier;
import com.gs.mall.partner.service.SupplierService;
import com.gs.mall.solr.bean.CommoditySolrBean;

/**
 * Created by huangyp on 2017/8/17.
 */
@Validated
@RequestMapping("/app/commodity")
@RestController
public class CommodityController extends BaseController {

    Logger logger = Logger.getLogger(CommodityController.class);
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CategoryService categoryService;


    @Autowired
    private CommentService commentService;
    
    private SolrService solrService;


    private SolrService getSolr(){
		try {
			if (solrService == null) {
				solrService = SolrManager.getManager().getSolrService("commodity", SolrTypeEnum.HTTP);
			}
		} catch (Exception e) {
			logger.error("");
		}
		return solrService;
    }
    
    /**
     *分页查询商品,
     * 按分类查询传入categoryId
     * 按供应商查询传入supplierId
     * @param params
     * @param page
     * @return
     */
    @RequestMapping("/getCommoditys")
    public ResponsePageResult getCommoditys(@RequestParam Map<String,Object> params, PageUtil page){
    	QueryParam param = new QueryParam();
    	param.setPageNo(page.getPageNo());
    	param.setPageSize(page.getPageSize());
        param.addAndQueryMap("status", String.valueOf(NormalStatusEnum.NORMAL.getValue()));
        
    	ResponsePageResult result = ResponsePageResult.successInstance();
    	Map<String, String> other = new HashMap<>();
        params.put("status", NormalStatusEnum.NORMAL.getValue());
        String sort = params.get("sort") == null ?"":params.get("sort").toString();
        StringBuffer querySql = new StringBuffer("status:1");
        switch (sort) {
            case "1" :params.put("col","create_time");//新品
                params.put("sort","desc"); 
                param.addSortMap("createTime", "desc");
                break;
            case "2" :params.put("col","comment_score/comment_total");//好评
                params.put("sort","desc"); 
                param.addSortMap("commentScore", "desc");break;
            case "3" :params.put("col","sale_total");//销量
                params.put("sort","desc"); 
                param.addSortMap("saleTotal", "desc");break;
            case "4" :params.put("col","min_sale_price");//价格
                params.put("sort","asc"); 
                param.addSortMap("minSalePrice", "desc");
                break;
            default:params.put("col","last_buy");
                params.put("sort","desc"); 
                param.addSortMap("lastBuy", "desc");
                break;
        }
        if(null!=params.get("categoryId") && !"".equals(params.get("categoryId"))){
            Long parentId = Long.valueOf(params.get("categoryId").toString());
            if(-1==parentId){
                params.remove("categoryId");
            }else{
            	List<Category> categories=categoryService.findByParentId(parentId);
            	StringBuffer ids = new StringBuffer();
                List<Long> categoryIds = categories.stream().map(Category::getCategoryId).collect(toList());
                categoryIds.add(parentId);
                params.put("categoryIds",categoryIds);
                params.remove("categoryId");
                categoryIds.forEach(id -> ids.append(id+" OR "));
                param.addAndQueryMap("categoryId", "("+ids.substring(0, ids.length()-3)+")");
            }

        }else {
        	param.addAndQueryMap("supplierId", (String)params.get("supplierId"));
        }
        try {
        	SolrDataPage query = getSolr().query(param, CommoditySolrBean.class);
        	List<CommoditySolrBean> datas = query.getDatas();
        	filedChange(datas);
			result.setData(datas).setCurrentPageNo(page.getPageNo()).
				setPageSize(page.getPageSize()).setTotalCount(query.getTotalCount().intValue());
		} catch (Exception e) {
			result = commodityService.getListByPage(params, page.getPageNo(), page.getPageSize());
		} 
        return result;
    }

    /**
     * 分页查询推荐商品
     * @param page
     * @return
     */
    @RequestMapping("/recommendList")
    public ResponsePageResult recommendList(PageUtil page){
//    	Map<String,Object> params = new HashMap<>();
//        params.put("status",NormalStatusEnum.NORMAL.getValue().shortValue());
//        return commodityService.recommendList(params,page);
    	ResponsePageResult result = ResponsePageResult.successInstance();
    	QueryParam param = new QueryParam();
    	param.setPageNo(page.getPageNo());
    	param.setPageSize(page.getPageSize());
    	param.addAndQueryMap("status", "1");
    	param.addAndQueryMap("isRecommend", "1");
		try {
			SolrDataPage query = getSolr().query(param, CommoditySolrBean.class);
			result.setData(query.getDatas()).setCurrentPageNo(page.getPageNo()).setPageSize(page.getPageSize()).
				setTotalCount(query.getTotalCount().intValue());
		} catch (Exception e) {
			Map<String,Object> params = new HashMap<>();
			params.put("status",NormalStatusEnum.NORMAL.getValue().shortValue());
			result = commodityService.recommendList(params,page);
		}
		
        return result;
    }
    
    /**
     * 商品详情
     * @param commodityId
     * @return
     */
    @RequestMapping("/commodityDetail")
        public ResponseResult commodityDetail(@RequestParam Long commodityId){

            ResponseResult responseResult = commodityService.commodityDetail(commodityId);
            Commodity commodity = (Commodity) responseResult.getData();
            if(commodity == null){
                responseResult.setMessage("商品不存在");
                responseResult.setCode(-1);
            }else{
                List<Comment> comments = commentService.queryComodityComments(commodityId, 0, 1);
                commodity.setComments(comments);
                Supplier supplierData = supplierService.getById(commodity.getSupplierId());
                if( supplierData != null ){
                    // Supplier supplier = supplierData;
                    commodity.setSupplierImg(supplierData.getImgUri());
                }else{
                    commodity.setSupplierImg("");
                }
            }
            return  responseResult.setData(commodity);
    }


    /**
     * 商品评价分页查询
     * @param param
     * @param page
     * @return
     */
    @RequestMapping("/comments")
    @Valid
    public ResponsePageResult comments(@NotBlank String commodityId,PageUtil page,String img){
        Map<String,Object> map = new HashMap<String,Object>(1);
        map.put("commodityId", commodityId);
        if (img.equals("1")) {
        	map.put("img", "1");
		}
        ResponsePageResult result = commentService.getListByPage(map, page.getPageNo(), page.getPageSize());
        return result;
    }

    /**
     * 评论
     * @param comment
     * @param request
     * @return
     */
    @RequestMapping("/comment")
    public ResponseResult comment(Comment comment, HttpServletRequest request){
    	AppSecreConfig config = AppSecreConfig.build(Constant.appID, Constant.appSecretKey, Constant.appPrivateSecret);
    	UserInfo user = (UserInfo)request.getAttribute(Constant.APP_USER_SESSION_KEY);
    	ResponseResult rr =UserApiService.getUserInfo(config, user.getOpenId());
    	
        String header = getUserInfo(request).getHeadImgUrl();
        header = StringUtils.isEmpty(header)?"":header;
        comment.setOpenId(user.getOpenId());
        comment.setCommentor(user.getUserName().substring(0, 1)+"**");
        
        if (rr.getCode() == 0) {
        	UserInfo info = (UserInfo) rr.getData();
        	comment.setCommentorImg(info.getHeadImgUrl());
		}
        
        comment.setCommentTime(new Date());
        if(null==comment.getCommodityId()){
            return  ResponseResult.instance(102050);
        }
        ResponseResult res = commentService.comment(comment);
        Commodity com = commodityService.getById(comment.getCommodityId());
        push(com, JSONArray.parseArray(JSON.toJSONString(com.getDetailList())),com.getAttributeList());
        return res;
    }

    /**
     * 商品评价总数
     * @param commodityId
     * @return
     */
    @RequestMapping("/commentsCount")
    public ResponseResult commentsCount(@RequestParam Long commodityId){
        //Map<String,Object> map = new HashMap<String,Object>();
        ResponseResult result = commentService.commentsCount(commodityId);
        return result;
    }


    @RequestMapping("/search")
    public ResponsePageResult search(@RequestParam String keywords,PageUtil page) {
    	ResponsePageResult result = ResponsePageResult.successInstance();   
    	String querySql= "keywords:*"+keywords +"* AND status:1";
    	QueryParam param = new QueryParam();
    	param.addAndQueryMap("keywords", "*"+keywords+"*");
    	param.addAndQueryMap("status", "1");
    	param.setPageNo(page.getPageNo());
    	param.setPageSize(page.getPageSize());
		try {
			SolrDataPage query = getSolr().query(param, CommoditySolrBean.class);
			filedChange(query.getDatas());
			result.setData(query.getDatas()).setCurrentPageNo(page.getPageNo()).setPageSize(page.getPageSize()).
				setTotalCount(query.getTotalCount().intValue());
		} catch (Exception e) {
			result =  commodityService.serach(keywords, page);
		} 
        return result;
    }
    
    private void filedChange(List<CommoditySolrBean> datas){
    	datas.forEach(bean -> {bean.setId(bean.getId());bean.setImgUri1(bean.getImgUri1());});
    }
    
    /**
     * solr 分词
     * @param commodity
     * @param commodityDetails
     * @param commodityAttributes
     */
    private void push(Commodity commodity, JSONArray commodityDetails, List<CommodityAttributeRef> commodityAttributes){
        SolrInputDocument doc  = new SolrInputDocument();
        doc.setField("commodityId",commodity.getId());
        doc.setField("title",commodity.getTitle());
        doc.setField("subTitle",commodity.getSubTitle());
        doc.setField("category",commodity.getCategoryName());
        doc.setField("supplier",commodity.getSupplierName());
        doc.setField("status",commodity.getStatus());
        doc.setField("categoryId",commodity.getCategoryId());
        doc.setField("minSalePrice",commodity.getMinSalePrice());
        doc.setField("maxSalePrice",commodity.getMaxSalePrice());
        doc.setField("imgUrl",commodity.getImgUri1());
        doc.setField("saleTotal",commodity.getSaleTotal());
        doc.setField("isRecommend",commodity.getIsRecommend()==null?"0":commodity.getIsRecommend());
        doc.setField("commentScore",commodity.getCommentScore().intValue());
        if (commodity.getCreateTime() != null) {
        	doc.setField("createTime",(int)(commodity.getCreateTime().getTime()/1000));
		}
        doc.setField("supplierId",commodity.getSupplierId());
        doc.setField("merchantId",commodity.getSupplierId());
        String skuName = "";
        for (CommodityAttributeRef a :commodityAttributes ){
            skuName += a.getAttributeValue();
        }
        String barCode = "";
        for (int i = 0; i < commodityDetails.size(); i++){
            JSONObject o = (JSONObject) commodityDetails.get(i);
            barCode += o.get("barCode");
        }
        doc.setField("barCode",barCode);
        doc.setField("skuName",skuName);
        int segment = getSolr().segment(null, doc);
    }

}
