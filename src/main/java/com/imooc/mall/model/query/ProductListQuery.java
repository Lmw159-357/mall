package com.imooc.mall.model.query;

import java.util.List;

/**
 * 描述：查询商品列表的Query
 */
public class ProductListQuery {

    private String keyword;//搜索关键词，这个条件
    private List<Integer> categoryIds;//商品分类，这个条件

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}