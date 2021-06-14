package com.transmedika.transmedikakitui.modul.consultation;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;


import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RootBindingActivity;
import com.transmedika.transmedikakitui.contract.consultation.JawabanContract;
import com.transmedika.transmedikakitui.databinding.ActivityPertanyaanBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Jawaban;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.JawabanParam;
import com.transmedika.transmedikakitui.presenter.consultation.JawabanPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;
import com.transmedika.transmedikakitui.widget.NetkromTextView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class JawabanActivity extends RootBindingActivity<ActivityPertanyaanBinding, JawabanContract.View, JawabanPresenter>
        implements JawabanContract.View{

    private SignIn signIn;
    private Long konsultasiId;

    @NonNull
    @NotNull
    @Override
    protected JawabanContract.View getBaseView() {
        return this;
    }

    @Override
    protected ActivityPertanyaanBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivityPertanyaanBinding.inflate(inflater);
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        mPresenter = new JawabanPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(bundle);
        signIn = mPresenter.selectLogin();
        Bundle b = getIntent().getBundleExtra(Constants.DATA_USER);
        if (b != null) {
            konsultasiId = b.getLong(Constants.ID_KONSULTASI);
        }
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        super.initEventAndData(bundle);
        mPresenter.jawaban(signIn.getaUTHTOKEN(), konsultasiId, mContext);
        setToolBar();
    }

    private void setToolBar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.toolbar.setTitle(R.string.jawaban);
    }

    @Override
    public void jawabanResp(BaseResponse<Jawaban> response) {
        if (response != null && response.getData() != null) {
            if (response.getData().getListJawabanModel() != null) {
                for (JawabanParam jawaban : response.getData().getListJawabanModel()) {
                    Class<?> viewClass;
                    LinearLayout view;
                    try {
                        viewClass = Class.forName(LinearLayout.class.getCanonicalName());
                        view = (LinearLayout) viewClass.getConstructor(Context.class).newInstance(new Object[]{mContext});
                        view.setOrientation(LinearLayout.VERTICAL);
                        view.setBackgroundResource(R.drawable.ed_normal);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, TransmedikaUtils.dip2px(mContext, 5), 0, TransmedikaUtils.dip2px(mContext, 5));
                        view.setLayoutParams(params);
                        view.addView(tvPertanyaan(mContext, jawaban));
                        view.addView(tvJawaban(mContext, jawaban.getAnswer()));
                        binding.parentView.addView(view);
                    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected NetkromTextView tvPertanyaan(Context context, JawabanParam jawabanParam) {
        NetkromTextView tv = new NetkromTextView(context);
        if(transmedikaSettings.getFontRegular()!=null)
            tv.setCustomFont(mContext, transmedikaSettings.getFontRegular());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.textDefault, null));
        tv.setText(jawabanParam.getQuestion());
        return tv;
    }

    protected NetkromTextView tvJawaban(Context context, String jawaban) {
        NetkromTextView tv = new NetkromTextView(context);
        if(transmedikaSettings.getFontRegular()!=null)
            tv.setCustomFont(mContext, transmedikaSettings.getFontRegular());
        tv.setText(jawaban.replace("|", ", "));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, TransmedikaUtils.dip2px(mContext, 5), 0, 0);
        tv.setLayoutParams(params);
        return tv;
    }

}
