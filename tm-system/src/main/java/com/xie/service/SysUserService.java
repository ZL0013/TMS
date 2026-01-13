package com.xie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.core.page.PageQuery;
import com.xie.core.page.PageResult;
import com.xie.model.dto.UserAddDTO;
import com.xie.model.dto.UserQueryDTO;
import com.xie.model.dto.UserUpdateDTO;
import com.xie.model.entity.SysUser;
import com.xie.model.vo.UserExcelVO;
import com.xie.model.vo.UserVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SysUserService extends IService<SysUser> {
    /**
     * 分页查询用户信息
     */
    PageResult<UserVO> pageUsers(PageQuery<UserQueryDTO> pageQuery);

    /**
     * 根据 ID 获取用户信息
     */
    UserVO getUserById(Long userId);

    /**
     * 添加用户
     */
    void addUser(UserAddDTO userAddDTO);

    /**
     * 修改用户
     */
    void updateUser(UserUpdateDTO userUpdateDTO);

    /**
     * 删除用户
     */
    void deleteUser(Long userId);

    /**
     * 批量删除用户
     */
    void batchDelete(Long[] userIds);

    /**
     * 重置密码
     */
    void resetPassword(Long userId);

    /**
     * 导出用户
     */
    void exportUsers(HttpServletResponse response, UserQueryDTO query);

    /**
     * 导出用户（动态字段）
     *
     * @param response 响应对象
     * @param query 查询条件
     * @param fields 需要导出的字段列表
     */
    void exportUsersWithFields(HttpServletResponse response, UserQueryDTO query, List<String> fields);

    /**
     * 下载导入模板
     */
    void downloadTemplate(HttpServletResponse response);

    /**
     * 导入用户
     */
    void importUsers(MultipartFile file);
}
