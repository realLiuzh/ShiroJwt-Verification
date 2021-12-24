package com.lzh.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

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
    @Length(min = 6,max = 12)
    private String username;

    @NotNull
    @Length(min = 6,max = 20)
    private String password;
}