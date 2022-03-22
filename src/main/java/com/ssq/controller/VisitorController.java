package com.ssq.controller;


import com.ssq.pojo.RespBean;
import com.ssq.service.IVisitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author SSQ
 * @since 2022-02-24
 */
@RestController
@RequestMapping("/visitor")
@Api(value = "统计接口",tags="统计博客相关借口")
public class VisitorController {
    @Autowired
    IVisitorService visitorService;

    @PostMapping("/getVisitor")
    @ApiOperation(value = "获取所有统计好的访问记录")
    RespBean getVisitor()
    {
        return visitorService.getVisitor();
    }

    @PostMapping("/getStaticsMsg")
    @ApiOperation(value = "获取后端统计过的数据")
    RespBean getStaticsMsg()
    {
        return visitorService.getStaticsMsg();
    }

}
