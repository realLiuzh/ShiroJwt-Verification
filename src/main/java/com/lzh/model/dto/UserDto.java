package com.lzh.model.dto;

import com.lzh.model.po.Role;
import com.lzh.model.po.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 志昊的刘
 * @date 2021/12/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends User {
    //一个用户只有一个角色
    private Role role;

    public UserDto(Integer id, String username, String password, String phone, Date regTime, Role role) {
        super(id, username, password, phone, regTime);
        this.role = role;
    }
}
