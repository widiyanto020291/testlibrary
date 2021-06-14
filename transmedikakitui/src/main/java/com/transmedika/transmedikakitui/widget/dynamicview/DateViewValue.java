package com.transmedika.transmedikakitui.widget.dynamicview;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.models.bean.JawabanDetail;
import com.transmedika.transmedikakitui.models.bean.JawabanSingle;
import com.transmedika.transmedikakitui.models.bean.json.PertanyaanResponse;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.DateUtil;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.util.Calendar;



public class DateViewValue extends SingleValueWidget<String> implements View.OnClickListener {
    private Calendar tgl;
    public DateViewValue(Context context) {
        super(context);
    }

    @Override
    protected void onGetPertanyaan(PertanyaanResponse question) {
        super.onGetPertanyaan(question);
        editText.setOnClickListener(this);
    }

    @Override
    protected void setOnFocusChangeListenerS(View v, boolean hasFocus) {
        editText.setInputType(InputType.TYPE_NULL);
        if(TextUtils.isEmpty(editText.getText().toString()) && !hasFocus){
            errorListener.showError("");
        }

        if(hasFocus){
            showDialog(v);
        }
    }

    @Override
    protected void beforeTextChangedS(CharSequence s, int start, int count, int after) {

    }

    @Override
    protected void onTextChangedS(CharSequence s, int start, int before, int count) {
        if(count > 0){
            errorListener.hideError();
        }else {
            errorListener.showError("");
        }
        changeValue(getValue());
    }

    @Override
    protected void afterTextChangedS(Editable s) {

    }

    @Override
    public void onClick(View v) {
        showDialog(v);
    }

    private void showDialog(View v){
        TransmedikaUtils.toggleSoftKeyBoard(getContext(), true,v);
        Calendar initCalendar = Calendar.getInstance();
        DatePickerDialog pickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, (view, year, month, dayOfMonth) -> {
            if (tgl == null) {
                tgl = initCalendar;
            }
            tgl.set(Calendar.YEAR, year);
            tgl.set(Calendar.MONTH, month);
            tgl.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            editText.setText(getValue() != null ? getValue() : "");
            editText.setError(null);

            changeValue(getValue());

        }, initCalendar.get(Calendar.YEAR), initCalendar.get(Calendar.MONTH), initCalendar.get(Calendar.DAY_OF_MONTH));

        pickerDialog.setOnShowListener(dialog -> {
            pickerDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
            pickerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        });


        pickerDialog.show();
    }

    @Override
    void onSetUiFromJawaban(JawabanSingle jawaban) {
        String answer = jawaban.getDetailJawaban().getJawaban();
        if (answer != null && !answer.isEmpty()) {
            if (tgl == null) {
                tgl = Calendar.getInstance();
            }
            tgl.setTime(DateUtil.toDate(answer, pertanyaan.getFormat() != null ? pertanyaan.getFormat() : Constants.DATE_TIME_ZONE_4));
            editText.setText(getValue() != null ? getValue() : "");
        }
    }

    @Override
    void onResetJawaban() {
        tgl = null;
        editText.setText("");
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

    public String getValue(){
        if (tgl != null) {
            return DateUtil.dateToString(tgl.getTime(),  pertanyaan.getFormat()!=null ? pertanyaan.getFormat() : Constants.DATE_TIME_ZONE_4);
        }
        return null;
    }

    public View getView(){
        return editText;
    }
}
