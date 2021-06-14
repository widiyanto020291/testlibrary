
package com.transmedika.transmedikakitui.models.bean.json.param;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KonsultasiParam {

    @SerializedName("email_doctor") private String emailDoctor;
    @SerializedName("email_patient") private String emailPatient;
    @Expose private Long rates;
    @SerializedName("uuid_doctor") private String uuidDoctor;
    @SerializedName("uuid_patient") private String uuidPatient;
    @SerializedName("voucher") private String voucher;
    @SerializedName("voucher_amount") private Long voucherAmount;
    @Expose private String complaint;
    @SerializedName("payment_id") private Integer paymentId;
    @SerializedName("payment_name") private String paymentName;
    @SerializedName("pin") private String pin;
    @SerializedName("trans_merchant_id") private String transMerchantId;

    public String getEmailDoctor() {
        return emailDoctor;
    }

    public void setEmailDoctor(String emailDoctor) {
        this.emailDoctor = emailDoctor;
    }

    public String getEmailPatient() {
        return emailPatient;
    }

    public void setEmailPatient(String emailPatient) {
        this.emailPatient = emailPatient;
    }

    public Long getRates() {
        return rates;
    }

    public void setRates(Long rates) {
        this.rates = rates;
    }

    public String getUuidDoctor() {
        return uuidDoctor;
    }

    public void setUuidDoctor(String uuidDoctor) {
        this.uuidDoctor = uuidDoctor;
    }

    public String getUuidPatient() {
        return uuidPatient;
    }

    public void setUuidPatient(String uuidPatient) {
        this.uuidPatient = uuidPatient;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public Long getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(Long voucherAmount) {
        this.voucherAmount = voucherAmount;
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

    public String getTransMerchantId() {
        return transMerchantId;
    }

    public void setTransMerchantId(String transMerchantId) {
        this.transMerchantId = transMerchantId;
    }
}
