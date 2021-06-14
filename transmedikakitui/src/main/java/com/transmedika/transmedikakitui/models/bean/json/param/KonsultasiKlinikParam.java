
package com.transmedika.transmedikakitui.models.bean.json.param;

import com.google.gson.annotations.SerializedName;

public class KonsultasiKlinikParam extends KonsultasiParam {

    @SerializedName("medical_facility_id") private Long medicalFacilityId;
    @SerializedName("answers")private String jawabanParams;

    public Long getMedicalFacilityId() {
        return medicalFacilityId;
    }

    public void setMedicalFacilityId(Long medicalFacilityId) {
        this.medicalFacilityId = medicalFacilityId;
    }

    public String getJawabanParams() {
        return jawabanParams;
    }

    public void setJawabanParams(String jawabanParams) {
        this.jawabanParams = jawabanParams;
    }
}
