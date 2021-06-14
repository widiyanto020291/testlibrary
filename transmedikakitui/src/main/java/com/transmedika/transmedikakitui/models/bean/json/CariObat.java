package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CariObat {
    @SerializedName("id")private Long id;
    @SerializedName("pharmacy_name")private String pharmacyName;
    @SerializedName("medicine_available")private Long medicineAvailable;
    @SerializedName("distances")private Double distance;
    @SerializedName("medicines")private List<Obat2> obats;

    //helper
    @Expose private Boolean perbaharui;

    public CariObat() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public Long getMedicineAvailable() {
        return medicineAvailable;
    }

    public void setMedicineAvailable(Long medicineAvailable) {
        this.medicineAvailable = medicineAvailable;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public List<Obat2> getObats() {
        return obats;
    }

    public void setObats(List<Obat2> obats) {
        this.obats = obats;
    }

    public Boolean getPerbaharui() {
        return perbaharui;
    }

    public void setPerbaharui(Boolean perbaharui) {
        this.perbaharui = perbaharui;
    }
}
