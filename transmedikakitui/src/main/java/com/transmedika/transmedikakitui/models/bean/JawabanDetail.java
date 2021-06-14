package com.transmedika.transmedikakitui.models.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class JawabanDetail {
    @SerializedName("id_jawaban") private Long idJawaban;
    @SerializedName("jawaban") private String jawaban;

    public Long getIdJawaban() {
        return idJawaban;
    }

    public void setIdJawaban(Long idJawaban) {
        this.idJawaban = idJawaban;
    }

    public String getJawaban() {
        return jawaban;
    }

    public void setJawaban(String jawaban) {
        this.jawaban = jawaban;
    }

    @Override
    public String toString() {
        return String.format("id_jawaban: %s, jawaban: %s", idJawaban, jawaban);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JawabanDetail)) return false;
        JawabanDetail that = (JawabanDetail) o;
        return getIdJawaban().equals(that.getIdJawaban());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdJawaban());
    }
}
