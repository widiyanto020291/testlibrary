package com.transmedika.transmedikakitui.modul.consultation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RootBindingActivity;
import com.transmedika.transmedikakitui.contract.PertanyaanContract;
import com.transmedika.transmedikakitui.databinding.ActivityPertanyaanBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.Pertanyaan;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Clinic;
import com.transmedika.transmedikakitui.models.bean.json.PertanyaanResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.models.bean.json.param.FormParam;
import com.transmedika.transmedikakitui.models.bean.json.param.JawabanParam;
import com.transmedika.transmedikakitui.models.bean.realm.TempJawaban;
import com.transmedika.transmedikakitui.presenter.PertanyaanPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;
import com.transmedika.transmedikakitui.widget.NetkromButton;
import com.transmedika.transmedikakitui.widget.dynamicview.DynamicView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PertanyaanActivity extends RootBindingActivity<ActivityPertanyaanBinding, PertanyaanContract.View, PertanyaanPresenter>
        implements PertanyaanContract.View{


    private Clinic klinik;
    private Specialist spesialis;
    private SignIn signIn;
    private DynamicView dynamicView;

    @Override
    protected ActivityPertanyaanBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivityPertanyaanBinding.inflate(inflater);
    }

    @NonNull
    @NotNull
    @Override
    protected PertanyaanContract.View getBaseView() {
        return this;
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        mPresenter = new PertanyaanPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(bundle);
        Bundle b = getIntent().getBundleExtra(Constants.DATA_USER);
        if (b != null) {
            klinik = b.getParcelable(Constants.DATA);
            spesialis = b.getParcelable(Constants.SPESIALIS);
        }
        signIn = mPresenter.selectLogin();
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        super.initEventAndData(bundle);
        if(transmedikaSettings.getFontBold()!=null)
            binding.tvHeader.setCustomFont(mContext, transmedikaSettings.getFontBold());
        req();
        setOnRefreshClickListener(this::req);
    }

    private void req() {
        FormParam formParam = new FormParam();
        formParam.setMedicalFacilityId(klinik.getId());
        formParam.setSpecialistId(spesialis.getSlug());
        mPresenter.pertanyaan(signIn.getaUTHTOKEN(), formParam, mContext);
    }



    private List<Pertanyaan> getPertanyaan() {
        return new Gson().fromJson(TransmedikaUtils.getAssetsJSON(mContext, "json/sample.json"),
                new TypeToken<ArrayList<Pertanyaan>>() {
                }.getType());
    }

    @Override
    public void pertanyaanResp(BaseResponse<List<PertanyaanResponse>> response) {
        dynamicView = new DynamicView(response.getData(), PertanyaanActivity.this, binding.parentView, jawabanParams -> {
            final ArrayList<JawabanParam> jawabans = new ArrayList<>(jawabanParams);
            mPresenter.insertTempJawaban(new TempJawaban(klinik.getId(), spesialis.getSlug(),
                    new Gson().toJson(jawabans, new TypeToken<ArrayList<JawabanParam>>() {
                    }.getType())
            ));
            Bundle b = new Bundle();
            b.putParcelableArrayList(Constants.DATA, jawabans);
            Intent i = new Intent();
            i.putExtra(Constants.DATA_USER, b);
            setResult(Activity.RESULT_OK, i);
            finish();
        });
        dynamicView.setScrollView(binding.viewMain);

        NetkromButton button = new NetkromButton(mContext);
        if(transmedikaSettings.getFontRegular()!=null){
            button.setCustomFont(mContext,transmedikaSettings.getFontRegular());
        }

        if(transmedikaSettings.getButtonPrimaryBackground()!=null) {
            button.setBackground(ResourcesCompat.getDrawable(getResources(),
                    TransmedikaUtils.setDrawable(mContext, transmedikaSettings.getButtonPrimaryBackground()),null));
        }
        button.setText(getString(R.string.konfirmasi));
        button.setTextColor(getResources().getColor(R.color.white));
        binding.parentView.addView(button);

        button.setOnClickListener(v -> dynamicView.getValue());

        setTempJawaban();
    }

    private void setTempJawaban() {
        TempJawaban tempJawaban = mPresenter.getTempJawaban(klinik.getId(), spesialis.getSlug());
        if (tempJawaban != null) {
            ArrayList<JawabanParam> jawabanParams = new Gson().fromJson(tempJawaban.getJawaban(),
                    new TypeToken<ArrayList<JawabanParam>>() {
                    }.getType());
            dynamicView.setWidgetValueAll(jawabanParams);

            binding.clTempAvailable.setVisibility(View.VISIBLE);
            binding.btnJawabanBaru.setOnClickListener(view -> {
                dynamicView.resetAll();
                binding.clTempAvailable.setVisibility(View.GONE);
            });
            binding.ivClose.setOnClickListener(view -> binding.clTempAvailable.setVisibility(View.GONE));
        }
    }
}
