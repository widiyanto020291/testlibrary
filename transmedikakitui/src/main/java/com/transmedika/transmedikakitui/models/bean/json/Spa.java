package com.transmedika.transmedikakitui.models.bean.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Spa implements Parcelable {
    @SerializedName("symptoms") private String symptoms;
    @SerializedName("possible_diagnosis") private String possibleDiagnosis;
    @SerializedName("advice")  private String advice;
    @SerializedName("icd") private List<ICD> icd;

    public Spa() {
    }

    protected Spa(Parcel in) {
        symptoms = in.readString();
        possibleDiagnosis = in.readString();
        advice = in.readString();
        icd = in.createTypedArrayList(ICD.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symptoms);
        dest.writeString(possibleDiagnosis);
        dest.writeString(advice);
        dest.writeTypedList(icd);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Spa> CREATOR = new Creator<Spa>() {
        @Override
        public Spa createFromParcel(Parcel in) {
            return new Spa(in);
        }

        @Override
        public Spa[] newArray(int size) {
            return new Spa[size];
        }
    };

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getPossibleDiagnosis() {
        return possibleDiagnosis;
    }

    public void setPossibleDiagnosis(String possibleDiagnosis) {
        this.possibleDiagnosis = possibleDiagnosis;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public List<ICD> getIcd() {
        return icd;
    }

    public void setIcd(List<ICD> icd) {
        this.icd = icd;
    }
}
