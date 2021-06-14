package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.SerializedName;

public class ResepPasien {
    @SerializedName("patient_name")private String patientName;
    @SerializedName("age") private String age;
    @SerializedName("image")private String image;

    public ResepPasien() {
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
