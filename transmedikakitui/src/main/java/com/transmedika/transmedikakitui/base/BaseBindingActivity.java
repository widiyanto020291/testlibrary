package com.transmedika.transmedikakitui.base;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.snackbar.Snackbar;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.utils.MsgUiUtil;


/**
 * Created by Widiyanto02 on 1/23/2018.
 */

public abstract class BaseBindingActivity<VB extends ViewBinding, V extends BaseView, T extends BasePresenter<V>>
        extends SimpleBindingActivity<VB> implements BaseView {

    protected T mPresenter;
    protected MsgUiUtil msgUiUtil;
    protected abstract @NonNull V getBaseView();
    private boolean usingDialog = false;

    @Override
    protected void onViewCreated(Bundle bundle) {
        super.onViewCreated(bundle);
        if (mPresenter != null) mPresenter.attachView(getBaseView());
        msgUiUtil = new MsgUiUtil(mContext);
    }

    @Override
    public void showErrorMsg(String msg) {
        showErrorMsg(msg, usingDialog);
    }

    @Override
    public void showErrorMsg(String msg, boolean usingDialog) {
        MsgUiUtil.showSnackBar(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0),
                msg,mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();
        if (msgUiUtil != null)
            msgUiUtil.dismisPgCommon();
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        showLoading(usingDialog);
    }

    @Override
    public void showLoading(boolean usingDialog) {
        msgUiUtil.showPgCommon();
    }

    @Override
    public void hideLoading() {
        hideLoading(usingDialog);
    }

    @Override
    public void hideLoading(boolean usingDialog) {
        msgUiUtil.dismisPgCommon();
    }

    @Override
    public void gotoPagePerbaikan() {

    }

    @Override
    public void onDataIsEmpty() {

    }

    @Override
    public void sessionExpired() {
        /*Intent intent = new Intent(mContext, SignInMainActivity.class);
        startActivity(intent);
        finish();*/
    }

    public void setUsingDialog(boolean usingDialog) {
        this.usingDialog = usingDialog;
    }
}
