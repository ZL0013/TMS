package com.xie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.core.util.BeanConverter;
import com.xie.core.util.TreeBuilder;
import com.xie.model.entity.SysMenu;
import com.xie.mapper.SysMenuMapper;
import com.xie.model.vo.MenuTreeVO;
import com.xie.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<MenuTreeVO> getMenuTree() {
        // 1. 查询所有正常状态的菜单，按 orderNum 排序
        List<SysMenu> allMenus = baseMapper.selectList(
                new LambdaQueryWrapper<SysMenu>()
                        .eq(SysMenu::getStatus, "0")
                        .orderByAsc(SysMenu::getOrderNum)
        );

        // 2. 转换为 VO
        List<MenuTreeVO> menuVOList = BeanConverter.convertList(allMenus, MenuTreeVO.class);

        // 3. 使用通用工具类构建树形结构
        return TreeBuilder.buildTree(
                menuVOList,
                MenuTreeVO::getMenuId,
                MenuTreeVO::getParentId,
                MenuTreeVO::setChildren
        );
    }
}
