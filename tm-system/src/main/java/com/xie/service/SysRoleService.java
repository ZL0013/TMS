package com.xie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.model.entity.SysRole;
import com.xie.model.vo.RoleVO;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 查询所有角色列表
     * @return 角色列表
     */
    List<RoleVO> listRoles();
}
