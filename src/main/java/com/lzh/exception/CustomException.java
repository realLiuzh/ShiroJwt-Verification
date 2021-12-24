package com.lzh.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 自定义异常
 *
 * @author 志昊的刘
 * @date 2021/12/21
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends RuntimeException {
    private Integer code;
    private String message;

    public CustomException(CustomExceptionType customExceptionType) {
        this.code = customExceptionType.getCode();
        this.message = customExceptionType.getTypeDesc();
    }

}
