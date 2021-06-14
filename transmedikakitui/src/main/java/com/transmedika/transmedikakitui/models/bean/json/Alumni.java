
package com.transmedika.transmedikakitui.models.bean.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Alumni implements Parcelable {

    @SerializedName("doctors_id")
    private Long doctorsId;
    @Expose
    private String education;
    @SerializedName("graduation_year")
    private Long graduationYear;
    @Expose
    private Long id;

    public Alumni() {
    }

    protected Alumni(Parcel in) {
        if (in.readByte() == 0) {
            doctorsId = null;
        } else {
            doctorsId = in.readLong();
        }
        education = in.readString();
        if (in.readByte() == 0) {
            graduationYear = null;
        } else {
            graduationYear = in.readLong();
        }
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
    }

    public static final Creator<Alumni> CREATOR = new Creator<Alumni>() {
        @Override
        public Alumni createFromParcel(Parcel in) {
            return new Alumni(in);
        }

        @Override
        public Alumni[] newArray(int size) {
            return new Alumni[size];
        }
    };

    public Long getDoctorsId() {
        return doctorsId;
    }

    public void setDoctorsId(Long doctorsId) {
        this.doctorsId = doctorsId;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public Long getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Long graduationYear) {
        this.graduationYear = graduationYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (doctorsId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(doctorsId);
        }
        dest.writeString(education);
        if (graduationYear == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(graduationYear);
        }
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
    }
}
