package com.lzh.controller;

import com.lzh.data.Response;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限测试
 *
 * @author 志昊的刘
 * @date 2021/12/24
 */
@RestController
@RequestMapping("/required")
public class TestController {

    @GetMapping("/admin")
    @RequiresRoles("admin")
    public Response requiredAdmin() {
        return Response.success();
    }

    @GetMapping("/customer")
    @RequiresRoles("customer")
    public Response requiredCustomer() {
        return Response.success();
    }


    @GetMapping("/userView")
    @RequiresPermissions("user:view")
    public Response requiredUserView() {
        return Response.success();
    }

    @GetMapping("/userAll")
    @RequiresPermissions(value = {"user:view", "user:edit"}, logical = Logical.AND)
    public Response requiredUserAll() {
        return Response.success();
    }
}
