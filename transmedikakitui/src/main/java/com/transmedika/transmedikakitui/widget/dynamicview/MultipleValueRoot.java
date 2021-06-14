package com.transmedika.transmedikakitui.widget.dynamicview;

import android.content.Context;


import com.transmedika.transmedikakitui.models.bean.JawabanDetail;
import com.transmedika.transmedikakitui.models.bean.JawabanMultiple;
import com.transmedika.transmedikakitui.models.bean.Pertanyaan;
import com.transmedika.transmedikakitui.models.bean.json.PertanyaanResponse;
import com.transmedika.transmedikakitui.utils.Constants;

import java.util.ArrayList;
import java.util.List;


public class MultipleValueRoot extends Root<JawabanMultiple, MultipleValueWidget<?>> {

    public MultipleValueRoot(Context context, PertanyaanResponse pertanyaan) {
        super(context, pertanyaan);
    }

    @Override
    protected void onTypePertanyaan(String type, String inputType) {
        if(type.equals(Pertanyaan.TYPE_CHECK_GROUP)) {
            addView(new CheckGroupValue(context));
        }
    }

    public List<JawabanDetail> createJawabanFromString(String jawabanDetail) {
        final List<JawabanDetail> resultJd = new ArrayList<>();
        if(Pertanyaan.TYPE_CHECK_GROUP.equals(getPertanyaan().getTipeView())) {
            String[] listJawaban = jawabanDetail.split("\\|");
            for (String jawaban: listJawaban) {
                JawabanDetail jd = new JawabanDetail();
                for (PertanyaanResponse.Jawaban pj: getPertanyaan().getJawabans()) {
                    if (pj.getJawaban().equalsIgnoreCase(jawaban)) {
                        jd.setIdJawaban(pj.getIdJawaban());
                        jd.setJawaban(pj.getJawaban());
                        resultJd.add(jd);
                        break;
                    } else if (pj.getJawaban().equals(Constants.LAINNYYA)) {
                        jd.setIdJawaban(pj.getIdJawaban());
                        jd.setJawaban(jawaban);
                        resultJd.add(jd);
                        break;
                    }
                }
            }
        }
        return resultJd;
    }
}
