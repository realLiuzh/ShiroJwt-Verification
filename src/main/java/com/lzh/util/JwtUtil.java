package com.lzh.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.lzh.data.Constant;
import org.apache.shiro.authc.AuthenticationException;

import java.util.Date;

/**
 * Jwt工具类
 *
 * @author 志昊的刘
 * @date 2021/12/21
 */
public class JwtUtil {

    /**
     * 生成token
     * claim:
     * username -> RealUsername
     * exp      -> 有效时间5天
     * 加密算法   -> HMAC256
     */
    public static String getToken(String username) {
        //username + 私钥 = 最终的secret
        Algorithm algorithm = Algorithm.HMAC256(username + Constant.JWT_PRIVATE_KEY);
        return JWT.create()
                //jwt payload
                .withClaim(Constant.JWT_CLAIM_KEY, username)
                //jwt 有效时间
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(Constant.JWT_EXPIRE_TIME) * 1000))
                .sign(algorithm);
    }

    /**
     * 检验token
     */
    public static void verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(getClaim(token, Constant.JWT_CLAIM_KEY) + Constant.JWT_PRIVATE_KEY);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            jwtVerifier.verify(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationException();
        }
    }

    /**
     * 获得Token中的荷载信息  (无需解码)
     */
    public static String getClaim(String token, String claimKey) {
        return JWT.decode(token).getClaim(claimKey).asString();
    }
}
