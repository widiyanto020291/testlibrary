package com.transmedika.transmedikakitui.modul.consultation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RootBindingActivity;
import com.transmedika.transmedikakitui.contract.consultation.SpecialistContract;
import com.transmedika.transmedikakitui.databinding.ActivitySpecialistBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.modul.CommonCategoriesAdapter;
import com.transmedika.transmedikakitui.presenter.consultation.SpecialistPresenter;
import com.transmedika.transmedikakitui.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpecialistActivity extends RootBindingActivity<ActivitySpecialistBinding, SpecialistContract.View, SpecialistPresenter>
        implements SpecialistContract.View, RootBindingActivity.OnRefreshClickListener{

    private CommonCategoriesAdapter<Specialist> commonCategoriesAdapter;
    private final List<Specialist> spesialis = new ArrayList<>();
    private SignIn uSignIn;

    @Override
    protected ActivitySpecialistBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivitySpecialistBinding.inflate(inflater);
    }

    @NonNull
    @NotNull
    @Override
    protected SpecialistContract.View getBaseView() {
        return this;
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        mPresenter = new SpecialistPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(bundle);
        uSignIn = mPresenter.selectLogin();
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setmLoadingResource(R.layout.categori_place_holder);
        super.initEventAndData(bundle);
        setRv();
        req();
        setOnRefreshClickListener(this);
    }

    private void req(){
        mPresenter.specialist(uSignIn.getaUTHTOKEN(), mContext);
    }

    private void setRv(){
        commonCategoriesAdapter = new CommonCategoriesAdapter<>(mContext, spesialis);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mContext, 3);
        binding.viewMain.setLayoutManager(gridLayoutManager1);
        binding.viewMain.setAdapter(commonCategoriesAdapter);
        commonCategoriesAdapter.setOnItemClickListener((o, pos) -> {
            if(o instanceof  Specialist) {
                Specialist spesialis = (Specialist) o;
                Bundle b = new Bundle();
                b.putParcelable(Constants.DATA, spesialis);
                b.putParcelable(Constants.KLINIK, null);
                Intent i = new Intent(mContext, DoctorActivity.class);
                i.putExtra(Constants.DATA_USER, b);
                startActivity(i);
            }
        });
    }

    @Override
    public void specialistResp(BaseResponse<List<Specialist>> response) {
        if(response.getData()!=null){
            if(response.getData().size()>0){
                spesialis.addAll(response.getData());
                commonCategoriesAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void getBroadcastEvents(BroadcastEvents.Event event) {
        if(event.getInitString().equals(Constants.BROADCAST_FINISH_KONSULTASI)){
            finish();
        }
    }


    @Override
    public void onRefreshClick() {
        req();
    }
}
