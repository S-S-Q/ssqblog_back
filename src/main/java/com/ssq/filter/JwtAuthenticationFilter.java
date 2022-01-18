package com.ssq.filter;

import com.ssq.constant.JwtConstant;
import com.ssq.util.JwtTokenUtil;
import com.ssq.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//这部分是我认为是认证后然后给请求授予权限

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader(JwtConstant.TOKEN_HEADER);
        if(null!=authHeader&& authHeader.startsWith(JwtConstant.TOKEN_HEAD))
        {
            //jwt token
            String token=authHeader.substring(JwtConstant.TOKEN_HEAD.length());
            String username= JwtTokenUtil.getUsernameFromToken(token);
            //认证该token是否在缓存中 是否有效
            System.out.println(redisUtil.get(username));
            if(redisUtil.hasKey(username)&&redisUtil.get(username).equals(authHeader))
            {
                //token 存在用户名 但未登录
                if(username!=null&&null== SecurityContextHolder.getContext().getAuthentication())
                {
                    //登录
                    UserDetails userDetails=userDetailsService.loadUserByUsername(username);
                    if(!JwtTokenUtil.isTokenExpired(token)){
                        //向springSecurity中认证
                        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    }

                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
