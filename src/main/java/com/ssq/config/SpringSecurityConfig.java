package com.ssq.config;

import com.ssq.filter.JwtAuthenticationFilter;
import com.ssq.filter.RestAuthorizationEntryPoint;
import com.ssq.filter.RestfulAccessDeniedHandler;
import com.ssq.pojo.User;
import com.ssq.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Lazy
    @Autowired
    private UserDetailsService userDetailsService;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private IUserService userService;

    @Lazy
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Lazy
    @Autowired
    private FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;

    @Autowired
    AccessDecisionManager urlAccessDecisionManager;

    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //使用JWT ，不需要csrf
        http.csrf().disable()
                //不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //需要认证的接口
                .authorizeRequests()
                //改内容用于进行动态url判断,现在已经被抛弃
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
//                        o.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource);
//                        o.setAccessDecisionManager(urlAccessDecisionManager);
//                        return o;
//                    }
//                })
                .antMatchers("/blog/add","/blog/deleteBlogById","/blog/getAllBlogList","/blog/updateBlogStatusById"
                        ,"/user/logout","/user/updateUserAvatar","/user/updateUserInfo")
                .authenticated()
                //除了上面所有请求都可以通过
                .anyRequest()
//                .antMatchers("/blog/searchBlogs","/image/avatar.svg","/blog/deleteBlogById","/blog/getBlogsByTag/*","/tag/getTagList","/user/login","/blog/getBlogList","/blog/getAllBlogList","/blog/getBlogDetail/*","/blog/add","/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**")
                .permitAll()
                //禁用缓存
                .and()
                .headers()
                .cacheControl();

        //授权过滤器部分
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权 未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthorizationEntryPoint);


    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService()
    {
        return (username)->{
            User user=userService.getUserByUsername(username);
            if(user!=null)
                return user;
            return null;
        };
    }
}
