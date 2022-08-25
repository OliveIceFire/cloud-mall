package com.example.cloud.mall.product.service;


import com.example.cloud.mall.product.model.entity.Product;
import com.example.cloud.mall.product.model.request.AddProductReq;
import com.example.cloud.mall.product.model.request.ProductListReq;
import com.github.pagehelper.PageInfo;

import java.io.File;
import java.io.IOException;

public interface ProductService {

    void add(AddProductReq addProductReq);

    void update(Product updateProduct);

    void delete(Integer id);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    Product detail(Integer id);

    PageInfo list(ProductListReq productListReq);

    void addProductByExcel(File destFile) throws IOException;

    void updateStock(Integer productId, Integer stock);
}
