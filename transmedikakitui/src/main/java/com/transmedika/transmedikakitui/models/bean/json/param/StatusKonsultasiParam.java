package com.transmedika.transmedikakitui.models.bean.json.param;

import com.google.gson.annotations.Expose;

public class StatusKonsultasiParam {
    @Expose
    private String status;

    public StatusKonsultasiParam() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
