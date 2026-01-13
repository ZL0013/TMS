package com.xie.framework.web.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xie.core.result.Result;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局响应包装
 */
@Hidden
@Slf4j
@RestControllerAdvice({"com.xie.web.controller"})
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否支持
     *
     * @param returnType    方法参数
     * @param converterType 转换器类型
     * @return 是否支持
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 不是Result类型
        return !returnType.getParameterType().isAssignableFrom(Result.class);
    }

    /**
     * 响应数据包装
     *
     * @param body                  响应数据
     * @param returnType            方法参数
     * @param selectedContentType   媒体类型
     * @param selectedConverterType 转换器类型
     * @param request               请求
     * @param response              响应
     * @return 响应数据
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // String类型返回数据进行包装
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(Result.success(body));
            } catch (Exception e) {
                log.error("响应数据包装失败", e);
            }
        }
        return Result.success(body);
    }
}
