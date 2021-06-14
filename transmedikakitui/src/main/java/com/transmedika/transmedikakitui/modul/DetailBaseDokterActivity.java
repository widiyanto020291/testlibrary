package com.transmedika.transmedikakitui.modul;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;


import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RootBindingActivity;
import com.transmedika.transmedikakitui.contract.DetailDoctorContract;
import com.transmedika.transmedikakitui.databinding.ActivityDetailDoctorBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.Alumni;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Clinic;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.Facility;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.presenter.DetailDokterPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.ImageLoader;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import org.jetbrains.annotations.NotNull;

public class DetailBaseDokterActivity extends RootBindingActivity<ActivityDetailDoctorBinding, DetailDoctorContract.View, DetailDokterPresenter>
        implements DetailDoctorContract.View{

    protected Doctor dokter;
    protected Specialist spesialis;
    protected Clinic klinik;

    private SignIn signIn;

    @Override
    protected ActivityDetailDoctorBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivityDetailDoctorBinding.inflate(inflater);
    }

    @NonNull
    @NotNull
    @Override
    protected DetailDoctorContract.View getBaseView() {
        return this;
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        mPresenter = new DetailDokterPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(bundle);
        signIn = mPresenter.selectLogin();
        Bundle b = getIntent().getBundleExtra(Constants.DATA_USER);
        if (b != null) {
            dokter = b.getParcelable(Constants.DATA);
            klinik = b.getParcelable(Constants.KLINIK);
            spesialis = b.getParcelable(Constants.SPESIALIS);
        }
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setmLoadingResource(R.layout.detail_doctor_place_holder);
        super.initEventAndData(bundle);
        detailDokter();
        //showData(dokter);
        setOnRefreshClickListener(this::detailDokter);
        binding.detailDoctorView.getBtnChat().setOnClickListener(view -> onChatClick());
        binding.btnChatFloating.setOnClickListener(v -> onChatClick());

        if(transmedikaSettings.getButtonPrimaryBackground()!=null){
            binding.btnChatFloating.setBackground(ResourcesCompat.getDrawable(getResources(),
                    TransmedikaUtils.setDrawable(mContext,transmedikaSettings.getButtonPrimaryBackground()),null));

            binding.detailDoctorView.getBtnChat().setBackground(ResourcesCompat.getDrawable(getResources(),
                    TransmedikaUtils.setDrawable(mContext,transmedikaSettings.getButtonPrimaryBackground()),null));
        }
    }

    public void setToolBar(String title) {
        binding.toolbar.setTitle(title);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void detailDokter() {
        mPresenter.detailDokter(signIn.getaUTHTOKEN(), dokter.getUuid(), mContext);
    }

    protected void onChatClick() {}

    private void showData(Doctor dokter) {
        binding.detailDoctorView.getTvNama().setText(dokter.getFullName());
        binding.detailDoctorView.getTvSpesialis().setText(dokter.getSpecialist());
        binding.detailDoctorView.getTvDesc().setText(dokter.getDescription());
        binding.detailDoctorView.getTvStatus().setText(dokter.getDescription());
        ImageLoader.load(mContext, dokter.getProfileDoctor(), binding.detailDoctorView.getImgProfile());
        binding.detailDoctorView.getTvHarga().setText(TransmedikaUtils.numberFormat().format(dokter.getRates()));
        binding.detailDoctorView.getTvHargaDisc().setPaintFlags(binding.detailDoctorView.getTvHargaDisc().getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.detailDoctorView.getTvPengalaman().setText(dokter.getExperience());
        binding.detailDoctorView.getTvRating().setText(dokter.getRating() != null && !dokter.getRating().equals("0") ? dokter.getRating() : "-");
        binding.detailDoctorView.getTvJadwalKonsultasi().setText(dokter.getJadwalKonsul());
        /*if(dokter.getVoucher()!=null && dokter.getVoucher()!=0){
            mTvHargaDisc.setVisibility(View.VISIBLE);
            mTvHargaDisc.setText(SystemUtil.numberFormat().format(dokter.getVoucher()));
        }else {
            mTvHargaDisc.setVisibility(View.INVISIBLE);
        }*/

        if (dokter.getStatusDocter().equals(Constants.ONLINE)) {
            setStatusStyle(R.drawable.bg_online, true, R.drawable.bg_online1, R.color.green, R.drawable.bg_status_online);
        } else if (dokter.getStatusDocter().equals(Constants.OFFLINE)) {
            setStatusStyle(R.drawable.bg_offline, false, R.drawable.bg_offline1, R.color.gray1, R.drawable.bg_status_offline);
        } else {
            setStatusStyle(R.drawable.bg_busy, false, R.drawable.bg_busy1, R.color.red_dark, R.drawable.bg_status_busy);
        }
        binding.detailDoctorView.getTvStr().setText(dokter.getNoStr());

        StringBuilder strAlumni = new StringBuilder();
        StringBuilder strPraktik = new StringBuilder();
        if (dokter.getFacilities() != null && dokter.getFacilities().size() > 0) {
            int i = 0;
            for (Facility praktik : dokter.getFacilities()) {
                i++;
                if (i == dokter.getFacilities().size()) {
                    strPraktik.append(praktik.getFacilityName());
                } else {
                    strPraktik.append(praktik.getFacilityName().concat(System.lineSeparator()));
                }
            }
            binding.detailDoctorView.getTvPraktik().setText(strPraktik.toString());
        } else {
            binding.detailDoctorView.getTvPraktik().setText(Constants.STRIP);
        }

        if (dokter.getEducations() != null && dokter.getEducations().size() > 0) {
            int i = 0;
            for (Alumni alumni : dokter.getEducations()) {
                i++;
                if (i == dokter.getEducations().size()) {
                    strAlumni.append(alumni.getEducation());
                } else {
                    strAlumni.append(alumni.getEducation().concat(System.lineSeparator()));
                }

            }
            binding.detailDoctorView.getTvAlumni().setText(strAlumni.toString());
        } else {
            binding.detailDoctorView.getTvAlumni().setText(Constants.STRIP);
        }
    }

    private void setStatusStyle(int imgStatusBg, boolean statusBtn, int descBg, int descTextColor, int bgStatus) {
        binding.detailDoctorView.getImgStatus().setBackground(ContextCompat.getDrawable(mContext, imgStatusBg));
        binding.detailDoctorView.getBtnChat().setEnabled(statusBtn);
        binding.btnChatFloating.setEnabled(statusBtn);
        binding.detailDoctorView.getTvDesc().setBackground(ContextCompat.getDrawable(mContext, descBg));
        binding.detailDoctorView.getTvDesc().setTextColor(ContextCompat.getColor(mContext, descTextColor));
        binding.detailDoctorView.getTvStatus().setBackground(ContextCompat.getDrawable(mContext, bgStatus));
    }


    @Override
    public void detailDokterResp(BaseResponse<Doctor> response) {
        if (response.getData() != null) {
            showData(response.getData());
        }
    }
}
