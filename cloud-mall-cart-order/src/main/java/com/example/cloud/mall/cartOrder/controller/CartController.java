package com.example.cloud.mall.cartOrder.controller;

import com.example.cloud.mall.cartOrder.feign.UserFeignClient;
import com.example.cloud.mall.cartOrder.model.vo.CartVO;
import com.example.cloud.mall.cartOrder.service.CartService;
import com.example.cloud.mall.common.common.ApiRestResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @Autowired
    UserFeignClient userFeignClient;

    @GetMapping("/list")
    @ApiOperation("购物车列表")
    public ApiRestResponse list() {
        //找到现在的用户
        List<CartVO> cartList = cartService.list(userFeignClient.getUser().getId());
        for (CartVO cartVO : cartList) {
            System.out.println(cartVO);
        }
        return ApiRestResponse.success(cartList);
    }

    @PostMapping("/add")
    @ApiOperation("添加商品到购物车")
    public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count) {
        List<CartVO> cartVOList = cartService.add(userFeignClient.getUser().getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/update")
    @ApiOperation("添加商品到购物车")
    public ApiRestResponse update(@RequestParam Integer productId, @RequestParam Integer count) {
        List<CartVO> cartVOList = cartService.update(userFeignClient.getUser().getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/delete")
    @ApiOperation("删除商品到购物车")
    public ApiRestResponse delete(@RequestParam Integer productId) {
        System.out.println(userFeignClient.getUser().getId());
        List<CartVO> cartVOList = cartService.delete(userFeignClient.getUser().getId(), productId);

        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/select")
    @ApiOperation("选择购物车中某商品")
    public ApiRestResponse select(@RequestParam Integer productId, @RequestParam Integer selected) {
        List<CartVO> cartVOList = cartService.selectOrNot(userFeignClient.getUser().getId(), productId, selected);
        return ApiRestResponse.success(cartVOList);
    }

    @GetMapping("/selectAll")
    @ApiOperation("选择购物车中所有商品")
    public ApiRestResponse selectAll(@RequestParam Integer selected) {
        List<CartVO> cartVOList = cartService.selectAllOrNot(userFeignClient.getUser().getId(), selected);
        return ApiRestResponse.success(cartVOList);
    }
}
