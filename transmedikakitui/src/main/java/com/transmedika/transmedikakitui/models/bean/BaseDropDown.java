package com.transmedika.transmedikakitui.models.bean;

import java.util.Objects;

public class BaseDropDown {
    private String name;
    private String id;
    private Integer img;

    public BaseDropDown(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public BaseDropDown(String name, String id, Integer img) {
        this.name = name;
        this.id = id;
        this.img = img;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseDropDown)) return false;
        BaseDropDown that = (BaseDropDown) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
