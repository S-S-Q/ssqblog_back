package com.ssq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssq.pojo.Comment;
import com.ssq.mapper.CommentMapper;
import com.ssq.pojo.RespBean;
import com.ssq.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author SSQ
 * @since 2022-02-21
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    CommentMapper commentMapper;

    @Override
    @CacheEvict(value = "blog_comment",key = "#comment.blog_Id")
    public RespBean addComment(Comment comment) {
        comment.setCreated(LocalDateTime.now());
        commentMapper.insert(comment);
        return RespBean.success("发布评论成功");
    }

    @Override
    @Cacheable(value = "blog_comment",key = "#blogID")
    public RespBean getCommentByBlogId(Long blogID) {
        List<Comment> comments=commentMapper.selectList(new QueryWrapper<Comment>().eq("blog_id",blogID));
        return RespBean.success("获取评论成功",comments);
    }

    @Override
    @CacheEvict(value = "blog_comment",key = "#blogId")
    public RespBean deleteCommentByBlogId(Long blogId) {
        commentMapper.delete(new QueryWrapper<Comment>().eq("blog_id",blogId));
        return RespBean.error("删除评论成功");
    }
}
