package com.transmedika.transmedikakitui.models.bean.json.param;

import com.google.gson.annotations.SerializedName;

public class NoPonselUpdateParam {

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("password")
    private String password;

    public NoPonselUpdateParam() {}

    public NoPonselUpdateParam(String nomorPonsel, String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
