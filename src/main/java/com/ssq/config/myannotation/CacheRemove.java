package com.ssq.config.myannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
//注解保留的时间 ，RetentionPolicy.source指编译时抛弃  RetentionPolicy.class指生成类文件时存在，加载类文件时抛弃，RetentionPolicy.RunTime指运行时依旧存在。
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheRemove {

    //指定的表
    String value()default "";

    //指定模糊匹配的键值
    String key() default "";
}
