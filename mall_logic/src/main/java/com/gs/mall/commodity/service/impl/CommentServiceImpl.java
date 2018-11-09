package com.gs.mall.commodity.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.result.ResponseResult;
import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.common.service.AbstractBaseService;
import com.gs.mall.enums.SolrEnum;
import com.gs.mall.solr.service.SolrService;
import com.gs.mall.commodity.po.Comment;
import com.gs.mall.commodity.po.Commodity;
import com.gs.mall.commodity.po.CommodityAttributeRef;
import com.gs.mall.commodity.dao.CommentDao;
import com.gs.mall.commodity.service.CommentService;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Comment service implement
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:15
 */
@Service("commentService")
public class CommentServiceImpl extends AbstractBaseService<Comment,java.lang.Long> implements CommentService {

    @Resource
    private CommentDao commentDao;
	
    @Override
    protected BaseDao<Comment,java.lang.Long> getBaseDao() {
        return commentDao;
    }
    

	

    @Transactional
    @Override
    public Boolean saveOrUpdate(Comment t) {
        int i = 0;
        //Comment order = CommentFactory.getCommentInstance();		
        if( t.getCommentId() == null ) {			
            i = commentDao.insert(t); 			
        } else {
            i = commentDao.update(t);
        }
        return (i > 0);
    }

    @Override
    public List<Comment> queryComodityComments(Long commodityId, int startIndex, int fetchSize) {
        Map<String,Object> param = new HashMap<String,Object>(1);
        param.put("commodityId",commodityId);
        return commentDao.queryComodityComments(param,startIndex,fetchSize);
    }

    @Transactional
    @Override
    public ResponseResult comment(Comment comment) {
        int i = commentDao.queryScoreCommentNum(comment.getSnapshootId());
        ResponseResult r = ResponseResult.successInstance();
        Map<String,Object> params = new HashMap<>(3);
        params.put("id",comment.getCommodityId());
        params.put("num",1);
        if(i > 0){//只存评论不存评分
            comment.setScore(-1);
            commentDao.insert(comment);
//            r = ResponseResult.failInstance("重复评价，评分无效");
        }else{
            commentDao.insert(comment);
            params.put("score",comment.getScore());
        }
        commentDao.updateCommentCount(params);
        return r;
    }


    @Override
    public ResponseResult commentsCount(Long commodityId) {
        List<Map<String, Object>> list = commentDao.commentsCount(commodityId);
        Map<String,Integer> result = new HashMap<String,Integer>();
        int img = 0;
        int noImg = 0;
        if(list != null || list.size() > 0){
            for(Map<String, Object> map : list){
                //Object img = map.get("img");
                if("1".equals(map.get("img").toString())){
                    img= Integer.parseInt(map.get("num").toString());
                }
                if("0".equals(map.get("img").toString())){
                    noImg= Integer.parseInt(map.get("num").toString());
                }
            }
        }
        result.put("img",img);
        result.put("noImg",noImg);
        return ResponseResult.successInstance().setData(result);
    }
    
    @Override
	public ResponseResult deleteById(Long commentId) {
		int deleteById = commentDao.deleteById(commentId);
		return deleteById>0?ResponseResult.successInstance():ResponseResult.failInstance();
	}
    
}
