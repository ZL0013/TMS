package com.xie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.core.util.BeanConverter;
import com.xie.model.entity.SysRole;
import com.xie.mapper.SysRoleMapper;
import com.xie.model.vo.RoleVO;
import com.xie.service.SysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public List<RoleVO> listRoles() {
        List<SysRole> roles = this.list();
        return BeanConverter.convertList(roles, RoleVO.class);
    }
}
