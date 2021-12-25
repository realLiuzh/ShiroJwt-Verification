package com.lzh.controller;

import com.lzh.anno.Phone;
import com.lzh.data.Response;
import com.lzh.model.dto.PhoneLoginDto;
import com.lzh.model.dto.UsernameLoginDto;
import com.lzh.model.dto.UserRegisterDto;
import com.lzh.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 志昊的刘
 * @date 2021/12/21
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/usernameLogin")
    public Response loginWithUsernamePassword(@RequestBody @Validated UsernameLoginDto usernameLoginDto) {
        String token = userService.loginWithUsernamePassword(usernameLoginDto.getUsername(), usernameLoginDto.getPassword());
        return Response.success(token);
    }

    @PostMapping("/phoneLogin")
    public Response loginWithPhone(@RequestBody @Validated PhoneLoginDto phoneLoginDto) {
        String token = userService.loginWithPhone(phoneLoginDto.getPhone(), phoneLoginDto.getVerCode());
        return Response.success(token);
    }


    @PostMapping("/register")
    public Response register(@RequestBody @Validated UserRegisterDto userRegisterDto) {
        userService.checkVerCode(userRegisterDto.getPhone(), userRegisterDto.getVerCode());
        userService.registerUser(userRegisterDto.getUsername(), userRegisterDto.getPassword(), userRegisterDto.getPhone());
        return Response.success();
    }

    @GetMapping("/sendVerCode")
    public Response sendVerCode(@Phone String phone) {
        userService.sendVerCode(phone);
        return Response.success();
    }
}
