package com.example.cloud.mall.cartOrder.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CartOrderConstant {
    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.path}")
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }
}
