package com.xie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.core.page.PageQuery;
import com.xie.core.page.PageResult;
import com.xie.core.result.ErrorCode;
import com.xie.core.util.BeanConverter;
import com.xie.core.util.ExcelUtils;
import com.xie.facade.UserFacade;
import com.xie.framework.web.exception.BusinessException;
import com.xie.framework.web.utils.BCryptUtils;
import com.xie.mapper.SysDeptMapper;
import com.xie.mapper.SysUserRoleMapper;
import com.xie.model.dto.UserAddDTO;
import com.xie.model.dto.UserQueryDTO;
import com.xie.model.dto.UserUpdateDTO;
import com.xie.model.entity.SysDept;
import com.xie.model.entity.SysUser;
import com.xie.mapper.SysUserMapper;
import com.xie.model.entity.SysUserRole;
import com.xie.model.vo.UserExcelVO;
import com.xie.model.vo.UserVO;
import com.xie.service.SysUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    //    private final SysDeptMapper deptMapper;
//    private final SysUserRoleMapper userRoleMapper;
    private final UserFacade userFacade;

    @Override
    public PageResult<UserVO> pageUsers(PageQuery<UserQueryDTO> pageQuery) {

        UserQueryDTO query = pageQuery.getQuery();

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getUserName()),
                SysUser::getUserName, query.getUserName());

        Page<SysUser> page = this.page(pageQuery.toPage(), wrapper);

        return PageResult.of(page, userFacade::toVO);
    }

    @Override
    public UserVO getUserById(Long userId) {
        return userFacade.getVOById(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserAddDTO userAddDTO) {
        // 1. 校验用户名是否已存在
        checkUserNameNotExist(userAddDTO.getUserName());

        // 2. 校验邮箱是否已被占用
        if (StringUtils.hasText(userAddDTO.getEmail())) {
            checkEmailNotExist(userAddDTO.getEmail());
        }

        // 3. 校验手机号是否已被占用
        if (StringUtils.hasText(userAddDTO.getPhonenumber())) {
            checkPhoneNotExist(userAddDTO.getPhonenumber());
        }

        // 4. 校验部门是否存在（如果指定了部门ID）
        if (userAddDTO.getDeptId() != null) {
            checkDeptExist(userAddDTO.getDeptId());
        }

        // 5. 构建用户实体
//        SysUser user = new SysUser();
//        user.setDeptId(userAddDTO.getDeptId());
//        user.setUserName(userAddDTO.getUserName());
//        user.setNickName(userAddDTO.getNickName());
//        user.setEmail(userAddDTO.getEmail());
//        user.setPhonenumber(userAddDTO.getPhonenumber());
//        user.setSex(userAddDTO.getSex() != null ? userAddDTO.getSex() : "2"); // 默认未知
//        user.setStatus(userAddDTO.getStatus() != null ? userAddDTO.getStatus() : "0"); // 默认正常
        SysUser user = userFacade.toEntity(userAddDTO);

        // 6. 加密密码
        String encryptedPassword = BCryptUtils.encryptPassword(userAddDTO.getPassword());
        user.setPassword(encryptedPassword);
        user.setPwdUpdateDate(new Date());

        // 7. 保存用户
//        this.save(user);
        userFacade.saveUserWithRoles(user, userAddDTO.getRoleIds());

        // 8. 关联角色（如果有）
//        saveUserRoles(user.getUserId(), userAddDTO.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        // 1. 检查用户是否存在
        checkUserExist(userUpdateDTO.getUserId());

        // 2. 检查邮箱是否被其他用户占用
        if (StringUtils.hasText(userUpdateDTO.getEmail())) {
            checkEmailNotExistExcludeSelf(userUpdateDTO.getEmail(), userUpdateDTO.getUserId());
        }

        // 3. 检查手机号是否被其他用户占用
        if (StringUtils.hasText(userUpdateDTO.getPhonenumber())) {
            checkPhoneNotExistExcludeSelf(userUpdateDTO.getPhonenumber(), userUpdateDTO.getUserId());
        }

        // 4. 检查部门是否存在（如果指定了部门ID）
        if (userUpdateDTO.getDeptId() != null) {
            checkDeptExist(userUpdateDTO.getDeptId());
        }

        // 5. 更新用户信息
//        SysUser user = new SysUser();
//        user.setUserId(userUpdateDTO.getUserId());
//        user.setDeptId(userUpdateDTO.getDeptId());
//        user.setNickName(userUpdateDTO.getNickName());
//        user.setEmail(userUpdateDTO.getEmail());
//        user.setPhonenumber(userUpdateDTO.getPhonenumber());
//        user.setSex(userUpdateDTO.getSex());
//        user.setStatus(userUpdateDTO.getStatus());
//
//        this.updateById(user);

        // 6. 更新用户角色关联
//        updateUserRoles(userUpdateDTO.getUserId(), userUpdateDTO.getRoleIds());
        userFacade.updateUserWithRoles(userUpdateDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {
        // 1. 检查用户是否存在
        checkUserExist(userId);

        // 2. 删除用户（逻辑删除，由 @TableLogic 处理）
        this.removeById(userId);

        // 3. 删除用户角色关联
        userFacade.deleteUserRoles(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] userIds) {
        if (userIds == null || userIds.length == 0) {
            throw new BusinessException(ErrorCode.USER_SELECT_REQUIRED);
        }

        // 1. 批量删除用户（逻辑删除）
        this.removeByIds(Arrays.asList(userIds));

        // 2. 批量删除用户角色关联
//        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
//        wrapper.in(SysUserRole::getUserId, Arrays.asList(userIds));
//        userRoleMapper.delete(wrapper);
        userFacade.batchDeleteUserRoles(userIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long userId) {
        // 1. 检查用户是否存在
        checkUserExist(userId);

        // 2. 重置为默认密码 123456
//        String defaultPassword = "123456";
//        String encryptedPassword = BCryptUtils.encryptPassword(defaultPassword);
//
//        SysUser updateUser = new SysUser();
//        updateUser.setUserId(userId);
//        updateUser.setPassword(encryptedPassword);
//        updateUser.setPwdUpdateDate(new Date());
//
//        this.updateById(updateUser);
        userFacade.resetUserPassword(userId, "123456");
    }

    @Override
    public void exportUsers(HttpServletResponse response, UserQueryDTO query) {
        List<UserExcelVO> excelList = queryAndConvertToExcelVO(query);
        ExcelUtils.export(response, excelList, UserExcelVO.class, "用户数据", "用户列表");
    }

    @Override
    public void exportUsersWithFields(HttpServletResponse response, UserQueryDTO query, List<String> fields) {
        List<UserExcelVO> excelList = queryAndConvertToExcelVO(query);
        ExcelUtils.exportWithFields(response, excelList, UserExcelVO.class,
                "用户数据", "用户列表", fields);
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) {
        // 下载空模板
        ExcelUtils.downloadTemplate(response, UserExcelVO.class, "用户导入模板");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importUsers(MultipartFile file) {
        try {
            // 1. 读取 Excel 数据
            List<UserExcelVO> excelList = ExcelUtils.importExcel(
                    file.getInputStream(),
                    UserExcelVO.class
            );

            if (excelList.isEmpty()) {
                throw new BusinessException(ErrorCode.IMPORT_DATA_EMPTY);
            }

            // 2. 转换为实体并保存
            for (UserExcelVO excelVO : excelList) {
                // 校验必填字段
                if (!StringUtils.hasText(excelVO.getUserName())) {
                    throw new BusinessException(ErrorCode.USER_NAME_REQUIRED);
                }

                // 检查用户名是否已存在
                checkUserNameNotExist(excelVO.getUserName());

                // 转换并保存用户
                SysUser user = convertExcelVOToEntity(excelVO);
                this.save(user);
            }

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.IMPORT_FAILED);
        }
    }

    /**
     * 将 Entity 转换为 VO（带部门名称）
     */
//    private UserVO convertToVO(SysUser user) {
//        return BeanConverter.convert(user, UserVO.class, vo -> {
//            // 查询并设置部门名称
//            if (user.getDeptId() != null) {
//                SysDept dept = deptMapper.selectById(user.getDeptId());
//                if (dept != null) {
//                    vo.setDeptName(dept.getDeptName());
//                }
//            }
//        });
//    }

    /**
     * 检查用户是否存在
     */
    private void checkUserExist(Long userId) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST);
        }
    }

    /**
     * 检查用户名是否已存在
     */
    private void checkUserNameNotExist(String userName) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserName, userName);
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXIST);
        }
    }

    /**
     * 检查邮箱是否已被占用（新增时使用）
     *
     * @param email 邮箱
     */
    private void checkEmailNotExist(String email) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getEmail, email);
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXIST);
        }
    }

    /**
     * 检查手机号是否已被占用（新增时使用）
     *
     * @param phonenumber 手机号
     */
    private void checkPhoneNotExist(String phonenumber) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getPhonenumber, phonenumber);
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ErrorCode.PHONE_ALREADY_EXIST);
        }
    }

    /**
     * 检查邮箱是否被其他用户占用（排除自己）
     *
     * @param email  邮箱
     * @param userId 当前用户ID
     */
    private void checkEmailNotExistExcludeSelf(String email, Long userId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getEmail, email)
                .ne(SysUser::getUserId, userId);
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXIST);
        }
    }

    /**
     * 检查手机号是否被其他用户占用（排除自己）
     *
     * @param phonenumber 手机号
     * @param userId      当前用户ID
     */
    private void checkPhoneNotExistExcludeSelf(String phonenumber, Long userId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getPhonenumber, phonenumber)
                .ne(SysUser::getUserId, userId);
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ErrorCode.PHONE_ALREADY_EXIST);
        }
    }

    /**
     * 检查部门是否存在
     *
     * @param deptId 部门ID
     */
    private void checkDeptExist(Long deptId) {
        userFacade.checkDeptExist(deptId);
    }

    /**
     * 保存用户角色关联
     */
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

    /**
     * 更新用户角色关联（先删除后新增）
     */
//    private void updateUserRoles(Long userId, Long[] roleIds) {
//        if (roleIds != null) {
//            // 删除原有角色关联
//            deleteUserRoles(userId);
//            // 新增角色关联
//            saveUserRoles(userId, roleIds);
//        }
//    }

//    /**
//     * 删除用户角色关联
//     */
//    private void deleteUserRoles(Long userId) {
//        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(SysUserRole::getUserId, userId);
//        userRoleMapper.delete(wrapper);
//    }

    /**
     * 查询用户并转换为 Excel VO
     */
    private List<UserExcelVO> queryAndConvertToExcelVO(UserQueryDTO query) {
        // 1. 查询用户数据
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getUserName()),
                SysUser::getUserName, query.getUserName());
        List<SysUser> userList = this.list(wrapper);

        // 2. 转换为 Excel VO
        return userList.stream()
                .map(this::convertToExcelVO)
                .collect(Collectors.toList());
    }

    /**
     * 将 Excel VO 转换为 Entity
     */
    private SysUser convertExcelVOToEntity(UserExcelVO excelVO) {
        SysUser user = BeanConverter.convert(excelVO, SysUser.class);

        // 转换性别
        user.setSex(convertSexLabelToCode(excelVO.getSexLabel()));

        // 转换状态
        user.setStatus(convertStatusLabelToCode(excelVO.getStatusLabel()));

        // 设置默认密码
        String encryptedPassword = BCryptUtils.encryptPassword("123456");
        user.setPassword(encryptedPassword);
        user.setPwdUpdateDate(new Date());

        return user;
    }

    /**
     * 将 Entity 转换为 Excel VO
     */
    private UserExcelVO convertToExcelVO(SysUser user) {
        return BeanConverter.convert(user, UserExcelVO.class, excelVO -> {
            // 转换性别
            excelVO.setSexLabel(convertSexCodeToLabel(user.getSex()));

            // 转换状态
            excelVO.setStatusLabel(convertStatusCodeToLabel(user.getStatus()));

            // 查询并设置部门名称
            if (user.getDeptId() != null) {
//                SysDept dept = deptMapper.selectById(user.getDeptId());
//                if (dept != null) {
//                    excelVO.setDeptName(dept.getDeptName());
//                }
                String deptName = userFacade.getDeptName(user.getDeptId());
                if (deptName != null) {
                    excelVO.setDeptName(deptName);
                }
            }

            // 格式化创建时间
            if (user.getCreateTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                excelVO.setCreateTime(sdf.format(user.getCreateTime()));
            }
        });
    }

    /**
     * 性别代码转标签
     */
    private String convertSexCodeToLabel(String code) {
        if (code == null) return "未知";
        return switch (code) {
            case "0" -> "男";
            case "1" -> "女";
            default -> "未知";
        };
    }

    /**
     * 性别标签转代码
     */
    private String convertSexLabelToCode(String label) {
        if (label == null) return "2";
        return switch (label) {
            case "男" -> "0";
            case "女" -> "1";
            default -> "2";
        };
    }

    /**
     * 状态代码转标签
     */
    private String convertStatusCodeToLabel(String code) {
        if (code == null) return "正常";
        return switch (code) {
            case "0" -> "正常";
            case "1" -> "停用";
            default -> "正常";
        };
    }

    /**
     * 状态标签转代码
     */
    private String convertStatusLabelToCode(String label) {
        if (label == null) return "0";
        return switch (label) {
            case "正常" -> "0";
            case "停用" -> "1";
            default -> "0";
        };
    }
}
