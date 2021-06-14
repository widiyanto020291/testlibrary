package com.transmedika.transmedikakitui.models.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JawabanMultiple extends BaseJawaban {
    @SerializedName("jawabans") private List<JawabanDetail> jawabans;

    public List<JawabanDetail> getDetailJawabans() {
        return jawabans;
    }

    public void setDetailJawabans(List<JawabanDetail> jawabans) {
        this.jawabans = jawabans;
    }

    @Override
    public String toString() {
        return "jawabans=" + jawabans;
    }
}
