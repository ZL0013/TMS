package com.xie.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "新增角色参数")
public class RoleAddDTO {

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色权限")
    private String roleKey;

    @Schema(description = "显示顺序")
    private Integer roleSort;

    @Schema(description = "数据范围(1:全部数据权限；2:自定数据权限；3:本部门数据权限；4:本部门及以下数据权限")
    private String dataScope;
}
