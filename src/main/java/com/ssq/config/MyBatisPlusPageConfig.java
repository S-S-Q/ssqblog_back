package com.ssq.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//Mybatis 分页插件配置
@Configuration
@ConditionalOnClass(value = {PaginationInterceptor.class})
public class MyBatisPlusPageConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
