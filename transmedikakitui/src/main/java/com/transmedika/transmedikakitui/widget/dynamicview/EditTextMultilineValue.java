package com.transmedika.transmedikakitui.widget.dynamicview;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.transmedika.transmedikakitui.models.bean.JawabanDetail;
import com.transmedika.transmedikakitui.models.bean.JawabanSingle;
import com.transmedika.transmedikakitui.models.bean.json.PertanyaanResponse;


public class EditTextMultilineValue extends SingleValueWidget<String> {

    @Override
    protected void setOnFocusChangeListenerS(View v, boolean hasFocus) {
        if(TextUtils.isEmpty(editText.getText().toString()) && !hasFocus) {
            errorListener.showError("");
        }

        if(!TextUtils.isEmpty(editText.getText().toString()) && !hasFocus && pertanyaan.getTypeInput().equals(PertanyaanResponse.TYPE_INPUT_DECIMAL)){
            if(!isNumeric(editText.getText().toString())){
                errorListener.validationError("String is not numeric");
            }else {
                errorListener.hideValidationError();
            }
        }

        if(!TextUtils.isEmpty(editText.getText().toString()) && !hasFocus && pertanyaan.getTypeInput().equals(PertanyaanResponse.TYPE_INPUT_EMAIL)){
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches()){
                errorListener.validationError("String is not email");
            }else {
                errorListener.hideValidationError();
            }
        }
    }

    @Override
    protected void beforeTextChangedS(CharSequence s, int start, int count, int after) {

    }

    @Override
    protected void onTextChangedS(CharSequence s, int start, int before, int count) {
        if(s.length() > 0){
            if (errorListener != null)
                if(pertanyaan.getMandadori()) {
                    errorListener.hideError();
                }else {
                    errorListener.hideValidationError();
                }
        }else {
            if (errorListener != null)
                errorListener.showError("");
        }
        changeValue(s.toString());
    }

    @Override
    protected void afterTextChangedS(Editable s) {

    }

    public EditTextMultilineValue(Context context) {
        super(context);
    }

    @Override
    protected void onGetPertanyaan(PertanyaanResponse question) {
        super.onGetPertanyaan(question);
        this.editText.setMaxLines(4);
    }

    public String getValue(){
        return editText.getText().toString();
    }

    public View getView() {
        return editText;
    }

    @Override
    void onSetJawabanResult(JawabanSingle jawabanSingle) {
        if (getValue() != null && !getValue().isEmpty()) {
            JawabanDetail detailAnswer = new JawabanDetail();
            detailAnswer.setJawaban(getValue());

            jawabanSingle.setDetailJawaban(detailAnswer);
        } else {
            jawabanSingle.setDetailJawaban(null);
        }
    }

    @Override
    void onSetUiFromJawaban(JawabanSingle jawaban) {
        String answer = jawaban.getDetailJawaban().getJawaban();
        if (editText != null && answer != null) {
            editText.setText(answer);
        }
    }

    @Override
    void onResetJawaban() {
        editText.setText("");
    }
}
