package com.xie.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xie.core.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 用户实体类，仅对应数据表
 */
@Data
@Schema(description = "用户信息")
@TableName(value = "sys_user", schema = "xie_tm")
public class SysUser extends BaseEntity {

    @Schema(description = "用户ID")
    @TableId(type = IdType.AUTO)
    private Long userId;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户类型")
    private String userType;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phonenumber;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "帐号状态")
    private String status;

    @Schema(description = "删除标志")
    @TableLogic(value = "0", delval = "2")
    private String delFlag;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    private Date loginDate;

    @Schema(description = "密码最后更新时间")
    private Date pwdUpdateDate;
}
