package com.transmedika.transmedikakitui.models.bean.parse;

import androidx.annotation.Nullable;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Consultations")
public class KonsultasiParse extends ParseObject {
    public static final String IDLE = "IDLE";
    public static final String CANCEL_BY_PATIEN = "CANCEL BY PATIENT";
    public static final String REJECTED_BY_DOCTOR = "REJECTED BY DOCTOR";
    public static final String FINISHED = "FINISHED";
    public static final String SESI_BERAKHIR = "SESI BERAKHIR";
    public static final String ON_CHAT = "ON CHAT";
    public static final String NOT_ANSWERED = "NOT ANSWERED";


    public Long getConsultationId() {
        return getLong("consultation_id");
    }

    public void setConsultationId(Long consultationId) {
        put("consultation_id", consultationId);
    }

    public ParseUser getPatient() {
        return getParseUser("patient");
    }

    public void setPatient(ParseUser patient) {
        put("patient", patient);
    }

    public ParseUser getDoctor() {
        return getParseUser("doctor");
    }

    public void setDoctor(ParseUser doctor) {
        put("doctor", doctor);
    }

    public String getStatus() {
        return getString("status");
    }

    public void setStatus(String status) {
        put("status", status);
    }

    public String getPhr_permission() {
        return getString("phr_permission");
    }

    public void setPhr_permission(String phr_permission) {
        put("phr_permission", phr_permission);
    }

    public String getDetail_patient() {
        return getString("detail_patient");
    }

    public void setDetail_patient(String detail_patient) {
        put("detail_patient", detail_patient);
    }

    public String getKeluhan_patient() {
        return getString("keluhan_patient");
    }

    public void setKeluhan_patient(String keluhan_patient) {
        put("keluhan_patient",keluhan_patient);
    }

    public String getMedicalFacility() {
        return getString("medical_facility");
    }

    public void setMedicalFacility(String medicalFacility) {
        put("medical_facility", medicalFacility);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == this)
            return true;
        if (!(o instanceof KonsultasiParse))
            return false;
        KonsultasiParse other = (KonsultasiParse) o;
        return this.getConsultationId().equals(other.getConsultationId());
    }
}
