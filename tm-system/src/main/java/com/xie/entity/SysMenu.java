package com.xie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xie.core.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "菜单信息")
@TableName(value = "sys_menu", schema = "xie_tm")
public class SysMenu extends BaseEntity {

    @Schema(description = "菜单ID")
    @TableId(type = IdType.AUTO)
    private Long menuId;

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "父菜单ID")
    private Long parentId;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "路由参数")
    private String query;

    @Schema(description = "路由名称")
    private String routeName;

    @Schema(description = "是否为外链（0是 1否）")
    private Integer isFrame;

    @Schema(description = "是否缓存（0缓存 1不缓存）")
    private Integer isCache;

    @Schema(description = "菜单类型（M目录 C菜单 F按钮）")
    private String menuType;

    @Schema(description = "菜单状态（0显示 1隐藏）")
    private String visible;

    @Schema(description = "菜单状态（0正常 1停用）")
    private String status;

    @Schema(description = "权限标识")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;
}
