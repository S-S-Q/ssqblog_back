package com.ssq.service;

import com.ssq.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ssq.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author SSQ
 * @since 2022-02-21
 */
public interface ICommentService extends IService<Comment> {

    RespBean addComment(Comment comment);

    RespBean getCommentByBlogId(Long blogID);

    RespBean deleteCommentByBlogId(Long blogId);
}
