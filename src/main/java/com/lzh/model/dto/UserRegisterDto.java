package com.lzh.model.dto;

import com.lzh.anno.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.lzh.data.Constant.*;

/**
 * @author 志昊的刘
 * @date 2021/12/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {

    @NotNull
    @Length(min = 6, max = 12)
    private String username;

    @NotNull
    @Length(min = 6, max = 20)
    private String password;

    /**
     * 验证码
     */
    @NotNull
    @Length(min = 6, max = 6)
    private String verCode;

    /**
     * 手机号
     */
    @NotNull
    @Phone
    private String phone;
}
