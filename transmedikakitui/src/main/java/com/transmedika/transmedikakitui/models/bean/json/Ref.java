
package com.transmedika.transmedikakitui.models.bean.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Ref implements Parcelable {

    @SerializedName("allergy") private String mAllergy;
    @SerializedName("blood_type") private String mBloodType;
    @SerializedName("body_height") private Long mBodyHeight;
    @SerializedName("body_weight") private Long mBodyWeight;
    @SerializedName("dob") private Date mDob;
    @SerializedName("nik") private String mNik;
    @SerializedName("no_kk") private String mNoKk;
    @SerializedName("ref") private String mRef;
    @SerializedName("relationship") private String mRelationship;
    @SerializedName("medical_record_number") String medicalRecordNumber;

    protected Ref(Parcel in) {
        mAllergy = in.readString();
        mBloodType = in.readString();
        if (in.readByte() == 0) {
            mBodyHeight = null;
        } else {
            mBodyHeight = in.readLong();
        }
        if (in.readByte() == 0) {
            mBodyWeight = null;
        } else {
            mBodyWeight = in.readLong();
        }
        mDob = (Date) in.readSerializable();
        mNik = in.readString();
        mNoKk = in.readString();
        mRef = in.readString();
        mRelationship = in.readString();
        medicalRecordNumber = in.readString();
    }

    public static final Creator<Ref> CREATOR = new Creator<Ref>() {
        @Override
        public Ref createFromParcel(Parcel in) {
            return new Ref(in);
        }

        @Override
        public Ref[] newArray(int size) {
            return new Ref[size];
        }
    };

    public String getAllergy() {
        return mAllergy;
    }

    public void setAllergy(String allergy) {
        mAllergy = allergy;
    }

    public String getBloodType() {
        return mBloodType;
    }

    public void setBloodType(String bloodType) {
        mBloodType = bloodType;
    }

    public Long getBodyHeight() {
        return mBodyHeight;
    }

    public void setBodyHeight(Long bodyHeight) {
        mBodyHeight = bodyHeight;
    }

    public Long getBodyWeight() {
        return mBodyWeight;
    }

    public void setBodyWeight(Long bodyWeight) {
        mBodyWeight = bodyWeight;
    }

    public Date getDob() {
        return mDob;
    }

    public void setDob(Date dob) {
        mDob = dob;
    }

    public String getNik() {
        return mNik;
    }

    public void setNik(String nik) {
        mNik = nik;
    }

    public String getNoKk() {
        return mNoKk;
    }

    public void setNoKk(String noKk) {
        mNoKk = noKk;
    }

    public String getRef() {
        return mRef;
    }

    public void setRef(String ref) {
        mRef = ref;
    }

    public String getRelationship() {
        return mRelationship;
    }

    public void setRelationship(String relationship) {
        mRelationship = relationship;
    }

    public String getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    public void setMedicalRecordNumber(String medicalRecordNumber) {
        this.medicalRecordNumber = medicalRecordNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAllergy);
        dest.writeString(mBloodType);
        if (mBodyHeight == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mBodyHeight);
        }
        if (mBodyWeight == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mBodyWeight);
        }
        dest.writeSerializable(mDob);
        dest.writeString(mNik);
        dest.writeString(mNoKk);
        dest.writeString(mRef);
        dest.writeString(mRelationship);
        dest.writeString(medicalRecordNumber);
    }
}
