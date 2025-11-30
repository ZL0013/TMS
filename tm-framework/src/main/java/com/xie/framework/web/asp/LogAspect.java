package com.xie.framework.web.asp;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;


/**
 * 日志切面
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 切点
     */
    @Pointcut("execution(* com.xie.controller..*.*(..))")
    public void logPointCut() {
    }

    /**
     * 环绕通知
     * @param joinPoint 切点
     * @return 结果
     * @throws Throwable 抛出
     */
    @Around("logPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("日志切面执行");

        // 添加空值检查以避免 NullPointerException
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.warn("无法获取请求上下文，可能不在Web请求环境中");
            // 直接执行目标方法并返回结果
            return joinPoint.proceed();
        }

        // 获取请求
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        log.info("[请求] URL：{}, 方法：{}, 参数：{}",
                request.getRequestURI(),
                request.getMethod(),
                Arrays.toString(joinPoint.getArgs()));

        // 开始时间
        long startTime = System.currentTimeMillis();

        // 执行方法
        Object result = joinPoint.proceed();

        log.info("[响应] 耗时：{}ms, 返回结果：{}",
                System.currentTimeMillis() - startTime,
                JSON.toJSONString(result));

        return result;
    }
}
