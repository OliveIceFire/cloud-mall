package com.example.cloud.mall.product.controller;


import com.example.cloud.mall.common.common.ApiRestResponse;
import com.example.cloud.mall.product.model.entity.Product;
import com.example.cloud.mall.product.model.request.ProductListReq;
import com.example.cloud.mall.product.service.ProductService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("商品详情")
    @GetMapping("product/detailForFeign")
    public Product detailForFeign(@RequestParam Integer id) {
        return productService.detail(id);
    }

    @PostMapping("product/updateStock")
    public void updateStock(@RequestParam Integer productId, @RequestParam Integer stock) {
        productService.updateStock(productId, stock);
    }


}
