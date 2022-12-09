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

    public interface SaleStatus {
        int NOT_SALE = 0;//商品下架的状态
        int SALE = 1;//商品上架的状态
    }

    /**
     * 购物车中的商品，是否被选中
     */
    public interface CartIsSelected {
        int UN_CHECKED = 0; //未选中
        int CHECKED = 1; //选中
    }
}
