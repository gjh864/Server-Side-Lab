package com.gonjunhan.helloserver.exception;

import com.gonjunhan.helloserver.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获所有 Exception 类型异常
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        // 打印异常栈（方便调试）
        e.printStackTrace();
        // 返回统一 JSON 错误响应
        return Result.error(500, "服务器异常：" + e.getMessage());
    }

    @ExceptionHandler(ArithmeticException.class)
    public Result<String> handleArithmeticException(ArithmeticException e) {
        return Result.error(500, "算术运算异常：" + e.getMessage());
    }
}