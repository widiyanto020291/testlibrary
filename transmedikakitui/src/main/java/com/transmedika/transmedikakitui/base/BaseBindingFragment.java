package com.transmedika.transmedikakitui.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.snackbar.Snackbar;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.utils.MsgUiUtil;


/**
 * Created by Widiyanto02 on ic_3/ic_21/2018.
 */

public abstract class BaseBindingFragment<VB extends ViewBinding, V extends BaseView, T extends BasePresenter<V>> extends SimpleBindingFragment<VB> implements BaseView {

    protected T mPresenter;
    private boolean loadingActive = false;
    protected MsgUiUtil msgUiUtil;
    protected abstract V getBaseView();
    private boolean usingDialog = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter.attachView(getBaseView());
        super.onViewCreated(view, savedInstanceState);
        msgUiUtil = new MsgUiUtil(mContext);
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) mPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void showErrorMsg(String msg) {
        showErrorMsg(msg, usingDialog);
    }

    @Override
    public void showErrorMsg(String msg, boolean usingDialog) {
        MsgUiUtil.showSnackBar(((ViewGroup) _mActivity.findViewById(android.R.id.content)).getChildAt(0),
                msg, mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
    }

    @Override
    public void showLoading() {
        showLoading(usingDialog);
    }

    @Override
    public void showLoading(boolean usingDialog) {
        if (!loadingActive) {
            loadingActive = true;
            msgUiUtil.showPgCommon();
        }
    }

    @Override
    public void hideLoading() {
        hideLoading(usingDialog);
    }

    @Override
    public void hideLoading(boolean usingDialog) {
        msgUiUtil.dismisPgCommon();
        loadingActive = false;
    }

    @Override
    public void sessionExpired() {
        /*if(mActivity instanceof SignInMainActivity) {
            return;
        }else {
            Intent intent = new Intent(mContext, SignInMainActivity.class);
            startActivity(intent);
            mActivity.finish();
        }*/
    }

    @Override
    public void gotoPagePerbaikan() {

    }

    @Override
    public void onDataIsEmpty() {

    }

    public void setUsingDialog(boolean usingDialog) {
        this.usingDialog = usingDialog;
    }
}
