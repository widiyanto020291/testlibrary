
package com.transmedika.transmedikakitui.models.bean.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Specialist implements Parcelable {

    @Expose
    private Long id;
    @Expose
    private String img;
    @SerializedName("name")
    private String specialist;
    @Expose
    private String keterangan;
    @SerializedName("status_enabled")
    private Long statusEnabled;
    @Expose
    private String image;
    @Expose
    private String slug;

    public Specialist() {
    }

    protected Specialist(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        img = in.readString();
        specialist = in.readString();
        keterangan = in.readString();
        if (in.readByte() == 0) {
            statusEnabled = null;
        } else {
            statusEnabled = in.readLong();
        }
        image = in.readString();
        slug = in.readString();
    }

    public static final Creator<Specialist> CREATOR = new Creator<Specialist>() {
        @Override
        public Specialist createFromParcel(Parcel in) {
            return new Specialist(in);
        }

        @Override
        public Specialist[] newArray(int size) {
            return new Specialist[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public Long getStatusEnabled() {
        return statusEnabled;
    }

    public void setStatusEnabled(Long statusEnabled) {
        this.statusEnabled = statusEnabled;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(img);
        dest.writeString(specialist);
        dest.writeString(keterangan);
        if (statusEnabled == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(statusEnabled);
        }
        dest.writeString(image);
        dest.writeString(slug);
    }
}
