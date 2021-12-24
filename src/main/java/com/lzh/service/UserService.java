package com.lzh.service;

public interface UserService {
    String loginUser(String username,String password);

    void registerUser(String username,String password);
}
