package com.example.cloud.mall.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableRedisHttpSession
@MapperScan(basePackages = "com.example.cloud.mall.user.model.dao")
public class UserApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(UserApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
