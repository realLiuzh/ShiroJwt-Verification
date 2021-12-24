package com.lzh.data;

import lombok.AllArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 自定义JwtToken
 *
 * @author 志昊的刘
 * @date 2021/12/21
 */

@AllArgsConstructor
public class JwtToken implements AuthenticationToken {

    private String token;

    /**
     * 注意：实现的两个方法全部返回token。(对于理解ShiroJwt整合很重要)
     */
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
