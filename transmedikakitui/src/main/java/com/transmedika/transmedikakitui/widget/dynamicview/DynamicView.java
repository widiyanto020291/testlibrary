package com.transmedika.transmedikakitui.widget.dynamicview;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;


import com.transmedika.transmedikakitui.models.bean.JawabanDetail;
import com.transmedika.transmedikakitui.models.bean.JawabanMultiple;
import com.transmedika.transmedikakitui.models.bean.JawabanSingle;
import com.transmedika.transmedikakitui.models.bean.json.PertanyaanResponse;
import com.transmedika.transmedikakitui.models.bean.json.param.JawabanParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DynamicView {
    private final List<PertanyaanResponse> pertanyaans;
    private final Context context;
    private final LinearLayout linearLayout;
    private final Map<Long, Root<?, ?>> viewMap = new HashMap<>();
    private ScrollView scrollView;
    private final DynamicViewListener dynamicViewListener;
    private final List<JawabanParam> jawabanParams = new ArrayList<>();

    public DynamicView(List<PertanyaanResponse> pertanyaans, Context context, LinearLayout linearLayout,
            DynamicViewListener dynamicViewListener) {
        this.pertanyaans = pertanyaans;
        this.context = context;
        this.linearLayout = linearLayout;
        this.dynamicViewListener = dynamicViewListener;
        this.init();
    }
    
    private void init(){
        for (PertanyaanResponse pertanyaan:pertanyaans){
            View view;
            Root<?, ?> root = null;
            if(pertanyaan.getTypeAnswer()!=null && pertanyaan.getTypeAnswer().equals(PertanyaanResponse.TYPE_ANSWER_SINGLE)){
                root = new SingleValueRoot(context, pertanyaan);
            }

            if(pertanyaan.getTypeAnswer()!=null && pertanyaan.getTypeAnswer().equals(PertanyaanResponse.TYPE_ANSWER_MULTIPLE)){
                root = new MultipleValueRoot(context, pertanyaan);
            }

            if (root != null) {
                view = root.getView();
                viewMap.put(pertanyaan.getIdPertanyaan(), root);
                if(view != null) {
                    linearLayout.addView(view);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
                    params.setMargins(0, 0, 0, 20);
                    view.setLayoutParams(params);
                }
            }
        }
    }

    public void getValue(){
        int dyBefore = 0;
        boolean firstErrorFind = false;
        for (Map.Entry<Long, Root<?, ?>> pair : viewMap.entrySet()) {
            Root<?, ?> root = pair.getValue();
            //Long id = pair.getKey();
            if(root instanceof SingleValueRoot){
                SingleValueRoot sV = (SingleValueRoot) root;
                if(root.getJawaban()!=null && sV.getJawaban().getDetailJawaban() != null) {
                    if(root.isValid()) {
                        JawabanDetail jawabanDetail = sV.getJawaban().getDetailJawaban();
                        Log.i("Pertanyaan Single", sV.getPertanyaan().getPertanyaan());
                        Log.i("Jawaban Single", jawabanDetail.toString() + '\n');
                        addJawaban(sV.getPertanyaan(), jawabanDetail.getJawaban());
                    }else {
                        root.validationError(root.getMessageErrorValidation());
                        firstErrorFind = true;
                    }
                } else {
                    if (root.isMandatory()) {
                        root.showError("");
                        firstErrorFind = true;
                    }else if(!root.isValid()){
                        root.validationError(root.getMessageErrorValidation());
                        firstErrorFind = true;
                    }
                }
            }

            if(root instanceof MultipleValueRoot){
                MultipleValueRoot mV = (MultipleValueRoot) root;
                if(root.getJawaban()!=null && mV.getJawaban().getDetailJawabans() != null){
                    if(root.isValid()) {
                        List<JawabanDetail> jawabanDetails = mV.getJawaban().getDetailJawabans();
                        Log.i("Pertanyaan Multiple", mV.getPertanyaan().getPertanyaan());
                        Log.i("Jawaban Multiple", jawabanDetails.toString() + '\n');

                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0 ; i < jawabanDetails.size() ; i++){
                            if(i == (jawabanDetails.size() - 1)){
                                stringBuilder.append(jawabanDetails.get(i).getJawaban());
                            }else {
                                stringBuilder.append(jawabanDetails.get(i).getJawaban()).append("|");
                            }
                        }

                        addJawaban(mV.getPertanyaan(), stringBuilder.toString());
                    }else {
                        root.validationError(root.getMessageErrorValidation());
                        firstErrorFind = true;
                    }
                } else {
                    if (root.isMandatory()) {
                        root.showError("");
                        firstErrorFind = true;
                    }else if(!root.isValid()){
                        root.validationError(root.getMessageErrorValidation());
                        firstErrorFind = true;
                    }
                }
            }

            if (!firstErrorFind) {
                dyBefore = getScroll(root.getView());
            }
        }

        if (firstErrorFind) {
            scrollView.smoothScrollTo(0, dyBefore);
        }else {
            dynamicViewListener.onFormIsValid(jawabanParams);
        }
    }

    public void setWidgetValueAll(List<JawabanParam> jawabanParams) {
        for (JawabanParam jawaban : jawabanParams) {
            setWidgetValue(jawaban);
        }
    }

    public void setWidgetValue(JawabanParam jawabanParam) {
        Root<?,?> root = viewMap.get(Long.parseLong(jawabanParam.getId()));
        if (root instanceof SingleValueRoot) {
            SingleValueRoot singleValueRoot = ((SingleValueRoot) root);
            JawabanSingle jawabanSingle = singleValueRoot.getJawabanWithoutSet();
            jawabanSingle.setDetailJawaban(singleValueRoot.createJawabanFromString(jawabanParam.getAnswer()));
            singleValueRoot.setJawaban(jawabanSingle);
        } else if (root instanceof MultipleValueRoot) {
            MultipleValueRoot multipleValueRoot = ((MultipleValueRoot) root);
            JawabanMultiple jawabanMultiple = multipleValueRoot.getJawabanWithoutSet();
            jawabanMultiple.setDetailJawabans(multipleValueRoot.createJawabanFromString(jawabanParam.getAnswer()));
            multipleValueRoot.setJawaban(jawabanMultiple);
        }
    }

    public void resetAll() {
        for (Map.Entry<Long, Root<?, ?>> pair : viewMap.entrySet()) {
            Root<?,?> root = pair.getValue();
            root.resetJawaban();
        }
    }

    private void addJawaban(PertanyaanResponse pertanyaanResponse, String jawaban){
        JawabanParam jawabanParam = new JawabanParam();
        jawabanParam.setId(String.valueOf(pertanyaanResponse.getIdPertanyaan()));
        jawabanParam.setQuestion(pertanyaanResponse.getPertanyaan());
        jawabanParam.setAnswer(jawaban);
        jawabanParams.remove(jawabanParam);
        jawabanParams.add(jawabanParam);

    }

    public void setScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    public int getScroll(LinearLayout selectedLayout) {
        if (selectedLayout == null || scrollView == null) {
            return 0;
        }
        Rect rect = new Rect();
        Rect linearLayoutRect = new Rect();
        Rect scrollViewRect = new Rect();
        selectedLayout.getHitRect(rect);
        linearLayout.getHitRect(linearLayoutRect);
        scrollView.getDrawingRect(scrollViewRect);
        return rect.bottom;
    }

    public interface DynamicViewListener{
        void onFormIsValid(List<JawabanParam> jawabanParams);
    }
}
