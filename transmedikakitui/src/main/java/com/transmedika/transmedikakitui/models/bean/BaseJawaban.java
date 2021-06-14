package com.transmedika.transmedikakitui.models.bean;

import com.google.gson.annotations.SerializedName;

public class BaseJawaban {
    @SerializedName("id_pertanyaan") private Long idPertanyaan;
    @SerializedName("pertanyaan") private String pertanyaan;
    @SerializedName("type_view") private String tipeView;

    public Long getIdPertanyaan() {
        return idPertanyaan;
    }

    public void setIdPertanyaan(Long idPertanyaan) {
        this.idPertanyaan = idPertanyaan;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getTipeView() {
        return tipeView;
    }

    public void setTipeView(String tipeView) {
        this.tipeView = tipeView;
    }

    @Override
    public String toString() {
        return "pertanyaan='" + pertanyaan + '\'';
    }
}
