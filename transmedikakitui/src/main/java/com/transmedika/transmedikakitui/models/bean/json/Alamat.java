
package com.transmedika.transmedikakitui.models.bean.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Alamat implements Parcelable {

    @Expose
    private String address;
    @SerializedName("address_type")
    private String addressType;
    @Expose
    private Long id;
    @SerializedName("map_lat")
    private String mapLat;
    @SerializedName("map_lng")
    private String mapLng;
    @Expose
    private String note;
    @SerializedName("patient_id")
    private Long patientId;

    public Alamat() {
    }

    protected Alamat(Parcel in) {
        address = in.readString();
        addressType = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        mapLat = in.readString();
        mapLng = in.readString();
        note = in.readString();
        if (in.readByte() == 0) {
            patientId = null;
        } else {
            patientId = in.readLong();
        }
    }

    public static final Creator<Alamat> CREATOR = new Creator<Alamat>() {
        @Override
        public Alamat createFromParcel(Parcel in) {
            return new Alamat(in);
        }

        @Override
        public Alamat[] newArray(int size) {
            return new Alamat[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMapLat() {
        return mapLat;
    }

    public void setMapLat(String mapLat) {
        this.mapLat = mapLat;
    }

    public String getMapLng() {
        return mapLng;
    }

    public void setMapLng(String mapLng) {
        this.mapLng = mapLng;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(addressType);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(mapLat);
        dest.writeString(mapLng);
        dest.writeString(note);
        if (patientId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(patientId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alamat)) return false;
        Alamat alamat = (Alamat) o;
        return getId().equals(alamat.getId()) &&
                getPatientId().equals(alamat.getPatientId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPatientId());
    }
}
