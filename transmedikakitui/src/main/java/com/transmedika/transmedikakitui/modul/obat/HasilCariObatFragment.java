package com.transmedika.transmedikakitui.modul.obat;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RootBindingFragment;
import com.transmedika.transmedikakitui.component.RxBus;
import com.transmedika.transmedikakitui.contract.obat.HasilCariObatContract;
import com.transmedika.transmedikakitui.databinding.FragmentHasilCariObatBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.Alamat;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.CariObat;
import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.bean.json.Obat2;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.CariObatParam;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.modul.AlamatActivity;
import com.transmedika.transmedikakitui.presenter.obat.HasilCariObatPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.MsgUiUtil;
import com.transmedika.transmedikakitui.utils.PathUtil;
import com.transmedika.transmedikakitui.utils.RxUtil;
import com.transmedika.transmedikakitui.utils.SpanUtils;

import net.sourceforge.opencamera.MainCameraActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HasilCariObatFragment extends RootBindingFragment<FragmentHasilCariObatBinding, HasilCariObatContract.View, HasilCariObatPresenter>
    implements HasilCariObatContract.View{

    private static final String LON = "LON";
    private static final String LAT = "LAT";
    private SignIn signIn;
    private List<Obat2> obats = new ArrayList<>();
    private HasilCariObatAdapter obatAdapter;
    private IObat iObat;
    private Double lon;
    private Double lat;
    private static final int RQ_ALAMAT= 111;
    private Long idCari;
    private Long orderId = null;
    private static final int REQ_IMG = 200;
    private final List<Uri> uriUploads = new ArrayList<>();
    private SpanUtils spanUtils;

    @Override
    protected HasilCariObatContract.View getBaseView() {
        return this;
    }

    @Override
    protected FragmentHasilCariObatBinding getViewBinding(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return FragmentHasilCariObatBinding.inflate(inflater);
    }

    public static HasilCariObatFragment newInstance(Double lon, Double lat) {

        Bundle args = new Bundle();

        HasilCariObatFragment fragment = new HasilCariObatFragment();
        args.putDouble(LON, lon);
        args.putDouble(LAT, lat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        iObat = (IObat) context;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new HasilCariObatPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(view, savedInstanceState);
        signIn = mPresenter.selectLogin();
        if(getArguments()!=null){
            lon = getArguments().getDouble(LON);
            lat = getArguments().getDouble(LAT);

            /*Lokasi Netkrom
            lat = -6.896171;
            lon = 107.637755;*/
        }
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setmLoadingResource(R.layout.cari_obat_place_holder);
        super.initEventAndData(bundle);
        setToolBar(getString(R.string.detail_pembelian));
        setUpListener();
        setRv();
        setAlamat();
        cariObat();

        spanUtils = new SpanUtils(mContext);
        spanUpdateAlamat(getString(R.string.ganti_alamat));
        spanUtils.setSpanClickListener(() -> {
            Bundle b = new Bundle();
            b.putBoolean(Constants.FLAG, true);
            Intent i = new Intent(mContext, AlamatActivity.class);
            i.putExtra(Constants.DATA_USER, b);
            startActivityForResult(i,RQ_ALAMAT);
        });
        binding.inputAlamat.etNote.setText(mPresenter.getCatatanOrder());
        enablebtnPesan();
        binding.llTambahGambar.llTambahGambar.setOnClickListener(v -> mPresenter.checkPermission(new RxPermissions(_mActivity), mContext));
    }

    private void spanUpdateAlamat(String name) {
        spanUtils.setSpan(binding.tvUbahAlamat, name, getResources().getColor(R.color.colorPrimary), name);
    }

    private void setUpListener(){
        setOnRefreshClickListener(this::cariObat);

        binding.btnTambah.setOnClickListener(v -> {
            mPresenter.setCatatanOrder(Objects.requireNonNull(binding.inputAlamat.etNote.getText()).toString());
            back();
        });

//        binding.inputAlamat.etNote.setOnClickListener(v -> {
//            View view = View.inflate(mContext, R.layout.dialog_note, null);
//            NetkromEditText mEdNoteD = view.findViewById(R.id.ed_note_d);
//            NetkromButton btnKonfirmasi = view.findViewById(R.id.btn_konfirmasi);
//            mEdNoteD.setText(Objects.requireNonNull(binding.inputAlamat.etNote.getText()).toString());
//            BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
//            btnKonfirmasi.setOnClickListener(vi -> {
//                hideSoftInput();
//                binding.inputAlamat.etNote.setText(mEdNoteD.getText());
//                dialog.dismiss();
//            });
//            dialog.setContentView(view);
//            dialog.show();
//        });
    }

    private void tambahGambar(int idx, Uri uri){
        View view = getLayoutInflater().inflate(R.layout.item_unggah_resep, (ViewGroup) getView(), false);
        ImageView imgClose = view.findViewById(R.id.img_close);
        ImageView img = view.findViewById(R.id.img);
        imgClose.setVisibility(View.VISIBLE);
        RelativeLayout mLlMain = view.findViewById(R.id.ll_tambah_gambar);
        mLlMain.setOnClickListener(v -> {
            Uri uriP = FileProvider.getUriForFile(mContext, transmedikaSettings.getApplicationId() + ".fileprovider", new File(PathUtil.getPath(mContext, uri)));
            String mime = mContext.getContentResolver().getType(uriP);
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            newIntent.setDataAndType(uriP,mime);
            newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(newIntent);
            } catch (ActivityNotFoundException e) {
                MsgUiUtil.showSnackBar(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0),
                        getString(R.string.tidak_ada_aplikasi_buka_file),mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
            }
        });
        imgClose.setOnClickListener(v -> {
            binding.llUnggahResep.removeView(view);
            uriUploads.remove(uri);

            if(uriUploads.size() < 3){
                binding.llTambahGambar.llTambahGambar.setVisibility(View.VISIBLE);
            }

        });
        Glide.with(mContext).load(uri).thumbnail(0.1f).apply(new RequestOptions().centerCrop()).into(img);
        uriUploads.add(idx,uri);
        binding.llUnggahResep.addView(view,idx);
        if(uriUploads.size() >= 3){
            binding.llTambahGambar.llTambahGambar.setVisibility(View.GONE);
        }
    }

    private void setToolBar(String title){
        binding.toolbar.setNavigationOnClickListener(v -> mActivity.onBackPressed());
        binding.toolbar.setTitle(title);
    }

    private void back(){
        Intent i = new Intent(mContext, KategoriObatActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        mActivity.finish();
    }


    private void cariObat(){
        List<Obat> obatLocal = mPresenter.selectObat();
        List<CariObatParam.Obat> obats = new ArrayList<>();
        for (Obat obat:obatLocal){
            CariObatParam.Obat obat1 = new CariObatParam.Obat();
            obat1.setMedicine(obat.getSlug());
            obat1.setQty(obat.getJumlah());
            obat1.setPrescriptionId(obat.getPrescriptionId());
            obats.add(obat1);
        }

        CariObatParam param = new CariObatParam();
        param.setLat(lat);
        param.setLon(lon);
        param.setObats(obats);
        if(orderId!=null)
            param.setOrderId(orderId);
        mPresenter.cariObat(signIn.getaUTHTOKEN(),param, mContext);
    }

    private void setAlamat(){
        RxUtil.runOnUi(o -> {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                String address = addresses.get(0).getAddressLine(0);
                /*String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();*/
                binding.inputAlamat.etAlamat.setText(address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    void enablebtnPesan(){
        if (getActivity() instanceof ObatMainActivity) {
            ((ObatMainActivity) getActivity()).enablebtnPesan(false);
        }
    }

    public Alamat getAlamat() {
        Alamat alamat = new Alamat();
        alamat.setMapLat(String.valueOf(lat));
        alamat.setMapLng(String.valueOf(lon));
        alamat.setAddress(Objects.requireNonNull(binding.inputAlamat.etAlamat.getText()).toString());
        alamat.setNote(Objects.requireNonNull(binding.inputAlamat.etNote.getText()).toString());
        return alamat;
    }

    public List<Obat2> getObats() {
        return obats;
    }

    public void setObats(List<Obat2> obats) {
        this.obats = obats;
        obatAdapter.notifyDataSetChanged();
        calculateObat();
    }

    public List<Uri> getUriUploads() {
        return uriUploads;
    }

    private void setRv() {
        obatAdapter = new HasilCariObatAdapter(mContext, obats);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        binding.rv.addItemDecoration(itemDecorator);
        binding.rv.setLayoutManager(linearLayoutManager);
        binding.rv.setAdapter(obatAdapter);
        binding.rv.setItemAnimator(null);
        obatAdapter.setOnItemClickListener(new HasilCariObatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Obat2 dokter, int pos) {

            }

            @Override
            public void onPlusItemClick(Obat2 o, int pos) {
                obats.set(pos,o);
                mPresenter.insertObat(obat(o));
                sendBroadcast(obat(o));
                calculateObat();
            }

            @Override
            public void onMinusItemClick(Obat2 o, int pos) {
                obats.set(pos,o);
                mPresenter.insertObat(obat(o));
                sendBroadcast(obat(o));
                calculateObat();
            }

            @Override
            public void onDelete(Obat2 o, int pos) {
                obats.remove(o);
                obatAdapter.notifyItemRemoved(pos);
                sendBroadcast(obat(o));
                mPresenter.deleteObat(o.getSlug());
                calculateObat();
            }

            @Override
            public void onPerbaharuiPlus(Obat2 o, int pos) {
                obats.set(pos,o);
                showBtnPerbaharui(true);
                mPresenter.insertObat(obat(o));
                sendBroadcast(obat(o));
                calculateObat();
            }

            @Override
            public void onPerbaharuiMinus(Obat2 o, int pos) {
                obats.set(pos,o);
                showBtnPerbaharui(false);
                mPresenter.insertObat(obat(o));
                sendBroadcast(obat(o));
                calculateObat();
            }

            @Override
            public void onDeleteTemp(Obat2 o, int pos) {
                o.setJumlah(0);
                obats.remove(o);
                obatAdapter.notifyItemRemoved(pos);
                sendBroadcast(obat(o));
                mPresenter.deleteObat(o.getSlug());
            }
        });
    }

    private void sendBroadcast(Obat obat){
        BroadcastEvents.Event event = new BroadcastEvents.Event();
        event.setInitString(Constants.UPDATE_OBAT);
        event.setObject(obat);
        RxBus.getDefault().post(new BroadcastEvents(event));
    }

    private Obat obat(Obat2 obat2){
        Obat obat = mPresenter.selectObat(obat2.getSlug());
        obat.setPrices(obat2.getPrice());
        obat.setSlug(obat2.getSlug());
        obat.setJumlah(obat2.getJumlah());
        obat.setMinPrices(obat2.getMinPrices());
        obat.setMaxPrices(obat2.getMaxPrices());
        return obat;
    }

    @Override
    public void cariObatResp(BaseResponse<CariObat> response) {
        obats.clear();
        if(response.getData()!=null){
            if(response.getData().getObats().size() > 0){
                obats.addAll(response.getData().getObats());
                this.idCari = response.getData().getId();
                calculateObat();
                if(response.getData().getPerbaharui()!=null && response.getData().getPerbaharui()){
                    showBtnPerbaharui(true);
                }
            }
        }
        obatAdapter.notifyDataSetChanged();
    }

    @Override
    public void jump() {
        Intent i = new Intent(mContext, MainCameraActivity.class);
        startActivityForResult(i, REQ_IMG);
    }

    private void showBtnPerbaharui(boolean b){
        if (getActivity() instanceof ObatMainActivity) {
            if (b) {
                ((ObatMainActivity) getActivity()).getBinding().btnPerbaharui.setVisibility(View.VISIBLE);
                ((ObatMainActivity) getActivity()).getBinding().llPesan.setVisibility(View.GONE);
            } else {
                ((ObatMainActivity) getActivity()).getBinding().btnPerbaharui.setVisibility(View.GONE);
                ((ObatMainActivity) getActivity()).getBinding().llPesan.setVisibility(View.VISIBLE);
            }
        }
    }

    private void calculateObat(){
        long total = 0;
        List<Obat2> obat2s = new ArrayList<>();
        for(Obat2 obat:obats){
            if(obat.getAvailable() && obat.getStatus()){
                obat2s.add(obat);
                total = total + (obat.getPrice() * obat.getJumlah());
            }
        }
        iObat.calculateRealObat(obat2s.size(), total, idCari);
    }

    @Override
    public boolean onBackPressedSupport() {
        return super.onBackPressedSupport();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RQ_ALAMAT && resultCode == Activity.RESULT_OK){
            if(data!=null) {
                Bundle b = data.getBundleExtra(Constants.DATA_USER);
                if (b != null) {
                    idCari = null;
                    obats.clear();
                    obatAdapter.notifyDataSetChanged();
                    Alamat alamat = b.getParcelable(Constants.DATA);
                    if (alamat != null) {
                        lon = Double.valueOf(alamat.getMapLng());
                        lat = Double.valueOf(alamat.getMapLat());
                        if (alamat.getAddressType() == null || alamat.getAddressType().isEmpty()) {
                            binding.tvUbahAlamat.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            spanUpdateAlamat(getString(R.string.ganti_alamat));
                        } else {
                            @DrawableRes int resId = -1;
                            switch (alamat.getAddressType()) {
                                case Constants.ALAMAT_LAIN:
                                    resId = R.drawable.ic_alamat_lain;
                                    break;
                                case Constants.ALAMAT_KANTOR:
                                    resId = R.drawable.ic_alamat_kantor;
                                    break;
                                case Constants.ALAMAT_RUMAH:
                                    resId = R.drawable.ic_alamat_rumah;
                                    break;
                            }

                            if (resId != -1) {
                                binding.tvUbahAlamat.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(mContext, resId), null, null, null);
                                spanUpdateAlamat(alamat.getAddressType());
                            }
                        }
                        binding.inputAlamat.etNote.setText(alamat.getNote());
                        setAlamat();
                    }
                    cariObat();
                }
            }
        }else if(requestCode == REQ_IMG && resultCode == Activity.RESULT_OK){
            if (data != null) {
                String from = data.getStringExtra("image_or_video");
                Uri uri = data.getData();
                if(uri!=null && from!=null) {
                    if(from.equals("IMAGE")){
                        tambahGambar(binding.llUnggahResep.getChildCount()-1, uri);
                    }
                }
            }
        }
    }

    @Override
    public void onDataIsEmpty() {
        super.onDataIsEmpty();
        calculateObat();
    }
}
