package com.xie.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xie.core.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 用户实体类，仅对应数据表
 */
@Data
@TableName(value = "sys_user", schema = "xie_tm")
public class SysUser extends BaseEntity {

    @TableId
    private Long userId;

    private Long deptId;

    private String userName;

    private String nickName;

    private String userType;

    private String email;

    private String phonenumber;

    private String sex;

    private String avatar;

    private String password;

    private String status;

    @TableLogic(value = "0", delval = "2")
    private String delFlag;

    private String loginIp;

    private Date loginDate;

    private Date pwdUpdateDate;
}
