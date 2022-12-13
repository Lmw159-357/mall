package com.imooc.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 描述：【增加目录分类】接口的参数类；
 */
public class AddCategoryReq {
    @Size(min = 2,max = 5)
    @NotNull(message = "name目录名不能为null")
    private String name;//目录名

    @NotNull(message = "type目录级别不能为null")
    @Max(3)
    private Integer type;//目录级别

    @NotNull(message = "parentId上一级目录id不能为null")
    private Integer parentId;//上一级目录的id

    @NotNull(message = "orderNum目录排序顺序不能为null")
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

    @Override
    public String toString() {
        return "AddCategoryReq{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                '}';
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}