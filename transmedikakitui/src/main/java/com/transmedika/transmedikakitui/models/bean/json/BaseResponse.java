
package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {

    @Expose
    private Long code;
    @Expose
    private Boolean success;
    @SerializedName(value = "messages" , alternate = "message")
    private String messages;
    @Expose
    private T data;

    public BaseResponse(Long code, Boolean success, String messages, T data) {
        this.code = code;
        this.success = success;
        this.messages = messages;
        this.data = data;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
