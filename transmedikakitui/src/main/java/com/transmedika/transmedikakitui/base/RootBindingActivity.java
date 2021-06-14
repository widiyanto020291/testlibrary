package com.transmedika.transmedikakitui.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.snackbar.Snackbar;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.utils.MsgUiUtil;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;
import com.transmedika.transmedikakitui.widget.NetkromButton;
import com.transmedika.transmedikakitui.widget.NetkromTextView;


public abstract class RootBindingActivity<VB extends ViewBinding, V extends BaseView, T extends BasePresenter<V>> extends BaseBindingActivity<VB, V, T>{

    private static final int STATE_MAIN = 0x00;
    private static final int STATE_LOADING = 0x01;
    private static final int STATE_ERROR = 0x02;

    protected SwipeRefreshLayout viewError;
    protected View viewLoading;
    public ViewGroup viewMain;
    private ViewGroup mParent;
    private NetkromTextView mTvError;
    private ImageView imageView;

    private int mErrorResource = R.layout.view_error_or_empty;
    private int mLoadingResource = R.layout.view_progress;

    private OnRefreshClickListener onRefreshClickListener;
    private int currentState = STATE_MAIN;
    private boolean isErrorViewAdded = false, isEmpty = false;
    private String emptyMsgDefault;

    @Override
    protected void initEventAndData(Bundle bundle) {
        viewMain =  findViewById(R.id.view_main);
        if (viewMain == null) {
            throw new IllegalStateException(
                    "The subclass of RootActivity must contain a View named 'view_main'.");
        }
        if (!(viewMain.getParent() instanceof ViewGroup)) {
            throw new IllegalStateException(
                    "view_main's ParentView should be a ViewGroup.");
        }
        mParent = (ViewGroup) viewMain.getParent();
        View.inflate(mContext, mLoadingResource, mParent);
        viewLoading = mParent.findViewById(R.id.view_loading);
        viewLoading.setVisibility(View.GONE);
        viewMain.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorMsg(String msg, boolean usingDialog) {
        if(!usingDialog) {
            errorView(msg, TransmedikaUtils.setDrawable(mContext, transmedikaSettings.getErrorResource()));
        }

        MsgUiUtil.showSnackBar(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0),
                msg,mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
    }

    public void errorView(String msg, int image){
        if (currentState == STATE_ERROR)
            return;

        if (!isErrorViewAdded) {
            isErrorViewAdded = true;
            View.inflate(mContext, mErrorResource, mParent);
            viewError = mParent.findViewById(R.id.view_error);
            viewError.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
            mTvError = mParent.findViewById(R.id.tv_error);
            imageView = mParent.findViewById(R.id.image);
            NetkromButton btnRefresh = mParent.findViewById(R.id.btn_refresh);
            btnRefresh.setOnClickListener(v -> {
                if(onRefreshClickListener!=null)
                    onRefreshClickListener.onRefreshClick();
            });
            viewError.setOnRefreshListener(() -> {
                if(onRefreshClickListener!=null)
                    onRefreshClickListener.onRefreshClick();
            });
            if (viewError == null) {
                throw new IllegalStateException(
                        "A View should be named 'view_error' in ErrorLayoutResource.");
            }
        }

        mTvError.setText(msg);
        imageView.setImageDrawable(AppCompatResources.getDrawable(mContext,image));
        viewError.setRefreshing(false);
        hideCurrentView();
        currentState = STATE_ERROR;
        viewError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading(boolean usingDialog) {
        if(!usingDialog) {
            if (currentState == STATE_LOADING)
                return;
            hideCurrentView();
            currentState = STATE_LOADING;
            viewLoading.setVisibility(View.VISIBLE);
            if (isErrorViewAdded) {
                viewError.setRefreshing(true);
            }
        }else {
            msgUiUtil.showPgCommon();
        }
    }

    @Override
    public void hideLoading(boolean usingDialog) {
        if(!usingDialog) {
            if (!isEmpty) {
                if (currentState == STATE_MAIN)
                    return;

                if (isErrorViewAdded) {
                    viewError.setRefreshing(false);
                }
                hideCurrentView();
                currentState = STATE_MAIN;
                viewMain.setVisibility(View.VISIBLE);
            } else {
                errorView(emptyMsgDefault!=null? emptyMsgDefault:getString(R.string.tidak_ada_data_yang_ditampilkan),
                        TransmedikaUtils.setDrawable(mContext, transmedikaSettings.getEmptyDataResource()));
            }
            isEmpty = false;
        }else {
            msgUiUtil.dismisPgCommon();
        }
    }

    @Override
    public void onDataIsEmpty() {
        isEmpty = true;
    }

    private void hideCurrentView() {
        switch (currentState) {
            case STATE_MAIN:
                viewMain.setVisibility(View.GONE);
                break;
            case STATE_LOADING:
                viewLoading.setVisibility(View.GONE);
                break;
            case STATE_ERROR:
                if (viewError != null) {
                    viewError.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void setErrorResource(int errorLayoutResource) {
        this.mErrorResource = errorLayoutResource;
    }

    public void setmLoadingResource(int mLoadingResource) {
        this.mLoadingResource = mLoadingResource;
    }

    public void setOnRefreshClickListener(OnRefreshClickListener onRefreshClickListener) {
        this.onRefreshClickListener = onRefreshClickListener;
    }

    public interface OnRefreshClickListener{
        void onRefreshClick();
    }

    public void setEmptyMsgDefault(String emptyMsgDefault) {
        this.emptyMsgDefault = emptyMsgDefault;
    }
}
