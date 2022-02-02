package com.ssq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssq.constant.JwtConstant;
import com.ssq.pojo.RespBean;
import com.ssq.pojo.User;
import com.ssq.mapper.UserMapper;
import com.ssq.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ssq.util.FileUtil;
import com.ssq.util.JwtTokenUtil;
import com.ssq.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author SSQ
 * @since 2022-01-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserMapper userMapper;
    @Value("${file.avaterPath}")
    public String avaterPath;

    @Override
    public RespBean login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        //登录
        UserDetails userDetails =getUserByUsername(username);
        if(null==userDetails)
        {
            return RespBean.error("用户不存在");
        }

        if(!userDetails.getPassword().equals(password))
            return RespBean.error("密码错误");

        //登录成功后
        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        //生成token
        String token= JwtTokenUtil.generatorToken(username);
        response.setHeader(JwtConstant.TOKEN_HEADER,token);
        //向redis中加入token
        boolean tmp=redisUtil.set(username,token,JwtConstant.SAVE_TIME);
        //将USER实体类发送回去 并且剪掉敏感信息
        User user=(User)userDetails;
        user.setPassword("");
        return RespBean.success("登录成功",user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username",username));
    }

    @Override
    public RespBean logout(HttpServletRequest request) {
        String authToken=request.getHeader(JwtConstant.TOKEN_HEADER);
        if(authToken!=null&&authToken.startsWith(JwtConstant.TOKEN_HEAD))
        {
            String token=authToken.substring(JwtConstant.TOKEN_HEAD.length());
            String username=JwtTokenUtil.getUsernameFromToken(token);
            if (username!=null&&redisUtil.hasKey(username))
            {
                redisUtil.del(username);
                return RespBean.success("退出登录成功");
            }
        }
        return RespBean.error("退出登录失败");
    }

    @Override
    public RespBean updateAvatar(MultipartFile file) {

//        UsernamePasswordAuthenticationToken authenticationToken= (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        if(authenticationToken==null)
//            throw new RuntimeException("未认证");
        FileUtil.saveFile(file,avaterPath);
        return RespBean.success("修改成功");
    }

    @Override
    public RespBean updateUserInfo(String oldUsername,String username, String password) {
        User user=userMapper.selectOne(new QueryWrapper<User>().eq("username",oldUsername));
        if(user==null)
            return RespBean.error("修改失败");
        if(username!=null&&!username.equals(""))
            user.setUsername(username);
        if(password!=null&&!password.equals(""))
            user.setPassword(password);
        userMapper.updateById(user);
        //直接登出
        redisUtil.del(oldUsername);
        return RespBean.success("修改成功");
    }
}
