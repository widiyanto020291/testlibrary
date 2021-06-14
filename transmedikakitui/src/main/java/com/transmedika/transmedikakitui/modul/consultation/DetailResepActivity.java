package com.transmedika.transmedikakitui.modul.consultation;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.SimpleBindingActivity;
import com.transmedika.transmedikakitui.databinding.CommonFrameFragmentBinding;
import com.transmedika.transmedikakitui.models.bean.json.Konsultasi;
import com.transmedika.transmedikakitui.utils.Constants;

import java.util.Objects;

public class DetailResepActivity extends SimpleBindingActivity<CommonFrameFragmentBinding> {

    @Override
    protected CommonFrameFragmentBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return CommonFrameFragmentBinding.inflate(inflater);
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        super.onViewCreated(bundle);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        Bundle b = getIntent().getBundleExtra(Constants.DATA_USER);
        Konsultasi konsultasi = Objects.requireNonNull(b).getParcelable(Constants.DATA_KONSULTASI);
        Long idKonsultasi = getIntent().getLongExtra(Constants.ID_KONSULTASI,0);
        konsultasi.setConsultationId(idKonsultasi);
        if (findFragment(DetailResepFragment.class) == null) {
            loadRootFragment(R.id.fl_container, DetailResepFragment.newInstance(konsultasi));
        }
    }
}
