package com.imooc.mall.common;

import com.google.common.collect.Sets;
import javafx.scene.effect.SepiaTone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 描述：存放常量
 */
@Component
public class Constant {
    public static final String SALT = "uybY687]TBC9j,[0hkVBHy76"; //盐值
    public static final String IMOOC_MALL_USER = "imooc_mall_user"; //登录用户信息保存在Session，key值；
    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }
}
