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
import com.transmedika.transmedikakitui.contract.consultation.SpesialisKlinikContract;
import com.transmedika.transmedikakitui.databinding.ActivityClinicSpecialistBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Clinic;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.modul.CommonCategoriesAdapter;
import com.transmedika.transmedikakitui.presenter.consultation.SpesialisKlinikPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.ImageLoader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpesialisKlinikActivity extends RootBindingActivity<ActivityClinicSpecialistBinding, SpesialisKlinikContract.View, SpesialisKlinikPresenter>
    implements SpesialisKlinikContract.View {

    private Clinic klinik;
    private SignIn signIn;
    private final List<Specialist> spesialis = new ArrayList<>();
    private CommonCategoriesAdapter<Specialist> commonCategoriesAdapter;
    private GridLayoutManager gridLayoutManager1;

    @NonNull
    @NotNull
    @Override
    protected SpesialisKlinikContract.View getBaseView() {
        return this;
    }

    @Override
    protected ActivityClinicSpecialistBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivityClinicSpecialistBinding.inflate(inflater);
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        mPresenter = new SpesialisKlinikPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(bundle);
        Bundle b = getIntent().getBundleExtra(Constants.DATA_USER);
        if (b != null) {
            klinik = b.getParcelable(Constants.DATA);
            ImageLoader.loadIcon(mContext, klinik.getImage(), binding.img, R.color.line_light);
            binding.tvAlamat.setText(klinik.getAddress());
            binding.tvNama.setText(klinik.getName());
        }
        signIn = mPresenter.selectLogin();
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setmLoadingResource(R.layout.clinic_specialist_place_holder);
        super.initEventAndData(bundle);
        setRv();
        mPresenter.spesialis(signIn.getaUTHTOKEN(), klinik.getId(), mContext);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setRv(){
        commonCategoriesAdapter = new CommonCategoriesAdapter<>(mContext, spesialis);
        gridLayoutManager1 = new GridLayoutManager(mContext, 2);
        binding.rv.setLayoutManager(gridLayoutManager1);
        binding.rv.setAdapter(commonCategoriesAdapter);
        commonCategoriesAdapter.setOnItemClickListener((o, pos) -> {
            if(o instanceof Specialist) {
                Specialist spesialis = (Specialist) o;
                Bundle b = new Bundle();
                b.putParcelable(Constants.DATA, spesialis);
                b.putParcelable(Constants.KLINIK, klinik);
                Intent i = new Intent(mContext, DoctorActivity.class);
                i.putExtra(Constants.DATA_USER, b);
                startActivity(i);
            }
        });
    }

    @Override
    public void spesialisResp(BaseResponse<List<Specialist>> response) {
        if(response.getData()!=null){
            if(response.getData().size()>0){
                if(response.getData().size() == 2){
                    gridLayoutManager1.setSpanCount(2);
                }else {
                    gridLayoutManager1.setSpanCount(3);
                }
                binding.rv.setLayoutManager(gridLayoutManager1);
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
}
