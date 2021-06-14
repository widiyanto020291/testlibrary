package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.SerializedName;

public class SpaPasien {
    @SerializedName("spa")
    Spa spa;

    public SpaPasien() {
    }

    public Spa getSpa() {
        return spa;
    }

    public void setSpa(Spa spa) {
        this.spa = spa;
    }
}
