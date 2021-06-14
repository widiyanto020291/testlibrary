
package com.transmedika.transmedikakitui.models.bean.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Doctor implements Parcelable {

    @Expose private String description;
    @SerializedName("device_id") private String deviceId;
    @Expose private String email;
    @Expose private String experience;
    @SerializedName("full_name") private String fullName;
    @SerializedName("no_str") private String noStr;
    @SerializedName("object_id") private String objectId;
    @SerializedName("phone_number") private String phoneNumber;
    @SerializedName("province_id") private Long provinceId;
    @Expose private Long rates;
    @Expose private String rating;
    @SerializedName("regency_id") private Long regencyId;
    @Expose private String specialist;
    @SerializedName("specialist_slug") private String specialistSlug;
    //@SerializedName("start_experience") private Date startExperience;
    @Expose private String status;
    @SerializedName("status_docter") private String statusDocter;
    @Expose private String uuid;
    @SerializedName("profile_picture") private String profileDoctor;
    @Expose private List<Alumni> educations;
    @Expose private List<Facility> facilities;

    public Doctor() {
    }

    public Doctor(String uuid) {
        this.uuid = uuid;
    }

    protected Doctor(Parcel in) {
        description = in.readString();
        deviceId = in.readString();
        email = in.readString();
        experience = in.readString();
        fullName = in.readString();
        noStr = in.readString();
        objectId = in.readString();
        phoneNumber = in.readString();
        if (in.readByte() == 0) {
            provinceId = null;
        } else {
            provinceId = in.readLong();
        }
        if (in.readByte() == 0) {
            rates = null;
        } else {
            rates = in.readLong();
        }
        rating = in.readString();
        if (in.readByte() == 0) {
            regencyId = null;
        } else {
            regencyId = in.readLong();
        }
        specialist = in.readString();
        specialistSlug = in.readString();
        //startExperience =  (java.util.Date) in.readSerializable();
        status = in.readString();
        statusDocter = in.readString();
        uuid = in.readString();
        profileDoctor = in.readString();

        educations = new ArrayList<>();
        in.readList(educations, Alumni.class.getClassLoader());

        facilities = new ArrayList<>();
        in.readList(facilities, Facility.class.getClassLoader());
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNoStr() {
        return noStr;
    }

    public void setNoStr(String noStr) {
        this.noStr = noStr;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getRates() {
        return rates;
    }

    public void setRates(Long rates) {
        this.rates = rates;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Long getRegencyId() {
        return regencyId;
    }

    public void setRegencyId(Long regencyId) {
        this.regencyId = regencyId;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getSpecialistSlug() {
        return specialistSlug;
    }

    public void setSpecialistSlug(String specialistSlug) {
        this.specialistSlug = specialistSlug;
    }

    /*public Date getStartExperience() {
        return startExperience;
    }*/

    /*public void setStartExperience(Date startExperience) {
        this.startExperience = startExperience;
    }*/

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDocter() {
        return statusDocter;
    }

    public void setStatusDocter(String statusDocter) {
        this.statusDocter = statusDocter;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProfileDoctor() {
        return profileDoctor;
    }

    public void setProfileDoctor(String profileDoctor) {
        this.profileDoctor = profileDoctor;
    }

    public List<Alumni> getEducations() {
        return educations;
    }

    public void setEducations(List<Alumni> educations) {
        this.educations = educations;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    public String getJadwalKonsul() {
        return "-";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(deviceId);
        dest.writeString(email);
        dest.writeString(experience);
        dest.writeString(fullName);
        dest.writeString(noStr);
        dest.writeString(objectId);
        dest.writeString(phoneNumber);
        if (provinceId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(provinceId);
        }
        if (rates == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(rates);
        }
        dest.writeString(rating);
        if (regencyId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(regencyId);
        }
        dest.writeString(specialist);
        dest.writeString(specialistSlug);
        //dest.writeSerializable(startExperience);
        dest.writeString(status);
        dest.writeString(statusDocter);
        dest.writeString(uuid);
        dest.writeString(profileDoctor);
        dest.writeList(educations);
        dest.writeList(facilities);
    }
}
