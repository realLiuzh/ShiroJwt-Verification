package com.lzh.mapper;

import com.lzh.model.dto.UserDto;
import com.lzh.model.po.User;

public interface UserMapper {

    User selectByUsername(String username);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    UserDto selectRoleByUserName(String username);

    int checkUserNameIsValid(String username);

    User selectByPhone(String phone);
}