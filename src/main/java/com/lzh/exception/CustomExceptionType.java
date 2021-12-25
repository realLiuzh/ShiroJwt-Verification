package com.lzh.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常类型
 *
 * @author 志昊的刘
 * @date 2021/12/21
 */
@AllArgsConstructor
@Getter
public enum CustomExceptionType {

    SYSTEM_INNER_ERROR(-1, "系统内部错误"),

    PARAM_ERROR(1, "参数错误"),
    VER_CODE_ERROR(2, "验证码错误"),

    NO_LOGIN(50, "未登录,请先登录后访问"),
    NO_PERMISSION(60, "您没有权限,请联系管理员"),

    TOKEN_VERIFY(100, "token校验错误"),
    TOKEN_EMPTY(101, "token为空"),

    NO_ACCOUNT(140, "请先注册"),
    ACCOUNT_ERROR(150, "用户名或密码错误"),
    USERNAME_USED(160, "用户名已经被占用");


    private Integer code;
    private String typeDesc;
}
