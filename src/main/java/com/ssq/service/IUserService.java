package com.ssq.service;

import com.ssq.pojo.RespBean;
import com.ssq.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author SSQ
 * @since 2022-01-18
 */
public interface IUserService extends IService<User> {

    RespBean login(String username, String password, HttpServletRequest request);

    User getUserByUsername(String username);

    RespBean logout(HttpServletRequest request);
}
