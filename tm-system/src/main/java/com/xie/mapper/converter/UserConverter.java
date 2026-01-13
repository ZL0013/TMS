package com.xie.mapper.converter;


import com.xie.mapper.SysDeptMapper;
import com.xie.model.entity.SysUser;
import com.xie.model.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

/**
 * 用户自定义转换器
 * <p>自定义 mapStruct 无法自动完成的复杂映射</p>
 */
@Component
@RequiredArgsConstructor
public class UserConverter {

    private final SysDeptMapper deptMapper;

    /**
     * 自定义转换
     *
     * @param vo   VO
     * @param user 实体类
     */
    @AfterMapping
    public void setDeptName(@MappingTarget UserVO vo, SysUser user) {
        if (user.getDeptId() != null && deptMapper.selectById(user.getDeptId()) != null) {
            String deptName = deptMapper.selectById(user.getDeptId()).getDeptName();
            vo.setDeptName(deptName);
        }
    }
}
