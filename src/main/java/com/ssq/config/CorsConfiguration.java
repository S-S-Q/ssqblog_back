package com.ssq.config;

import com.ssq.constant.JwtConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//解决跨域问题
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //允许跨域的域名
                .allowedOrigins("*")
                //允许跨域的头部 忽略这个
                .exposedHeaders(JwtConstant.TOKEN_HEADER)
                //是否允许证书
                .allowCredentials(true)
                //允许跨域的方法
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                //maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
                .maxAge(3600);
    }
}
