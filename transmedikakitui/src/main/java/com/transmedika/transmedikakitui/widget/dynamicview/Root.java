package com.transmedika.transmedikakitui.widget.dynamicview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.models.bean.BaseJawaban;
import com.transmedika.transmedikakitui.models.bean.json.PertanyaanResponse;
import com.transmedika.transmedikakitui.utils.SpanUtils;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.lang.reflect.InvocationTargetException;

import static com.transmedika.transmedikakitui.utils.TransmedikaUtils.getFace;


public abstract class Root<RESULT extends BaseJawaban, T extends BaseWidget<?, RESULT>>
        implements BaseErrorListener {
    private final PertanyaanResponse pertanyaan;
    private LinearLayout view;
    protected final Context context;
    private T widget;
    private TextView tvError;
    private boolean valid = true;
    private String messageErrorValidation;
    private boolean isReset = false;
    protected TransmedikaSettings transmedikaSettings;

    abstract protected void onTypePertanyaan(@PertanyaanResponse.TYPE_VIEW String type, @PertanyaanResponse.TYPE_INPUT String typeInput);

    public Root(Context context, PertanyaanResponse pertanyaan) {
        this.context = context;
        this.pertanyaan = pertanyaan;
        transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);
        init();
    }

    protected void init() {
        Class<?> viewClass;
        try {
            viewClass = Class.forName(LinearLayout.class.getCanonicalName());
            view = (LinearLayout) viewClass.getConstructor(Context.class).newInstance(new Object[] { context });
            view.setOrientation(LinearLayout.VERTICAL);
            view.setBackgroundResource(R.drawable.bg_pertanyaan);

            view.addView(tvPertanyaan(context));

            onTypePertanyaan(pertanyaan.getTipeView(), pertanyaan.getTypeInput());

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    protected void addView(T widget) {
        this.widget = widget;
        this.widget.setErrorListener(this);
        this.widget.initBaseJawaban(pertanyaan);
        View widgetView  = widget.getView();
        widgetView.setTag(pertanyaan.getIdPertanyaan());
        view.addView(widgetView);
    }

    public void setJawaban(RESULT jawaban) {
        widget.setJawaban(jawaban);
    }

    public void resetJawaban() {
        isReset = true;
        widget.resetJawaban();
    }

    public LinearLayout getView() {
        return view;
    }

    public TextView tvError(Context context){
        TextView tv = new TextView(context);
        tv.setText(context.getString(R.string.pertanyaan_ini_harus_diisi));
        if(transmedikaSettings.getFontRegular()!=null)
            tv.setTypeface(getFace(context,transmedikaSettings.getFontRegular()));
        tv.setTextColor(context.getResources().getColor(R.color.red));
        tv.setTextSize(12);
        tv.setPadding(TransmedikaUtils.dip2px(context,10),0,TransmedikaUtils.dip2px(context,10),TransmedikaUtils.dip2px(context,10));
        return tv;
    }

    protected TextView tvPertanyaan(Context context){
        TextView tvPertanyaan = new TextView(context);
        if(transmedikaSettings.getFontRegular()!=null)
            tvPertanyaan.setTypeface(getFace(context,transmedikaSettings.getFontRegular()));
        tvPertanyaan.setText(pertanyaan.getPertanyaan());
        tvPertanyaan.setBackground(AppCompatResources.getDrawable(context,R.drawable.bg_tv_pertanyaan));
        tvPertanyaan.setPadding(TransmedikaUtils.dip2px(context,10),TransmedikaUtils.dip2px(context,10),TransmedikaUtils.dip2px(context,10),TransmedikaUtils.dip2px(context,10));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = TransmedikaUtils.dip2px(context,(float) 1.25);
        params.setMargins(margin,margin,margin,0);
        tvPertanyaan.setLayoutParams(params);
        String textPertanyaan;
        if(pertanyaan.getMandadori()){
            textPertanyaan = pertanyaan.getPertanyaan().concat(" \u002A");
            SpanUtils spanUtils = new SpanUtils(context);
            spanUtils.setSpanWithoutUnderline(tvPertanyaan, textPertanyaan,
                    context.getResources().getColor(R.color.red)," \u002A");
        }


        return tvPertanyaan;
    }

    public PertanyaanResponse getPertanyaan() {
        return pertanyaan;
    }

    public T getWidget() {
        return widget;
    }

    public RESULT getJawaban() {
        return widget.getJawaban();
    }

    public RESULT getJawabanWithoutSet() {
        return widget.getJawabanWithoutSet();
    }

    @Override
    public void showError(String message) {
        if (isReset) {
            isReset = false;
            return;
        }
        if(isMandatory()) {
            if (tvError == null) {
                tvError = tvError(context);
            } else {
                view.removeView(tvError);
            }

            if (message != null && !message.isEmpty()) {
                tvError.setText(message);
            }else {
                tvError.setText(context.getString(R.string.required));
            }
            view.addView(tvError);
            view.setBackgroundResource(R.drawable.ed_normal_red);
        }
    }

    @Override
    public void validationError(String message) {
        this.messageErrorValidation = message;
        this.valid = false;
        if(!message.isEmpty()) {
            if (tvError == null) {
                tvError = tvError(context);
            }else {
                view.removeView(tvError);
            }
            tvError.setText(message);
            view.addView(tvError);
            view.setBackgroundResource(R.drawable.ed_normal_red);
        }
    }

    @Override
    public void hideError() {
        if(isMandatory()) {
            if (tvError != null) {
                view.removeView(tvError);
                view.setBackgroundResource(R.drawable.bg_pertanyaan);
            }
        }

        valid = true;
    }

    @Override
    public void hideValidationError() {
        if (tvError != null) {
            view.removeView(tvError);
            view.setBackgroundResource(R.drawable.ed_normal);
            valid = true;
        }
    }

    public boolean isMandatory() {
        return pertanyaan.getMandadori();
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessageErrorValidation() {
        return messageErrorValidation;
    }
}
