package com.xie.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色菜单关联")
@TableName(value = "sys_role_menu", schema = "xie_tm")
public class SysRoleMenu {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "菜单ID")
    private Long menuId;
}
