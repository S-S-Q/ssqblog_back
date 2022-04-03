package com.ssq.config.aspect;

import com.ssq.config.myannotation.CacheRemove;
import com.ssq.exception.AuthenticationException;
import com.ssq.pojo.RespBean;
import com.ssq.pojo.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
@Aspect
public class AuthenticationAspect {

    @Pointcut(value = "execution(* *(..))&&@annotation(com.ssq.config.myannotation.IdentityAuthentication)")
    public void authenticationMethod(){

    }

    //正常情况下 需要登录后区分身份的才用该注解
    @Around("authenticationMethod()")
    public Object isAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(authentication==null||!(authentication instanceof UsernamePasswordAuthenticationToken))
            throw  new AuthenticationException();//未登录抛出未授权异常
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)authentication;

        //details里面可能存放了当前登录用户的详细信息
        User userDetails = (User) authenticationToken.getPrincipal();
        Identity userIdentity=userDetails.getIdentity();


        //获取方法参数值数组
        Object[] args = joinPoint.getArgs();
        Object result=joinPoint.proceed(args);
//        //获取切入方法的数据
//        Method method= ((MethodSignature) joinPoint.getSignature()).getMethod();
//        //获得注解
//        IdentityAuthentication identityAuthentication = method.getAnnotation(IdentityAuthentication.class);
        RespBean respBean=null;
        if(!(result instanceof RespBean)){
            throw new RuntimeException("服务端出现问题");
        }
        respBean=(RespBean) result;
        switch (userIdentity){
            case ADMIN:
                break;
            case CONSUMER:
                if(respBean.getIdentity()==Identity.ADMIN)
                    throw new AuthenticationException();
                break;
            case NOT_LOG://出现这种情况就是出现bug了
                throw new AuthenticationException();
        }
        return result;
    }
}
