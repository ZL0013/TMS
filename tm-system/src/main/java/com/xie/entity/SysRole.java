package com.xie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xie.core.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色信息")
@TableName(value = "sys_role", schema = "xie_tm")
public class SysRole extends BaseEntity {

    @Schema(description = "角色ID")
    @TableId(type = IdType.AUTO)
    private Long roleId;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色权限")
    private String roleKey;

    @Schema(description = "显示顺序")
    private Integer roleSort;

    @Schema(description = "数据范围(1:全部数据权限；2:自定数据权限；3:本部门数据权限；4:本部门及以下数据权限")
    private String dataScope;

    @Schema(description = "菜单树选择项是否关联显示")
    private Boolean menuCheckStrictly;

    @Schema(description = "部门树选择项是否关联显示")
    private Boolean deptCheckStrictly;

    @Schema(description = "角色状态")
    private String status;

    @Schema(description = "删除标志")
    @TableLogic(value = "0", delval = "2")
    private String delFlag;
}
