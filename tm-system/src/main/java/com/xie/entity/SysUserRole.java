package com.xie.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户角色关联信息")
@TableName(value = "sys_user_role", schema = "xie_tm")
public class SysUserRole {

    private Long userId;

    private Long roleId;
}
