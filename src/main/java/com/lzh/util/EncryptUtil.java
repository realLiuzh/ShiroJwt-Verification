package com.lzh.util;

import com.lzh.data.Constant;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.util.DigestUtils;


/**
 * 加密工具类
 *
 * @author 志昊的刘
 * @date 2021/12/21
 */
public class EncryptUtil {

    /**
     * sh1加密方法加密
     */
    public static String encryptor(String input) {
        BasicPasswordEncryptor basicPasswordEncryptor = new BasicPasswordEncryptor();
        return basicPasswordEncryptor.encryptPassword(input);
    }
}
