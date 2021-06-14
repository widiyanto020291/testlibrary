package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Voucher {
    @SerializedName("code")private String code;
    @SerializedName("name")private String name;
    @SerializedName("slug")private String slug;
    @SerializedName("start_date")private Date startDate;
    @SerializedName("end_date")private Date endDate;
    @SerializedName("type")private Integer type;
    @SerializedName("nominal")private Long nominal;
    @SerializedName("quota")private Integer quota;
    @SerializedName("notes")private String note;
    @SerializedName("image")private String image;

    public Voucher() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getNominal() {
        return nominal;
    }

    public void setNominal(Long nominal) {
        this.nominal = nominal;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
