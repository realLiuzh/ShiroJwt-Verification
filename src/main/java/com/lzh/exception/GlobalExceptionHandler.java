package com.lzh.exception;

import com.lzh.data.Response;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

import static com.lzh.exception.CustomExceptionType.*;

/**
 * 全局统一异常处理
 *
 * @author 志昊的刘
 * @date 2021/12/21
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常
     */
    @ExceptionHandler(CustomException.class)
    public Response handlerCustomException(CustomException e) {
        return Response.error(e);
    }

    /**
     * 权限异常
     */
    @ExceptionHandler(AuthorizationException.class)
    public Response handlerAuthorizationException(AuthorizationException e) {
        return Response.error(new CustomException(NO_PERMISSION));
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder("参数校验失败:");
        BindingResult bindingResult = e.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append(", ");
        }
        /**
         * CustomExceptionType code和 typeDesc不是固定的！！！
         */
        return Response.error(new CustomException(PARAM_ERROR.getCode(), sb.toString()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Response handleConstraintViolationException(ConstraintViolationException ex) {
        return Response.error(new CustomException(PARAM_ERROR.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        e.printStackTrace();
        return Response.error(new CustomException(SYSTEM_INNER_ERROR));
    }


}
