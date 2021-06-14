
package com.transmedika.transmedikakitui.models.bean.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Profile implements Parcelable {

    @Expose private String email;
    @SerializedName("full_name") private String fullName;
    @Expose private String gender;
    @SerializedName("phone_number") private String phoneNumber;
    @Expose private String status;
    @Expose private String uuid;
    @SerializedName("device_id") private String deviceId;
    @Expose private Ref ref;
    @SerializedName("profile_picture") private String profilePicture;
    @SerializedName("created_at") private Date createdAt;
    @SerializedName("updated_at") private Date updatedAt;

    //helper
    private List<String> deviceIds;

    public Profile() {
    }

    protected Profile(Parcel in) {
        deviceId = in.readString();
        uuid = in.readString();
        email = in.readString();
        fullName = in.readString();
        gender = in.readString();
        phoneNumber = in.readString();
        status = in.readString();
        ref = in.readParcelable(Ref.class.getClassLoader());
        profilePicture = in.readString();

        deviceIds = new ArrayList<>();
        in.readList(deviceIds, String.class.getClassLoader());
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Ref getRef() {
        return ref;
    }

    public void setRef(Ref ref) {
        this.ref = ref;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceId);
        dest.writeString(uuid);
        dest.writeString(email);
        dest.writeString(fullName);
        dest.writeString(gender);
        dest.writeString(phoneNumber);
        dest.writeString(status);
        dest.writeParcelable(ref, flags);
        dest.writeString(profilePicture);
        dest.writeList(deviceIds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile)) return false;
        Profile profil = (Profile) o;
        return getUuid().equals(profil.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }
}
