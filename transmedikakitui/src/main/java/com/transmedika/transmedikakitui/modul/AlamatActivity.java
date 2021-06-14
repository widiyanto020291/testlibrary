package com.transmedika.transmedikakitui.modul;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RootBindingActivity;
import com.transmedika.transmedikakitui.contract.AlamatContract;
import com.transmedika.transmedikakitui.databinding.ActivityAlamatBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.Alamat;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.presenter.AlamatPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.widget.NetkromDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlamatActivity extends RootBindingActivity<ActivityAlamatBinding, AlamatContract.View, AlamatPresenter>
    implements AlamatContract.View{

    private static final int RQ_ALAMAT= 111;
    private AlamatAdapter adapter;
    private SignIn signIn;
    private final List<Alamat> alamats = new ArrayList<>();
    private int position;
    private boolean flag = false;

    @NonNull
    @NotNull
    @Override
    protected AlamatContract.View getBaseView() {
        return this;
    }

    @Override
    protected ActivityAlamatBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivityAlamatBinding.inflate(inflater);
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        mPresenter = new AlamatPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(bundle);
        signIn = mPresenter.selectLogin();
        Bundle b = getIntent().getBundleExtra(Constants.DATA_USER);
        if(b!=null) {
            flag = b.getBoolean(Constants.FLAG);
        }

    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setmLoadingResource(R.layout.alamat_place_holder);
        super.initEventAndData(bundle);
        setToolBar(getString(R.string.alamat));
        setRv();

        mPresenter.alamat(signIn.getaUTHTOKEN(), mContext);
        setOnRefreshClickListener(() -> {
            setUsingDialog(false);
            mPresenter.alamat(signIn.getaUTHTOKEN(), mContext);
        });
    }

    private void setToolBar(String title){
        binding.toolbar.inflateMenu(R.menu.alamat_menu);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.toolbar.setTitle(title);

        MenuItem menuI = binding.toolbar.getMenu().findItem(R.id.action_tambah_alamat);
        if(flag){
            Drawable searchIc = AppCompatResources.getDrawable(mContext, R.drawable.ic_search);
            if(searchIc!=null)
                searchIc.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.gray, null), PorterDuff.Mode.SRC_IN);
            menuI.setIcon(searchIc);
        }else {
            menuI.setIcon(AppCompatResources.getDrawable(mContext,R.drawable.ic_tambah_alamat));
        }

        binding.toolbar.setOnMenuItemClickListener(
                item -> {
                    int id = item.getItemId();
                    if (id == R.id.action_tambah_alamat) {
                        gotoMap();
                    }
                    return true;
                });
    }

    private void gotoMap() {
        Bundle b = new Bundle();
        b.putString(Constants.FLAG, flag ? Constants.FLAG_SEARCH : Constants.FLAG_ADD);
        Intent i = new Intent(mContext, TambahAlamatActivity.class);
        i.putExtra(Constants.DATA_USER, b);
        startActivityForResult(i, RQ_ALAMAT);
    }

    private void setRv(){
        adapter = new AlamatAdapter(mContext, alamats, flag, transmedikaSettings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        binding.viewMain.setLayoutManager(linearLayoutManager);
        binding.viewMain.setAdapter(adapter);
        adapter.setOnItemClickListener(new AlamatAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Alamat alamat, int pos) {
                position = pos;
                Bundle b = new Bundle();
                b.putParcelable(Constants.DATA, alamat);
                b.putString(Constants.FLAG, Constants.FLAG_EDIT);
                Intent i = new Intent(mContext, TambahAlamatActivity.class);
                i.putExtra(Constants.DATA_USER, b);
                startActivityForResult(i,RQ_ALAMAT);
            }

            @Override
            public void onHapusClick(Alamat alamat, int pos) {
                position = pos;
                NetkromDialog netkromDialog =
                        new NetkromDialog(mContext, 0,
                                getString(R.string.hapus_alamat),
                                getString(R.string.hapus_alamat_ask), getString(R.string.hapus), getString(R.string.batal),0);
                netkromDialog.setOnButtonClick(new NetkromDialog.onButtonClick() {
                    @Override
                    public void onBtnYaClick() {
                        setUsingDialog(true);
                        mPresenter.hapusAlamat(signIn.getaUTHTOKEN(), String.valueOf(alamat.getId()), mContext);
                        netkromDialog.dismiss();
                    }

                    @Override
                    public void onBntbatalClick() {
                        netkromDialog.dismiss();
                    }
                });
                netkromDialog.show();
            }

            @Override
            public void onLoveClick(Alamat alamat, int pos) {

            }

            @Override
            public void onSelected(Alamat alamat, int pos) {
                selectAlamat(alamat);
            }
        });
    }

    private void selectAlamat(Alamat alamat){
        Bundle b = new Bundle();
        b.putParcelable(Constants.DATA, alamat);
        Intent i = new Intent();
        i.putExtra(Constants.DATA_USER, b);
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    @Override
    public void alamatResp(BaseResponse<List<Alamat>> response) {
        if(response.getData()!=null){
            if(response.getData().size() > 0){
                alamats.addAll(response.getData());
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDataIsEmpty() {
        super.onDataIsEmpty();
        gotoMap();
    }

    @Override
    public void hapusAlamatResp(BaseResponse<Alamat> response) {
        alamats.remove(position);
        adapter.notifyItemRemoved(position);

        if(alamats.size()  == 0){
            errorView(getString(R.string.data_tidak_ditemukan), R.drawable.ic_empty_page);
            viewError.setVisibility(View.VISIBLE);
            viewMain.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RQ_ALAMAT){
            if(resultCode == Activity.RESULT_OK){
                if(data!=null) {
                    Bundle b = data.getBundleExtra(Constants.DATA_USER);
                    if (b != null) {
                        Alamat alamat = b.getParcelable(Constants.DATA);
                        String flag = b.getString(Constants.FLAG);
                        if (alamat != null) {
                            if(Objects.equals(flag, Constants.FLAG_EDIT)){
                                int pos = alamats.indexOf(alamat);
                                alamats.set(pos, alamat);
                                adapter.notifyItemChanged(pos);
                                binding.viewMain.scheduleLayoutAnimation();
                            }

                            if(Objects.equals(flag, Constants.FLAG_SEARCH)){
                                selectAlamat(alamat);
                            }

                            if(Objects.equals(flag, Constants.FLAG_ADD)){
                                alamats.add(0, alamat);
                                adapter.notifyItemInserted(0);
                                binding.viewMain.scheduleLayoutAnimation();
                                binding.viewMain.scrollToPosition(0);
                                if(viewError!=null)
                                    viewError.setVisibility(View.GONE);
                                viewMain.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        }
    }
}
