package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.transmedika.transmedikakitui.models.bean.json.param.JawabanParam;

import java.util.ArrayList;
import java.util.List;


public class Jawaban {

	@SerializedName("consultation_id")
	private Integer consultationId;

	@SerializedName("medical_facility_id")
	private Integer medicalFacilityId;

	@SerializedName("patient_id")
	private Integer patientId;

	@SerializedName("answers")
	private String answers;

	@SerializedName("id")
	private Integer id;

	public void setConsultationId(Integer consultationId){
		this.consultationId = consultationId;
	}

	public Integer getConsultationId(){
		return consultationId;
	}

	public void setMedicalFacilityId(Integer medicalFacilityId){
		this.medicalFacilityId = medicalFacilityId;
	}

	public Integer getMedicalFacilityId(){
		return medicalFacilityId;
	}

	public void setPatientId(Integer patientId){
		this.patientId = patientId;
	}

	public Integer getPatientId(){
		return patientId;
	}

	public void setAnswers(String answers){
		this.answers = answers;
	}

	public String getAnswers(){
		return answers;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return id;
	}


	public List<JawabanParam> getListJawabanModel(){
		return new Gson().fromJson(getAnswers(), new TypeToken<ArrayList<JawabanParam>>(){}.getType());
	}
}