package com.transmedika.transmedikakitui.models.bean.json.param;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CariObatParam {
    @SerializedName("map_lat") private Double lat;
    @SerializedName("map_lng") private Double lon;
    @SerializedName("transaction_id") private Long orderId;
    @SerializedName("orders")private List<Obat> obats;

    public CariObatParam() {
    }



    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public List<Obat> getObats() {
        return obats;
    }

    public void setObats(List<Obat> obats) {
        this.obats = obats;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public static class Obat{
        @SerializedName("medicine")private String medicine;
        @SerializedName("qty")private Integer qty;
        @SerializedName("prescription_id")private Long prescriptionId;

        public Obat() {
        }

        public String getMedicine() {
            return medicine;
        }

        public void setMedicine(String medicine) {
            this.medicine = medicine;
        }

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            this.qty = qty;
        }

        public Long getPrescriptionId() {
            return prescriptionId;
        }

        public void setPrescriptionId(Long prescriptionId) {
            this.prescriptionId = prescriptionId;
        }
    }
}
