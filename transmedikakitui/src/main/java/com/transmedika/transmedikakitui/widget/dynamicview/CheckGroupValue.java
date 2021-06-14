package com.transmedika.transmedikakitui.widget.dynamicview;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.models.bean.JawabanDetail;
import com.transmedika.transmedikakitui.models.bean.JawabanMultiple;
import com.transmedika.transmedikakitui.models.bean.json.PertanyaanResponse;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.util.ArrayList;
import java.util.List;

import static com.transmedika.transmedikakitui.utils.TransmedikaUtils.getFace;


public class CheckGroupValue extends MultipleValueWidget<List<JawabanDetail>> {
    private final RelativeLayout relativeLayout;
    private final List<JawabanDetail> jawabans = new ArrayList<>();
    private EditText editText;
    private LinearLayout checkBoxGroup;

    public CheckGroupValue(Context context) {
        super(context);
        this.relativeLayout = new RelativeLayout(context);
        this.relativeLayout.setLayoutParams(createParentParam());
    }

    @Override
    protected void onGetPertanyaan(PertanyaanResponse question) {
        super.onGetPertanyaan(question);
        checkBoxGroup = createCheckBoxGroup();
        editText = createEdittext(checkBoxGroup);
        if(question.getTypeInput()!=null) {
            this.editText.setRawInputType(question.getInputTypeAndroid());
        }else {
            this.editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        }
        this.editText.setMaxLines(4);
        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        if(question.getHint()!=null) editText.setHint(question.getHint());
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && TextUtils.isEmpty(editText.getText().toString()) && jawabans.size() == 0) {
                errorListener.showError("");
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    CheckBox ck = (CheckBox) checkBoxGroup.getChildAt(question.getJawabans().size() - 1);
                    ck.setChecked(true);

                    createAndRemoveJawabanDetail(question.getJawabans().get(question.getJawabans().size() - 1).getIdJawaban(), s.toString());
                    if(question.getMandadori()) {
                        errorListener.hideError();
                    }else {
                        errorListener.hideValidationError();
                    }
                } else {
                    if (jawabans.size() == 0) {
                        errorListener.showError("");
                    }else {
                        CheckBox checkBox = (CheckBox) checkBoxGroup.getChildAt(question.getJawabans().size()-1);
                        if(checkBox.isChecked() && s.length() == 0){
                            errorListener.validationError(getContext().getString(R.string.orther_required));
                            jawabans.remove(createJawabanDetail(question.getJawabans().get(question.getJawabans().size() - 1).getIdJawaban(),
                                    question.getJawabans().get(question.getJawabans().size() - 1).getJawaban()));
                        }
                    }
                }
                changeValue(jawabans);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        relativeLayout.addView(checkBoxGroup);

        int optionsSize = question.getJawabans().size();
        if(question.getJawabans()!=null && question.getJawabans().size() > 0 && question.getJawabans().get(optionsSize-1).getJawaban().equals(Constants.LAINNYYA)) {
            relativeLayout.addView(editText);
        }
    }

    private void createAndRemoveJawabanDetail(Long id, String val) {
        JawabanDetail jawaban = new JawabanDetail();
        jawaban.setIdJawaban(id);
        jawaban.setJawaban(val);

        jawabans.remove(jawaban);
        jawabans.add(jawaban);
    }

    private JawabanDetail createJawabanDetail(Long id, String val) {
        JawabanDetail jawaban = new JawabanDetail();
        jawaban.setIdJawaban(id);
        jawaban.setJawaban(val);
        return jawaban;
    }

    private LinearLayout createCheckBoxGroup() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0, TransmedikaUtils.dip2px(getContext(),(float) 5));
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (PertanyaanResponse.Jawaban jwb : pertanyaan.getJawabans()) {
            CheckBox ck = new CheckBox(getContext());
            if(transmedikaSettings.getFontRegular()!=null)
                ck.setTypeface(getFace(getContext(),transmedikaSettings.getFontRegular()));
            ck.setTag(pertanyaan.getIdPertanyaan() + "_" + jwb.getIdJawaban());
            ck.setText(jwb.getJawaban());
            ck.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    editText.requestFocus();
                    if (jwb.getJawaban().equals(Constants.LAINNYYA)) {
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            if (jawabans.size() == 0) {
                                if(pertanyaan.getMandadori()) {
                                    errorListener.showError("");
                                }else {
                                    errorListener.validationError(getContext().getString(R.string.orther_required));
                                }
                            }else {
                                if (TextUtils.isEmpty(editText.getText().toString())) {
                                    errorListener.validationError(getContext().getString(R.string.orther_required));
                                }
                            }
                        } else {
                            createAndRemoveJawabanDetail(jwb.getIdJawaban(), editText.getText().toString());
                            errorListener.hideError();
                            changeValue(jawabans);
                        }
                    } else {
                        CheckBox checkBox = (CheckBox) linearLayout.getChildAt(pertanyaan.getJawabans().size() - 1);

                        createAndRemoveJawabanDetail(jwb.getIdJawaban(), jwb.getJawaban());
                        errorListener.hideError();
                        changeValue(jawabans);

                        if (checkBox.isChecked() && TextUtils.isEmpty(editText.getText().toString()) && checkBox.getText().toString().equals(Constants.LAINNYYA)) {
                            errorListener.validationError(getContext().getString(R.string.orther_required));
                        }
                    }
                } else {
                    jawabans.remove(createJawabanDetail(jwb.getIdJawaban(), jwb.getJawaban()));
                    if (jawabans.size() > 0) {
                        CheckBox checkBox = (CheckBox) linearLayout.getChildAt(pertanyaan.getJawabans().size() - 1);
                        if(checkBox.isChecked() && TextUtils.isEmpty(editText.getText().toString()) && checkBox.getText().toString().equals(Constants.LAINNYYA)){
                            errorListener.validationError(getContext().getString(R.string.orther_required));
                        }else {
                            if(pertanyaan.getMandadori()) {
                                errorListener.hideError();
                            }else {
                                errorListener.hideValidationError();
                            }
                        }
                    } else {
                        if(pertanyaan.getMandadori()) {
                            errorListener.showError("");
                        }else {
                            errorListener.hideValidationError();
                        }
                    }
                    changeValue(jawabans);
                }
            });
            linearLayout.addView(ck);
        }
        return linearLayout;

    }

    private EditText createEdittext(LinearLayout ly) {
        EditText editText = new EditText(getContext());
        if(transmedikaSettings.getFontRegular()!=null)
            editText.setTypeface(getFace(getContext(),transmedikaSettings.getFontRegular()));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.END_OF, ly.getChildAt(pertanyaan.getJawabans().size() - 1).getId());
        layoutParams.addRule(RelativeLayout.RIGHT_OF, ly.getChildAt(pertanyaan.getJawabans().size() - 1).getId());
        layoutParams.setMarginStart(TransmedikaUtils.dip2px(getContext(), 85));
        int padding = TransmedikaUtils.dip2px(getContext(),10);
        editText.setPadding(padding,0,padding,padding);
        editText.setLayoutParams(layoutParams);
        return editText;
    }

    private RelativeLayout.LayoutParams createParentParam() {
        RelativeLayout.LayoutParams paramsContainer = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsContainer.setMargins(TransmedikaUtils.dip2px(getContext(),5), 10, 0, TransmedikaUtils.dip2px(getContext(),5));
        return paramsContainer;
    }

    @Override
    void onSetJawabanResult(JawabanMultiple jawabanMultiple) {
        if (getValue() != null && !getValue().isEmpty()) {
            jawabanMultiple.setDetailJawabans(getValue());
        }else {
            jawabanMultiple.setDetailJawabans(null);
        }
    }

    @Override
    void onSetUiFromJawaban(JawabanMultiple jawaban) {
        for (JawabanDetail jd: jawaban.getDetailJawabans()) {
            if (jd != null && jd.getIdJawaban() != null) {
                CheckBox cb = checkBoxGroup.findViewWithTag(pertanyaan.getIdPertanyaan() + "_" + jd.getIdJawaban());
                if (cb != null) {
                    if (Constants.LAINNYYA.equals(cb.getText().toString())) {
                        editText.setText(jd.getJawaban());
                    }
                    cb.setChecked(true);
                }
            }
        }
    }

    @Override
    void onResetJawaban() {
        editText.setText("");
        for (PertanyaanResponse.Jawaban jwb : pertanyaan.getJawabans()) {
            CheckBox cb = checkBoxGroup.findViewWithTag(pertanyaan.getIdPertanyaan() + "_" + jwb.getIdJawaban());
            if (cb != null) {
                cb.setChecked(false);
            }
        }
    }

    @Override
    public List<JawabanDetail> getValue() {
        return jawabans;
    }

    public View getView() {
        return relativeLayout;
    }
}
