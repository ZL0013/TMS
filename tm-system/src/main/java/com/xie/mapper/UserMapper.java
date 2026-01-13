package com.xie.mapper;

import com.xie.mapper.converter.UserConverter;
import com.xie.model.dto.UserAddDTO;
import com.xie.model.dto.UserUpdateDTO;
import com.xie.model.entity.SysUser;
import com.xie.model.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 用户映射器
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        uses = UserConverter.class)
public interface UserMapper {

    /**
     * 用户映射为VO
     *
     * @param user 用户
     * @return VO
     */
    @Mapping(target = "deptName", ignore = true)
    UserVO toVO(SysUser user);

    /**
     * 用户列表映射为VO列表
     *
     * @param users 用户列表
     * @return VO列表
     */
    List<UserVO> toVOList(List<SysUser> users);

    /**
     * 用户DTO映射为实体
     *
     * @param dto DTO
     * @return 实体
     */
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "delFlag", ignore = true)
    @Mapping(target = "loginIp", ignore = true)
    @Mapping(target = "loginDate", ignore = true)
    @Mapping(target = "pwdUpdateDate", ignore = true)
    @Mapping(target = "userType", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    SysUser toEntity(UserAddDTO dto);

    /**
     * 用户DTO映射为实体
     *
     * @param dto DTO
     * @return 实体
     */
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "delFlag", ignore = true)
    @Mapping(target = "loginIp", ignore = true)
    @Mapping(target = "loginDate", ignore = true)
    @Mapping(target = "pwdUpdateDate", ignore = true)
    @Mapping(target = "userType", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    SysUser toEntity(UserUpdateDTO dto);
}
