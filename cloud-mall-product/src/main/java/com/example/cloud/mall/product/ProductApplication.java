package com.example.cloud.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
@EnableFeignClients
@MapperScan(basePackages = "com.example.cloud.mall.product.model.dao")
public class ProductApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ProductApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
