package com.xie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.entity.SysMenu;
import com.xie.mapper.SysMenuMapper;
import com.xie.service.SysMenuService;
import org.springframework.stereotype.Service;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
}
