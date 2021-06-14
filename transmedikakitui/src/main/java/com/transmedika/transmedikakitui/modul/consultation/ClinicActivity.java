package com.transmedika.transmedikakitui.modul.consultation;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RootBindingActivity;
import com.transmedika.transmedikakitui.contract.consultation.ClinicContract;
import com.transmedika.transmedikakitui.databinding.ActivityClinicBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BasePage;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Clinic;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.presenter.consultation.ClinicPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;
import com.transmedika.transmedikakitui.widget.EndlessOnScrollListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClinicActivity extends RootBindingActivity<ActivityClinicBinding, ClinicContract.View, ClinicPresenter>
    implements ClinicContract.View{

    private KlinikAdapter klinikAdapter;
    private final List<Clinic> kliniks = new ArrayList<>();
    private EndlessOnScrollListener scrollListener;
    private String nextPage = null;
    private SignIn uSignIn;
    private GridLayoutManager gridLayoutManager1;
    private Clinic klinikTmp;

    @NonNull
    @NotNull
    @Override
    protected ClinicContract.View getBaseView() {
        return this;
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        mPresenter = new ClinicPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(bundle);
        uSignIn = mPresenter.selectLogin();
    }

    @Override
    protected ActivityClinicBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivityClinicBinding.inflate(inflater);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setmLoadingResource(R.layout.clinic_place_holder);
        super.initEventAndData(bundle);
        setRv();
        binding.edSearch.setVisibility(View.VISIBLE);
        mPresenter.klinik(uSignIn.getaUTHTOKEN(),null,mContext);
        binding.edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                scrollListener.resetState();
                mPresenter.klinik(uSignIn.getaUTHTOKEN(), v.getText().toString(), mContext);
                TransmedikaUtils.toggleSoftKeyBoard(this,true);
                return true;
            }
            return false;
        });

        setOnRefreshClickListener(() -> {
            setUsingDialog(false);
            mPresenter.klinik(uSignIn.getaUTHTOKEN(),null,mContext);
        });

        binding.edSearch.setOnFocusChangeListener((view, b) -> {
            binding.tvPlaceholderSearch.setVisibility(view.hasFocus() ? View.GONE : View.VISIBLE);
            Drawable searchIc = view.hasFocus() ? AppCompatResources.getDrawable(mContext, R.drawable.ic_search) : null;
            binding.edSearch.setCompoundDrawablesWithIntrinsicBounds(searchIc, null, null, null);
        });

        if(transmedikaSettings.getDoctorCenterSearchHint()!=null && transmedikaSettings.getDoctorCenterSearchHint()){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)binding.tvPlaceholderSearch.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            binding.tvPlaceholderSearch.setLayoutParams(layoutParams);
        }

        if(transmedikaSettings.getDoctorSearchBackground()!=null){
            binding.edSearch.setBackground(ResourcesCompat.getDrawable(getResources(),
                    TransmedikaUtils.setDrawable(mContext,transmedikaSettings.getDoctorSearchBackground()),null));
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void getBroadcastEvents(BroadcastEvents.Event event) {

    }

    @Override
    public void klinikDynamicResp(BaseResponse<BasePage<List<Clinic>>> response) {
        if(response.getData()!=null && response.getData().getData()!=null && response.getData().getData().size() > 0) {
            int startPos = kliniks.size()-1;
            kliniks.remove(startPos);
            klinikAdapter.notifyItemRemoved(startPos);
            kliniks.addAll(response.getData().getData());
            if (response.getData().getNextPageUrl()!=null){
                nextPage = response.getData().getNextPageUrl();
                kliniks.add(new Clinic(Constants.LOADING_LONG));
            }else {
                nextPage = null;
            }
            klinikAdapter.notifyItemRangeInserted(startPos, kliniks.size());
        }
    }

    @Override
    public void klinikResp(BaseResponse<BasePage<List<Clinic>>> response) {
        if(response.getData()!=null){
            kliniks.clear();
            if(response.getData().getData() != null && response.getData().getData().size() > 0){
                kliniks.addAll(response.getData().getData());

                if (response.getData().getNextPageUrl()!=null){
                    nextPage = response.getData().getNextPageUrl();
                    kliniks.add(new Clinic(Constants.LOADING_LONG));
                }else {
                    nextPage = null;
                }
            }
            klinikAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void klinikDynamicError() {
        if(kliniks.size() > 0) {
            int startPos = kliniks.size() - 1;
            kliniks.get(startPos).setId(Constants.ERROR_LONG);
            klinikAdapter.notifyItemChanged(startPos,kliniks.get(startPos));
        }
    }

    @Override
    public void spesialisResp(BaseResponse<List<Specialist>> response) {
        if(response.getData()!=null && response.getData().size() == 1){
            Bundle b = new Bundle();
            b.putParcelable(Constants.DATA, response.getData().get(0));
            b.putParcelable(Constants.KLINIK, klinikTmp);
            Intent i = new Intent(mContext, DoctorActivity.class);
            i.putExtra(Constants.DATA_USER, b);
            startActivity(i);
        }

        if(response.getData()!=null && response.getData().size() >= 2){
            Bundle b = new Bundle();
            b.putParcelable(Constants.DATA, klinikTmp);
            Intent i = new Intent(mContext, SpesialisKlinikActivity.class);
            i.putExtra(Constants.DATA_USER, b);
            startActivity(i);
        }

        if(response.getData()==null){
            moveDoctorWithoutSpesialist();
        }

        if(response.getData()!=null && response.getData().size() ==0){
            moveDoctorWithoutSpesialist();
        }
    }

    private void moveDoctorWithoutSpesialist(){
        Bundle b = new Bundle();
        b.putParcelable(Constants.KLINIK, klinikTmp);
        b.putParcelable(Constants.DATA, new Specialist());
        Intent i = new Intent(mContext, DoctorActivity.class);
        i.putExtra(Constants.DATA_USER, b);
        startActivity(i);
    }

    private void setRv(){
        klinikAdapter = new KlinikAdapter(mContext, kliniks);
        gridLayoutManager1 = new GridLayoutManager(mContext, 2);
        scrollListener = new EndlessOnScrollListener(gridLayoutManager1) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(nextPage!=null)
                    new Handler(Looper.getMainLooper()).postDelayed(() -> mPresenter.klinikDynamic(uSignIn.getaUTHTOKEN(),nextPage, mContext), 1000);
            }
        };

        binding.rv.setItemAnimator(null);
        binding.rv.addOnScrollListener(scrollListener);
        binding.rv.setLayoutManager(gridLayoutManager1);
        gridLayoutManager1.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return klinikAdapter.isPositionFooter(position) ? gridLayoutManager1.getSpanCount() : 1;
            }
        });
        binding.rv.setAdapter(klinikAdapter);
        klinikAdapter.setOnItemClickListener(new KlinikAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Clinic klinik, int pos) {
                klinikTmp = klinik;
                setUsingDialog(true);
                mPresenter.spesialis(uSignIn.getaUTHTOKEN(), klinik.getId(), mContext);
            }

            @Override
            public void onItemCobaLagiClick(Clinic klinik, int pos) {
                int startPos = kliniks.size() - 1;
                kliniks.get(startPos).setId(Constants.LOADING_LONG);
                klinikAdapter.notifyItemChanged(startPos,kliniks.get(startPos));
                mPresenter.klinikDynamic(uSignIn.getaUTHTOKEN(),nextPage, mContext);
            }
        });
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
    public void hideLoading() {
        super.hideLoading();
        if (viewError != null){
            CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0, TransmedikaUtils.dip2px(mContext, 108), 0, 0);
            viewError.setLayoutParams(params);
        }
    }
}
