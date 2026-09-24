package com.xxx.myspringboot.handler;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotPermissionException;
import com.xxx.myspringboot.common.ApiResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 没权限的情况
    @ExceptionHandler(value = NotPermissionException.class)
    public ApiResult notPermissionException(NotPermissionException e) {
        SaHolder.getResponse().setStatus(403);
        return ApiResult.forbidden(e.getMessage());
    }


    // @RequestBody 对象上的 @Valid 校验失败
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult methodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 从异常对象中拿到ObjectError对象
        BindingResult br = e.getBindingResult();
        if (br.hasFieldErrors()) {
            List<FieldError> fieldErrorList = br.getFieldErrors();
            List<String> errors = new ArrayList<>(fieldErrorList.size());
            for (FieldError error : fieldErrorList) {
                errors.add(error.getField() + ":" + error.getDefaultMessage());
            }
            // 然后提取错误提示信息进行返回
            return ApiResult.failed(errors.toString());
        }
        // 然后提取错误提示信息进行返回
        return ApiResult.failed("校验错误");
    }

    // @RequestParam / @PathVariable 上的 @NotBlank 等失败（类上加了 @Validated）
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult constraintViolation(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return ApiResult.failed(msg);
    }

    // 处理传入都是空格的字符串
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResult missingParam(MissingServletRequestParameterException e) {
        return ApiResult.failed(e.getParameterName() + " 不能为空");
    }

    // 处理 Spring 包装后的
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResult handleDataIntegrity(DataIntegrityViolationException e) {
        Throwable root = e.getMostSpecificCause();
        if (root instanceof SQLIntegrityConstraintViolationException sqlEx) {
            int code = sqlEx.getErrorCode();
            // MySQL: 1062 唯一键冲突, 1452 外键约束, 1048 非空
            if (code == 1062) {
                return ApiResult.failed("数据已存在");
            } else if (code == 1452) {
                return ApiResult.failed("关联数据不存在");
            }
        }
        return ApiResult.failed("数据完整性校验失败");
    }

    // 处理原生 JDBC 异常（如果没被转换）
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ApiResult handleSqlIntegrity(SQLIntegrityConstraintViolationException e) {
        return ApiResult.failed("数据完整性冲突: " + e.getMessage());
    }
}
