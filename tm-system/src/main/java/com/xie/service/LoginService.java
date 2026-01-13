package com.xie.service;

import com.xie.model.dto.LoginDTO;
import com.xie.model.dto.RefreshTokenDTO;
import com.xie.model.vo.LoginVO;

public interface LoginService {
    /**
     * 用户登录
     * @param loginDTO 登录请求
     * @return 登录响应，包含 access token 和 refresh token
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 刷新访问令牌
     * @param refreshTokenDTO 刷新令牌请求
     * @return 新的 access token 和 refresh token
     */
    LoginVO refreshToken(RefreshTokenDTO refreshTokenDTO);
}
