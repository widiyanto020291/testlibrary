package com.transmedika.transmedikakitui.modul.password;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.SimpleBindingActivity;
import com.transmedika.transmedikakitui.databinding.ActivityManagePinBinding;
import com.transmedika.transmedikakitui.modul.pin.PinTask;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.widget.NetkromDialog;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ManagePasswordActivity extends SimpleBindingActivity<ActivityManagePinBinding>
        implements PasswordFragment.PasswordManageListener {

    @PasswordTask
    String taskType;
    private final static int FORGET_CODE = 100;

    public static Intent createIntentConfirm(Context context, String title) {
        return createIntent(context, PasswordTask.CONFIRM, title);
    }

    public static Intent createIntent(Context context, @PasswordTask String taskType) {
        return createIntent(context, taskType, null);
    }

    public static Intent createIntent(Context context, @PasswordTask String taskType, String title) {
        Intent intent = new Intent(context, ManagePasswordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TASK_TYPE, taskType);
        bundle.putString(Constants.TITLE, title);
        intent.putExtra(Constants.DATA, bundle);
        return intent;
    }


    @Override
    protected ActivityManagePinBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivityManagePinBinding.inflate(inflater);
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        super.onViewCreated(bundle);
        Bundle dtBundle = getIntent().getBundleExtra(Constants.DATA);
        if (dtBundle != null) {
            taskType = dtBundle.getString(Constants.TASK_TYPE, PasswordTask.CONFIRM);
        } else {
            taskType = PasswordTask.CONFIRM;
            // TODO: 18/12/2020 Untuk deeplink
//            String action = getIntent().getAction();
//            Uri data = getIntent().getData();
//            String path = data.getPath();
//            if (path.equals("/reset-password")) {
//                taskType = PasswordTask.FORGOT;
//            }
//            Log.d("", "onViewCreated: " + path + ", action: " + action);
        }
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        String toolbarName = "";
        switch (taskType) {
            case PasswordTask.CREATE_NEW:
            case PasswordTask.FORGOT:
                toolbarName = getString(R.string.kata_sandi_baru);
                break;
            case PasswordTask.CHANGE:
                toolbarName = getString(R.string.ubah_kata_sandi);
                break;
            case PasswordTask.CONFIRM:
                toolbarName = getIntent().getBundleExtra(Constants.DATA) != null ?
                        Objects.requireNonNull(getIntent().getBundleExtra(Constants.DATA))
                                .getString(Constants.TITLE, getString(R.string.konfirmasi_password))
                        : getString(R.string.konfirmasi_password);
                break;
        }
        setToolBar(toolbarName);
        loadRootFragment(binding.flContainer.getId(), PasswordFragment.newInstance(taskType));
    }

    public void setToolBar(String title) {
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressedSupport());
        binding.toolbar.setTitle(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FORGET_CODE && data != null) {
                String type = data.getStringExtra(Constants.TASK_TYPE);
                if (Constants.OTP.equals(type)) {
                    if (getTopFragment() instanceof PasswordFragment) {
                        PasswordFragment fragment = ((PasswordFragment) getTopFragment());
                        fragment.setTaskType(PasswordTask.FORGOT);
                        fragment.createUi();
                    }
                }
            }
        }
    }

    @Override
    public void onForgotPassword() {
        startActivityForResult(ForgotPasswordActivity.createIntent(mContext), FORGET_CODE);
    }

    @Override
    public void onGetStatus(@PasswordTask String task, Boolean status) {
        if (status) {
            if (PasswordTask.CHANGE.equals(task) || PasswordTask.FORGOT.equals(task) || PasswordTask.CREATE_NEW.equals(task)) {
                String title = PinTask.CHANGE.equals(task) ?
                        getString(R.string.success_change_password) :
                        getString(R.string.success_set_password);
                NetkromDialog netkromDialog = new NetkromDialog(mContext, R.drawable.ic_info_checklis, title, true);
                netkromDialog.setOnCancelListener(dialogInterface -> {
                    /*Intent intent = SignInActivity.toLoginIntent(mContext);
                    startActivity(intent);*/
                });
                netkromDialog.show();
            } else {
                setResult(Activity.RESULT_OK);
                finish();
            }
        }
    }
}
