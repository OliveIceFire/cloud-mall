package com.example.cloud.mall.product.controller;

import com.example.cloud.mall.common.common.ApiRestResponse;
import com.example.cloud.mall.common.common.Constant;
import com.example.cloud.mall.common.common.ValidList;
import com.example.cloud.mall.common.exception.MallException;
import com.example.cloud.mall.common.exception.MallExceptionEnum;
import com.example.cloud.mall.product.common.ProductConstant;
import com.example.cloud.mall.product.model.entity.Product;
import com.example.cloud.mall.product.model.request.AddProductReq;
import com.example.cloud.mall.product.model.request.UpdateProductReq;
import com.example.cloud.mall.product.service.ProductService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
//@Validated
public class ProductAdminController {

    @Autowired
    ProductService productService;

    @Value("${file.upload.uri}")
    String uri;

    @PostMapping("admin/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) {
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    @PostMapping("admin/upload/file")
    public ApiRestResponse upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) {
        String filename = file.getOriginalFilename();
        String suffixName = filename.substring(filename.lastIndexOf("."));
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid + suffixName;
        File fileDirectory = new File(ProductConstant.FILE_UPLOAD_DIR);
        File destFile = new File(ProductConstant.FILE_UPLOAD_DIR + newFileName);
        createFile(file, fileDirectory, destFile);
        String address = uri;
        return ApiRestResponse.success("http://" + address + "/images/" + newFileName);
//        try {
//            return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/images/" + newFileName);
//        } catch (URISyntaxException e) {
////            throw new RuntimeException(e);
//            return ApiRestResponse.error(MallExceptionEnum.UPLOAD_FAILED);
//        }

    }

    private URI getHost(URI uri) {
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }


    @ApiOperation("??????????????????")
    @PostMapping("/admin/product/update")
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        productService.update(product);
        return ApiRestResponse.success();
    }

    @ApiOperation("??????????????????")
    @PostMapping("/admin/product/delete")
    public ApiRestResponse deleteProduct(@Valid @RequestBody Integer id) {
        productService.delete(id);
        return ApiRestResponse.success();
    }


    @ApiOperation("???????????????")
    @PostMapping("/admin/product/batchUpdateSellStatus")
    public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids, @RequestParam Integer sellStatus) {
        productService.batchUpdateSellStatus(ids, sellStatus);
        return ApiRestResponse.success();
    }

    @ApiOperation("??????????????????")
    @GetMapping("/admin/product/list")
    public ApiRestResponse list(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageInfo = productService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("??????????????????")
    @PostMapping("/admin/upload/product")
    public ApiRestResponse uploadProduct(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid + suffixName;
        //????????????
        File fileDirectory = new File(ProductConstant.FILE_UPLOAD_DIR);
        File destFile = new File(ProductConstant.FILE_UPLOAD_DIR + newFileName);
        createFile(multipartFile, fileDirectory, destFile);
        productService.addProductByExcel(destFile);
        return ApiRestResponse.success();
    }

    @ApiOperation("??????????????????")
    @PostMapping("/admin/upload/image")
    public ApiRestResponse uploadImage(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        assert filename != null;
        String suffixName = filename.substring(filename.lastIndexOf("."));
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString().substring(0, 5) + "_image_" + suffixName;
        File fileDirectory = new File(ProductConstant.FILE_UPLOAD_DIR);
        File destFile = new File(ProductConstant.FILE_UPLOAD_DIR + newFileName);
        createFile(file, fileDirectory, destFile);
        Thumbnails.of(destFile).size(Constant.IMAGE_SIZE, Constant.IMAGE_SIZE).
                watermark(Positions.BOTTOM_RIGHT,
//                        ImageIO.read(new File(Constant.FILE_UPLOAD_DIR + Constant.WATER_MARK_JPG)
                        ImageIO.read(new File("image/watermark.jpg")

                        ),
                        Constant.IMAGE_OPACITY)
                .toFile(ProductConstant.FILE_UPLOAD_DIR + newFileName);
        String address = uri;
        return ApiRestResponse.success("http://" + address + "/product/images/" + newFileName);
//        try {
//            return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/images/" + newFileName);
//        } catch (URISyntaxException e) {
////            throw new RuntimeException(e);
//            return ApiRestResponse.error(MallExceptionEnum.UPLOAD_FAILED);
//        }
    }

    private void createFile(MultipartFile multipartFile, File fileDirectory, File destFile) {
        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdir()) {
                throw new MallException(MallExceptionEnum.MKDIR_FAILED);
            }
        }
        try {
            multipartFile.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("????????????????????????")
    @PostMapping("/admin/product/batchUpdate")
    public ApiRestResponse batchUpdateProduct(@Valid @RequestBody ValidList<UpdateProductReq> updateProductReqList) {
//        Product product = new Product();
//        BeanUtils.copyProperties(updateProductReq, product);
//        productService.update(product);
        return ApiRestResponse.success();
    }
}
