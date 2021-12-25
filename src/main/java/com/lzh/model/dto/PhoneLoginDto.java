package com.lzh.model.dto;

import com.lzh.anno.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 手机号+验证码登录
 *
 * @author 志昊的刘
 * @date 2021/12/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneLoginDto {

    @NotNull
    @Phone
    private String phone;

    @NotNull
    @Length(min = 6, max = 6)
    private String verCode;
}
