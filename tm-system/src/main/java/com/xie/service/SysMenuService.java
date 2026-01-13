package com.xie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.model.entity.SysMenu;
import com.xie.model.vo.MenuTreeVO;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取菜单树形结构
     * @return 菜单树列表
     */
    List<MenuTreeVO> getMenuTree();
}
