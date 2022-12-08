package com.imooc.mall.utils;

import com.imooc.mall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 描述：通过MD5来实现加密效果；
 */
public class MD5Utils {

    public static String getMD5String(String strValue) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return Base64.encodeBase64String(md5.digest((strValue + Constant.SALT).getBytes()));
    }

    //及时的测试
    public static void main(String[] args) {
        String md5 = null;
        try {
            md5 = getMD5String("12345");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println(md5);

    }
}