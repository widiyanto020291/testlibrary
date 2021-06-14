package com.transmedika.transmedikakitui.models.bean.json.param;

import com.google.gson.annotations.SerializedName;

public class FormParam {

    @SerializedName("medical_facility_id")
    private Long medicalFacilityId;

    @SerializedName("specialist_id")
    private String specialistId;

    public void setMedicalFacilityId(Long medicalFacilityId){
        this.medicalFacilityId = medicalFacilityId;
    }

    public Long getMedicalFacilityId(){
        return medicalFacilityId;
    }

    public void setSpecialistId(String specialistId){
        this.specialistId = specialistId;
    }

    public String getSpecialistId(){
        return specialistId;
    }
}