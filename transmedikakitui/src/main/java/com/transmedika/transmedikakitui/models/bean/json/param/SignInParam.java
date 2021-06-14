
package com.transmedika.transmedikakitui.models.bean.json.param;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignInParam {

    public static final String REF_TYPE_DOCTOR = "doctor";
    public static final String REF_TYPE_PATIENT = "patient";

    @SerializedName(value = "email", alternate = {"username"}) private String username;
    @Expose private String password;
    @SerializedName("ref_type") String refType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }
}
