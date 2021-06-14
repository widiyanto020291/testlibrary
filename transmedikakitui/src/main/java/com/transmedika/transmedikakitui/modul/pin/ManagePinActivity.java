package com.transmedika.transmedikakitui.modul.pin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.SimpleBindingActivity;
import com.transmedika.transmedikakitui.component.RxBus;
import com.transmedika.transmedikakitui.databinding.ActivityManagePinBinding;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.modul.password.ManagePasswordActivity;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.widget.NetkromDialog;

public class ManagePinActivity extends SimpleBindingActivity<ActivityManagePinBinding> implements PinFragment.PinListener {

    @PinTask
    String taskType;
    boolean fromForgetPassword = false;
    @PinTask
    String taskNotavailablePin;

    private final static int CONFIRM_PASSWORD_CODE = 1;

    public static Intent createIntent(Context context, @PinTask String taskType) {
        return createIntent(context, taskType, false);
    }

    public static Intent createIntent(Context context, @PinTask String taskType, boolean fromForgetPassword) {
        Intent intent = new Intent(context, ManagePinActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TASK_TYPE, taskType);
        bundle.putBoolean(Constants.FROM_FORGET_PASSWORD, fromForgetPassword);
        intent.putExtra(Constants.DATA, bundle);
        return intent;
    }

    @Override
    protected ActivityManagePinBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return ActivityManagePinBinding.inflate(inflater);
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        super.onViewCreated(bundle);
        Bundle dtBundle = getIntent().getBundleExtra(Constants.DATA);
        taskType = dtBundle != null ? dtBundle.getString(Constants.TASK_TYPE, PinTask.CREATE_NEW) : PinTask.CREATE_NEW;
        fromForgetPassword = dtBundle != null && dtBundle.getBoolean(Constants.FROM_FORGET_PASSWORD, false);
        loadRootFragment(binding.flContainer.getId(), PinFragment.newInstance(taskType, fromForgetPassword));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONFIRM_PASSWORD_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                finish();
            } else {
                binding.flContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setToolbarByTask(taskType);
    }

    public void setToolbarByTask(String taskType) {
        String toolbarName = "";
        switch (taskType) {
            case PinTask.CREATE_NEW:
                toolbarName = getString(R.string.buat_pin);
                break;
            case PinTask.CHANGE:
            case PinTask.FORGOT:
                toolbarName = getString(R.string.ubah_pin);
                break;
            case PinTask.CONFIRM:
                toolbarName = fromForgetPassword ? getString(R.string.lupa_password) : getString(R.string.pin_anda);
                break;
        }
        setToolBar(toolbarName);
    }

    public void setToolBar(String title) {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.toolbar.setTitle(title);
    }

    @Override
    public void onGetStatus(@PinTask String task, Boolean status, String pin) {
        if (status) {
            if (PinTask.CHANGE.equals(task) || PinTask.FORGOT.equals(task) || PinTask.CREATE_NEW.equals(task)) {
                String title = PinTask.CREATE_NEW.equals(task) ?
                        getString(R.string.success_set_pin) :
                        getString(R.string.success_change_pin);
                NetkromDialog netkromDialog = new NetkromDialog(mContext, R.drawable.ic_info_checklis, title, true, Constants.ALERT_CORRECT);
                netkromDialog.setOnCancelListener(dialogInterface -> {
                    if (PinTask.CONFIRM.equals(taskNotavailablePin)) {
                        replaceFragment(PinFragment.newInstance(taskNotavailablePin, fromForgetPassword), false);
                        taskType = taskNotavailablePin;
                        setToolbarByTask(taskType);
                        taskNotavailablePin = null;
                    } else {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(Constants.TASK_TYPE, task);
                        resultIntent.putExtra(Constants.PIN, pin);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }

                    if (PinTask.CREATE_NEW.equals(task)) {
                        BroadcastEvents.Event event = new BroadcastEvents.Event();
                        event.setObject(true);
                        event.setInitString(Constants.NEW_PIN_STATUS);
                        RxBus.getDefault().post(new BroadcastEvents(event));
                    }
                });
                netkromDialog.show();
            } else {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Constants.TASK_TYPE, task);
                resultIntent.putExtra(Constants.PIN, pin);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }
    }

    @Override
    public void onPinAvailable(Boolean status) {
        if (!status && !PinTask.CREATE_NEW.equals(taskType)) {
            binding.flContainer.setVisibility(View.INVISIBLE);
            NetkromDialog netkromDialog = new NetkromDialog(mContext, R.drawable.ic_pin,
                    getString(R.string.buat_pin),
                    getString(R.string.pin_belum_diterapkan), getString(R.string.buat_pin), getString(R.string.batal), R.drawable.btn_rec);
            netkromDialog.setOnButtonClick(new NetkromDialog.onButtonClick() {
                @Override
                public void onBtnYaClick() {
                    netkromDialog.dismiss();
                    taskNotavailablePin = taskType;
                    taskType = PinTask.CREATE_NEW;
                    setToolbarByTask(taskType);
                    checkTypeToPassword();
                }

                @Override
                public void onBntbatalClick() {
                    netkromDialog.dismiss();
                    finish();
                }
            });
            netkromDialog.setCanceledOnTouchOutside(false);
            netkromDialog.show();
        } else {
            checkTypeToPassword();
        }
    }

    private void checkTypeToPassword() {
        if (!taskType.equals(PinTask.CONFIRM)) {
            String title = "";
            switch (taskType) {
                case PinTask.CHANGE:
                    title = getString(R.string.ubah_pin);
                    break;
                case PinTask.CREATE_NEW:
                    title = getString(R.string.buat_pin);
                    break;
                case PinTask.FORGOT:
                    title = getString(R.string.lupa_pin);
                    break;
            }
            binding.flContainer.setVisibility(View.INVISIBLE);
            startActivityForResult(ManagePasswordActivity.createIntentConfirm(mContext, title), CONFIRM_PASSWORD_CODE);
        }
    }
}