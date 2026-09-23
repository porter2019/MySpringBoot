package com.xxx.myspringboot.handler;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotPermissionException;
import com.xxx.myspringboot.common.ApiResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotPermissionException.class)
    public ApiResult notPermissionException(NotPermissionException e){
        SaHolder.getResponse().setStatus(403);
        return ApiResult.forbidden(e.getMessage());
    }
}
