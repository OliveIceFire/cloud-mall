package com.example.cloud.mall.product.service;


import com.example.cloud.mall.product.model.entity.Category;
import com.example.cloud.mall.product.model.request.AddCategoryReq;
import com.example.cloud.mall.product.model.vo.CategoryVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CategoryService {
    void add(AddCategoryReq addCategoryReq);

    void update(Category updateCategoryReq);

    void delete(Integer id);

    PageInfo<Category> listForAdmin(Integer pageNum, Integer pageSize);

    //    @Cacheable(value = "listCategoryForCustomer")
    List<CategoryVO> listCategoryForCustomer(Integer parentId);
}
