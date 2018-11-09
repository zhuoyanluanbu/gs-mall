package com.gs.mall.commodity.service;

import com.gs.common.result.ResponseResult;
import com.gs.mall.common.service.BaseService;
import com.gs.mall.commodity.po.Comment;

import java.util.List;

/**
 * Comment service interface
 * @author:huangyp
 * @version:1.0
 * @since:1.0
 * @createTime:2017-12-20 18:40:15
 */
public interface CommentService extends BaseService<Comment,java.lang.Long>{

    /**
     * 查询商品评价
     * @param commodityId
     * @param startIndex
     * @param fetchSize
     * @return
     */
    List<Comment> queryComodityComments(Long commodityId, int startIndex, int fetchSize);

    /**
     * 评价商品
     * @param comment
     * @return
     */
    ResponseResult comment(Comment comment);

    /**
     * 查询品数
     * @param commodityId
     * @return
     */
    ResponseResult commentsCount(Long commodityId);
    
    ResponseResult deleteById(Long commentId );

}
