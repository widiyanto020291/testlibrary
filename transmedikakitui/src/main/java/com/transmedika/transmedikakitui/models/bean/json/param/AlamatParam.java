package com.transmedika.transmedikakitui.models.bean.json.param;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlamatParam {
     @Expose private Long id;
     @SerializedName("address_type") private String addressType;
     @SerializedName("address")private String address;
     @SerializedName("map_lat")private String mapLat;
     @SerializedName("map_lng")private String mapLng;
     @SerializedName("note")private String note;

    public AlamatParam() {
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
