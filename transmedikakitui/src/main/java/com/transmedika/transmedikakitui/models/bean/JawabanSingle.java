package com.transmedika.transmedikakitui.models.bean;

import com.google.gson.annotations.SerializedName;

public class JawabanSingle extends BaseJawaban {
    @SerializedName("jawaban") private JawabanDetail jawabans;

    public JawabanDetail getDetailJawaban() {
        return jawabans;
    }

    public void setDetailJawaban(JawabanDetail jawabans) {
        this.jawabans = jawabans;
    }
}
