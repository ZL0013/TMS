package com.xie.facade;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xie.core.result.ErrorCode;
import com.xie.framework.web.exception.BusinessException;
import com.xie.framework.web.utils.BCryptUtils;
import com.xie.mapper.*;
import com.xie.model.dto.UserAddDTO;
import com.xie.model.dto.UserUpdateDTO;
import com.xie.model.entity.SysDept;
import com.xie.model.entity.SysUser;
import com.xie.model.entity.SysUserRole;
import com.xie.model.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 用户门面类
 * <p>统一管理用户相关操作，进行业务聚合管理</p>
 */
@Component
@RequiredArgsConstructor
public class UserFacade {

    private final SysUserMapper sysUserMapper;
    private final UserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysDeptMapper deptMapper;
    private final SysUserRoleMapper userRoleMapper;

    public UserVO getVOById(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        return user != null ? userMapper.toVO(user) : null;
    }

    public UserVO toVO(SysUser user) {
        return userMapper.toVO(user);
    }

    public List<UserVO> toVOList(List<SysUser> list) {
        return userMapper.toVOList(list);
    }

    public SysUser toEntity(UserAddDTO dto) {
        return userMapper.toEntity(dto);
    }

    public SysUser toEntity(UserUpdateDTO dto) {
        return userMapper.toEntity(dto);
    }

    public UserVO getVOWithFullInfo(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            return null;
        }

        UserVO vo = userMapper.toVO(user);

        if (user.getDeptId() != null) {
            vo.setDeptName(deptMapper.selectById(user.getDeptId()).getDeptName());
        }

        return vo;
    }

    public List<UserVO> getUsersByDeptId(Long deptId) {
        List<SysUser> users = sysUserMapper.selectList(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getDeptId, deptId)
        );
        return userMapper.toVOList(users);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveUserWithRoles(SysUser user, Long[] roleIds) {
        // 1. 保存用户
        sysUserMapper.insert(user);

        // 2. 保存角色关联
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(user.getUserId());
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUserWithRelations(Long userId) {
        sysUserMapper.deleteById(userId);
    }

    public void updateUserWithRoles(UserUpdateDTO dto) {
        SysUser user = userMapper.toEntity(dto);
        sysUserMapper.updateById(user);
        updateUserRoles(user.getUserId(), dto.getRoleIds());
    }

    public void deleteUserRoles(Long userId) {
        userRoleMapper.delete(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
        );
    }

    public void batchDeleteUserRoles(Long[] userIds) {
        if (userIds == null || userIds.length == 0) {
            return;
        }
        userRoleMapper.delete(
                new LambdaQueryWrapper<SysUserRole>()
                        .in(SysUserRole::getUserId, Arrays.asList(userIds))
        );
    }

    public void checkDeptExist(Long deptId) {
        SysDept dept = deptMapper.selectById(deptId);
        if (dept == null) {
            throw new BusinessException(ErrorCode.DEPT_NOT_EXIST);
        }
    }

    public String getDeptName(Long deptId) {
        if (deptId == null) {
            return null;
        }
        SysDept dept = deptMapper.selectById(deptId);
        return dept != null ? dept.getDeptName() : null;
    }

    private void updateUserRoles(Long userId, Long[] roleIds) {
        if (roleIds != null) {
            deleteUserRoles(userId);
            for (Long roleId : roleIds) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
    }

//    private void deleteUserRoles(Long userId) {
//        userRoleMapper.delete(
//                new LambdaQueryWrapper<SysUserRole>()
//                        .eq(SysUserRole::getUserId, userId)
//        );
//    }



//    private void saveUserRoles(Long userId, Long[] roleIds) {
//        if (roleIds != null) {
//            for (Long roleId : roleIds) {
//                SysUserRole userRole = new SysUserRole();
//                userRole.setUserId(userId);
//                userRole.setRoleId(roleId);
//                userRoleMapper.insert(userRole);
//            }
//        }
//    }

    public void resetUserPassword(Long userId, String newPassword) {
        String encryptedPassword = BCryptUtils.encryptPassword(newPassword);
        SysUser updateUser = new SysUser();
        updateUser.setUserId(userId);
        updateUser.setPassword(encryptedPassword);
        updateUser.setPwdUpdateDate(new Date());
        sysUserMapper.updateById(updateUser);
    }
}
