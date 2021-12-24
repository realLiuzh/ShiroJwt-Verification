package com.lzh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lzh.mapper")
public class ShiroJwtVerificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroJwtVerificationApplication.class, args);
    }

}
