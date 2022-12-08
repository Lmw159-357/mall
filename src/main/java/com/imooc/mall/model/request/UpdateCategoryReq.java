package com.imooc.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 描述：【更新目录分类】接口的参数类；
 */
public class UpdateCategoryReq {
    @NotNull(message = "目录id不能为null")
    private Integer id;

    @Size(min = 2, max = 5)
    private String name;//目录名

    @Max(3)
    private Integer type;//目录级别

    private Integer parentId;//上一级目录的id

    private Integer orderNum;//同级目录的排序顺序


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}