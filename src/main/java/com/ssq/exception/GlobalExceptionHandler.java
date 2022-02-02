package com.ssq.exception;

import com.ssq.pojo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {




    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public RespBean handler(RuntimeException e)
    {
        log.error("出现异常",e);
        return RespBean.error(e.getMessage());
    }

    //该方法是用来捕获实体校验产生的异常的
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RespBean handler(MethodArgumentNotValidException e)
    {
        log.error("出现异常",e);
        //正常返回的错误信息是有一大堆的 所以要处理成人话
        BindingResult bindingResult =e.getBindingResult();
        ObjectError objectError=bindingResult.getAllErrors().stream().findFirst().get();
        return RespBean.error(objectError.getDefaultMessage());
    }

    //该方法是用来捕获未授权异常的
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public RespBean handler(AuthenticationException e)
    {
        log.error("出现异常",e);
        return RespBean.error(e.getMessage());
    }


}
