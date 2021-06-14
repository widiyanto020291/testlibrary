package com.transmedika.transmedikakitui.modul.consultation;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.SimpleBindingActivity;
import com.transmedika.transmedikakitui.databinding.ActivityDetailCatatanDokterBinding;
import com.transmedika.transmedikakitui.models.bean.json.CatatanDokter;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.DateUtil;
import com.transmedika.transmedikakitui.utils.ImageLoader;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

public class DetailCatatanDokterActivity extends SimpleBindingActivity<ActivityDetailCatatanDokterBinding> {
    @Override
    protected ActivityDetailCatatanDokterBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivityDetailCatatanDokterBinding.inflate(inflater);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setToolBar(getString(R.string.catatan));
        Bundle b = getIntent().getBundleExtra(Constants.DATA_USER);
        if(b!=null) {
            Doctor dokter = Objects.requireNonNull(b).getParcelable(Constants.DATA);
            //Konsultasi konsultasi = Objects.requireNonNull(b).getParcelable(Constants.DATA_KONSULTASI);
            //Profil profil = Objects.requireNonNull(b).getParcelable(Constants.DATA_PROFILE);
            String message = Objects.requireNonNull(b).getString(Constants.DATA_MSG);
            Date createdDate = (Date) b.getSerializable(Constants.DATA_DATE);

            binding.tvDokterNama.setText(dokter.getFullName());
            binding.tvDokterSpesialis.setText(dokter.getSpecialist());
            binding.tvDokterNoStr.setText(dokter.getNoStr());
            ImageLoader.loadAll(mContext, dokter.getProfileDoctor(), binding.cvDokterProfile);
            if (message != null) {
                CatatanDokter catatanDokter = TransmedikaUtils.gsonBuilder().fromJson(message, CatatanDokter.class);
                binding.tvJadwalBerikutnya.setText(DateUtil.dateType9(catatanDokter.getDate()));
                binding.tvDate.setText(DateUtil.ddMMMyyyy(createdDate));
                binding.tvCatatan.setText(catatanDokter.getNote());
            }
        }
    }

    private void setToolBar(String title){
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.toolbar.setTitle(title);
    }
}
