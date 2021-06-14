package com.transmedika.transmedikakitui.modul.pin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseBindingFragment;
import com.transmedika.transmedikakitui.contract.pin.PinManageContract;
import com.transmedika.transmedikakitui.databinding.PinFragmentBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.modul.password.ManagePasswordActivity;
import com.transmedika.transmedikakitui.presenter.pin.PinManagePresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.MsgUiUtil;
import com.transmedika.transmedikakitui.utils.SpanUtils;
import com.transmedika.transmedikakitui.widget.NetkromDialog;
import com.transmedika.transmedikakitui.widget.NetkromTextView;
import com.transmedika.transmedikakitui.widget.PinView;

public class PinFragment extends BaseBindingFragment<PinFragmentBinding, PinManageContract.View, PinManagePresenter>
    implements PinManageContract.View{

    private @PinTask
    String taskType;
    private boolean fromForgetPassword = false;

    private PinListener listener;
    private TaskPinBase task;

    private static final int CONFIRM_PASSWORD_CODE = 100;
    private static final int SET_CODE = 1;
    private static final int CHANGE_CODE = 2;

    public static PinFragment newInstance(@PinTask String taskType) {
        return newInstance(taskType, false);
    }

    public static PinFragment newInstance(@PinTask String taskType, boolean fromForgetPassword) {
        Bundle args = new Bundle();
        args.putString(Constants.TASK_TYPE, taskType);
        args.putBoolean(Constants.FROM_FORGET_PASSWORD, fromForgetPassword);
        PinFragment fragment = new PinFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected PinManageContract.View getBaseView() {
        return this;
    }

    @Override
    protected PinFragmentBinding getViewBinding(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return PinFragmentBinding.inflate(inflater);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PinListener) {
            setPinListener(((PinListener) context));
        }
    }

    public void setPinListener(PinListener listener) {
        this.listener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new PinManagePresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(view, savedInstanceState);
        Bundle dtBundle = getArguments();
        taskType = dtBundle != null && mPresenter.selectLogin().getPinStatus() ? dtBundle.getString(Constants.TASK_TYPE, PinTask.CREATE_NEW) : PinTask.CREATE_NEW;
        if (listener != null) {
            listener.onPinAvailable(mPresenter.selectLogin().getPinStatus());
        }
        fromForgetPassword = dtBundle != null && dtBundle.getBoolean(Constants.FROM_FORGET_PASSWORD, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CONFIRM_PASSWORD_CODE) {
                taskType = PinTask.FORGOT;
                createNewTask();
            }
        }
    }

    private void createNewTask() {
        switch (taskType) {
            case PinTask.CREATE_NEW:
                task = new CreateNew();
                break;
            case PinTask.CHANGE:
                task = new Change();
                break;
            case PinTask.FORGOT:
                task = new Forgot();
                break;
            case PinTask.CONFIRM:
                task = new Confirm();
                break;
        }
        task.onUiSet();
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        createNewTask();
        binding.btnSimpan.setOnClickListener(view -> task.onSubmit());
    }

    PinView.ChangeTextListener pinListener = new PinView.ChangeTextListener() {
        @Override
        public void onChange(View view, CharSequence charSequence, Boolean isEnd) {
            task.onPinChange();
        }
    };

    private boolean checkPinConfirm() {
        boolean result = binding.pin.isFull() && binding.pinConfirm.isFull();
        if (result) {
            if (!binding.pin.getTextPin().equals(binding.pinConfirm.getTextPin())) {
                binding.pin.setError(getString(R.string.konfirmasi_pin_baru_error));
                binding.pinConfirm.setError(getString(R.string.konfirmasi_pin_baru_error));
                result = false;
            } else {
                binding.pin.setError("");
                binding.pinConfirm.setError("");
            }
        }
        return result;
    }

    @Override
    public void onSetPin(Boolean statusResult) {
        onListener(statusResult);
    }

    @Override
    public void onUbahPin(Boolean statusResult) {
        if (statusResult) {
            hideLoading();
        } else {
            showErrorMsg(getString(R.string.pin_salah));
        }
        onListener(statusResult);
    }

//    @Override
//    public void onUbahConfirm(Boolean statusResult) {
//        if (statusResult) {
//            SignUpManualSmsVerificationFragment fragmentOtp =
//                    SignUpManualSmsVerificationFragment.newInstance(
//                            mPresenter.selectLogin().getPhoneNumber(), true);
//            startForResult(fragmentOtp, CHANGE_CODE);
//        } else {
//            hideLoading();
//            showErrorMsg(getString(R.string.pin_salah));
//        }
//    }

    @Override
    public void onConfirm(Boolean statusResult) {
        if (statusResult) {
            onListener(true);
        } else {
            binding.pin.setError(getString(R.string.pin_salah));
            MsgUiUtil.showSnackBar(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0),
                    getString(R.string.pin_salah), mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
        }
    }

    private void onListener(Boolean statusResult) {
        if (listener != null) {
            listener.onGetStatus(taskType, statusResult, binding.pin.getTextPin());
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SET_CODE) { // set
                mPresenter.setPin(mContext, binding.pin.getTextPin());
            } else if (requestCode == CHANGE_CODE) { // change
//                showLoading();
//                mPresenter.changePin(mContext, binding.pin.getTextPin());
            }
        }
    }

    private abstract class TaskPinBase {

        private NetkromTextView mTvLabelInputPin;
        private NetkromTextView mTvForgotPin;

        protected NetkromTextView getmTvLabelInputPin() {
            return mTvLabelInputPin;
        }

        protected NetkromTextView getmTvForgotPin() {
            return mTvForgotPin;
        }

        abstract void onUiSet();

        abstract void onPinChange();

        abstract void onSubmit();

        TaskPinBase() {
            binding.pinOld.setVisibility(View.VISIBLE);
            binding.pin.setVisibility(View.VISIBLE);
            binding.pinConfirm.setVisibility(View.VISIBLE);
            binding.tvKet.setVisibility(View.VISIBLE);
            binding.pin.setShowHideIcon(true);
            binding.pin.setTitle(getString(R.string.pin));
            binding.btnSimpan.setText(R.string.simpan);
            binding.rlConfirmLabel.setVisibility(View.GONE);
        }

        protected void setHeaderForgot() {
            binding.rlConfirmLabel.setVisibility(View.VISIBLE);

            mTvForgotPin = binding.rlConfirmLabel.findViewById(R.id.forgot_pin);
            mTvLabelInputPin = binding.rlConfirmLabel.findViewById(R.id.tv_label_input_pin);

            SpanUtils spanUtils = new SpanUtils(mContext);
            spanUtils.setSpan(mTvForgotPin, getString(R.string.lupa_pin), getResources().getColor(R.color.textDefault), getString(R.string.lupa_pin));
            spanUtils.setSpanClickListener(() -> {
                if (fromForgetPassword) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Constants.FROM_FORGET_PASSWORD, fromForgetPassword);
                    _mActivity.setResult(Activity.RESULT_OK, resultIntent);
                    _mActivity.finish();
                } else {
                    startActivityForResult(ManagePasswordActivity.createIntentConfirm(mContext, getString(R.string.lupa_pin)), CONFIRM_PASSWORD_CODE);
                }
            });
        }

    }

    private class CreateNew extends TaskPinBase {

        @Override
        void onUiSet() {
            binding.pinOld.setVisibility(View.GONE);

            binding.pin.setChangeTextListener(pinListener);
            binding.pinConfirm.setChangeTextListener(pinListener);
        }

        @Override
        void onPinChange() {
            binding.btnSimpan.setEnabled(checkPinConfirm());
        }

        @Override
        void onSubmit() {
            mPresenter.setPin(mContext, binding.pin.getTextPin());
        }
    }

    private class Change extends TaskPinBase {

        @Override
        void onUiSet() {
            binding.pin.setTitle(getString(R.string.pin_baru));
            binding.pinConfirm.setTitle(getString(R.string.konfirmasi_pin_baru));

            setHeaderForgot();
            getmTvLabelInputPin().setVisibility(View.GONE);

            binding.pinOld.setChangeTextListener(pinListener);
            binding.pin.setChangeTextListener(pinListener);
            binding.pinConfirm.setChangeTextListener(pinListener);
        }

        @Override
        void onPinChange() {
            binding.btnSimpan.setEnabled(checkPinConfirm() && binding.pinOld.isFull());
        }

        @Override
        void onSubmit() {
            NetkromDialog netkromDialog = new NetkromDialog(mContext, 0,
                    getString(R.string.ubah_pin),
                    getString(R.string.ubah_pin_ask), getString(R.string.ubah), getString(R.string.batal), 0);
            netkromDialog.setOnButtonClick(new NetkromDialog.onButtonClick() {
                @Override
                public void onBtnYaClick() {
                    netkromDialog.dismiss();
                    mPresenter.changePin(mContext, binding.pinOld.getTextPin(), binding.pin.getTextPin());
//                    showLoading();
                }

                @Override
                public void onBntbatalClick() {
                    netkromDialog.dismiss();
                }
            });
            netkromDialog.show();
        }
    }

    private class Forgot extends TaskPinBase {

        @Override
        void onUiSet() {
            binding.pinOld.setVisibility(View.GONE);
            binding.pin.setTitle(getString(R.string.pin_baru));
            binding.pinConfirm.setTitle(getString(R.string.konfirmasi_pin_baru));

            binding.pin.setChangeTextListener(pinListener);
            binding.pinConfirm.setChangeTextListener(pinListener);
        }

        @Override
        void onPinChange() {
            binding.btnSimpan.setEnabled(checkPinConfirm());
        }

        @Override
        void onSubmit() {
//            SignUpManualSmsVerificationFragment fragmentOtp =
//                    SignUpManualSmsVerificationFragment.newInstance(
//                            mPresenter.selectLogin().getPhoneNumber(), true);
//            startForResult(fragmentOtp, SET_CODE);
            mPresenter.setPin(mContext, binding.pin.getTextPin());
        }
    }

    private class Confirm extends TaskPinBase {

        @Override
        void onUiSet() {
            binding.pinOld.setVisibility(View.GONE);
            binding.pinConfirm.setVisibility(View.GONE);
            binding.tvKet.setVisibility(View.GONE);
            binding.pin.setTitle("");
            binding.pin.setShowHideIcon(false);
            binding.btnSimpan.setText(R.string.konfirmasi);

            setHeaderForgot();

            binding.pin.setChangeTextListener(pinListener);
        }

        @Override
        void onPinChange() {
            binding.btnSimpan.setEnabled(binding.pin.isFull());
            if (binding.pin.isFull()) {
                binding.pin.setError("");
            }
        }

        @Override
        void onSubmit() {
            mPresenter.confirmPin(mContext, binding.pin.getTextPin());
        }
    }

    public interface PinListener {
        void onGetStatus(@PinTask String task, Boolean status, String pin);

        void onPinAvailable(Boolean status);
    }
}
