package com.xie.framework.web.exception;

import com.xie.core.result.ErrorCode;
import com.xie.core.result.Result;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getErrorMessage());
        return Result.failed(e.getErrorCode(), e.getErrorMessage());
    }

    /**
     * 处理 Spring Security 6.x 权限异常
     * @param e AuthorizationDeniedException
     * @return 错误信息
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public Result<Void> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.warn("权限拒绝: {}", e.getMessage());
        return Result.failed(ErrorCode.FORBIDDEN.getCode(), "权限不足，无法访问该资源");
    }

    /**
     * 处理 Spring Security 访问拒绝异常（兼容旧版本）
     * @param e AccessDeniedException
     * @return 错误信息
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("访问拒绝: {}", e.getMessage());
        return Result.failed(ErrorCode.FORBIDDEN.getCode(), "权限不足，无法访问该资源");
    }

    /**
     * 处理所有异常
     * @param e 异常
     * @return 错误信息
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return Result.failed(ErrorCode.SERVER_ERROR.getCode(), ErrorCode.SERVER_ERROR.getMessage());
    }
}
