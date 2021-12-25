package com.lzh.service;

public interface UserService {
    String loginWithUsernamePassword(String username, String password);

    void registerUser(String username,String password,String phone);

    void checkVerCode(String phone, String verCode);

    void sendVerCode(String phone);

    String loginWithPhone(String phone, String verCode);
}
