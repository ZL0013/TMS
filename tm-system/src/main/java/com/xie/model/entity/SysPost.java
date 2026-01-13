package com.xie.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xie.core.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "岗位信息")
@TableName(value = "sys_post", schema = "xie_tm")
public class SysPost extends BaseEntity {
    @Schema(description = "岗位ID")
    @TableId(type = IdType.AUTO)
    private Long postId;

    @Schema(description = "岗位编码")
    private String postCode;

    @Schema(description = "岗位名称")
    private String postName;

    @Schema(description = "显示顺序")
    private Integer postSort;

    @Schema(description = "状态 0 正常，1 停用")
    private String status;


}
