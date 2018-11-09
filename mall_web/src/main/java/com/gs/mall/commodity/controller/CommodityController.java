package com.gs.mall.commodity.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.solr.common.SolrInputDocument;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.result.ResponsePageResult;
import com.gs.common.result.ResponseResult;
import com.gs.common.util.StringUtils;
import com.gs.mall.base.controller.BaseController;
import com.gs.mall.commodity.po.Comment;
import com.gs.mall.commodity.po.Commodity;
import com.gs.mall.commodity.po.CommodityAttributeRef;
import com.gs.mall.commodity.po.CommodityRecommend;
import com.gs.mall.commodity.service.CommentService;
import com.gs.mall.commodity.service.CommodityService;
import com.gs.mall.common.constant.Constant;
import com.gs.mall.common.po.Category;
import com.gs.mall.common.service.CategoryService;
import com.gs.mall.common.util.PageUtil;
import com.gs.mall.enums.NormalStatusEnum;
import com.gs.mall.enums.SolrEnum;
import com.gs.mall.solr.service.SolrService;
import com.gs.mall.user.po.User;

/**
 * 后台商品控制类
 */
@RequestMapping("/manage/commodity")
@RestController
public class CommodityController extends BaseController {
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CommentService commentService;

    @Value("${solr.url}")
    private String solrUrl;

    private SolrService solrService ; // new SolrService(new PropertiesUtil().getPropertiesByKey(""), SolrEnum.COMMODITY);

    private Logger logger = LoggerFactory.getLogger(CommodityController.class);
    
    private final static Map<String,SolrService> SOLRMAP = new ConcurrentHashMap<>();


    public SolrService getSolrService() {
    	solrService=SOLRMAP.get("commodity");
        if( solrService == null ) {
            solrService = new SolrService(solrUrl, SolrEnum.COMMODITY);
            SOLRMAP.put("commodity", solrService);
        }
        return solrService;
    }
    
//    private SolrService getSolrService(){
//		if (solrService == null) {
//			solrService = SolrManager.getManager().getSolrService("commodity", SolrTypeEnum.HTTP);
//		}
//    	return solrService;
//    }

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
        params.put("nonStatus", NormalStatusEnum.DELETE.getValue());
        if(null!=params.get("title")){
            params.put("titleLike",params.get("title"));
            params.remove("title");
        }
        if(null!=params.get("categoryId")){
            Long parentId = Long.valueOf(params.get("categoryId").toString());
            if(-1!=parentId){
                List<Long> categoryIds = categoryService.findIdsByParentId(parentId);
                categoryIds.add(parentId);
                params.put("categoryIds",categoryIds);
                params.remove("categoryId");
            }
        }
        return commodityService.getCommoditys(params, page.getPageNo(), page.getPageSize());
    }

    /**
     * 发布商品
     * @param commodity
     * @return
     * 参数：
     * attributes:[{attributeId:规格id,attributeValue:规格值}]
     * details[{sku:[{attributeId:规格id,attributeValue:规格值}],img:图片,inventory:库存,barCode:条码,salePrice:售价}]
     */
    @RequestMapping("/addCommoditys")
    public ResponseResult addCommoditys(Commodity commodity, @RequestParam Map<String,String> param, HttpServletRequest request){
        logger.debug("addCommoditys");
        logger.debug("commodity="+commodity);
        logger.debug("param=" + param);
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        commodity.setOperator(u.getOperator());
        commodity.setOperatorId(u.getOperatorId());
        commodity.setCreateTime(new Date());
        commodity.setMerchantId(1l);
        commodity.setStatus(NormalStatusEnum.NORMAL.getValue());

        String attributes = param.get("attributes");
        String details = param.get("details");
        String content = param.get("content");
        List<CommodityAttributeRef> commodityAttributes = JSON.parseArray(attributes, CommodityAttributeRef.class);
       // List<CommodityDetail> commodityDetails = JSON.parseArray(details, CommodityDetail.class);
        JSONArray detailsArray = JSON.parseArray(details);
        logger.debug("attributes=" + attributes);
        logger.debug("details="+details);
        logger.debug("commodityAttributes=" + commodityAttributes);
        logger.debug("detailsArray=" + detailsArray);
        ResponseResult responsePageResult = commodityService.addCommodity(commodity, detailsArray, commodityAttributes,content);

        //solr分词
        push(commodity,detailsArray,commodityAttributes,null);
        return responsePageResult;
    }

    /**
     * 修改商品
     * @param commodity
     * @param param
     * @param request
     * @return
     */
    @RequestMapping("/updateCommoditys")
    public ResponseResult updateCommoditys(Commodity commodity,@RequestParam Map<String,String> param,HttpServletRequest request){
        logger.debug("updateCommoditys");
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        commodity.setOperator(u.getOperator());
        commodity.setOperatorId(u.getOperatorId());
        commodity.setUpdateTime(new Date());
        commodity.setMerchantId(1l);
        //commodity.setStatus(NormalStatusEnum.NORMAL.getValue().shortValue());
        String attributes = param.get("attributes");
        String details = param.get("details");
        String content = param.get("content");
        List<CommodityAttributeRef> commodityAttributes = JSON.parseArray(attributes, CommodityAttributeRef.class);
        //List<CommodityDetail> commodityDetails = JSON.parseArray(details, CommodityDetail.class);
        JSONArray detailsArray = JSON.parseArray(details);
        logger.debug("commodityAttributes=" + attributes+ "size="+commodityAttributes.size());
        logger.debug("detailsArray=" + detailsArray);
        ResponseResult responseResult = commodityService.updateCommodity(commodity, detailsArray, commodityAttributes,content);
        logger.debug("commodity=" + commodity);
        //solr分词
        Commodity com = commodityService.getById(commodity.getId());
        CommodityRecommend recommend = commodityService.getRecommend(commodity.getId());
        push(com,detailsArray,commodityAttributes,recommend==null?null:recommend.getDisplayName());
        return responseResult;
    }

    /**
     * 推荐商品
     * @param commodity
     * @param displayName
     * @param request
     * @return
     */
    @RequestMapping("/recommend")
    public ResponseResult recommend(Commodity commodity,@RequestParam String displayName, HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        commodity.setOperator(u.getOperator());
        commodity.setOperatorId(u.getOperatorId());
        commodity.setCreateTime(new Date());
        
        /**更新solr*/
        String[] data = {commodity.getId().toString()};
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("isRecommend","1");
        doc.setField("displayName", displayName);
        int status = getSolrService().update(doc, data);
        logger.debug("solr batch update status=" + doc +"ids="+data+" result:{}",status );
        return commodityService.recommend(commodity,displayName);
    }

    /**
     * 修改推荐商品推荐名称
     * @param commodity
     * @param displayName
     * @param request
     * @return
     */
    @RequestMapping("/updRecommend")
    public ResponseResult updRecommend(Commodity commodity,@RequestParam String displayName, HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        commodity.setOperator(u.getOperator());
        commodity.setOperatorId(u.getOperatorId());
        commodity.setCreateTime(new Date());
        
        /**更新solr*/
        String[] data = {commodity.getId().toString()};
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("isRecommend","1");
        doc.setField("displayName", displayName);
        int status = getSolrService().update(doc, data);
        logger.debug("solr batch update status=" + doc +"ids="+data+" result:{}",status );
        return commodityService.updRecommend(commodity,displayName);
    }

    /**
     * 取消商品推荐
     * @param commodity
     * @param request
     * @return
     */
    @RequestMapping("/unRecommend")
    public ResponseResult unRecommend(Commodity commodity, HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        commodity.setOperator(u.getOperator());
        commodity.setOperatorId(u.getOperatorId());
        commodity.setCreateTime(new Date());
        
        /**更新solr*/
        String[] data = {commodity.getId().toString()};
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("isRecommend","0");
        int status = getSolrService().update(doc, data);
        logger.debug("solr batch update status=" + doc +"ids="+data+" result:{}",status );
        return commodityService.unRecommend(commodity);
    }

    /**
     * 商品下架
     * @param commodity
     * @param request
     * @return
     */
    @RequestMapping("/pull")
    public ResponseResult pull(Commodity commodity, HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        commodity.setOperator(u.getOperator());
        commodity.setOperatorId(u.getOperatorId());
        commodity.setCreateTime(new Date());
        commodity.setStatus((NormalStatusEnum.DISABLE.getValue()));
        Boolean b  = commodityService.saveOrUpdate(commodity);

        /**更新solr*/
        String[] data = {commodity.getId().toString()};
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("status",NormalStatusEnum.DISABLE.getValue());
        int status = getSolrService().update(doc, data);
        logger.debug("solr batch update status=" + doc +"ids="+data+" result:{}",status );
        return b ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }

    /**
     * 商品上架
     * @param commodity
     * @param request
     * @return
     */
    @RequestMapping("/push")
    public ResponseResult push(Commodity commodity, HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        commodity.setOperator(u.getOperator());
        commodity.setOperatorId(u.getOperatorId());
        commodity.setCreateTime(new Date());
        commodity.setStatus(NormalStatusEnum.NORMAL.getValue());

        Boolean b = commodityService.saveOrUpdate(commodity);

        /**更新solr*/
        String[] data = {commodity.getId().toString()};
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("status",NormalStatusEnum.NORMAL.getValue());
        int status = getSolrService().update(doc, data);
        logger.debug("solr batch update status=" + doc +"ids="+data+" result:{}", status );
        return b ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }

    /**
     * 商品删除
     * @param commodity
     * @param request
     * @return
     */
    @RequestMapping("/delCommodity")
    public ResponseResult delCommodity(Commodity commodity, HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        commodity.setOperator(u.getOperator());
        commodity.setOperatorId(u.getOperatorId());
        commodity.setCreateTime(new Date());
        commodity.setStatus(-1);
        
        boolean b = commodityService.saveOrUpdate(commodity);
        if (b) {
        	String[] data = {commodity.getId().toString()};
        	SolrInputDocument doc = new SolrInputDocument();
        	doc.setField("status",NormalStatusEnum.DELETE.getValue());
        	int status = getSolrService().update(doc, data);
        	logger.debug("solr batch update status=" + doc +"ids="+data+" result:{}", status );
		}
        return b ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }


    /**
     * 商品批量下架
     * @param param
     * @param request
     * @return
     */
    @RequestMapping("/batchPull")
    public ResponseResult batchPull(@RequestParam Map<String,Object> param, HttpServletRequest request){
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        Object ids = param.get("ids");
        String[] idsArr = null;
        if(ids != null && !"".equals(ids)){
            idsArr = ids.toString().split(",");
        }
        param.put("toStatus",NormalStatusEnum.DISABLE.getValue());
        param.put("status", NormalStatusEnum.NORMAL.getValue());
        ResponseResult responseResult = commodityService.batchUpdateStatus(idsArr, param, u);

        String[] data = (String[]) responseResult.getData();
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("status",NormalStatusEnum.DISABLE.getValue());
        int status = getSolrService().update(doc, data);
        logger.debug("solr batch update status=" + doc +"ids="+data+" result:{}", status );
        return responseResult;
    }

    /**
     * 批量商品上架
     * @param param
     * @param request
     * @return
     */
    @RequestMapping("/batchPush")
    public ResponseResult batchPush(@RequestParam Map<String,Object> param, HttpServletRequest request) {
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        Object ids = param.get("ids");
        String[] idsArr = null;
        if (ids != null && !"".equals(ids)) {
            idsArr = ids.toString().split(",");
        }
        param.put("toStatus", NormalStatusEnum.NORMAL.getValue());
        param.put("status", NormalStatusEnum.DISABLE.getValue());
        ResponseResult responseResult = commodityService.batchUpdateStatus(idsArr, param, u);

        String[] data = (String[]) responseResult.getData();
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("status",NormalStatusEnum.NORMAL.getValue());
        int status = getSolrService().update(doc, data);
        logger.debug("solr batch update status=" + doc +"ids="+data+" result:{}",status );
        return responseResult;
    }

    /**
     * 商品删除
     * @param param
     * @param request
     * @return
     */
    @RequestMapping("/batchDel")
    public ResponseResult batchDel(@RequestParam Map<String,Object> param, HttpServletRequest request) {
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        Object ids = param.get("ids");
        String[] idsArr = null;
        if (ids != null && !"".equals(ids)) {
            idsArr = ids.toString().split(",");
        }
        param.put("toStatus", NormalStatusEnum.DELETE.getValue());
        ResponseResult responseResult = commodityService.batchUpdateStatus(idsArr, param, u);
        String[] data = (String[])responseResult.getData();
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("status",NormalStatusEnum.DELETE.getValue());
        int status = getSolrService().update(doc, data);
        logger.debug("solr batch update status=" + doc +"ids="+data+" result:{}",status );
        return responseResult;
    }

    /**
     * 修改商品类型
     * @param param
     * @param request
     * @return
     */
    @RequestMapping("/batchChangeCategory")
    public ResponseResult batchChangeCategory(@RequestParam Map<String,Object> param, HttpServletRequest request) {
        User u = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        Object ids = param.get("ids");
        String[] idsArr = null;
        if (ids != null && !"".equals(ids)) {
            idsArr = ids.toString().split(",");
        }
        //param.put("toCategoryId", NormalStatusEnum.DELETE.getValue());
        ResponseResult responseResult = commodityService.batchUpdateStatus(idsArr, param, u);

        /**更新solr*/
        Long toCategory = Long.parseLong(param.get("toCategoryId").toString());
        Category byId = categoryService.getById(toCategory);
        String category = byId.getName();

        String[] data = (String[]) responseResult.getData();
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("category",category);
        doc.setField("categoryId",byId.getCategoryId());
        int status = getSolrService().update(doc, data);
        logger.debug("solr batch update status=" + doc +"ids="+data+" result:{}",status );

        return responseResult;
    }

    /**
     * 全部规格
     * @return
     */
    @RequestMapping("/allAttribute")
    public ResponseResult allAttribute(){
        return commodityService.allAttribute(NormalStatusEnum.NORMAL.getValue());
    }

    /**
     * 删除规格
     * @param id
     * @return
     */
    @RequestMapping("/delAttribute")
    public ResponseResult delAttribute(Long id){
        return commodityService.delAttribute(id);
    }

    /**
     * 添加规格
     * @param attribute
     * @return
     */
    @Valid
    @RequestMapping("/addAttribute")
    public ResponseResult addAttribute(@NotBlank(message="请输入规格名称") String name, HttpServletRequest request){
        User userInfo =getOperator(request);
        return commodityService.saveAttribute(name,userInfo);
    }


    /**
     * 分页查询推荐商品
     * @param page
     * @return
     */
    @RequestMapping("/recommendList")
    public ResponsePageResult recommendList(PageUtil page){
        Map<String,Object> params = new HashMap<>();
        params.put("status",NormalStatusEnum.NORMAL.getValue().shortValue());
        return commodityService.recommendList(params,page);
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
        }
        return  responseResult.setData(commodity);
    }



    /**
     * solr 分词
     * @param commodity
     * @param commodityDetails
     * @param commodityAttributes
     */
    private void push(Commodity commodity, JSONArray commodityDetails, List<CommodityAttributeRef> commodityAttributes,
    		String displayName){
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
        doc.setField("commentScore",commodity.getCommentScore()==null?0:commodity.getCommentScore().intValue());
        if (displayName != null) {
        	doc.setField("displayName", displayName);
        }
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
        int segment = getSolrService().segment(null, doc);
    }

    /**
     * 商品评价分页查询
     * @param categoryId
     * @param supplierId
     * @param commodityName
     * @param page
     * @return
     */
    @RequestMapping("/comments")
    public ResponsePageResult comments(@RequestParam Long categoryId,  String supplierId, String commodityName, PageUtil page){

        Map<String,Object> param = new HashMap<String,Object>();
        param.put("categoryId",categoryId);
        param.put("supplierId", StringUtils.isEmpty(supplierId)? null : supplierId);
        param.put("commodityName", StringUtils.isEmpty(commodityName)? null : commodityName);
        ResponsePageResult result = commentService.getListByPage(param, page.getPageNo(), page.getPageSize());
        return  result;
    }

    /**
     * 回复评论
     * @param commentId
     * @param replyContent
     * @param request
     * @return
     */
    @RequestMapping("/replyComment")
    public ResponseResult reply(Long commentId,String replyContent, HttpServletRequest request){
        if(commentId == null || StringUtils.isEmpty(replyContent)){
            return ResponseResult.failInstance("参数为空");
        }
        User user = (User) request.getAttribute(Constant.OPERATOR_SESSION_KEY);
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setReplyContent(replyContent);
        comment.setReplyer(user.getOperator());
        comment.setReplyTime(new Date());
        Boolean b = commentService.saveOrUpdate(comment);
        return b ? ResponseResult.successInstance() : ResponseResult.failInstance();
    }


    /**
     * 删除评论
     * @param commentId
     * @return
     */
    @RequestMapping("/delComment")
    public ResponseResult delCommon(Long commentId){
    	return ResponseResult.successInstance().setData(commentService.deleteById(commentId));
    }
    
    /**
     * solr同步
     * @param commentId
     * @return
     */
    @RequestMapping("/toSolr/re")
    public ResponseResult toSolr(){
    	Map<String, Object> params = new HashMap<>();
		ResponsePageResult commoditys = commodityService.getCommoditys(params , 1, 200);
		List<Commodity> data = (List<Commodity>) commoditys.getData();
		if (data == null || data.isEmpty()) {
			return ResponseResult.failInstance("kong");
		}
		data.forEach(d -> {
			Commodity com = commodityService.getById(d.getId());
			if ((com.getIsRecommend()==null?0:com.getIsRecommend())==1) {
				CommodityRecommend recommend = commodityService.getRecommend(com.getId());
				push(com,JSONArray.parseArray(JSON.toJSONString(com.getDetailList())),com.getAttributeList(),
						recommend.getDisplayName());
			}else {
 				push(com,JSONArray.parseArray(JSON.toJSONString(com.getDetailList())),com.getAttributeList(),null);
			}
		});
    	return ResponseResult.successInstance();
    }

}
