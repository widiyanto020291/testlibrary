
package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResepObat {

    @SerializedName(value = "medicines_name", alternate = {"medicine_name"})
    private String medicinesName;
    @Expose
    private String note;
    @Expose
    @SerializedName(value = "period", alternate = {"days_consume"})
    private String period;
    @Expose
    private String rule;
    @Expose
    private Integer qty;
    @SerializedName("prescription_id")
    private Long prescriptionId;
    @Expose
    private String slug;

    @Expose
    private String unit;

    @SerializedName("status")private Boolean status;

    public String getMedicinesName() {
        return medicinesName;
    }

    public void setMedicinesName(String medicinesName) {
        this.medicinesName = medicinesName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
