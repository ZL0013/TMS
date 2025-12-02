package com.xie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.entity.SysRole;
import com.xie.mapper.SysRoleMapper;
import com.xie.service.SysRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
