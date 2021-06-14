
package com.transmedika.transmedikakitui.models.bean.json.param;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SmsVerificationParam implements Parcelable {

    public final static int UBAH_PHONE_TYPE = 1;
    public final static int UBAH_PASSWORD_TYPE = 2;
//    public final static int RESET_PASSWORD_TYPE = 3;

    @Expose final private String phone_number;
    @Expose private String verification_code;
    @Expose(serialize = false, deserialize = false) private String cookie;
    @SerializedName("ref_type") String refType;
    @SerializedName("is_type") int isType = 0;
    @SerializedName("password") String password;
    @SerializedName("hash") String hash;

    public SmsVerificationParam(String phone_number) {
        this.phone_number = phone_number;
    }

    protected SmsVerificationParam(Parcel in) {
        phone_number = in.readString();
        verification_code = in.readString();
        refType = in.readString();
        isType = in.readInt();
        password = in.readString();
        hash = in.readString();
        cookie = in.readString();
    }

    public static final Creator<SmsVerificationParam> CREATOR = new Creator<SmsVerificationParam>() {
        @Override
        public SmsVerificationParam createFromParcel(Parcel in) {
            return new SmsVerificationParam(in);
        }

        @Override
        public SmsVerificationParam[] newArray(int size) {
            return new SmsVerificationParam[size];
        }
    };

    public String getPhone_number() {
        return phone_number;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public int getIsType() {
        return isType;
    }

    public void setIsType(int isType) {
        this.isType = isType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(phone_number);
        parcel.writeString(verification_code);
        parcel.writeString(refType);
        parcel.writeInt(isType);
        parcel.writeString(password);
        parcel.writeString(hash);
        parcel.writeString(cookie);
    }
}
