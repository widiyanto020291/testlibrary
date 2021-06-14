
package com.transmedika.transmedikakitui.models.bean.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;


public class Obat extends RealmObject implements Parcelable {

    @Expose
    private String name;
    @Expose
    private String unit;
    @SerializedName("unit_small")
    private String unitSmall;
    @SerializedName("min_prices")
    private Long minPrices;
    @SerializedName("max_prices")
    private Long maxPrices;
    @SerializedName("qty_unit_small")
    private String qtyUnitSmall;
    @SerializedName("medicine_type")
    private String medicineType;
    @PrimaryKey
    @Expose
    private String slug;
    @Expose
    private String image;
    @Ignore @Expose
    private Map<String, String> description;
    @SerializedName("prescription_id")private Long prescriptionId;
    @Expose private Long prices;

    //helper
    @SerializedName("qty")
    private Integer jumlah;

    public Obat() {
    }

    public Obat(String slug) {
        this.slug = slug;
    }

    protected Obat(Parcel in) {
        name = in.readString();
        unit = in.readString();
        unitSmall = in.readString();
        if (in.readByte() == 0) {
            minPrices = null;
        } else {
            minPrices = in.readLong();
        }
        if (in.readByte() == 0) {
            maxPrices = null;
        } else {
            maxPrices = in.readLong();
        }
        qtyUnitSmall = in.readString();
        medicineType = in.readString();
        slug = in.readString();
        image = in.readString();
        description = new HashMap<>();
        in.readMap(description,String.class.getClassLoader());
        if (in.readByte() == 0) {
            prescriptionId = null;
        } else {
            prescriptionId = in.readLong();
        }
        if (in.readByte() == 0) {
            prices = null;
        } else {
            prices = in.readLong();
        }
        if (in.readByte() == 0) {
            jumlah = null;
        } else {
            jumlah = in.readInt();
        }
    }

    public static final Creator<Obat> CREATOR = new Creator<Obat>() {
        @Override
        public Obat createFromParcel(Parcel in) {
            return new Obat(in);
        }

        @Override
        public Obat[] newArray(int size) {
            return new Obat[size];
        }
    };

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

    public String getUnitSmall() {
        return unitSmall;
    }

    public void setUnitSmall(String unitSmall) {
        this.unitSmall = unitSmall;
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

    public String getQtyUnitSmall() {
        return qtyUnitSmall;
    }

    public void setQtyUnitSmall(String qtyUnitSmall) {
        this.qtyUnitSmall = qtyUnitSmall;
    }

    public String getMedicineType() {
        return medicineType;
    }

    public void setMedicineType(String medicineType) {
        this.medicineType = medicineType;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Long getPrices() {
        return prices;
    }

    public void setPrices(Long prices) {
        this.prices = prices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Obat)) return false;
        Obat obat = (Obat) o;
        return getSlug().equals(obat.getSlug());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSlug());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(unit);
        dest.writeString(unitSmall);
        if (minPrices == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(minPrices);
        }
        if (maxPrices == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(maxPrices);
        }
        dest.writeString(qtyUnitSmall);
        dest.writeString(medicineType);
        dest.writeString(slug);
        dest.writeString(image);
        dest.writeMap(description);
        if (prescriptionId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(prescriptionId);
        }
        if (prices == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(prices);
        }
        if (jumlah == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(jumlah);
        }
    }
}
