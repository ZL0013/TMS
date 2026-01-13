package com.xie.web.controller.system;


import com.xie.model.dto.RoleAddDTO;
import com.xie.model.vo.RoleVO;
import com.xie.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/v1/api/roles")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    @Operation(summary = "获取角色列表")
    @PreAuthorize("@ps.hasPermission('system:role:list')")
    @GetMapping("/list")
    public List<RoleVO> list() {
        return roleService.listRoles();
    }

    @Operation(summary = "新增角色")
    @PreAuthorize("@ps.hasPermission('system:role:add')")
    @PostMapping
    public void add() {

    }

    @Operation(summary = "修改角色")
    @PreAuthorize("@ps.hasPermission('system:role:update')")
    @PutMapping
    public void update(@Valid @RequestBody RoleAddDTO roleAddDTO) {

    }

    @Operation(summary = "删除角色")
    @PreAuthorize("@ps.hasPermission('system:role:delete')")
    @DeleteMapping("/{roleId}")
    public void delete(@PathVariable Long roleId) {

    }

}
