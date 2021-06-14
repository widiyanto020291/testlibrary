package com.transmedika.transmedikakitui.modul.password;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.SimpleBindingActivity;
import com.transmedika.transmedikakitui.databinding.ActivityManagePinBinding;
import com.transmedika.transmedikakitui.utils.Constants;

public class ForgotPasswordActivity extends SimpleBindingActivity<ActivityManagePinBinding>
        implements ForgetPasswordMethodFragment.ForgetPasswordMethodListener {

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, ForgotPasswordActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra(Constants.DATA, bundle);
        return intent;
    }

    @Override
    protected ActivityManagePinBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return ActivityManagePinBinding.inflate(inflater);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setToolBar(getString(R.string.lupa_password));
        loadRootFragment(binding.flContainer.getId(), ForgetPasswordMethodFragment.newInstance());
    }


    public void setToolBar(String title) {
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressedSupport());
        binding.toolbar.setTitle(title);
    }

    @Override
    public void onSuccessSendEmail(boolean status) {
        Intent intent = new Intent();
        intent.putExtra(Constants.TASK_TYPE, Constants.EMAIL);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSuccessRequestOtp(boolean status, boolean isSigned) {
        Intent intent = new Intent();
        intent.putExtra(Constants.TASK_TYPE, Constants.OTP);
        intent.putExtra(Constants.IS_SIGNED, isSigned);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRememberPassword() {
        finish();
    }
}
