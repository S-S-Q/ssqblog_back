package com.ssq.controller;


import com.ssq.pojo.Comment;
import com.ssq.pojo.RespBean;
import com.ssq.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author SSQ
 * @since 2022-02-21
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    ICommentService commentService;

    @PostMapping("addComment")
    public RespBean addComment(Comment comment)
    {
        return commentService.addComment(comment);
    }

    @PostMapping("getComment")
    public RespBean getComment(Long blogId)
    {
        return commentService.getCommentByBlogId(blogId);
    }
}
