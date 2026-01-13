package com.xie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xie.core.result.ErrorCode;
import com.xie.framework.web.exception.BusinessException;
import com.xie.framework.web.security.JwtTokenProvider;
import com.xie.mapper.SysUserMapper;
import com.xie.model.dto.LoginDTO;
import com.xie.model.dto.RefreshTokenDTO;
import com.xie.model.entity.SysUser;
import com.xie.model.vo.LoginVO;
import com.xie.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * 登录服务实现类
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final SysUserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 登录
     *
     * @param loginDTO 登录参数
     * @return 登录结果（refreshToken 通过 Cookie 返回）
     */
    @Override
    public LoginVO login(LoginDTO loginDTO) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, loginDTO.username()));
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST);
        }
        if (BCrypt.checkpw(loginDTO.password(), user.getPassword())) {
            // 生成 access token
            String accessToken = jwtTokenProvider.createAccessToken(user.getUserName());
            Long expiresIn = jwtTokenProvider.getAccessTokenExpiration();
            
            // refreshToken 将在 Controller 层通过 HttpOnly Cookie 返回
            return new LoginVO(user.getUserName(), accessToken, expiresIn);
        } else {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR);
        }
    }

    /**
     * 刷新访问令牌
     *
     * @param refreshTokenDTO 刷新令牌请求
     * @return 新的 access token
     */
    @Override
    public LoginVO refreshToken(RefreshTokenDTO refreshTokenDTO) {
        String refreshToken = refreshTokenDTO.refreshToken();
        
        // 验证 refresh token 是否有效
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        
        // 从 refresh token 中获取用户名
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        
        // 验证用户是否存在
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, username));
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST);
        }
        
        // 生成新的 access token
        String newAccessToken = jwtTokenProvider.createAccessToken(username);
        Long expiresIn = jwtTokenProvider.getAccessTokenExpiration();
        
        // 新的 refreshToken 将在 Controller 层通过 HttpOnly Cookie 返回
        return new LoginVO(username, newAccessToken, expiresIn);
    }
}
