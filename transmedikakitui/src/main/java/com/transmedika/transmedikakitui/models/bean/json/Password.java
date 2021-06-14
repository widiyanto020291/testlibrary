package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.SerializedName;

public class Password {
    @SerializedName("old_password")
    private String oldPassword;

    @SerializedName("password")
    private String password;

    @SerializedName("pin")
    private String pin;

    @SerializedName("new_pin")
    private String newPin;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }
}
