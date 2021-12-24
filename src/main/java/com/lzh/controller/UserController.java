package com.lzh.controller;

import com.lzh.data.Response;
import com.lzh.model.dto.UserLoginDto;
import com.lzh.model.dto.UserRegisterDto;
import com.lzh.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 志昊的刘
 * @date 2021/12/21
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Response login(@RequestBody @Validated UserLoginDto userLoginDto) {
        String token = userService.loginUser(userLoginDto.getUsername(), userLoginDto.getPassword());
        return Response.success(token);
    }

    @PostMapping("/register")
    public Response register(@RequestBody @Validated UserRegisterDto userRegisterDto) {
        log.info("register.....");
        userService.registerUser(userRegisterDto.getUsername(), userRegisterDto.getPassword());
        return Response.success();
    }
}
