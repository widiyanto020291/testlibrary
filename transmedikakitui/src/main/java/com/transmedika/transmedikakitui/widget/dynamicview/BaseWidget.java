package com.transmedika.transmedikakitui.widget.dynamicview;

import android.content.Context;
import android.util.Log;
import android.view.View;


import com.transmedika.transmedikakitui.models.bean.BaseJawaban;
import com.transmedika.transmedikakitui.models.bean.json.PertanyaanResponse;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public abstract class BaseWidget<T, RESULT extends BaseJawaban> {
    protected static final String TAG = BaseWidget.class.getSimpleName();

    private final Context context;
    protected BaseErrorListener errorListener;
    //protected ChangeValueListener<T> changeValueListener;
    protected T t;

    protected RESULT jawaban;
    private final Class<RESULT> tClass;
    protected PertanyaanResponse pertanyaan;
    protected TransmedikaSettings transmedikaSettings;

    public BaseWidget(Context context, Class<RESULT> tClass) {
        this.context = context;
        this.tClass = tClass;
        transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);
    }

    protected void onGetPertanyaan(PertanyaanResponse pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public void initBaseJawaban(PertanyaanResponse pertanyaan) {
        onGetPertanyaan(pertanyaan);
        initJawaban();
    }

    private void initJawaban() {
        try {
            Constructor<RESULT> constructor = tClass.getConstructor();
            jawaban = constructor.newInstance();
            jawaban.setIdPertanyaan(pertanyaan.getIdPertanyaan());
            jawaban.setPertanyaan(pertanyaan.getPertanyaan());
            jawaban.setTipeView(pertanyaan.getTipeView());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void setJawaban(RESULT jawaban) {
        this.jawaban = jawaban;
        try {
            if (getView() != null) {
                onSetUiFromJawaban(jawaban);
            }
        } catch (Exception e) {
            Log.w(TAG, "setJawaban: ", e);
        }
    }

    public void resetJawaban() {
        initJawaban();
        try {
            if (getView() != null) {
                onResetJawaban();
            }
        } catch (Exception e) {
            Log.w(TAG, "setJawaban: ", e);
        }
    }

    public RESULT getJawaban() {
        onSetJawabanResult(jawaban);
        return jawaban;
    }

    public RESULT getJawabanWithoutSet() {
        return jawaban;
    }

    public void setErrorListener(BaseErrorListener listener) {
        errorListener = listener;
    }

   /* public void setChangeValueListener(ChangeValueListener<T> changeValueListener) {
        this.changeValueListener = changeValueListener;
    }

    protected void changeValue(T value) {
        if (changeValueListener != null)
            changeValueListener.onHaveValue(value);
    }*/

    protected void changeValue(T value){
        this.t = value;
    }

    public Context getContext() {
        return context;
    }

    abstract void onSetJawabanResult(RESULT jawaban);

    abstract void onSetUiFromJawaban(RESULT jawaban);

    abstract void onResetJawaban();

    public abstract T getValue();
    public abstract View getView();
}
