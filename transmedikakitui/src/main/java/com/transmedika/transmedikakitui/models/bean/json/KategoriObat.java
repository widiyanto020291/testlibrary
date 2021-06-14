
package com.transmedika.transmedikakitui.models.bean.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class KategoriObat implements Parcelable {

    @Expose
    private Long id;
    @Expose
    private String image;
    @Expose
    private String name;
    @Expose
    private String slug;

    public KategoriObat() {
    }

    protected KategoriObat(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        image = in.readString();
        name = in.readString();
        slug = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(slug);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KategoriObat> CREATOR = new Creator<KategoriObat>() {
        @Override
        public KategoriObat createFromParcel(Parcel in) {
            return new KategoriObat(in);
        }

        @Override
        public KategoriObat[] newArray(int size) {
            return new KategoriObat[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
