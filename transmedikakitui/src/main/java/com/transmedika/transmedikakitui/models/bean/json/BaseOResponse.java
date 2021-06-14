package com.transmedika.transmedikakitui.models.bean.json;

public class BaseOResponse extends BaseResponse<Object> {

    public BaseOResponse(Long code, Boolean success, String messages, Object data) {
        super(code, success, messages, data);
    }
}
