package com.transmedika.transmedikakitui.modul.obat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseBindingActivity;
import com.transmedika.transmedikakitui.contract.obat.ObatMainContract;
import com.transmedika.transmedikakitui.databinding.ActivityObatMainBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.Alamat;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.KategoriObat;
import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.bean.json.Obat2;
import com.transmedika.transmedikakitui.models.bean.json.Order;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.presenter.obat.ObatMainPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.MsgUiUtil;
import com.transmedika.transmedikakitui.utils.PathUtil;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;
import com.transmedika.transmedikakitui.widget.NetkromDialog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ObatMainActivity extends BaseBindingActivity<ActivityObatMainBinding, ObatMainContract.View, ObatMainPresenter>
        implements ObatMainContract.View, IObat {

    private KategoriObat kategoriObat;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates = false;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private boolean clickPesan = false;
    private long estimasi = 0;
    private Long idCari;
    private SignIn signIn;
    private List<Obat2> obats = new ArrayList<>();
    private Alamat alamat;
    private int currentPage = 0;
    private String kodeVoucher;
    private long totalVoucher;
    private Long orderId;
    private List<Uri> uris = new ArrayList<>();
    private MsgUiUtil msgUiUtil;

    @NonNull
    @NotNull
    @Override
    protected ObatMainContract.View getBaseView() {
        return this;
    }

    @Override
    protected ActivityObatMainBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivityObatMainBinding.inflate(inflater);
    }

    public ActivityObatMainBinding getBinding(){
        return binding;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        data();
    }


    @Override
    protected void onViewCreated(Bundle bundle) {
        mPresenter = new ObatMainPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(bundle);
        msgUiUtil = new MsgUiUtil(mContext);
        signIn = mPresenter.selectLogin();
        init();
        setUpListener();
        Bundle b = getIntent().getBundleExtra(Constants.DATA_USER);
        if(b!=null) {
            kategoriObat = Objects.requireNonNull(b).getParcelable(Constants.DATA);
            if(b.getString(Constants.ORDER_ID)!=null) {
                orderId = Long.valueOf(b.getString(Constants.ORDER_ID));
            }
            if (findFragment(ObatFragment.class) == null) {
                if (kategoriObat != null) {
                    loadRootFragment(R.id.fl_container, ObatFragment.newInstance(kategoriObat));
                }else {

                    /* comment by widy loadRootFragment(R.id.fl_container, BlankFragment.newInstance());
                    mPresenter.checkPermissionLocation(new RxPermissions(this), mContext);
                    currentPage = 1;*/
                }
            }
        }else {
            /* comment by widy if (findFragment(BlankFragment.class) == null) {
                loadRootFragment(R.id.fl_container, BlankFragment.newInstance());
                mPresenter.checkPermissionLocation(new RxPermissions(this), mContext);
                currentPage = 1;
            }*/
        }
        checkExistObat();
    }

    private void setUpListener(){
        binding.btnPesan.setOnClickListener(v -> {
            if(currentPage==1){
                currentPage = 2 ;
                getDataPesanan();
                /* comment by widy start(PembayaranObatFragment.newInstance(estimasi)); */
            }else if(currentPage == 2) {
                currentPage =3;
            }else {
                mPresenter.checkPermissionLocation(new RxPermissions(ObatMainActivity.this), mContext);
            }
        });

        binding.btnPerbaharui.setOnClickListener(v -> {
            getDataPesanan();
            mPresenter.order(obats);
        });

        binding.btnKonfirmasi.setOnClickListener(v -> {
            if(currentPage == 2){
                /* comment by widy startActivityForResult(ManagePinActivity.createIntent(mContext, PinTask.CONFIRM), 200);*/
            }
        });
    }


    @Override
    public void calculateObat() {
        data();
        checkExistObat();
    }

    @Override
    public void calculateRealObat(int jml, long total, Long idCari) {
        this.idCari = idCari;
        estimasi = total;
        binding.tvEstimasi.setText(getString(R.string.total_harga).concat(TransmedikaUtils.numberFormat().format(total)));
        binding.tvJumlahObat.setText(String.valueOf(jml));
        checkExistObat();
        enablebtnPesan(estimasi > 0);
    }

    @Override
    public void calculatePembayaran(long total, String kodeVoucher, long totalVoucher) {
        this.kodeVoucher = kodeVoucher;
        this.totalVoucher = totalVoucher;
        estimasi = total;
        binding.tvEstimasi.setText(getString(R.string.total_harga).concat(TransmedikaUtils.numberFormat().format(total)));
    }

    private void getDataPesanan(){
        SupportFragment currentFragment = (SupportFragment) getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if(currentFragment instanceof HasilCariObatFragment) {
            alamat = ((HasilCariObatFragment) currentFragment).getAlamat();
            obats = ((HasilCariObatFragment) currentFragment).getObats();
            uris = ((HasilCariObatFragment) currentFragment).getUriUploads();
        }
    }

    private void pesanObat(String pin){
        SupportFragment currentFragment = (SupportFragment) getSupportFragmentManager().findFragmentById(R.id.fl_container);
        /* comment by widy if(currentFragment instanceof PembayaranObatFragment) {
            GLAccount glAccount = ((PembayaranObatFragment) currentFragment).getGlAccount();
            PesanObatParam param = new PesanObatParam();
            List<PesanObatParam.Obat3> obats3 = new ArrayList<>();
            param.setId(idCari);
            param.setTotal(estimasi);
            param.setAddress(alamat.getAddress());
            param.setMapLat(alamat.getMapLat());
            param.setMapLon(alamat.getMapLng());
            param.setNote(alamat.getNote());
            param.setVoucher(kodeVoucher);
            param.setVoucherAmpunt(totalVoucher);
            param.setPaymentId(glAccount.getPaymentId());
            param.setPaymentName(glAccount.getPaymentName());
            param.setPin(pin);
            for (Obat2 obat : obats) {
                if(obat.getAvailable() && (obat.getStatus()!=null && obat.getStatus())) {
                    Obat obRealm = mPresenter.selectObat(obat.getSlug());
                    PesanObatParam.Obat3 obat3 = new PesanObatParam.Obat3();
                    obat3.setName(obat.getName());
                    obat3.setSlug(obat.getSlug());
                    obat3.setUnit(obat.getUnit());
                    obat3.setPrescriptionId(obRealm.getPrescriptionId());
                    obat3.setPrice(obat.getPrice());
                    obat3.setQty(obat.getJumlah());
                    obats3.add(obat3);
                }
            }
            param.setObat3s(obats3);
            //pesan obat biasa
            //mPresenter.pesanObat(signIn.getaUTHTOKEN(), param, mContext);

            //pesan upload resep
            MultipartBody.Part[] images = new MultipartBody.Part[uris.size()];
            String dataS = TransmedikaUtils.gsonBuilder().toJson(param);
            RequestBody data = RequestBody.create(MediaType.parse("text/plain"), dataS);
            for (int i = 0; i < uris.size(); i++) {
                String path = PathUtil.getPath(mContext, uris.get(i));
                if(path!=null) {
                    File file = new File(path);
                    RequestBody surveyBody = RequestBody.create(MediaType.parse("file/*"), file);
                    images[i] = MultipartBody.Part.createFormData("images[]", com.transmedika.transmedikakitui.utils.TransmedikaUtils.getFileName(file), surveyBody);
                }else {
                    MsgUiUtil.showSnackBar(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0),
                            getString(R.string.lokasi_gambar_tidak_ditemukan), mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
                }
            }

            mPresenter.pesanObatUpload(signIn.getaUTHTOKEN(), images, data, mContext);
        }*/
    }

    void data(){
        this.idCari = null;
        List<Obat> obats = mPresenter.selectObat();
        binding.tvJumlahObat.setText(String.valueOf(obats.size()));
        estimasi = 0;
        for (Obat obat: obats){
            estimasi = estimasi + (obat.getMaxPrices()*obat.getJumlah());
        }
        if(obats.size() > 0) {
            binding.tvEstimasi.setText(getString(R.string.estimasi).concat(TransmedikaUtils.numberFormat().format(estimasi)));
            binding.tvEstimasi.setVisibility(View.VISIBLE);
        }else {
            binding.tvEstimasi.setVisibility(View.INVISIBLE);
        }
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mSettingsClient = LocationServices.getSettingsClient(mContext);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //findLoc(false);
                mCurrentLocation = locationResult.getLastLocation();
                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Constants.LOCATION_CHANGE_INTERVAL_);
        mLocationRequest.setFastestInterval(Constants.LOCATION_CHANGE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            msgUiUtil.dismisPgCommon();
            if(this.kategoriObat!=null) {
                if (clickPesan) {
                    SupportFragment currentFragment = (SupportFragment) getSupportFragmentManager().findFragmentById(R.id.fl_container);
                    if (currentFragment instanceof ObatFragment || currentFragment instanceof DetailObatFragment) {
                        moveToCariObat();
                    }
                }
            }else {
                /* comment by widy SupportFragment currentFragment = (SupportFragment) getSupportFragmentManager().findFragmentById(R.id.fl_container);
                if (currentFragment instanceof BlankFragment) {
                    moveToCariObat();
                }*/
            }
            clickPesan = false;
        }
    }

    private void moveToCariObat(){
        HasilCariObatFragment hasilCariObatFragment = HasilCariObatFragment.newInstance(mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude());
        hasilCariObatFragment.setOrderId(orderId);
        start(hasilCariObatFragment);
    }

    @Override
    public void getMyLocation() {
        mRequestingLocationUpdates = true;
        currentPage = 1;
        clickPesan = true;
        if(mCurrentLocation==null){
            msgUiUtil.showPgCommon();
            startLocationUpdates();
        }else {
            msgUiUtil.dismisPgCommon();
            SupportFragment currentFragment = (SupportFragment) getSupportFragmentManager().findFragmentById(R.id.fl_container);
            if(this.kategoriObat!=null) {
                if (currentFragment instanceof ObatFragment || currentFragment instanceof DetailObatFragment) {
                    moveToCariObat();
                }
            }else {
                /* comment by widy if (currentFragment instanceof BlankFragment) {
                    moveToCariObat();
                }*/
            }
        }
    }

    @Override
    public void pesanResp(BaseResponse<Order> response) {
        NetkromDialog netkromDialog;
        if(response.getData()!=null) {
            netkromDialog = new NetkromDialog(mContext, R.drawable.ic_info_checklis, getString(R.string.selamat_transaksi_berhasil), true);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                netkromDialog.dismiss();
                finish();
            }, 1000);
        }else {
            netkromDialog = new NetkromDialog(mContext, R.drawable.ic_gagal, response.getMessages(), true);
        }
        netkromDialog.show();
    }

    @Override
    public void pesanUploadResp(BaseResponse<Order> response) {
        NetkromDialog netkromDialog;
        if(response.getData()!=null) {
            netkromDialog = new NetkromDialog(mContext, R.drawable.ic_info_checklis, getString(R.string.selamat_transaksi_berhasil), true, Constants.ALERT_CORRECT);
        }else {
            netkromDialog = new NetkromDialog(mContext, R.drawable.ic_gagal, response.getMessages(), true, Constants.ALERT_FAILED);
        }
        netkromDialog.setOnCancelListener(dialog -> finish());
        netkromDialog.show();
    }

    @Override
    public void callBackList(List<Obat2> obat2s) {
        this.obats = obat2s;
        SupportFragment currentFragment = (SupportFragment) getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if(currentFragment instanceof HasilCariObatFragment) {
            ((HasilCariObatFragment) currentFragment).setObats(obats);
            binding.btnPerbaharui.setVisibility(View.GONE);
            binding.llPesan.setVisibility(View.VISIBLE);
        }
    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponseOnSuccessListener)
                .addOnFailureListener(this, onFailureListener);
    }

    public void stopLocationUpdates() {
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, task -> { });
    }

    private final OnSuccessListener<LocationSettingsResponse> locationSettingsResponseOnSuccessListener = new OnSuccessListener<LocationSettingsResponse>() {
        @SuppressLint("MissingPermission")
        @Override
        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
            //findLoc(true);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        }
    };

    private final OnFailureListener onFailureListener = e -> {
        int statusCode = ((ApiException) e).getStatusCode();
        switch (statusCode) {
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    ResolvableApiException rae = (ResolvableApiException) e;
                    rae.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sie) {
                    Log.i(Constants.TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                msgUiUtil.dismisPgCommon();
                MsgUiUtil.showSnackBar(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0),
                        getString(R.string.setting_change_unavailable),mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
        }
        updateLocationUI();
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CHECK_SETTINGS){
            if(resultCode == Activity.RESULT_OK){
                if (mRequestingLocationUpdates) {
                    startLocationUpdates();
                }
            }

            if(resultCode == Activity.RESULT_CANCELED){
                mRequestingLocationUpdates = false;
                currentPage = 0;
                clickPesan = false;
                msgUiUtil.dismisPgCommon();
                SupportFragment currentFragment = (SupportFragment) getSupportFragmentManager().findFragmentById(R.id.fl_container);
                /* comment by widy if(currentFragment instanceof BlankFragment) {
                    finish();
                }*/
            }
        }else if(requestCode == 200 && resultCode == RESULT_OK){
            if(data!=null && data.getExtras().getString(Constants.PIN)!=null){
                String pin = data.getExtras().getString(Constants.PIN);
                pesanObat(pin);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestingLocationUpdates) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onBackPressedSupport() {
        SupportFragment currentFragment = (SupportFragment) getSupportFragmentManager().findFragmentById(R.id.fl_container);
         if(currentFragment instanceof HasilCariObatFragment){
            calculateObat();
            currentPage = 0;
            mPresenter.setCatatanOrder(null);
            if(this.kategoriObat!=null){
                currentFragment.pop();
                binding.btnKonfirmasi.setVisibility(View.GONE);
                binding.btnPerbaharui.setVisibility(View.GONE);
                binding.llPesan.setVisibility(View.VISIBLE);
            }else {
                finish();
            }
        /*}else if(currentFragment instanceof PembayaranObatFragment) {
             binding.btnKonfirmasi.setVisibility(View.GONE);
             binding.llPesan.setVisibility(View.VISIBLE);
             currentPage = 1;
             currentFragment.pop();*/
         }else if(currentFragment instanceof DetailObatFragment) {
            currentPage = 0;
            currentFragment.pop();
        }else if(currentFragment instanceof ObatFragment /* comment by widy || currentFragment instanceof  BlankFragment*/){
            currentPage = 0;
            finish();
        }
    }

    private void checkExistObat(){
        List<Obat> obats = mPresenter.selectObat();
        enablebtnPesan(obats != null && obats.size() > 0);
    }

    void enablebtnPesan(boolean b){
        if(b){
            binding.btnPesan.setBackground(ContextCompat.getDrawable(mContext, R.drawable.btn_round));
            binding.btnPesan.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            binding.btnPesan.setEnabled(true);
        }else {
            binding.btnPesan.setBackground(ContextCompat.getDrawable(mContext, R.drawable.btn_round_disable));
            binding.btnPesan.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
            binding.btnPesan.setEnabled(false);
        }
    }
}
