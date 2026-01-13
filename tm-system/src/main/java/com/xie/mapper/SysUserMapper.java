package com.xie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.model.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
