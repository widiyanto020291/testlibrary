package com.transmedika.transmedikakitui.modul;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.transmedika.transmedikakitui.base.BaseBindingActivity;
import com.transmedika.transmedikakitui.contract.SignInContract;
import com.transmedika.transmedikakitui.databinding.ActivityKitUiMainBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.SignInParam;
import com.transmedika.transmedikakitui.presenter.SignInPresenter;
import com.transmedika.transmedikakitui.utils.MsgUiUtil;

import org.jetbrains.annotations.NotNull;

public class KitUiMainActivity extends BaseBindingActivity<ActivityKitUiMainBinding, SignInContract.View, SignInPresenter>
        implements SignInContract.View{
    MsgUiUtil msgUiUtil;

    @Override
    protected ActivityKitUiMainBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivityKitUiMainBinding.inflate(inflater);
    }

    @NonNull
    @NotNull
    @Override
    protected SignInContract.View getBaseView() {
        return this;
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        mPresenter = new SignInPresenter(DataManager.getDataManagerInstance(mContext));
        msgUiUtil = new MsgUiUtil(mContext);
        super.onViewCreated(bundle);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        //MsgUiUtil.showSnackBar(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), "wkwkwkwkwkwwkwk",mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
        binding.toolbar.setNavigationOnClickListener(v -> msgUiUtil.showPgCommon());

        SignInParam param = new SignInParam();
        param.setUsername("087822204189");
        param.setPassword("123456");
        param.setRefType("patient");
        mPresenter.signIn("asd",param,mContext);
    }

    @Override
    public void signIn(BaseResponse<SignIn> response) {
        Log.i("",response.getMessages());
    }
}