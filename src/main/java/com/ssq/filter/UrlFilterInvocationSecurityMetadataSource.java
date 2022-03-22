package com.ssq.filter;

import com.ssq.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
//该内容用于进行动态url判断,现在已经被弃用
@Component
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    IBlogService blogService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 获取当前请求url
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        System.out.println(requestUrl);
        if (!requestUrl.contains("/getBlogDetail")) {
            return null;
        }

        // 如果数据中没有找到相应url资源则为非法访问，要求用户登录再进行操作
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
