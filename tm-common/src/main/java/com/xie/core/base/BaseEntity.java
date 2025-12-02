package com.xie.core.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "基础实体类")
public class BaseEntity {

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private String updateBy;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "备注")
    private String remark;
}
