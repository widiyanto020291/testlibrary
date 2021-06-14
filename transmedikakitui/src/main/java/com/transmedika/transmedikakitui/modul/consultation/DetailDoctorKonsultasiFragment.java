package com.transmedika.transmedikakitui.modul.consultation;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RootBindingFragment;
import com.transmedika.transmedikakitui.contract.consultation.DetailDokterKonsultasiContract;
import com.transmedika.transmedikakitui.databinding.FragmentDetailDokterKonsultasiBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.join.KeluargaJoin;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Clinic;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.JenisPembayaran;
import com.transmedika.transmedikakitui.models.bean.json.Konsultasi;
import com.transmedika.transmedikakitui.models.bean.json.Profile;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.models.bean.json.Voucher;
import com.transmedika.transmedikakitui.models.bean.json.param.JawabanParam;
import com.transmedika.transmedikakitui.models.bean.json.param.KonsultasiKlinikParam;
import com.transmedika.transmedikakitui.models.bean.json.param.VoucherParam;
import com.transmedika.transmedikakitui.modul.pin.ManagePinActivity;
import com.transmedika.transmedikakitui.modul.pin.PinTask;
import com.transmedika.transmedikakitui.presenter.consultation.DetailDokterKonsultasiPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.ImageLoader;
import com.transmedika.transmedikakitui.utils.MsgUiUtil;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;
import com.transmedika.transmedikakitui.widget.DiscView;
import com.transmedika.transmedikakitui.widget.NetkromEditText;
import com.transmedika.transmedikakitui.widget.ProfileHeaderView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailDoctorKonsultasiFragment extends RootBindingFragment<FragmentDetailDokterKonsultasiBinding, DetailDokterKonsultasiContract.View, DetailDokterKonsultasiPresenter>
        implements DetailDokterKonsultasiContract.View {
    private static final String TAG = DetailDoctorKonsultasiFragment.class.getSimpleName();
    private Doctor dokter;
    private Clinic klinik;
    private SignIn signIn;
    private Specialist spesialis;
    private final List<Profile> keluargas = new ArrayList<>();
    private Profile keluarga;
    private Voucher voucher;
    private static final int REQ_PERTANYAAN = 2;
    private static final int REQ_PEMBAYARAN = 3;
    private List<JawabanParam> jawabans;
    private JenisPembayaran jenisPembayaran;
    private final Rect scrollBounds = new Rect();

    public static DetailDoctorKonsultasiFragment newInstance(Doctor dokter, String transitionName) {

        Bundle args = new Bundle();
        DetailDoctorKonsultasiFragment fragment = new DetailDoctorKonsultasiFragment();
        args.putParcelable(Constants.DATA, dokter);
        args.putString(Constants.ARG_TRANSITION_IMAGE, transitionName);
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailDoctorKonsultasiFragment newInstance(Doctor dokter, Clinic klinik, Specialist spesialis) {

        Bundle args = new Bundle();
        DetailDoctorKonsultasiFragment fragment = new DetailDoctorKonsultasiFragment();
        args.putParcelable(Constants.DATA, dokter);
        args.putParcelable(Constants.KLINIK, klinik);
        args.putParcelable(Constants.SPESIALIS, spesialis);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected DetailDokterKonsultasiContract.View getBaseView() {
        return this;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new DetailDokterKonsultasiPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(view, savedInstanceState);
        signIn = mPresenter.selectLogin();
        if(getArguments()!=null) {
            dokter = getArguments().getParcelable(Constants.DATA);
            klinik = getArguments().getParcelable(Constants.KLINIK);
            spesialis = getArguments().getParcelable(Constants.SPESIALIS);
        }
    }

    @Override
    protected FragmentDetailDokterKonsultasiBinding getViewBinding(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return FragmentDetailDokterKonsultasiBinding.inflate(inflater);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setmLoadingResource(R.layout.detail_dokter_konsultasi_place_holder);
        super.initEventAndData(bundle);
        binding.viewMain.getHitRect(scrollBounds);
        setUpListener();
        setToolBar(dokter.getSpecialist());
        setSp();
        keluarga();
        setOnRefreshClickListener(() -> {
            setUsingDialog(false);
            keluarga();
        });

        showForm(klinik != null && klinik.getId() != null);

        if(transmedikaSettings.getFontBold()!=null){
            binding.tvNama.setCustomFont(mContext, transmedikaSettings.getFontBold());
        }

        binding.viewMain.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                Log.i(TAG, "Scroll DOWN");
                isVisibleFloating();
            }

            if (scrollY < oldScrollY) {
                Log.i(TAG, "Scroll UP");
                isVisibleFloating();
            }

            if (scrollY == 0) {
                Log.i(TAG, "TOP SCROLL");
            }

            if (scrollY == ( v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight() )) {
                Log.i(TAG, "BOTTOM SCROLL");
            }
        });

        if(transmedikaSettings.getButtonPrimaryBackground()!=null){
            binding.btnKonfirmasi.setBackground(ResourcesCompat.getDrawable(getResources(),
                    TransmedikaUtils.setDrawable(mContext,transmedikaSettings.getButtonPrimaryBackground()),null));

            binding.btnKonfirmasiFloating.setBackground(ResourcesCompat.getDrawable(getResources(),
                    TransmedikaUtils.setDrawable(mContext,transmedikaSettings.getButtonPrimaryBackground()),null));
        }
    }

    private void setToolBar(String a){
        binding.toolbar.setNavigationOnClickListener(v -> mActivity.finish());
        binding.toolbar.setTitle(a);
    }

    private void isVisibleFloating(){
        if (binding.btnKonfirmasi.getLocalVisibleRect(scrollBounds)) {
            binding.llFloating.setVisibility(View.GONE);
        } else {
            binding.llFloating.setVisibility(View.VISIBLE);
        }
    }

    private void showForm(boolean b){
        binding.discView.setEstimasi(dokter.getRates());
        if(b) {
            binding.edNote.setVisibility(View.GONE);
            binding.llFormKonsultasi.setVisibility(View.VISIBLE);
        }else {
            binding.edNote.setVisibility(View.VISIBLE);
            binding.llFormKonsultasi.setVisibility(View.GONE);
        }

        if(binding.discView.getEstimasi() == 0){
            binding.otherPaymentView.setVisibility(View.GONE);
            binding.discView.getLlDisc().setVisibility(View.GONE);
        }else {
            binding.otherPaymentView.setVisibility(View.VISIBLE);
            binding.discView.getLlDisc().setVisibility(View.VISIBLE);
        }
    }

    private void keluarga(){
        mPresenter.keluarga(signIn.getaUTHTOKEN(), dokter.getUuid(), mContext);
    }

    private void setUpListener(){
        binding.discView.setDiscListener(new DiscView.DiscListener() {
            @Override
            public void updateHarga(long estimasi, String kodeVoucher, long totalVoucher) {

            }

            @Override
            public void voucher(NetkromEditText voucherCode) {
                setUsingDialog(true);
                VoucherParam param = new VoucherParam();
                param.setTransactionType(1);
                param.setVoucherCode(Objects.requireNonNull(voucherCode.getText()).toString());
                mPresenter.voucher(signIn.getaUTHTOKEN(), param, mContext);
            }
        });

        binding.otherPaymentView.setOtherPaymentListener(() -> {
            /*double potongan = binding.discView.getPotongan() == 0 ? 0 : binding.discView.getEstimasi() - binding.discView.getPotongan();
            Bundle b = new Bundle();
            b.putParcelable(Constants.DATA, dokter);
            b.putDouble(Constants.POTONGAN, potongan);
            Intent i = new Intent(mContext, JenisPembayaranActivity.class);
            i.putExtra(Constants.DATA_USER, b);
            startActivityForResult(i, REQ_PEMBAYARAN);*/
        });

        binding.btnKonfirmasi.setOnClickListener(v -> {
            gotoPinManager();
        });

        binding.btnKonfirmasiFloating.setOnClickListener(v -> {
            gotoPinManager();
        });

        binding.saldoView.setSaloListener(b -> {
            if(klinik==null){
                disableBtn(b);
            }else {
                if(!b){
                    disableBtn(!validForm());
                }else {
                    disableBtn(true);
                }
            }
        });

        binding.llFormKonsultasi.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putParcelable(Constants.DATA, klinik);
            b.putParcelable(Constants.SPESIALIS, spesialis);
            Intent i = new Intent(mContext, PertanyaanActivity.class);
            i.putExtra(Constants.DATA_USER, b);
            startActivityForResult(i, REQ_PERTANYAAN);
        });

        binding.saldoView.getRbJenisBayar().setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                binding.saldoView.cekSaldo(binding.discView.getEstimasi(), binding.discView.getPotongan());
                binding.otherPaymentView.getRadioButton().setChecked(false);
                binding.saldoView.getRbJenisBayar().setChecked(true);
            }
        });

        binding.otherPaymentView.getRadioButton().setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                binding.saldoView.getRbJenisBayar().setChecked(false);
                binding.otherPaymentView.getRadioButton().setChecked(true);
                if(validForm() && jenisPembayaran!=null){
                    disableBtn(false);
                }
            }
        });
    }

    private void gotoPinManager() {
        if(klinik==null){
                startActivityForResult(ManagePinActivity.createIntent(mContext, PinTask.CONFIRM), 200);
        }else {
            if (valid()) {
                startActivityForResult(ManagePinActivity.createIntent(mContext, PinTask.CONFIRM), 200);
            }
        }
    }

    private void requestConsultation(String pin){
        setUsingDialog(true);
        KonsultasiKlinikParam param = new KonsultasiKlinikParam();
        param.setUuidPatient(keluarga.getUuid());
        param.setEmailPatient(signIn.getEmail());
        param.setUuidDoctor(dokter.getUuid());
        param.setEmailDoctor(dokter.getEmail());
        param.setRates(dokter.getRates());
        param.setComplaint(Objects.requireNonNull(binding.edNote.getText()).toString());
        if(jenisPembayaran!=null && jenisPembayaran.getAccountName()!=null && binding.otherPaymentView.getRadioButton().isChecked()){
            param.setPaymentId(jenisPembayaran.getId());
            param.setPaymentName(jenisPembayaran.getPaymentName());
        }else {
            param.setPaymentId(binding.saldoView.getGlAccount().getPaymentId());
            param.setPaymentName(binding.saldoView.getGlAccount().getPaymentName());
        }
        param.setPin(pin);
        //TODO tambah transmerchant id
        //param.setTransMerchantId("");
        if(voucher!=null) {
            param.setVoucher(voucher.getSlug());
        }else {
            param.setVoucher(Constants.EMPTY_STRING);
        }
        param.setVoucherAmount(binding.discView.getPotongan());

        if(klinik==null){
            mPresenter.konsultasi(signIn.getaUTHTOKEN(), param, mContext);
        }else {
            param.setMedicalFacilityId(klinik.getId());
            param.setJawabanParams(TransmedikaUtils.gsonBuilder().toJson(jawabans));
            mPresenter.konsultasiKlinik(signIn.getaUTHTOKEN(), param, mContext);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200 && resultCode == RESULT_OK){
            if(data!=null && data.getExtras().getString(Constants.PIN)!=null){
                String pin = data.getExtras().getString(Constants.PIN);
                requestConsultation(pin);

            }
        }else if(requestCode == REQ_PERTANYAAN && resultCode == RESULT_OK){
            if (data != null) {
                Bundle b = data.getBundleExtra(Constants.DATA_USER);
                if (b != null) {
                    jawabans = new ArrayList<>(b.getParcelableArrayList(Constants.DATA));
                    binding.rbForm.setChecked(true);
                    binding.tvWajibDiisi.setVisibility(View.GONE);
                    if((binding.discView.getEstimasi() == 0) && dokter.getStatusDocter().equals(Constants.ONLINE)) {
                        disableBtn(false);
                    }else {
                        if(binding.saldoView.getRbJenisBayar().isChecked()){
                            if(binding.saldoView.isSaldoCukup() && dokter.getStatusDocter().equals(Constants.ONLINE)) {
                                disableBtn(false);
                            }
                        }

                        if(binding.otherPaymentView.getRadioButton().isChecked()){
                            if (jenisPembayaran != null && dokter.getStatusDocter().equals(Constants.ONLINE)) {
                                disableBtn(false);
                            }
                        }

                    }
                }
            }
        }else if(requestCode == REQ_PEMBAYARAN && resultCode == RESULT_OK){
            if (data != null) {
                Bundle b = data.getBundleExtra(Constants.DATA_USER);
                if (b != null) {
                    boolean status = b.getBoolean(Constants.DATA);
                    jenisPembayaran = b.getParcelable(Constants.JENIS_PEMBAYARAN);
                    if(status) {
                        binding.saldoView.getRbJenisBayar().setChecked(false);
                        binding.otherPaymentView.showPembayaranTerpilih(true,jenisPembayaran);
                        checkPembayaranAndJawaban();
                    }
                }
            }
        }
    }

    private void checkPembayaranAndJawaban(){
        if((jenisPembayaran!=null) && (jawabans!=null && jawabans.size() > 0) && (dokter.getStatusDocter().equals(Constants.ONLINE))){
            disableBtn(false);
        }
    }

    private boolean valid(){
        /*boolean valid = true;
        if(!binding.saldoView.getRbJenisBayar().isChecked()) {
            valid = false;
            MsgUiUtil.showSnackBar(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0),
                    getString(R.string.pilih_jenis_pembayaran), mContext, R.drawable.ic_info_24dp, R.color.red, Snackbar.LENGTH_LONG);
        }*/
        return true;
    }

    private boolean validForm(){
        return jawabans != null;
    }

    private void setSp(){
        binding.profileHeader.setKeluargaData(keluargas);
        binding.profileHeader.setHeaderListener(new ProfileHeaderView.HeaderListener() {
            @Override
            public void onEditClickListener() {
                //start(KesehatanUmumFragment.newInstance(keluarga));
            }

            @Override
            public void onDataSelected(Profile profil) {
                keluarga = profil;
            }
        });
    }


    private void showData(Doctor dokter){
        binding.tvNama.setText(dokter.getFullName());
        binding.tvSpesialis.setText(dokter.getSpecialist());
        ImageLoader.load(mContext, dokter.getProfileDoctor(), binding.imgProfileDokter, R.color.line_light);
        binding.discView.showData();
        binding.tvDesc.setText(dokter.getDescription());
        binding.tvStatus.setText(dokter.getDescription());

        if(dokter.getStatusDocter().equals(Constants.ONLINE)){
            binding.imgStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_online));
            binding.btnKonfirmasi.setEnabled(true);
            binding.btnKonfirmasiFloating.setEnabled(true);
            binding.tvDesc.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_online1));
            binding.tvDesc.setTextColor(ContextCompat.getColor(mContext, R.color.green));
            binding.tvStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_status_online));
        }else if(dokter.getStatusDocter().equals(Constants.OFFLINE)) {
            binding.imgStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_offline));
            binding.btnKonfirmasi.setEnabled(false);
            binding.btnKonfirmasiFloating.setEnabled(false);
            binding.tvDesc.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_offline1));
            binding.tvDesc.setTextColor(ContextCompat.getColor(mContext, R.color.gray1));
            binding.tvStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_status_offline));
        }else {
            binding.imgStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_busy));
            binding.btnKonfirmasi.setEnabled(false);
            binding.btnKonfirmasiFloating.setEnabled(false);
            binding.tvDesc.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_busy1));
            binding.tvDesc.setTextColor(ContextCompat.getColor(mContext, R.color.red_dark));
            binding.tvStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_status_busy));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        return super.onBackPressedSupport();
    }

    @Override
    public void konsultasiResp(BaseResponse<Konsultasi> response) {
        moveKonsultasi(response);
    }

    @Override
    public void konsultasiKlinikResp(BaseResponse<Konsultasi> response) {
        moveKonsultasi(response);
    }

    private void moveKonsultasi(BaseResponse<Konsultasi> response){
        if(response.getData()!=null) {
            dokter.setDeviceId(response.getData().getDoctor().getDeviceId());
            Konsultasi konsultasi = response.getData();
            konsultasi.setPatient(keluarga);
            konsultasi.setDoctor(dokter);
            konsultasi.setComplaint(Objects.requireNonNull(binding.edNote.getText()).toString());
            if(klinik!=null){
                konsultasi.setKlinik(klinik);
            }
            start(TungguDokterFragment.newInstance(konsultasi));
        }else{
            MsgUiUtil.showSnackBar(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0),
                    response.getMessages(),mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void keluargaResp(KeluargaJoin join) {
        if(join.getProfil()!=null){
            if(join.getProfil().getData()!=null){
                keluarga = join.getProfil().getData();
                keluargas.add(join.getProfil().getData());
                binding.profileHeader.setKeluargaSpinnerValue(keluarga);
            }
        }

        if(join.getKeluargas()!=null){
            if(join.getKeluargas().getData().size() > 0){
                keluargas.addAll(join.getKeluargas().getData());
            }
        }

        binding.profileHeader.keluargaNotifyChange();

        if(join.getDokter()!=null){
            showData(join.getDokter().getData());
        }

        if(join.getGlAccount()!=null && join.getGlAccount().getData()!=null && join.getGlAccount().getData().getGLAccountBalance()!=null){
            binding.saldoView.setShowData(join.getGlAccount().getData(), binding.discView.getEstimasi(), binding.discView.getPotongan());
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        isVisibleFloating();
    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        binding.llFloating.setVisibility(View.GONE);
    }

    @Override
    public void voucherResp(BaseResponse<Voucher> response) {
        binding.discView.setDataDisc(response);
        this.voucher = response.getData();
        binding.saldoView.cekSaldo(binding.discView.getEstimasi(), binding.discView.getPotongan());
    }

    private void disableBtn(boolean b){
        if(b){
            binding.btnKonfirmasiFloating.setEnabled(false);
            binding.btnKonfirmasi.setEnabled(false);
        }else {
            binding.btnKonfirmasiFloating.setEnabled(true);
            binding.btnKonfirmasi.setEnabled(true);
        }
    }

    public void onUpdatedProfile(Profile profil) {
        keluarga = profil;
        int pos = keluargas.indexOf(profil);
        keluargas.set(pos, profil);
        binding.profileHeader.dataSelected(profil);
    }

    public void onUpdatedKeluarga(Profile profil) {
        keluarga = profil;
        int pos = keluargas.indexOf(profil);
        keluargas.set(pos, profil);
        binding.profileHeader.dataSelected(profil);
    }
}