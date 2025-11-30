package com.xie.controller;

import com.xie.entity.SysUser;
import com.xie.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试控制器
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "测试接口")
public class TestController {

    private final SysUserService sysUserService;

    /**
     * 测试方法
     * @return hello world
     */
    @GetMapping("/test")
    public String test() {
        return "hello world";
    }

    @Operation(summary = "查询用户列表")
    @GetMapping("/list")
    public List<SysUser> getUserlist() {
        List<SysUser> list = sysUserService.list();
        list.forEach(System.out::println);
        return sysUserService.list();
    }
}
