package com.ssq.controller;


import com.ssq.pojo.RespBean;
import com.ssq.service.ITagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author SSQ
 * @since 2022-01-25
 */
@RestController
@RequestMapping("/tag")
@Api(value = "标签接口",tags = "标签管理相关接口")
public class TagController {

    @Autowired
    ITagService tagService;

    @GetMapping("/getTagList")
    @ApiOperation(value = "获取标签列表")
    public RespBean getTagList()
    {
        return tagService.getTagList();
    }
}
