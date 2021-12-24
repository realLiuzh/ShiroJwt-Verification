package com.lzh.model.dto;

import com.lzh.model.po.Permission;
import com.lzh.model.po.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 志昊的刘
 * @date 2021/12/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto extends Role {
    //一种角色只有一种权限
    private Permission permission;

    public RoleDto(Integer id, String name, String comment, Permission permission) {
        super(id, name, comment);
        this.permission = permission;
    }
}
