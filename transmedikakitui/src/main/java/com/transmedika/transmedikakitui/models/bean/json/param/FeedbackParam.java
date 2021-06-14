package com.transmedika.transmedikakitui.models.bean.json.param;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedbackParam {
    @SerializedName("consultation_id")
    private Long consultationId;
    private List<String> comment;
    private int rating;
    private String description;

    public Long getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(Long consultationId) {
        this.consultationId = consultationId;
    }

    public List<String> getComment() {
        return comment;
    }

    public void setComment(List<String> comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
