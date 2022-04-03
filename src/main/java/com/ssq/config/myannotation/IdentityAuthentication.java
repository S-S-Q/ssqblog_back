package com.ssq.config.myannotation;

import com.ssq.config.aspect.Identity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IdentityAuthentication {

    //允许通过的身份
    Identity[] identity() default {};

    //是否进行校验
    boolean isCheck() default true;
}
