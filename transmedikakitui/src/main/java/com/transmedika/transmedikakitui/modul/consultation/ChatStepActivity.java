package com.transmedika.transmedikakitui.modul.consultation;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.SimpleBindingActivity;
import com.transmedika.transmedikakitui.databinding.CommonFrameFragmentBinding;
import com.transmedika.transmedikakitui.models.bean.json.Clinic;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.Profile;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.modul.IPhr;
import com.transmedika.transmedikakitui.utils.Constants;


import java.util.Objects;

public class ChatStepActivity extends SimpleBindingActivity<CommonFrameFragmentBinding> implements IPhr {
    private Doctor dokter;
    private Clinic klinik;
    private Specialist spesialis;
    private DetailDoctorKonsultasiFragment detailDoctorKonsultasiFragment;

    @Override
    protected void initEventAndData(Bundle bundle) {

    }

    @Override
    protected CommonFrameFragmentBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return CommonFrameFragmentBinding.inflate(inflater);
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        super.onViewCreated(bundle);
        Bundle b = getIntent().getBundleExtra(Constants.DATA_USER);
        if(b!=null) {
            dokter = Objects.requireNonNull(b).getParcelable(Constants.DATA);
            klinik = Objects.requireNonNull(b).getParcelable(Constants.KLINIK);
            spesialis = Objects.requireNonNull(b).getParcelable(Constants.SPESIALIS);
        }

        if (findFragment(DetailDoctorKonsultasiFragment.class) == null) {
            detailDoctorKonsultasiFragment = DetailDoctorKonsultasiFragment.newInstance(dokter, klinik, spesialis);
            if(dokter!=null) {
                loadRootFragment(R.id.fl_container, detailDoctorKonsultasiFragment);
            }
        }
    }

    @Override
    public void onUpdatedProfile(Profile profil) {
        detailDoctorKonsultasiFragment.onUpdatedProfile(profil);
    }

    @Override
    public void onUpdatedKeluarga(Profile profil) {
        detailDoctorKonsultasiFragment.onUpdatedKeluarga(profil);
    }
}