package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CatatanDokter {
    @SerializedName("note") private String note;
    @SerializedName("next_schedule") private Date date;

    public CatatanDokter() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
