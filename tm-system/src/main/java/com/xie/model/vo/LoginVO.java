package com.xie.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 登录响应 VO
 * 注意：refreshToken 通过 HttpOnly Cookie 返回，不在响应体中
 */
@Schema(description = "登录响应")
public record LoginVO(
        @Schema(description = "用户名") String username,
        @Schema(description = "访问令牌") String accessToken,
        @Schema(description = "访问令牌过期时间（毫秒）") Long expiresIn
) {
}
