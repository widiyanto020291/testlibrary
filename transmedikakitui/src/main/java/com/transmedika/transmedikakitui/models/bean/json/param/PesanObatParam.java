package com.transmedika.transmedikakitui.models.bean.json.param;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PesanObatParam {
    @SerializedName("id")private Long id;
    @SerializedName("total")private Long total;
    @SerializedName("map_lat")private String mapLat;
    @SerializedName("map_lng")private String mapLon;
    @SerializedName("address")private String address;
    @SerializedName("note")private String note;
    @SerializedName("voucher")private String voucher;
    @SerializedName("voucher_amount")private Long voucherAmount;
    @SerializedName("medicines")private List<Obat3> obat3s;
    @SerializedName("payment_id") private Integer paymentId;
    @SerializedName("payment_name") private String paymentName;
    @SerializedName("pin") private String pin;

    public PesanObatParam() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Obat3> getObat3s() {
        return obat3s;
    }

    public void setObat3s(List<Obat3> obat3s) {
        this.obat3s = obat3s;
    }

    public String getMapLat() {
        return mapLat;
    }

    public void setMapLat(String mapLat) {
        this.mapLat = mapLat;
    }


    public String getMapLon() {
        return mapLon;
    }

    public void setMapLon(String mapLon) {
        this.mapLon = mapLon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public Long getVoucherAmpunt() {
        return voucherAmount;
    }

    public void setVoucherAmpunt(Long voucherAmpunt) {
        this.voucherAmount = voucherAmpunt;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public static class Obat3{
        @SerializedName("slug")private String slug;
        @SerializedName("name")private String name;
        @SerializedName("unit")private String unit;
        @SerializedName("price")private Long price;
        @SerializedName("qty")private Integer qty;
        @SerializedName("prescription_id")private Long prescriptionId;

        public Obat3() {
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public Long getPrice() {
            return price;
        }

        public void setPrice(Long price) {
            this.price = price;
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
