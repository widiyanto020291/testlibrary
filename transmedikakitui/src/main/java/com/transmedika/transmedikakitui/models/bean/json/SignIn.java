
package com.transmedika.transmedikakitui.models.bean.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SignIn extends RealmObject implements Parcelable {

    @SerializedName(value = "AUTH-TOKEN", alternate = {"token"})
    private String aUTHTOKEN;
    @Expose
    private String email;

    @PrimaryKey
    @Expose
    private String id;

    @Expose
    private String name;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("object_id")
    private String objectId;

    @SerializedName("device_id")
    private String deviceId;

    @SerializedName("token_parse")
    private String tokenParse;

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("pin")
    private boolean pinStatus;

    @SerializedName("profile_picture")
    private String profilePicture;

    @SerializedName("nik")
    private String nik;

    private boolean isSosmed = false;

    public SignIn() {
    }

    protected SignIn(Parcel in) {
        aUTHTOKEN = in.readString();
        email = in.readString();
        id = in.readString();
        name = in.readString();
        phoneNumber = in.readString();
        objectId = in.readString();
        deviceId = in.readString();
        tokenParse = in.readString();
        uuid = in.readString();
        pinStatus = in.readInt() != 0;
        profilePicture = in.readString();
        nik = in.readString();
        isSosmed = in.readInt() != 0;
    }

    public static final Creator<SignIn> CREATOR = new Creator<SignIn>() {
        @Override
        public SignIn createFromParcel(Parcel in) {
            return new SignIn(in);
        }

        @Override
        public SignIn[] newArray(int size) {
            return new SignIn[size];
        }
    };

    public String getaUTHTOKEN() {
        return aUTHTOKEN;
    }

    public void setaUTHTOKEN(String aUTHTOKEN) {
        this.aUTHTOKEN = aUTHTOKEN;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTokenParse() {
        return tokenParse;
    }

    public void setTokenParse(String tokenParse) {
        this.tokenParse = tokenParse;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean getPinStatus() {
        return pinStatus;
    }

    public void setPinStatus(boolean pinStatus) {
        this.pinStatus = pinStatus;
    }

    public boolean isPinStatus() {
        return pinStatus;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public boolean isSosmed() {
        return isSosmed;
    }

    public void setSosmed(boolean sosmed) {
        isSosmed = sosmed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(aUTHTOKEN);
        dest.writeString(email);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(objectId);
        dest.writeString(deviceId);
        dest.writeString(tokenParse);
        dest.writeString(uuid);
        dest.writeInt(pinStatus ? 1 : 0);
        dest.writeString(profilePicture);
        dest.writeString(nik);
        dest.writeInt(isSosmed ? 1 : 0);
    }


}
