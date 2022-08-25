package com.example.cloud.mall.cartOrder.feign;

import com.example.cloud.mall.user.model.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "cloud-mall-user")
public interface UserFeignClient {

    @GetMapping("/product/getUser")
    User getUser();

    @GetMapping("/product/checkAdminRole")
    @PostMapping("/checkAdminRole")
    Boolean checkAdminRole(@RequestBody User user);
}
