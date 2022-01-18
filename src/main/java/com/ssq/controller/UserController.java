package com.ssq.controller;


import com.ssq.pojo.RespBean;
import com.ssq.pojo.UserLoginRequest;
import com.ssq.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author SSQ
 * @since 2022-01-18
 */
@RestController
@RequestMapping("/user")
//说明文档
@Api(value = "用户接口", tags = "用户管理相关的接口")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "用户登录接口",notes = "通过该方法返回用户Token")
    public RespBean login(UserLoginRequest userLoginRequest, HttpServletRequest request)
    {
        return userService.login(userLoginRequest.getUsername(),userLoginRequest.getPassword(),request);
    }

}
