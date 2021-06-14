package com.transmedika.transmedikakitui.widget.dynamicview;

import android.content.Context;

import com.transmedika.transmedikakitui.models.bean.JawabanDetail;
import com.transmedika.transmedikakitui.models.bean.JawabanSingle;
import com.transmedika.transmedikakitui.models.bean.json.PertanyaanResponse;
import com.transmedika.transmedikakitui.utils.Constants;


public class SingleValueRoot extends Root<JawabanSingle, SingleValueWidget<?>> {

    public SingleValueRoot(Context context, PertanyaanResponse pertanyaan) {
        super(context, pertanyaan);
    }

    @Override
    protected void onTypePertanyaan(@PertanyaanResponse.TYPE_VIEW String type, @PertanyaanResponse.TYPE_INPUT String typeInput) {
        switch (type) {
            case PertanyaanResponse.TYPE_EDITTEXT: {
                switch (typeInput) {
                    case PertanyaanResponse.TYPE_INPUT_DATE:
                        addView(new DateViewValue(context));
                        break;
                    case PertanyaanResponse.TYPE_INPUT_TIME:
                        addView(new TimeViewValue(context));
                        break;
                    case PertanyaanResponse.TYPE_INPUT_DATETIME:
                        addView(new DateTimeViewValue(context));
                        break;
                    default:
                        addView(new EditTextValue(context));
                        break;
                }
                break;
            }
            case PertanyaanResponse.TYPE_RADIO_GROUP:
                addView(new RadioGroupValue(context));
                break;
            case PertanyaanResponse.TYPE_TEXTAREA:
                addView(new EditTextMultilineValue(context));
                break;
        }
    }

    public JawabanDetail createJawabanFromString(String jawabanDetail) {
        JawabanDetail jd = new JawabanDetail();
        switch (getPertanyaan().getTipeView()) {
            case PertanyaanResponse.TYPE_EDITTEXT:
            case PertanyaanResponse.TYPE_TEXTAREA:
                jd.setJawaban(jawabanDetail);
                break;
            case PertanyaanResponse.TYPE_RADIO_GROUP:
                for (PertanyaanResponse.Jawaban pj: getPertanyaan().getJawabans()) {
                    if (pj.getJawaban().equalsIgnoreCase(jawabanDetail)) {
                        jd.setIdJawaban(pj.getIdJawaban());
                        jd.setJawaban(pj.getJawaban());
                        break;
                    } else if (pj.getJawaban().equals(Constants.LAINNYYA)) {
                        jd.setIdJawaban(pj.getIdJawaban());
                        jd.setJawaban(jawabanDetail);
                        break;
                    }
                }
                break;
        }
        return jd;
    }
}