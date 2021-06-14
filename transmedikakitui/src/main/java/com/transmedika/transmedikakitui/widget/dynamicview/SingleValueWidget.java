package com.transmedika.transmedikakitui.widget.dynamicview;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.transmedika.transmedikakitui.models.bean.JawabanSingle;
import com.transmedika.transmedikakitui.models.bean.json.PertanyaanResponse;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import static com.transmedika.transmedikakitui.utils.TransmedikaUtils.getFace;


public abstract class SingleValueWidget<T> extends BaseWidget<T, JawabanSingle> {
    protected EditText editText;
    protected abstract void setOnFocusChangeListenerS(View v, boolean hasFocus);
    protected abstract void beforeTextChangedS(CharSequence s, int start, int count, int after);
    protected abstract void onTextChangedS(CharSequence s, int start, int before, int count);
    protected abstract void afterTextChangedS(Editable s);

    public SingleValueWidget(Context context) {
        super(context, JawabanSingle.class);
    }

    @Override
    protected void onGetPertanyaan(PertanyaanResponse question) {
        super.onGetPertanyaan(question);
        this.editText = new EditText(getContext());
        if(transmedikaSettings.getFontRegular()!=null)
            this.editText.setTypeface(getFace(getContext(),transmedikaSettings.getFontRegular()));
        int padding = TransmedikaUtils.dip2px(getContext(),10);
        int margin = TransmedikaUtils.dip2px(getContext(),(float) 1.25);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.setMargins(0,padding,0,0);
        this.editText.setLayoutParams(param);
        this.editText.setPadding(padding,0,padding,padding);
        this.editText.setIncludeFontPadding(true);
        //this.editText.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        if(question.getTypeInput()!=null) {
            this.editText.setRawInputType(question.getInputTypeAndroid());
        }else {
            this.editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        }
        if(question.getMaxLength()!=null) this.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(question.getMaxLength())});
        if(question.getHint()!=null) this.editText.setHint(question.getHint());
        this.editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        this.editText.setOnFocusChangeListener(this::setOnFocusChangeListenerS);

        this.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextChangedS(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextChangedS(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                afterTextChangedS(s);
            }
        });
    }


    protected static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
