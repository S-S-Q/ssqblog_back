package com.ssq.config;

import com.ssq.constant.JwtConstant;
import com.ssq.filter.CommonInterceptor;
import com.ssq.filter.GetVisitorMsgInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//解决跨域问题
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    @Value("${file.mdPath}")
    public String mdPath;
    @Value("${file.mdMapping}")
    public String mdMapping;
    @Value("${file.htmlPath}")
    public String htmlPath;
    @Value("${file.htmlMapping}")
    public String htmlMapping;
    @Value("${file.avaterPath}")
    public String avaterPath;
    @Value("${file.avaterMapping}")
    public String   avaterMapping;
    @Autowired
    public GetVisitorMsgInterceptor getVisitorMsgInterceptor;
    @Autowired
    public CommonInterceptor commonInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //允许跨域的域名
                .allowedOriginPatterns("*")
                //允许跨域的头部 忽略这个
                .exposedHeaders(JwtConstant.TOKEN_HEADER)
                //是否允许证书
                .allowCredentials(true)
                //允许跨域的方法
                .allowedMethods("GET", "POST", "DELETE", "PUT","OPTIONS")
                //maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //上传的md在 /md/**
        registry.addResourceHandler(mdMapping + "**").addResourceLocations("file:" + mdPath);
        //转换的html在  /html/**
        registry.addResourceHandler(htmlMapping+ "**").addResourceLocations("file:" + htmlPath);
        //转换的头像 在/image/**
        registry.addResourceHandler(  avaterMapping+ "**").addResourceLocations("file:" + avaterPath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor);
        registry.addInterceptor(getVisitorMsgInterceptor);
    }
}
