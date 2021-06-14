package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("id") Long id;

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
