package com.ssq.controller;


import com.ssq.pojo.Blog;
import com.ssq.pojo.RespBean;
import com.ssq.service.IBlogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author SSQ
 * @since 2022-01-18
 */
@RestController
@RequestMapping("/blog")
@Api(value = "博客接口",tags = "博客管理相关接口")
public class BlogController {
    @Autowired
    IBlogService blogService;

    //普通格式
    @ApiOperation(value = "获取博客列表")
    @GetMapping("/getBlogList")
    public RespBean getBlogList(@RequestParam(defaultValue = "1") Integer currentPage)
    {
        return blogService.getBlogList(currentPage);
    }

    //Restful格式
    @ApiOperation(value = "获取博客详情页")
    @GetMapping("/blog/{id}")
    public RespBean detail(@PathVariable(name = "id")Long id)
    {
        return blogService.getDetailBlog(id);
    }

    @ApiOperation(value = "如果不存在则修改，存在则添加新博客")
    @GetMapping("/edit")
    public RespBean editBlog(@Validated @RequestBody Blog blog)
    {
        return blogService.editBlog(blog);
    }

}
