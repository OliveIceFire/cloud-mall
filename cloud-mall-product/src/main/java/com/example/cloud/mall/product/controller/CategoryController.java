package com.example.cloud.mall.product.controller;


import com.example.cloud.mall.common.common.ApiRestResponse;
import com.example.cloud.mall.product.model.entity.Category;
import com.example.cloud.mall.product.model.request.AddCategoryReq;
import com.example.cloud.mall.product.model.request.UpdateCategoryReq;
import com.example.cloud.mall.product.service.CategoryService;
import com.example.cloud.mall.product.model.vo.CategoryVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;


@RestController
public class CategoryController {

    @Autowired
    CategoryService categoryService;


    @ApiOperation("添加分类")
    @PostMapping("admin/category/add")
    public ApiRestResponse addCategory(@Valid @RequestBody AddCategoryReq req, HttpSession session) {

        categoryService.add(req);
        return ApiRestResponse.success();
    }

    @ApiOperation("更新分类")
    @PostMapping("admin/category/update")
    public ApiRestResponse updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq, HttpSession session) {
        Category category = new Category();
        BeanUtils.copyProperties(updateCategoryReq, category);
        categoryService.update(category);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台删除目录")
    @PostMapping("admin/category/delete")
    public ApiRestResponse deleteCategory(@RequestParam("id") Integer id) {
        categoryService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台获取目录列表")
    @GetMapping("admin/category/list")
    public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("前台获取目录列表")
    @GetMapping("category/list")
    public ApiRestResponse listCategoryForCustomer() {
        List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer(0);
        return ApiRestResponse.success(categoryVOS);
    }

}
