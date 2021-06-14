package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Obat2 {
    @SerializedName("slug")private String slug;
    @SerializedName("name")private String name;
    @SerializedName("unit")private String unit;
    @SerializedName("price")private Long price;
    @SerializedName("stock")private Long stock;
    @SerializedName("qty")private Integer jumlah;
    @SerializedName("available")private Boolean available;
    @SerializedName("min_prices") private Long minPrices;
    @SerializedName("max_prices") private Long maxPrices;
    @SerializedName("status")private Boolean status;

    //helper
    @Expose private Boolean perbaharui;


    public Obat2() {
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

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public Long getMinPrices() {
        return minPrices;
    }

    public void setMinPrices(Long minPrices) {
        this.minPrices = minPrices;
    }

    public Long getMaxPrices() {
        return maxPrices;
    }

    public void setMaxPrices(Long maxPrices) {
        this.maxPrices = maxPrices;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getPerbaharui() {
        return perbaharui;
    }

    public void setPerbaharui(Boolean perbaharui) {
        this.perbaharui = perbaharui;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Obat2)) return false;
        Obat2 obat2 = (Obat2) o;
        return getSlug().equals(obat2.getSlug());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSlug());
    }
}
