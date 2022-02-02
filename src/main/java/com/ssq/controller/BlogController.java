package com.ssq.controller;


import com.ssq.pojo.Blog;
import com.ssq.pojo.PackingList;
import com.ssq.pojo.RespBean;
import com.ssq.service.IBlogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @ApiOperation(value = "获取所有博客列表，用于管理博客页面，只有登录后才能访问")
    @GetMapping("/getAllBlogList")
    public RespBean getAllBlogList()
    {
        return blogService.getAllBlogList();
    }

    //Restful格式
    @ApiOperation(value = "获取博客详情页")
    @GetMapping("/getBlogDetail/{id}")
    public RespBean detail(@PathVariable(name = "id")Long id, HttpServletRequest request)
    {
        return blogService.getDetailBlog(id,request);
    }

    @ApiOperation(value = "如果不存在则修改，存在则添加新博客")
    @GetMapping("/edit")
    public RespBean editBlog(@Validated @RequestBody Blog blog)
    {
        return blogService.editBlog(blog);
    }


    //从前端获取新上传的博客
    @ApiOperation(value = "上传新的博客")
    @PostMapping("/add")
    public RespBean addBlog(@RequestParam("file") MultipartFile file)
    {
        return blogService.addBlog(file);
    }

    @ApiOperation(value = "通过标签获取博客")
    @GetMapping("/getBlogsByTag/{tagName}")
    public RespBean getBlogsByTag(@PathVariable(name = "tagName")String tagName){
        return blogService.getBlogsByTag(tagName);
    }

    @ApiOperation(value = "删除博客")
    @PostMapping("/deleteBlogById")
    public RespBean deleteBlogsById(@RequestBody PackingList packingList)
    {
        return blogService.deleteBlogsById(packingList.getList());
    }

    @ApiOperation(value = "更改博客查看权限（状态）")
    @PostMapping("/updateBlogStatusById")
    public RespBean updateBlogStatusById(Long id,Boolean status)
    {
        return blogService.updateBlogStatusById(id,status);
    }

    @ApiOperation(value = "搜索博客内容")
    @PostMapping("/searchBlogs")
    public RespBean searchBlogs(String key)
    {
        return blogService.searchBlogs(key);
    }

}
