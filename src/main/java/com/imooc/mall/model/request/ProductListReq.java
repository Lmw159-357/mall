package com.imooc.mall.model.request;

import io.swagger.models.auth.In;

/**
 * 在开发【前台的商品列表】接口时，用该类的对象接收参数
 */
public class ProductListReq {

    private String orderBy;//排序方式
    private Integer categoryId;//商品分类Id
    private String keyword;//搜索关键词
    private Integer pageNum = 1;//页数
    private Integer pageSize = 10;//每页条数

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}