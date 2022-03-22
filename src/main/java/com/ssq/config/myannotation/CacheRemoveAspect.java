package com.ssq.config.myannotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;


@Component
@Aspect
public class CacheRemoveAspect {

    @Autowired
    RedisTemplate redisTemplate;

    //@annotation()用于匹配有该注解的方法
    //execution()用于匹配某个方法的执行
    //execution(modifiers-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern) throws-pattern?)
    //第一个modifiers-pattern?表示匹配修饰符
    //第二个ret-type-pattern 匹配返回类型
    //declaring-type-pattern? 类路径匹配
    //name-pattern 指定方法名
    //(param-pattern) 参数类型
    //throws-pattern? 抛出异常
    //带有?表示可选项，所以一般是 返回类型 方法名（方法参数）

    //可用通过&& || !修饰符来合并条件
    @Pointcut("execution(* *(..))&&@annotation(com.ssq.config.myannotation.CacheRemove)")
    public void cacheRemoveFun(){

    }

    @AfterReturning(value = "cacheRemoveFun()")
    private void process(JoinPoint joinPoint){
        //解析spel语法的解析器
        ExpressionParser parser = new SpelExpressionParser();
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

        //获取被代理方法的参数的实际值
        Object[]args= joinPoint.getArgs();
        //获取切入方法的数据
        Method method= ((MethodSignature) joinPoint.getSignature()).getMethod();
        //获取被代理方法的参数的名称
        String []params= discoverer.getParameterNames(method);

        //将参数名称 和对应的参数值 添加到context中
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }


        //获得注解
        CacheRemove cacheRemove = method.getAnnotation(CacheRemove.class);

        if (cacheRemove != null) {

            //获取cacheRemove中的key Spel语法 和value 值
            String value = cacheRemove.value();
            String keySpel = cacheRemove.key();

            //需要移除的正则key
            if (!value.equals("")) {
                if (!keySpel.equals("")) {
                    Expression keyExpression = parser.parseExpression(keySpel);
                    String key = keyExpression.getValue(context, String.class);
                    String pattern=new String(value+"::"+key+'_' + "1");
                    System.out.println(value+"::"+key+'_' + "*");
                    Set<String> keys = redisTemplate.keys(pattern);
                    redisTemplate.delete(keys);
                }
            }
        }

    }

}
