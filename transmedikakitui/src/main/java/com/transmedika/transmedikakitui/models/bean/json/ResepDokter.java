package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.SerializedName;

public class ResepDokter {
     @SerializedName(value = "doctor_name", alternate = {"full_name"}) private String dokterName;
     @SerializedName("specialist")private String spesialist;
     @SerializedName("number")private String number;
    @SerializedName("image")private String image;

    public ResepDokter() {
    }

    public String getDokterName() {
        return dokterName;
    }

    public void setDokterName(String dokterName) {
        this.dokterName = dokterName;
    }

    public String getSpesialist() {
        return spesialist;
    }

    public void setSpesialist(String spesialist) {
        this.spesialist = spesialist;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
