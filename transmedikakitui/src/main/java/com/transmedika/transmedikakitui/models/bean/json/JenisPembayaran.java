package com.transmedika.transmedikakitui.models.bean.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class JenisPembayaran implements Parcelable {

    @SerializedName("image")
    private String image;

    @SerializedName("payment_number")
    private String paymentNumber;

    @SerializedName("account_name")
    private String accountName;

    @SerializedName("description")
    private String description;

    @SerializedName("payment_name")
    private String paymentName;

    @SerializedName("id")
    private int id;

    @SerializedName("payment_category_id")
    private int paymentCategoryId;

    public JenisPembayaran(String paymentName, int id) {
        this.paymentName = paymentName;
        this.id = id;
    }

    public JenisPembayaran() {
    }

    protected JenisPembayaran(Parcel in) {
        image = in.readString();
        paymentNumber = in.readString();
        accountName = in.readString();
        description = in.readString();
        paymentName = in.readString();
        id = in.readInt();
        paymentCategoryId = in.readInt();
    }

    public static final Creator<JenisPembayaran> CREATOR = new Creator<JenisPembayaran>() {
        @Override
        public JenisPembayaran createFromParcel(Parcel in) {
            return new JenisPembayaran(in);
        }

        @Override
        public JenisPembayaran[] newArray(int size) {
            return new JenisPembayaran[size];
        }
    };

    public String getImage(){
        return image;
    }

    public String getPaymentNumber(){
        return paymentNumber;
    }

    public String getAccountName(){
        return accountName;
    }

    public String getDescription(){
        return description;
    }

    public String getPaymentName(){
        return paymentName;
    }

    public int getId(){
        return id;
    }

    public int getPaymentCategoryId(){
        return paymentCategoryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(paymentNumber);
        dest.writeString(accountName);
        dest.writeString(description);
        dest.writeString(paymentName);
        dest.writeInt(id);
        dest.writeInt(paymentCategoryId);
    }
}