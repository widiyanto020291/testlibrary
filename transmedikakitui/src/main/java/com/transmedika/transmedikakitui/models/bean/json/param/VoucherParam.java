package com.transmedika.transmedikakitui.models.bean.json.param;

import com.google.gson.annotations.SerializedName;

public class VoucherParam {
    @SerializedName("voucher_code")private String voucherCode;
    @SerializedName("transaction_type")private Integer transactionType; //1 untuk konsultasi, 2 untuk obat

    public VoucherParam() {
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }
}
