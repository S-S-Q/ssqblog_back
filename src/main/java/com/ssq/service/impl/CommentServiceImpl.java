package com.ssq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssq.config.myannotation.CacheRemove;
import com.ssq.pojo.Blog;
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
    @CacheRemove(value = "blog_comment",key = "#comment.blog_Id")
    public RespBean addComment(Comment comment) {
        comment.setCreated(LocalDateTime.now());
        commentMapper.insert(comment);
        return RespBean.success("发布评论成功");
    }

    @Override
    @Cacheable(value = "blog_comment",key = "#blogID+'_'+#currentPage")
    public RespBean getCommentByBlogId(Long blogID,Integer currentPage) {
        Page page=new Page(currentPage,5);
        IPage pageData=commentMapper.selectPage(page,new QueryWrapper<Comment>().eq("blog_id",blogID).orderByDesc("created"));
        return RespBean.success("获取评论成功",pageData);
    }

    @Override
    @CacheRemove(value = "blog_comment",key = "#blogId")
    public RespBean deleteCommentByBlogId(Long blogId) {
        commentMapper.delete(new QueryWrapper<Comment>().eq("blog_id",blogId));
        return RespBean.error("删除评论成功");
    }
}
