package com.lzh.mapper;

import com.lzh.model.dto.RoleDto;
import com.lzh.model.po.Role;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    RoleDto selectPermissionByRoleId(Integer id);
}