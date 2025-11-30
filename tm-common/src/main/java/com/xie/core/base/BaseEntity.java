package com.xie.core.base;

import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String remark;
}
