package com.lzh.service.impl;

import com.lzh.exception.CustomExceptionType;
import com.lzh.mapper.UserMapper;
import com.lzh.model.po.User;
import com.lzh.service.UserService;
import com.lzh.util.AssertUtil;
import com.lzh.util.EncryptUtil;
import com.lzh.util.JwtUtil;
import com.lzh.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.lzh.exception.CustomExceptionType.NO_ACCOUNT;
import static com.lzh.exception.CustomExceptionType.VER_CODE_ERROR;

/**
 * @author 志昊的刘
 * @date 2021/12/21
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 用户登录
     */
    @Override
    public String loginWithUsernamePassword(String username, String password) {
        User user = userMapper.selectByUsername(username);
        AssertUtil.isTrue(user == null, CustomExceptionType.ACCOUNT_ERROR);
        EncryptUtil.checkPassword(password, user.getPassword());
        return JwtUtil.getToken(user.getUsername());
    }


    /**
     * 用户注册
     */
    @Override
    public void registerUser(String username, String password, String phone) {

        int flag = userMapper.checkUserNameIsValid(username);
        AssertUtil.isTrue(flag != 0, CustomExceptionType.USERNAME_USED);
        User user = new User(username, EncryptUtil.encryptor(password), phone, new Date());
        userMapper.insert(user);
    }

    /**
     * 检查用户名的合法性
     */
    @Override
    public void checkVerCode(String phone, String verCode) {
        AssertUtil.isTrue(!verCode.equals(redisTemplate.boundValueOps(phone).get()), VER_CODE_ERROR);
    }

    /**
     * 向手机号发送验证码码
     */
    @Override
    public void sendVerCode(String phone) {
        String captcha = generateCaptcha();
        redisTemplate.boundValueOps(phone).set(captcha, 5, TimeUnit.MINUTES);
        SmsUtil.sendSms(phone, captcha);
    }

    /**
     * 手机-验证码方式登录登录
     */
    @Override
    public String loginWithPhone(String phone, String verCode) {
        checkVerCode(phone, verCode);
        User user = userMapper.selectByPhone(phone);
        AssertUtil.isTrue(user == null, NO_ACCOUNT);
        return JwtUtil.getToken(user.getUsername());
    }

    /**
     * 生成随机验证码
     */
    private String generateCaptcha() {
        Random random = new Random();
        return String.valueOf(random.nextInt(900000) + 100000);
    }
}
