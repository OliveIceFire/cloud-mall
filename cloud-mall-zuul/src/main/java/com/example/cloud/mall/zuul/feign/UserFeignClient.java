package com.example.cloud.mall.zuul.feign;

import com.example.cloud.mall.user.model.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "cloud-mall-user")
public interface UserFeignClient {

    @PostMapping("/checkAdminRole")
    Boolean checkAdminRole(@RequestBody User user);
}
