package com.transmedika.transmedikakitui.models.bean.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ICD implements Parcelable {
    private Long id;
    private String name;
    @SerializedName("code")
    private String code;

    protected ICD(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        code = in.readString();
    }

    public static final Creator<ICD> CREATOR = new Creator<ICD>() {
        @Override
        public ICD createFromParcel(Parcel in) {
            return new ICD(in);
        }

        @Override
        public ICD[] newArray(int size) {
            return new ICD[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        if (code != null && name != null) {
            return code + " - " + name;
        } else if (name != null){
            return name;
        } else {
            return "-";
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(name);
        parcel.writeString(code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ICD icd = (ICD) o;
        return Objects.equals(id, icd.id) &&
                Objects.equals(name, icd.name) &&
                Objects.equals(code, icd.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code);
    }
}
