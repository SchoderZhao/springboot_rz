package com.qf.exception;

import com.qf.utils.R;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/29  9:38
 */

//全局异常处理
//跳页面
//@ControllerAdvice
//响应json
@RestControllerAdvice
public class MyExceptionHandler {


    @ExceptionHandler(AuthorizationException.class)
    public R handlerException(AuthorizationException e){
        return R.error("您无权操作");
    }

    @ExceptionHandler(RZException.class)
    public R handlerException(RZException e){
        return R.error(e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public R handlerException(Exception e){
        return R.error(e.getMessage());
    }

}
