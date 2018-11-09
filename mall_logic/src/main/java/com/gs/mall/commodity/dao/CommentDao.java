package com.gs.mall.commodity.dao;

import com.gs.mall.common.dao.BaseDao;
import com.gs.mall.commodity.po.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Comment dao interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:15
 */
@Mapper
public interface CommentDao extends BaseDao<Comment,java.lang.Long> {

    /**
     * 查询商品评价
     * @param params
     * @param startIndex
     * @param fetchSize
     * @return
     */
    List<Comment> queryComodityComments(@Param("params") Map<String, Object> params,
                                        @Param("startIndex") int startIndex,
                                        @Param("fetchSize") int fetchSize);


    /**
     * 查询商品快照的带分（首次评论）评论数
     * @param snapshootId
     * @return
     */
     int queryScoreCommentNum(Long snapshootId);

    /**
     * 查询商品有图和无图评论数量
     * @param commodityId
     * @return
     */
     List<Map<String,Object>> commentsCount(Long commodityId);

     void updateCommentCount(Map<String,Object> params);

}
