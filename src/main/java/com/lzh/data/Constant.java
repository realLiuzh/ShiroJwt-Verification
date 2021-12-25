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

    /**
     * sms-secretId
     */
    String SMS_SECRET_ID = "lzhAKIDPcZFn68tOcZFemeFZdRxCBtLWWGjvqUr";

    /**
     * sms-secretKey
     */
    String SMS_SECRET_KEY = "lzh7xYsugCSWXHm0yf5F8kALFsEJ8gxykFT";

    /**
     * sms-sdkAppId
     */
    String SMS_SDK_APP_ID = "lzh1400609863";

    /**
     * sms-signName
     */
    String SMS_SIGN_NAME = "机务维修常识";

    /**
     * sms-templateId
     */
    String SMS_TEMPLATE_ID = "lzh1236911";


}
