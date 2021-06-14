package com.transmedika.transmedikakitui.modul.obat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RootBindingFragment;
import com.transmedika.transmedikakitui.contract.obat.ObatContract;
import com.transmedika.transmedikakitui.databinding.FragmentObatBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BasePage;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.KategoriObat;
import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.presenter.obat.ObatPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.MsgUiUtil;
import com.transmedika.transmedikakitui.utils.RxUtil;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;
import com.transmedika.transmedikakitui.widget.EndlessOnScrollListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObatFragment extends RootBindingFragment<FragmentObatBinding, ObatContract.View, ObatPresenter>
    implements ObatContract.View{

    private static final String DATA = "DATA";
    private SignIn signIn;
    private KategoriObat kategoriObat;
    private final List<Obat> obats = new ArrayList<>();
    private ObatAdapter obatAdapter;
    private IObat iObat;
    private GridLayoutManager gridLayoutManager1;
    private String nextPage = null;
    private EndlessOnScrollListener scrollListener;

    @Override
    protected ObatContract.View getBaseView() {
        return this;
    }

    @Override
    protected FragmentObatBinding getViewBinding(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return FragmentObatBinding.inflate(inflater);
    }

    public static ObatFragment newInstance(KategoriObat kategoriObat) {

        Bundle args = new Bundle();

        ObatFragment fragment = new ObatFragment();
        args.putParcelable(DATA, kategoriObat);
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
        mPresenter = new ObatPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(view, savedInstanceState);
        signIn = mPresenter.selectLogin();
        if(getArguments()!=null){
            kategoriObat = getArguments().getParcelable(DATA);
            if(kategoriObat.getSlug()!=null) {
                setToolBar(kategoriObat.getName());
                binding.rlSearch.setVisibility(View.GONE);
            }else {
                setToolBar(getString(R.string.cari_obat));
                binding.rlSearch.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setmLoadingResource(R.layout.obat_place_holder);
        super.initEventAndData(bundle);
        setRv();
        requestObat();
        setOnRefreshClickListener(() -> {
            if(TextUtils.isEmpty(binding.edSearch.getText())) {
                requestObat();
            }else {
                scrollListener.resetState();
                mPresenter.cariObat(signIn.getaUTHTOKEN(), Objects.requireNonNull(binding.edSearch.getText()).toString(), mContext);
            }
        });
        binding.edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if(v.getText().length() < 3){
                    MsgUiUtil.showSnackBar(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0),
                            getString(R.string.minimal_cari),mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
                }else {
                    scrollListener.resetState();
                    mPresenter.cariObat(signIn.getaUTHTOKEN(), v.getText().toString(), mContext);

                    if (viewLoading != null){
                        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
                        params.setMargins(0, TransmedikaUtils.dip2px(mContext, 108), 0, 0);
                        viewLoading.setLayoutParams(params);
                    }

                    hideSoftInput();
                }

                return true;
            }
            return false;
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


        binding.edSearch.setOnFocusChangeListener((view, b) -> {
            binding.tvPlaceholderSearch.setVisibility(View.GONE);
            Drawable searchIc = AppCompatResources.getDrawable(mContext, R.drawable.ic_search);
            binding.edSearch.setCompoundDrawablesWithIntrinsicBounds(searchIc, null, null, null);
        });
    }

    private void setToolBar(String title){
        binding.toolbar.setNavigationOnClickListener(v -> mActivity.finish());
        binding.toolbar.setTitle(title);
    }

    private void requestObat(){
        if(kategoriObat.getSlug()!=null) {
            mPresenter.obat(signIn.getaUTHTOKEN(), kategoriObat.getSlug(), mContext);
        }
    }

    private void setRv() {
        obatAdapter = new ObatAdapter(mContext, obats);
        gridLayoutManager1 = new GridLayoutManager(mContext, 2);
        scrollListener = new EndlessOnScrollListener(gridLayoutManager1) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (nextPage != null)
                    new Handler(Looper.getMainLooper()).postDelayed(() -> mPresenter.obatDynamic(signIn.getaUTHTOKEN(), nextPage, mContext), 1000);
            }
        };

        binding.rv.addOnScrollListener(scrollListener);
        binding.rv.setLayoutManager(gridLayoutManager1);
        binding.rv.setItemAnimator(null);
        gridLayoutManager1.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return obatAdapter.isPositionFooter(position) ? gridLayoutManager1.getSpanCount() : 1;
            }
        });
        binding.rv.setAdapter(obatAdapter);
        obatAdapter.setOnItemClickListener(new ObatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Obat o, int pos, ImageView imageView) {
                start(DetailObatFragment.newInstance(o, kategoriObat));
            }

            @Override
            public void onPlusItemClick(Obat o, int pos) {
                obats.set(pos,o);
                mPresenter.insertObat(o);
                iObat.calculateObat();
            }

            @Override
            public void onMinusItemClick(Obat o, int pos) {
                obats.set(pos,o);
                mPresenter.insertObat(o);
                iObat.calculateObat();
            }

            @Override
            public void onDelete(Obat o, int pos) {
                obats.set(pos,o);
                mPresenter.deleteObat(o.getSlug());
                iObat.calculateObat();
            }

            @Override
            public void onItemCobaLagiClick(Obat o, int pos) {
                int startPos = obats.size() - 1;
                obats.get(startPos).setSlug(Constants.LOADING);
                obatAdapter.notifyItemChanged(startPos,obats.get(startPos));
                mPresenter.obatDynamic(signIn.getaUTHTOKEN(),nextPage, mContext);
            }
        });
    }

    @Override
    public void getBroadcastEvents(BroadcastEvents.Event event) {
        RxUtil.runOnUi(o -> {
            if(event.getInitString().equals(Constants.UPDATE_OBAT)) {
                Obat obat = (Obat) event.getObject();
                int pos = obats.indexOf(obat);
                Obat obat1 = obats.get(pos);
                obat1.setJumlah(obat.getJumlah());
                obats.set(pos, obat1);
                obatAdapter.notifyItemChanged(pos, obat1);
            }
        });
    }

    @Override
    public void obatResp(BaseResponse<BasePage<List<Obat>>> response) {
        if(response.getData()!=null && response.getData().getData()!=null && response.getData().getData().size()>0){
            obats.addAll(response.getData().getData());

            if (response.getData().getNextPageUrl()!=null){
                nextPage = response.getData().getNextPageUrl();
                obats.add(new Obat(Constants.LOADING));
            }else {
                nextPage = null;
            }

            obatAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void obatDynamicResp(BaseResponse<BasePage<List<Obat>>> response) {
        if(response.getData()!=null && response.getData().getData()!=null && response.getData().getData().size() > 0) {
            int startPos = obats.size()-1;
            obats.remove(startPos);
            obatAdapter.notifyItemRemoved(startPos);
            obats.addAll(response.getData().getData());
            if (response.getData().getNextPageUrl()!=null){
                nextPage = response.getData().getNextPageUrl();
                obats.add(new Obat(Constants.LOADING));
            }else {
                nextPage = null;
            }
            obatAdapter.notifyItemRangeInserted(startPos, obats.size());
        }
    }

    @Override
    public void cariObatResp(BaseResponse<BasePage<List<Obat>>> response) {
        obats.clear();
        if(response.getData()!=null && response.getData().getData()!=null && response.getData().getData().size()>0){
            obats.addAll(response.getData().getData());

            if (response.getData().getNextPageUrl()!=null){
                nextPage = response.getData().getNextPageUrl();
                obats.add(new Obat(Constants.LOADING));
            }else {
                nextPage = null;
            }
        }
        obatAdapter.notifyDataSetChanged();
    }

    @Override
    public void articleDynamicError() {
        if(obats.size() > 0) {
            int startPos = obats.size() - 1;
            obats.get(startPos).setSlug(Constants.ERROR);
            obatAdapter.notifyItemChanged(startPos,obats.get(startPos));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        if(binding.edSearch.isFocused()) {
            if (!TextUtils.isEmpty(Objects.requireNonNull(binding.edSearch.getText()).toString())){
                binding.edSearch.setText(null);
            }else {
                binding.edSearch.clearFocus();
            }
            return false;
        }else {
            return super.onBackPressedSupport();
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        if (viewError != null && (binding.rlSearch.getVisibility() == View.VISIBLE)){
            CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0, TransmedikaUtils.dip2px(mContext, 108), 0, 0);
            viewError.setLayoutParams(params);
        }
    }
}
