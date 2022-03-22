package com.ssq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@EnableCaching
public class SsqblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsqblogApplication.class, args);
    }

}
