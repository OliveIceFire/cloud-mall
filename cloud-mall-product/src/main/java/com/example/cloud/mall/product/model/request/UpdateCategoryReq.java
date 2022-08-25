package com.example.cloud.mall.product.model.request;


import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class UpdateCategoryReq {

    @NotNull(message = "分类id不能为空")
    private Integer id;

    @Size(min = 2, max = 5)
    private String name;
    @Max(3)
    private Integer type;
    private Integer parentId;
    private Integer orderNum;

    public UpdateCategoryReq() {
        name = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "UpdateCategoryReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                '}';
    }
}
