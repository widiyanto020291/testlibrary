package com.transmedika.transmedikakitui.models.bean.realm;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TempJawaban extends RealmObject implements Parcelable {

    @PrimaryKey
    private String pmId;

    @SerializedName("klinik_id")
    private Long klinikId;

    @SerializedName("specialize_id")
    private String specializeId;

    @SerializedName("jawaban")
    private String jawaban;

    public TempJawaban() { }

    public TempJawaban(Long klinikId, String specializeId, String jawaban) {
        this.klinikId = klinikId;
        this.specializeId = specializeId;
        this.jawaban = jawaban;
        this.pmId = klinikId + "|" + specializeId;
    }

    protected TempJawaban(Parcel in) {
        pmId = in.readString();
        if (in.readByte() == 0) {
            klinikId = null;
        } else {
            klinikId = in.readLong();
        }
        specializeId = in.readString();
        jawaban = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pmId);
        if (klinikId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(klinikId);
        }
        dest.writeString(specializeId);
        dest.writeString(jawaban);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TempJawaban> CREATOR = new Creator<TempJawaban>() {
        @Override
        public TempJawaban createFromParcel(Parcel in) {
            return new TempJawaban(in);
        }

        @Override
        public TempJawaban[] newArray(int size) {
            return new TempJawaban[size];
        }
    };

    public Long getKlinikId() {
        return klinikId;
    }

    public String getSpecializeId() {
        return specializeId;
    }

    public String getJawaban() {
        return jawaban;
    }
}
