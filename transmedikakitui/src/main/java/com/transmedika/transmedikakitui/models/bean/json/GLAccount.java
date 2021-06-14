package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.SerializedName;

public class GLAccount {
    @SerializedName("GLAccountBalance") private String gLAccountBalance;
    @SerializedName("PointRewardBalance") private String pointRewardBalance;
    @SerializedName("GLAccountName") private String gLAccountName;
    @SerializedName("GLAccountNo") private String gLAccountNo;
    @SerializedName("GLAccountTypeCode") private String gLAccountTypeCode;
    @SerializedName("PaymentId") private Integer paymentId;
    @SerializedName("PaymentName") private String paymentName;

    public void setGLAccountBalance(String gLAccountBalance){
        this.gLAccountBalance = gLAccountBalance;
    }

    public String getGLAccountBalance(){
        return gLAccountBalance;
    }

    public void setPointRewardBalance(String pointRewardBalance){
        this.pointRewardBalance = pointRewardBalance;
    }

    public String getPointRewardBalance(){
        return pointRewardBalance;
    }

    public void setGLAccountName(String gLAccountName){
        this.gLAccountName = gLAccountName;
    }

    public String getGLAccountName(){
        return gLAccountName;
    }

    public void setGLAccountNo(String gLAccountNo){
        this.gLAccountNo = gLAccountNo;
    }

    public String getGLAccountNo(){
        return gLAccountNo;
    }

    public void setGLAccountTypeCode(String gLAccountTypeCode){
        this.gLAccountTypeCode = gLAccountTypeCode;
    }

    public String getGLAccountTypeCode(){
        return gLAccountTypeCode;
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
}
