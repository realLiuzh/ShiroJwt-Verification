package com.lzh.data;

/**
 * 常量
 *
 * @author 志昊的刘
 * @date 2021/12/21
 */
public interface Constant {

    /**
     * Jwt 私钥
     */
    String JWT_PRIVATE_KEY = "RealLzh";

    /**
     * JWT payload中的key
     */
    String JWT_CLAIM_KEY = "username";

    /**
     * JWT 有效时间(s) 5天
     */
    String JWT_EXPIRE_TIME = "432000";

    /**
     * HTTP请求头中token位置
     */
    String HTTP_HEADER_TOKEN = "token";



}
