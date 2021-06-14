package com.transmedika.transmedikakitui.modul.obat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RootBindingActivity;
import com.transmedika.transmedikakitui.contract.KategoriObatContract;
import com.transmedika.transmedikakitui.databinding.ActivityKategoriObatBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.KategoriObat;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.modul.CommonCategoriesAdapter;
import com.transmedika.transmedikakitui.presenter.KategoriObatPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KategoriObatActivity extends RootBindingActivity<ActivityKategoriObatBinding,
        KategoriObatContract.View, KategoriObatPresenter>
    implements KategoriObatContract.View, RootBindingActivity.OnRefreshClickListener{

    private CommonCategoriesAdapter<KategoriObat> commonCategoriesAdapter;
    private final List<KategoriObat> kategoriObat = new ArrayList<>();
    private SignIn uSignIn;

    @NonNull
    @NotNull
    @Override
    protected KategoriObatContract.View getBaseView() {
        return this;
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        mPresenter = new KategoriObatPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(bundle);
        uSignIn = mPresenter.selectLogin();
    }


    @Override
    protected ActivityKategoriObatBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivityKategoriObatBinding.inflate(inflater);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setmLoadingResource(R.layout.categori_place_holder);
        super.initEventAndData(bundle);
        setRv();
        setToolBar(getString(R.string.kategori_obat));
        mPresenter.kategoriObat(uSignIn.getaUTHTOKEN(), mContext);
        setUpListener();

        if(transmedikaSettings.getDoctorCenterSearchHint()!=null && transmedikaSettings.getDoctorCenterSearchHint()){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)binding.tvPlaceholderSearch.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            binding.tvPlaceholderSearch.setLayoutParams(layoutParams);
        }

        if(transmedikaSettings.getDoctorSearchBackground()!=null){
            binding.edSearch.setBackground(ResourcesCompat.getDrawable(getResources(),
                    TransmedikaUtils.setDrawable(mContext,transmedikaSettings.getDoctorSearchBackground()),null));
        }

        if (viewLoading != null){
            CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0, TransmedikaUtils.dip2px(mContext, 108), 0, 0);
            viewLoading.setLayoutParams(params);
        }
    }

    private void setUpListener(){
        setOnRefreshClickListener(this);
        binding.edSearch.setOnClickListener(v -> {
            Bundle b = new Bundle();
            Intent i = new Intent(mContext, ObatMainActivity.class);
            b.putParcelable(Constants.DATA, new KategoriObat());
            i.putExtra(Constants.DATA_USER, b);
            startActivity(i);
        });
    }

    @Override
    public void onRefreshClick() {
        mPresenter.kategoriObat(uSignIn.getaUTHTOKEN(), mContext);
    }

    private void setToolBar(String title){
        binding.toolbar.setTitle(getString(R.string.kategori_obat));
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setRv(){
        commonCategoriesAdapter = new CommonCategoriesAdapter<>(mContext, kategoriObat);
        //GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mContext, SystemUtil.calculateNoOfColumns(mContext,150));
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mContext, 3);
        binding.rv.setLayoutManager(gridLayoutManager1);
        binding.rv.setAdapter(commonCategoriesAdapter);
        commonCategoriesAdapter.setOnItemClickListener((o, pos) -> {
            if(o instanceof  KategoriObat) {
                KategoriObat kObat = (KategoriObat) o;
                Bundle b = new Bundle();
                b.putParcelable(Constants.DATA, kObat);
                Intent i = new Intent(mContext, ObatMainActivity.class);
                i.putExtra(Constants.DATA_USER, b);
                startActivity(i);
            }
        });
    }

    @Override
    public void kategoriObatResp(BaseResponse<List<KategoriObat>> response) {
        if(response.getData()!=null){
            if(response.getData().size()>0){
                kategoriObat.addAll(response.getData());
                commonCategoriesAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressedSupport() {
        if(binding.edSearch.isFocused()) {
            if (!TextUtils.isEmpty(Objects.requireNonNull(binding.edSearch.getText()).toString())){
                binding.edSearch.setText(null);
            }else {
                binding.edSearch.clearFocus();
            }
        }else {
            super.onBackPressedSupport();
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        if (viewError != null){
            CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0, TransmedikaUtils.dip2px(mContext, 108), 0, 0);
            viewError.setLayoutParams(params);
        }
    }
}
