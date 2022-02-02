package com.ssq.service;

import com.ssq.pojo.RespBean;
import com.ssq.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author SSQ
 * @since 2022-01-18
 */
public interface IUserService extends IService<User> {

    RespBean login(String username, String password, HttpServletRequest request, HttpServletResponse response);

    User getUserByUsername(String username);

    RespBean logout(HttpServletRequest request);

    RespBean updateAvatar(MultipartFile file);

    RespBean updateUserInfo(String oldUsername,String username,String password);
}
