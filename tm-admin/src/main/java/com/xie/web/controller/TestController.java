package com.xie.web.controller;

import com.xie.model.entity.SysMenu;
import com.xie.model.entity.SysRole;
import com.xie.model.entity.SysUser;
import com.xie.service.SysMenuService;
import com.xie.service.SysRoleService;
import com.xie.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测试控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Tag(name = "测试接口")
public class TestController {

    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;
    private final SysMenuService sysMenuService;

    /**
     * 测试方法
     * @return hello world
     */
    @GetMapping("/test")
    public String test() {
        return "hello world";
    }

    @Operation(summary = "查询用户列表")
    @GetMapping("/user/list")
    public List<SysUser> getUserlist() {
        return sysUserService.list();
    }

    @GetMapping("/user/{id}")
    public SysUser getUser(@PathVariable Long id) {
        return sysUserService.getById(id);
    }

    @Operation(summary = "查询角色列表")
    @GetMapping("/role/list")
    public List<SysRole> getRolelist() {
        return sysRoleService.list();
    }

    @Operation(summary = "查询菜单列表")
    @GetMapping("/menu/list")
    public List<SysMenu> getMenulist() {
        return sysMenuService.list();
    }
}
