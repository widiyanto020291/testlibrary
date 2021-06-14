
package com.transmedika.transmedikakitui.modul.videocall.kurento.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CallPush {
    @SerializedName("to") private List<String> tos;
    @SerializedName("data") private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<String> getTo() {
        return tos;
    }

    public void setTo(List<String> tos) {
        this.tos = tos;
    }

}
