package com.transmedika.transmedikakitui.modul.consultation;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RootBindingActivity;
import com.transmedika.transmedikakitui.contract.consultation.DoctorContract;
import com.transmedika.transmedikakitui.databinding.ActivityDoctorBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.join.DokterJoin;
import com.transmedika.transmedikakitui.models.bean.json.BasePage;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Clinic;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.Filter;
import com.transmedika.transmedikakitui.models.bean.json.FilterFilter;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.presenter.consultation.DoctorPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;
import com.transmedika.transmedikakitui.widget.EndlessOnScrollListener;
import com.transmedika.transmedikakitui.widget.NetkromButton;
import com.transmedika.transmedikakitui.widget.NetkromTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DoctorActivity extends RootBindingActivity<ActivityDoctorBinding, DoctorContract.View, DoctorPresenter>
        implements DoctorContract.View{

    private DoctorAdapter doctorAdapter;
    private final List<Doctor> dokters = new ArrayList<>();
    private SignIn uSignIn;
    private Specialist spesialis;
    private Clinic klinik;
    private String nextPage = null;
    private EndlessOnScrollListener scrollListener;
    private final List<Filter> filters = new ArrayList<>();
    private FilterDoctorSelectedAdapter selectedAdapter;
    private final List<Filter> filterSelected = new ArrayList<>();


    @NonNull
    @NotNull
    @Override
    protected DoctorContract.View getBaseView() {
        return this;
    }

    @Override
    protected ActivityDoctorBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivityDoctorBinding.inflate(inflater);
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        mPresenter = new DoctorPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(bundle);
        Bundle b = getIntent().getBundleExtra(Constants.DATA_USER);
        if(b!=null) {
            spesialis = b.getParcelable(Constants.DATA);
            klinik = b.getParcelable(Constants.KLINIK);
        }
        uSignIn = mPresenter.selectLogin();
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setmLoadingResource(R.layout.doctor_palce_holder);
        super.initEventAndData(bundle);

        if(klinik!=null && klinik.getName()!=null){
            setToolbar(klinik.getName());
        } else {
            if(spesialis != null && spesialis.getSpecialist()!=null)
                setToolbar(spesialis.getSpecialist());
        }

        setRv();
        setRvFilterSelected();
        mPresenter.dokterJoin(uSignIn.getaUTHTOKEN(), spesialis.getSlug(), klinik!=null ? klinik.getId() : null, mContext);
        setOnRefreshClickListener(() -> {
            if(filters.size() > 0){
                binding.rv.scrollToPosition(0);
                scrollListener.resetState();
                mPresenter.dokter(uSignIn.getaUTHTOKEN(), spesialis.getSlug(), klinik!=null ? klinik.getId() : null, getParamFilter(), mContext);
            }else {
                mPresenter.dokterJoin(uSignIn.getaUTHTOKEN(), spesialis.getSlug(), klinik!=null ? klinik.getId() : null , mContext);
            }
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

        binding.edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.rv.scrollToPosition(0);
                scrollListener.resetState();
                mPresenter.dokter(uSignIn.getaUTHTOKEN(), spesialis.getSlug(), klinik!=null ? klinik.getId() : null, getParamFilter(), mContext);
                TransmedikaUtils.toggleSoftKeyBoard(mContext, true);
                binding.edSearch.requestFocus();
                return true;
            }
            return false;
        });

        binding.edSearch.setOnFocusChangeListener((view, b) -> {
            binding.tvPlaceholderSearch.setVisibility(view.hasFocus() ? View.GONE : View.VISIBLE);
            Drawable searchIc = view.hasFocus() ? AppCompatResources.getDrawable(mContext, R.drawable.ic_search) : null;
            binding.edSearch.setCompoundDrawablesWithIntrinsicBounds(searchIc, null, null, null);
        });
    }

    private void setToolbar(String title){
        binding.toolbar.inflateMenu(R.menu.doctor_menu);
        MenuItem menu = binding.toolbar.getMenu().findItem(R.id.action_filter);

        if(!transmedikaSettings.getToolbarDoctorMenuIcon().equals(Constants.EMPTY_STRING)) {
            menu.setIcon(TransmedikaUtils.setDrawable(mContext, transmedikaSettings.getToolbarDoctorMenuIcon()));
        }else {
            menu.setIcon(R.drawable.ic_filter);
        }

        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.toolbar.setTitle(title);
        binding.toolbar.setOnMenuItemClickListener(
                item -> {
                    int id = item.getItemId();
                    if (id == R.id.action_filter) {
                        if(filters.size() > 0)
                            dialogFilter();
                    }
                    return true;
                });
    }

    private void setRv(){
        doctorAdapter = new DoctorAdapter(mContext, dokters, transmedikaSettings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        scrollListener = new EndlessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(nextPage!=null)
                    new Handler(Looper.getMainLooper()).postDelayed(() -> mPresenter.dokterDynamic(uSignIn.getaUTHTOKEN(),nextPage, getParamFilter(), mContext), 1000);
            }
        };

        binding.rv.setItemAnimator(null);
        binding.rv.addOnScrollListener(scrollListener);
        binding.rv.setLayoutManager(linearLayoutManager);
        binding.rv.setAdapter(doctorAdapter);
        doctorAdapter.setOnItemClickListener(new DoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Doctor dokter, int pos, ImageView imageView) {
                Bundle b = new Bundle();
                b.putParcelable(Constants.DATA, dokter);
                b.putParcelable(Constants.KLINIK, klinik);
                b.putParcelable(Constants.SPESIALIS, spesialis);
                Intent i = new Intent(mContext, ChatStepActivity.class);
                i.putExtra(Constants.DATA_USER, b);
                startActivity(i);
            }

            @Override
            public void onProfileClick(Doctor dokter, int pos, ImageView imageView) {
                Bundle b = new Bundle();
                b.putParcelable(Constants.DATA, dokter);
                b.putParcelable(Constants.SPESIALIS, spesialis);
                b.putParcelable(Constants.KLINIK, klinik);
                Intent i = new Intent(mContext, DetailDoctorActivity.class);
                i.putExtra(Constants.DATA_USER, b);
                startActivity(i);
            }

            @Override
            public void onItemCobaLagiClick(Doctor dokter, int pos) {
                int startPos = dokters.size() - 1;
                dokters.get(startPos).setUuid(Constants.LOADING);
                doctorAdapter.notifyItemChanged(startPos,dokters.get(startPos));
                mPresenter.dokterDynamic(uSignIn.getaUTHTOKEN(),nextPage, getParamFilter(), mContext);
            }
        });
    }

    private void setRvFilterSelected(){
        selectedAdapter = new FilterDoctorSelectedAdapter(mContext, filterSelected);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        binding.rvFilter.setItemAnimator(null);
        binding.rvFilter.setLayoutManager(linearLayoutManager);
        binding.rvFilter.setAdapter(selectedAdapter);
        selectedAdapter.setOnItemClickListener((filter, pos) -> {
            filterSelected.remove(filter);
            int posiiton = filters.indexOf(filter);

            filter.setChecked(false);
            filters.set(posiiton, filter);
            selectedAdapter.notifyDataSetChanged();

            binding.rv.scrollToPosition(0);
            scrollListener.resetState();
            mPresenter.dokter(uSignIn.getaUTHTOKEN(), spesialis.getSlug(), klinik.getId(), getParamFilter(), mContext);
        });
    }

    @Override
    public void dokterResp(BaseResponse<BasePage<List<Doctor>>> response) {
        if(response.getData()!=null){
            dokters.clear();
            if(response.getData().getData() != null && response.getData().getData().size() > 0){
                dokters.addAll(response.getData().getData());

                if (response.getData().getNextPageUrl()!=null){
                    nextPage = response.getData().getNextPageUrl();
                    dokters.add(new Doctor(Constants.LOADING));
                }else {
                    nextPage = null;
                }
            }
            doctorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void dokterDynamicResp(BaseResponse<BasePage<List<Doctor>>> response) {
        if(response.getData()!=null && response.getData().getData()!=null && response.getData().getData().size() > 0) {
            int startPos = dokters.size()-1;
            dokters.remove(startPos);
            doctorAdapter.notifyItemRemoved(startPos);
            dokters.addAll(response.getData().getData());
            if (response.getData().getNextPageUrl()!=null){
                nextPage = response.getData().getNextPageUrl();
                dokters.add(new Doctor(Constants.LOADING));
            }else {
                nextPage = null;
            }
            doctorAdapter.notifyItemRangeInserted(startPos, dokters.size());
        }
    }

    @Override
    public void doctorDynamicError() {
        if(dokters.size() > 0) {
            int startPos = dokters.size() - 1;
            dokters.get(startPos).setUuid(Constants.ERROR);
            doctorAdapter.notifyItemChanged(startPos,dokters.get(startPos));
        }
    }

    @Override
    public void dokterJoinResp(DokterJoin response) {
        if(response!=null) {
            dokters.clear();
            if (response.getDoctors() != null &&
                    response.getDoctors().getData()!=null &&
                    response.getDoctors().getData().getData()!=null) {
                dokters.addAll(response.getDoctors().getData().getData());
                if (response.getDoctors().getData().getNextPageUrl() != null) {
                    nextPage = response.getDoctors().getData().getNextPageUrl();
                    dokters.add(new Doctor(Constants.LOADING));
                } else {
                    nextPage = null;
                }
            }

            if(response.getFilters()!=null &&
                    response.getFilters().getData()!=null){
                if(response.getFilters().getData().getRates()!=null){
                    filters.add(new Filter(Constants.HEADER_FILTER, getString(R.string.prices)));
                    for (Filter filter :response.getFilters().getData().getRates()){
                        filter.setGroup(Filter.rates);
                        filters.add(filter);
                    }
                }

                if(response.getFilters().getData().getExperiences()!=null){
                    filters.add(new Filter(Constants.HEADER_FILTER, getString(R.string.experiences)));
                    for (Filter filter :response.getFilters().getData().getExperiences()){
                        filter.setGroup(Filter.experience);
                        filters.add(filter);
                    }
                }
            }

            doctorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getBroadcastEvents(BroadcastEvents.Event event) {
        if(event.getInitString().equals(Constants.BROADCAST_FINISH_KONSULTASI)){
            finish();
        }
    }

    private void dialogFilter(){
        View view = View.inflate(mContext, R.layout.filter_dialog_doctor, null);
        RecyclerView rv = view.findViewById(R.id.rv);
        NetkromTextView mTvTitle = view.findViewById(R.id.tv_title);
        ImageView mImgClose = view.findViewById(R.id.img_close);
        NetkromButton btnYa = view.findViewById(R.id.btn_ya);
        NetkromButton btnBatal = view.findViewById(R.id.btn_batal);

        FilterDoctorAdapter filterDoctorAdapter = new FilterDoctorAdapter(filters);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(filterDoctorAdapter);
        BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.AppBottomSheetDialogTheme);

        List<Filter> list = new ArrayList<>(filterSelected);
        filterDoctorAdapter.setOnItemCheckedChangeListener((filter, pos, checked) -> {
            if(checked){
                filter.setChecked(true);
                list.add(0,filter);
            }else {
                filter.setChecked(false);
                list.remove(filter);
            }
        });

        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.setContentView(view);

        mTvTitle.setText(R.string.filter_doctor);
        mImgClose.setOnClickListener(v -> dialog.dismiss());
        btnYa.setOnClickListener(v -> {
            filterSelected.clear();
            filterSelected.addAll(list);
            selectedAdapter.notifyDataSetChanged();
            terapkan();
            dialog.dismiss();
        });

        btnBatal.setOnClickListener(v -> {
            list.clear();
            reset();
            dialog.dismiss();
        });

        BottomSheetBehavior<View> mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(TransmedikaUtils.dip2px(mContext, view.getMeasuredHeight()));
        dialog.show();
    }

    private void terapkan(){
        for (int i = 0 ; i < this.filters.size() ;i++){
            Filter filter = this.filters.get(i);
            for (int x = 0 ; x < filterSelected.size() ;x++){
                Filter filterS = filterSelected.get(x);
                if(filter.equals(filterS)){
                    this.filters.set(i, filterS);
                    break;
                }
            }
        }

        showHidePlaceholderFilter();

        binding.rv.scrollToPosition(0);
        scrollListener.resetState();
        mPresenter.dokter(uSignIn.getaUTHTOKEN(), spesialis.getSlug(), klinik!=null ? klinik.getId() : null, getParamFilter(), mContext);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (viewError != null){
            CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
            if (this.filterSelected.size() > 0) {
                params.setMargins(0, TransmedikaUtils.dip2px(mContext, 142), 0, 0);
            } else {
                params.setMargins(0, TransmedikaUtils.dip2px(mContext, 108), 0, 0);
        }
        viewError.setLayoutParams(params);
        }
    }

    private void showHidePlaceholderFilter(){
        LinearLayout view = findViewById(R.id.ll_place_holder);
        if(this.filterSelected.size() > 0){
            view.setPadding(0,TransmedikaUtils.dip2px(mContext, 92),0,0);
        }else {
            view.setPadding(0, TransmedikaUtils.dip2px(mContext, 54), 0, 0);
        }

    }

    private FilterFilter getParamFilter(){
        FilterFilter filterFilter = new FilterFilter();
        List<Filter> listHarga = new ArrayList<>();
        List<Filter> listPengalaman = new ArrayList<>();
        for(Filter filter: filterSelected){
            if(filter.getGroup().equals(Filter.rates)){
                listHarga.add(new Filter(filter.getValue(), filter.getSymbol()));
            }

            if(filter.getGroup().equals(Filter.experience)){
                listPengalaman.add(new Filter(filter.getValue(), filter.getSymbol()));
            }
        }


        filterFilter.setRates(listHarga.size() == 0? null:listHarga);
        filterFilter.setExperiences(listPengalaman.size() == 0? null:listPengalaman);
        filterFilter.setSearch(TextUtils.isEmpty(Objects.requireNonNull(binding.edSearch.getText()).toString()) ?
                Constants.EMPTY_STRING : binding.edSearch.getText().toString());

        return filterFilter;
    }

    private void reset(){
        for (int i = 0 ; i < filters.size() ;i ++){
            filters.get(i).setChecked(false);
        }
        filterSelected.clear();
        binding.rv.scrollToPosition(0);
        scrollListener.resetState();
        mPresenter.dokter(uSignIn.getaUTHTOKEN(), spesialis.getSlug(), klinik!=null ? klinik.getId() : null, getParamFilter(), mContext);
        selectedAdapter.notifyDataSetChanged();
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
}
