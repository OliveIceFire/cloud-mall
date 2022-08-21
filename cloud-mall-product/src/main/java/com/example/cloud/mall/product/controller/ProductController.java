package com.example.cloud.mall.product.controller;


import com.example.cloud.mall.common.common.ApiRestResponse;
import com.example.cloud.mall.product.model.entity.Product;
import com.example.cloud.mall.product.model.request.ProductListReq;
import com.example.cloud.mall.product.service.ProductService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @ApiOperation("商品详情")
    @GetMapping("product/detail")
    public ApiRestResponse detail(@RequestParam Integer id) {
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }

    @ApiOperation("商品列表")
    @GetMapping("product/list")
    public ApiRestResponse list(@RequestBody ProductListReq productListReq) {
        PageInfo pageInfo = productService.list(productListReq);
        return ApiRestResponse.success(pageInfo);
    }


}
