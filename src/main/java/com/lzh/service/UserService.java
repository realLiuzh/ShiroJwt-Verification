package com.lzh.service;

public interface UserService {
    String loginUser(String username,String password);

    void registerUser(String username,String password);

    void checkVerCode(String phone, String verCode);

    void sendVerCode(String phone);
}
