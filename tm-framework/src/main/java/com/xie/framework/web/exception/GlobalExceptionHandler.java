package com.xie.framework.web.exception;

import com.xie.core.result.ErrorCode;
import com.xie.core.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        return Result.failed(ErrorCode.SERVER_ERROR.getCode(), ErrorCode.SERVER_ERROR.getMessage());
    }
}
