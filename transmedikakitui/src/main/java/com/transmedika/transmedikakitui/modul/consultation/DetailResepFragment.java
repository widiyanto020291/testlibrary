package com.transmedika.transmedikakitui.modul.consultation;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RootBindingActivity;
import com.transmedika.transmedikakitui.base.RootBindingFragment;
import com.transmedika.transmedikakitui.contract.consultation.DetailResepContract;
import com.transmedika.transmedikakitui.databinding.FragmentDetailResepBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Konsultasi;
import com.transmedika.transmedikakitui.models.bean.json.Resep;
import com.transmedika.transmedikakitui.models.bean.json.ResepObat;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.presenter.consultation.DetailResepPresenter;
import com.transmedika.transmedikakitui.presenter.consultation.ResepAdapter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.DateUtil;
import com.transmedika.transmedikakitui.utils.ImageLoader;
import com.transmedika.transmedikakitui.utils.MsgUiUtil;
import com.transmedika.transmedikakitui.widget.NetkromDialog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DetailResepFragment extends RootBindingFragment<FragmentDetailResepBinding, DetailResepContract.View, DetailResepPresenter>
    implements DetailResepContract.View{

    private SignIn signIn;
    private Konsultasi konsultasi;
    private ResepAdapter resepAdapter;
    private final List<ResepObat> resepObats = new ArrayList<>();
    private String resepId;

    public static DetailResepFragment newInstance(Konsultasi konsultasi) {

        Bundle args = new Bundle();
        DetailResepFragment fragment = new DetailResepFragment();
        args.putParcelable(Constants.DATA_KONSULTASI, konsultasi);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new DetailResepPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(view, savedInstanceState);
        if(getArguments()!=null) {
            konsultasi = getArguments().getParcelable(Constants.DATA_KONSULTASI);
        }
        signIn = mPresenter.selectLogin();
    }

    @NonNull
    @NotNull
    @Override
    protected DetailResepContract.View getBaseView() {
        return this;
    }

    @Override
    protected FragmentDetailResepBinding getViewBinding(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return FragmentDetailResepBinding.inflate(inflater);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setmLoadingResource(R.layout.detail_resep_place_holder);
        super.initEventAndData(bundle);
        setRv();
        setToolBar(getString(R.string.resep_obat));
        setUpListener();
        mPresenter.detailResep(signIn.getaUTHTOKEN(), konsultasi.getConsultationId(), mContext);
        setOnRefreshClickListener(() -> mPresenter.detailResep(signIn.getaUTHTOKEN(), konsultasi.getConsultationId(), mContext));
    }

    private void setUpListener(){
        binding.btnOrder.setOnClickListener(v -> {
            NetkromDialog netkromDialog = new NetkromDialog(mContext, 0,
                    getString(R.string.perbaharui_keranjang),
                    getString(R.string.perbaharui_keranjang_ask), getString(R.string.perbaharui), getString(R.string.batal),0);
            netkromDialog.setOnButtonClick(new NetkromDialog.onButtonClick() {
                @Override
                public void onBtnYaClick() {
                    mPresenter.order(resepObats);
                    netkromDialog.dismiss();
                }

                @Override
                public void onBntbatalClick() {
                    netkromDialog.dismiss();
                }
            });
            netkromDialog.show();
        });
    }

    private void setToolBar(String title){
        binding.toolbar.inflateMenu(R.menu.detail_resep_menu);
        binding.toolbar.setNavigationOnClickListener(v -> mActivity.finish());
        binding.toolbar.setTitle(title);

        binding.toolbar.setOnMenuItemClickListener(
                item -> {
                    int id = item.getItemId();
                    if (id == R.id.action_download) {
                        if(resepId!=null) {
                            setUsingDialog(true);
                            mPresenter.downLoadResep(signIn.getaUTHTOKEN(), resepId, mContext);
                        }
                    }
                    return true;
                });

    }

    private void setRv(){
        resepAdapter = new ResepAdapter(resepObats);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        binding.rv.setLayoutManager(linearLayoutManager);
        binding.rv.setAdapter(resepAdapter);
    }

    @Override
    public void detailResepResp(BaseResponse<Resep> response) {
        if(response.getData()!=null){
            if(response.getData().getReseps()!=null && response.getData().getReseps().size() > 0){
                resepId = String.valueOf(response.getData().getReseps().get(0).getPrescriptionId());
                resepObats.addAll(response.getData().getReseps());
                resepAdapter.notifyDataSetChanged();

                binding.tvDokterNama.setText(response.getData().getDokter().getDokterName());
                binding.tvDokterSpesialis.setText(response.getData().getDokter().getSpesialist());
                binding.tvDokterNoStr.setText(response.getData().getDokter().getNumber());
                if(response.getData().getPrescriptionDate()!=null)
                    binding.tvTglResep.setText(DateUtil.ddMMMyyyy(response.getData().getPrescriptionDate()));
                ImageLoader.loadAll(mContext, response.getData().getDokter().getImage(), binding.cvDokterProfile);
                ImageLoader.loadAll(mContext, response.getData().getPasien().getImage(), binding.cvPasienProfile);
                binding.tvPasienNama.setText(response.getData().getPasien().getPatientName());
                binding.tvPasienUmur.setText(response.getData().getPasien().getAge());
                binding.tvCatatan.setText(response.getData().getPrescriptionNote());
                if(response.getData().getExpires()!=null) {
                    binding.tvExpires.setText(DateUtil.dateType9(response.getData().getExpires()));
                }

                int i = 0;
                for(ResepObat resepObat:response.getData().getReseps()){
                    if(!resepObat.getStatus()){
                        i++;
                    }
                }
                if(response.getData().getReseps().size() == i){
                    binding.btnOrder.setVisibility(View.GONE);
                }else {
                    binding.btnOrder.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void success() {
        /*Intent intent = new Intent(mContext, ObatActivity.class);
        startActivity(intent);*/
    }

    @Override
    public void donwloadResepResp(String path) {
        File file = new File(path);
        Uri uri = FileProvider.getUriForFile(mContext, transmedikaSettings.getApplicationId() + ".fileprovider", file);
        String mime = mActivity.getContentResolver().getType(uri);

        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        newIntent.setDataAndType(uri,mime);
        newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            MsgUiUtil.showSnackBar(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0),
                    getString(R.string.tidak_ada_aplikasi_buka_file),mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
        }
    }
}
