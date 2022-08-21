package com.example.cloud.mall.common.util;


import com.example.cloud.mall.common.common.Constant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MD5Utils {
    public static String getMD5Str(String strValue) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return Base64.getEncoder().encodeToString(md5.digest((strValue + Constant.SALT).getBytes()));
    }

}
