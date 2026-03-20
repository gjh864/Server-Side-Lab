package com.gonjunhan.helloserver.exception;

import com.gonjunhan.helloserver.common.Result;
import com.gonjunhan.helloserver.common.ResultCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获所有 Exception 类型异常
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        e.printStackTrace();
        // 用枚举返回统一错误响应
        return Result.error(ResultCode.ERROR);
    }

    // 捕获算术异常
    @ExceptionHandler(ArithmeticException.class)
    public Result<String> handleArithmeticException(ArithmeticException e) {
        e.printStackTrace();
        return Result.error(ResultCode.ERROR);
    }
}