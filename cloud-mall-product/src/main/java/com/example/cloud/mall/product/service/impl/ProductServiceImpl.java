package com.example.cloud.mall.product.service.impl;


import com.example.cloud.mall.common.common.Constant;
import com.example.cloud.mall.common.exception.MallException;
import com.example.cloud.mall.common.exception.MallExceptionEnum;
import com.example.cloud.mall.common.util.ExcelUtil;
import com.example.cloud.mall.product.model.dao.ProductMapper;
import com.example.cloud.mall.product.model.entity.Product;
import com.example.cloud.mall.product.model.query.ProductListQuery;
import com.example.cloud.mall.product.model.request.AddProductReq;
import com.example.cloud.mall.product.model.request.ProductListReq;
import com.example.cloud.mall.product.service.CategoryService;
import com.example.cloud.mall.product.service.ProductService;
import com.example.cloud.mall.product.model.vo.CategoryVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.util.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryService categoryService;

    @Override
    public void add(AddProductReq addProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.insertSelective(product);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public void update(Product updateProduct) {
        Product productOld = productMapper.selectByName(updateProduct.getName());
        if (productOld != null && !productOld.getId().equals(updateProduct.getId())) {
            System.out.println("?????????????????????");
            System.out.println("????????????id???????????????");
            throw new MallException(MallExceptionEnum.UPLOAD_FAILED);
        }
        int count = productMapper.updateByPrimaryKeySelective(updateProduct);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id) {
        Product productOld = productMapper.selectByPrimaryKey(id);
        if (productOld == null) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
        productMapper.batchUpdateSellStatus(ids, sellStatus);
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectListForAdmin();
        PageInfo pageInfo = new PageInfo(products);
        return pageInfo;
    }

    @Override
    public Product detail(Integer id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Product> list(ProductListReq productListReq) {
//        ProductListReq: keyword, categoryId, orderBy;
//        productListQuery: keyword, categoryIds;

        ProductListQuery productListQuery = new ProductListQuery();
        //keyword
        if (!StringUtils.isNullOrEmpty(productListReq.getKeyword())) {
            String keyword = "%" + productListReq.getKeyword() + "%";
            productListQuery.setKeyword(keyword);
        }
        //categoryId
        if (productListReq.getCategoryId() != null) {
            List<Integer> ids = new ArrayList<>();
            List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer(productListReq.getCategoryId());
            for (CategoryVO categoryVO : categoryVOS) {
                ids.add(categoryVO.getId());
            }
            productListQuery.setCategoryIds(ids);
        }
        //???????????? orderBy
        String orderBy = productListReq.getOrderBy();
        if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize(), orderBy);
        } else {
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize());
        }
        List<Product> productList = productMapper.selectList(productListQuery);
        return new PageInfo<>(productList);

    }


    @Override
    public void addProductByExcel(File destFile) throws IOException {
        List<Product> products = readProductsFromExcel(destFile);
        for (Product product : products) {
            Product productOld = productMapper.selectByName(product.getName());
            if (productOld != null) {
                throw new MallException(MallExceptionEnum.NAME_EXISTED);
            }
            int count = productMapper.insertSelective(product);
            if (count == 0) {
                throw new MallException(MallExceptionEnum.CREATE_FAILED);
            }
        }
    }

    private List<Product> readProductsFromExcel(File excelFile) throws IOException {
        ArrayList<Product> listProducts = new ArrayList<>();


        FileInputStream inputStream = new FileInputStream(excelFile);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            Product product = new Product();
            while (cellIterator.hasNext()) {
                Cell nextCell = cellIterator.next();
                int columnIndex = nextCell.getColumnIndex();
                switch (columnIndex) {
                    case 0:
                        product.setName((String) ExcelUtil.getCellValue(nextCell));
                        break;
                    case 1:
                        product.setImage((String) ExcelUtil.getCellValue(nextCell));
                        break;
                    case 2:
                        product.setDetail((String) ExcelUtil.getCellValue(nextCell));
                        break;
                    case 3:
                        //??????????????????Double??????
                        Double cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                        product.setCategoryId(cellValue.intValue());
                        break;
                    case 4:
                        //??????????????????Double??????
                        cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                        product.setPrice(cellValue.intValue());
                        break;
                    case 5:
                        //??????????????????Double??????
                        cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                        product.setStock(cellValue.intValue());
                        break;
                    case 6:
                        //??????????????????Double??????
                        cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                        product.setStatus(cellValue.intValue());
                        break;

                }
            }
            listProducts.add(product);
        }
        workbook.close();
        inputStream.close();
        return listProducts;
    }
    @Override
    public void updateStock(Integer productId, Integer stock) {
        Product product = new Product();
        product.setId(productId);
        product.setStock(stock);
        productMapper.updateByPrimaryKeySelective(product);
    }

}
