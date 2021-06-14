package com.transmedika.transmedikakitui.widget.dynamicview;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.models.bean.JawabanDetail;
import com.transmedika.transmedikakitui.models.bean.JawabanSingle;
import com.transmedika.transmedikakitui.models.bean.json.PertanyaanResponse;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import static com.transmedika.transmedikakitui.utils.TransmedikaUtils.getFace;


public class RadioGroupValue extends SingleValueWidget<JawabanDetail> {
    private final RelativeLayout relativeLayout;
    private JawabanDetail jawaban;
    private RadioGroup radioGroup;

    @Override
    protected void setOnFocusChangeListenerS(View v, boolean hasFocus) {
        if(TextUtils.isEmpty(editText.getText().toString()) && !hasFocus && jawaban==null) {
            errorListener.showError("");
            jawaban = null;
        }
    }

    @Override
    protected void beforeTextChangedS(CharSequence s, int start, int count, int after) {

    }

    @Override
    protected void onTextChangedS(CharSequence s, int start, int before, int count) {
        if(s.length() > 0){
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(pertanyaan.getJawabans().size()-1);
            radioButton.setChecked(true);

            setJawabanDetail(pertanyaan.getJawabans().get(pertanyaan.getJawabans().size()-1).getIdJawaban(),
                    s.toString());
            if(pertanyaan.getMandadori()) {
                errorListener.hideError();
            }else {
                errorListener.hideValidationError();
            }
        }else {
            jawaban = null;
            if(pertanyaan.getMandadori()) {
                errorListener.showError("");
            }else {
                errorListener.validationError(getContext().getString(R.string.orther_required));
            }
        }
        changeValue(jawaban);
    }

    @Override
    protected void afterTextChangedS(Editable s) {

    }

    public RadioGroupValue(Context context) {
        super(context);
        this.relativeLayout = new RelativeLayout(context);
        this.relativeLayout.setLayoutParams(createParentParam());
    }

    @Override
    protected void onGetPertanyaan(PertanyaanResponse question) {
        super.onGetPertanyaan(question);
        radioGroup = createRadioGroup();

        relativeLayout.addView(radioGroup);
        int optionsSize = question.getJawabans().size();
        if(question.getJawabans()!=null && question.getJawabans().size() > 0 && question.getJawabans().get(optionsSize-1).getJawaban().equals(Constants.LAINNYYA)) {
            putEditTextSideRadioButton();
            relativeLayout.addView(editText);
        }
    }

    private RadioGroup createRadioGroup(){
        RadioGroup radioGroup = new RadioGroup(getContext());
        RelativeLayout.LayoutParams rgParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rgParam.setMargins(0,0,0, TransmedikaUtils.dip2px(getContext(),(float) 5));
        radioGroup.setLayoutParams(rgParam);
        for (PertanyaanResponse.Jawaban jwb: pertanyaan.getJawabans()){
            RadioButton vRb = new RadioButton(getContext());
            if(transmedikaSettings.getFontRegular()!=null)
                vRb.setTypeface(getFace(getContext(),transmedikaSettings.getFontRegular()));
            vRb.setTag(pertanyaan.getIdPertanyaan() + "_" + jwb.getIdJawaban());
            vRb.setId(pertanyaan.getIdPertanyaan().intValue()+jwb.getIdJawaban().intValue());
            vRb.setText(jwb.getJawaban());
            vRb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) {
                    if (jwb.getJawaban().equals(Constants.LAINNYYA)) {
                        editText.requestFocus();
                        if(TextUtils.isEmpty(editText.getText().toString())) {
                            jawaban = null;
                            if(pertanyaan.getMandadori()) {
                                errorListener.showError("");
                            }else {
                                errorListener.validationError(getContext().getString(R.string.orther_required));
                            }
                        }else {
                            setJawabanDetail(jwb.getIdJawaban(), editText.getText().toString());
                            errorListener.hideError();
                        }
                    } else {
                        setJawabanDetail(jwb.getIdJawaban(), jwb.getJawaban());
                        if(pertanyaan.getMandadori()) {
                            errorListener.hideError();
                        }else {
                            errorListener.hideValidationError();
                        }
                    }
                    changeValue(jawaban);
                }
            });
            radioGroup.addView(vRb);
        }
        return radioGroup;
    }

    private void setJawabanDetail(Long id, String val){
        if (jawaban == null) {
            jawaban = new JawabanDetail();
        }
        jawaban.setIdJawaban(id);
        jawaban.setJawaban(val);
    }

    private void putEditTextSideRadioButton(){
        editText.setMaxLines(4);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.END_OF, radioGroup.getChildAt(pertanyaan.getJawabans().size()-1).getId());
        layoutParams.addRule(RelativeLayout.RIGHT_OF, radioGroup.getChildAt(pertanyaan.getJawabans().size()-1).getId());
        layoutParams.setMarginStart(TransmedikaUtils.dip2px(getContext(),85));
        editText.setLayoutParams(layoutParams);
    }

    private RelativeLayout.LayoutParams createParentParam(){
        RelativeLayout.LayoutParams paramsContainer = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsContainer.setMargins(TransmedikaUtils.dip2px(getContext(),5),10,0,TransmedikaUtils.dip2px(getContext(),5));
        return paramsContainer;
    }

    @Override
    void onSetJawabanResult(JawabanSingle jawabanSingle) {
        if (getValue() != null) {
            jawabanSingle.setDetailJawaban(getValue());
        } else {
            jawabanSingle.setDetailJawaban(null);
        }
    }

    @Override
    void onSetUiFromJawaban(JawabanSingle jawaban) {
        JawabanDetail jd = jawaban.getDetailJawaban();
        if (jd != null && jd.getIdJawaban() != null) {
            RadioButton radioButton = radioGroup.findViewWithTag(pertanyaan.getIdPertanyaan() + "_" + jd.getIdJawaban());
            if (radioButton != null) {
                if (Constants.LAINNYYA.equals(radioButton.getText().toString())) {
                    editText.setText(jd.getJawaban());
                }
                radioButton.setChecked(true);
            }
        }
    }

    @Override
    void onResetJawaban() {
        editText.setText("");
        radioGroup.clearCheck();
    }

    @Override
    public JawabanDetail getValue() {
        return jawaban;
    }

    public View getView(){
        return relativeLayout;
    }
}
