package com.transmedika.transmedikakitui.models.bean.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Konsultasi implements Parcelable{
    @SerializedName("doctor")private Doctor doctor;
    @SerializedName("patient")private Profile patient;
    @SerializedName("consultation_id") private Long consultationId;
    @SerializedName("spa")private Boolean spa;
    @SerializedName("complaint")private String complaint;
    @SerializedName("doctor_note")private CatatanDokter doctorNote;
    @SerializedName("prescription")private Resep resep;
    @SerializedName("clinic")private Clinic klinik;

    public Konsultasi() {
    }

    protected Konsultasi(Parcel in) {
        doctor = in.readParcelable(Doctor.class.getClassLoader());
        patient = in.readParcelable(Profile.class.getClassLoader());
        if (in.readByte() == 0) {
            consultationId = null;
        } else {
            consultationId = in.readLong();
        }
        byte tmpSpa = in.readByte();
        spa = tmpSpa == 0 ? null : tmpSpa == 1;
        complaint = in.readString();
        klinik = in.readParcelable(Clinic.class.getClassLoader());
    }

    public static final Creator<Konsultasi> CREATOR = new Creator<Konsultasi>() {
        @Override
        public Konsultasi createFromParcel(Parcel in) {
            return new Konsultasi(in);
        }

        @Override
        public Konsultasi[] newArray(int size) {
            return new Konsultasi[size];
        }
    };

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Profile getPatient() {
        return patient;
    }

    public void setPatient(Profile patient) {
        this.patient = patient;
    }

    public Long getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(Long consultationId) {
        this.consultationId = consultationId;
    }

    public Boolean getSpa() {
        return spa;
    }

    public void setSpa(Boolean spa) {
        this.spa = spa;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public CatatanDokter getDoctorNote() {
        return doctorNote;
    }

    public void setDoctorNote(CatatanDokter doctorNote) {
        this.doctorNote = doctorNote;
    }

    public Resep getResep() {
        return resep;
    }

    public void setResep(Resep resep) {
        this.resep = resep;
    }

    public Clinic getKlinik() {
        return klinik;
    }

    public void setKlinik(Clinic klinik) {
        this.klinik = klinik;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(doctor, flags);
        dest.writeParcelable(patient, flags);
        if (consultationId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(consultationId);
        }
        dest.writeByte((byte) (spa == null ? 0 : spa ? 1 : 2));
        dest.writeString(complaint);
        dest.writeParcelable(klinik, flags);
    }

    public static class Resep {
        @SerializedName("id")private Long id;
        @SerializedName("consultation_id")private Long consultationDd;
        @SerializedName("prescription_number")private String prescriptionNumber;
        @SerializedName("order_status")private Integer orderStatus;
        @SerializedName("note")private String note;
        @SerializedName("status")private Integer status;
        @SerializedName("created_at")private Date createdAt;
        @SerializedName("expires")private Date expires;

        public Resep() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getConsultationDd() {
            return consultationDd;
        }

        public void setConsultationDd(Long consultationDd) {
            this.consultationDd = consultationDd;
        }

        public String getPrescriptionNumber() {
            return prescriptionNumber;
        }

        public void setPrescriptionNumber(String prescriptionNumber) {
            this.prescriptionNumber = prescriptionNumber;
        }

        public Integer getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(Integer orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public Date getExpires() {
            return expires;
        }

        public void setExpires(Date expires) {
            this.expires = expires;
        }

    }
}
