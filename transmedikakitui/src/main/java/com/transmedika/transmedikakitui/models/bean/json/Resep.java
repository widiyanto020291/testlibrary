package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Resep {
    @SerializedName("doctor") private ResepDokter dokter;
    @SerializedName("patient") private ResepPasien pasien;
    @SerializedName("recipes") private List<ResepObat> reseps;
    @SerializedName("expires") private Date expires;
    @SerializedName("prescription_note")private String prescriptionNote;
    @SerializedName("prescription_date") private Date prescriptionDate;


    public Resep() {
    }

    public ResepDokter getDokter() {
        return dokter;
    }

    public void setDokter(ResepDokter dokter) {
        this.dokter = dokter;
    }

    public ResepPasien getPasien() {
        return pasien;
    }

    public void setPasien(ResepPasien pasien) {
        this.pasien = pasien;
    }

    public List<ResepObat> getReseps() {
        return reseps;
    }

    public void setReseps(List<ResepObat> reseps) {
        this.reseps = reseps;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getPrescriptionNote() {
        return prescriptionNote;
    }

    public void setPrescriptionNote(String prescriptionNote) {
        this.prescriptionNote = prescriptionNote;
    }

    public Date getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(Date prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }
}
