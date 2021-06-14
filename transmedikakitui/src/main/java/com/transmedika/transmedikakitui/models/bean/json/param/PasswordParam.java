package com.transmedika.transmedikakitui.models.bean.json.param;

import com.google.gson.annotations.SerializedName;

public class PasswordParam {

    @SerializedName("old_password")
    private final String oldPassword;

    @SerializedName("password")
    private final String password;

    @SerializedName("password_confirmation")
    private final String passwordConfirmation;

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("phone_number")
    private String phoneNumber;

    public PasswordParam(String password) {
        this(null, password, null);
    }

    public PasswordParam(String password, String passwordConfirmation) {
        this(null, password, passwordConfirmation);
    }

    public PasswordParam(String oldPassword, String password, String passwordConfirmation) {
        this.oldPassword = oldPassword;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
