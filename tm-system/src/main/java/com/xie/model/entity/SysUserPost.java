package com.xie.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户岗位关联信息")
@TableName(value = "sys_user_post", schema = "xie_tm")
public class SysUserPost {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "岗位ID")
    private Long postId;
}
