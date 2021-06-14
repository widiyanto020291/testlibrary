package com.transmedika.transmedikakitui.modul.password;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseBindingFragment;
import com.transmedika.transmedikakitui.contract.password.PasswordManageContract;
import com.transmedika.transmedikakitui.databinding.PasswordFragmentBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.param.SmsVerificationParam;
import com.transmedika.transmedikakitui.modul.SmsVerificationFragment;
import com.transmedika.transmedikakitui.presenter.password.PasswordManagePresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.SpanUtils;
import com.transmedika.transmedikakitui.widget.NetkromDialog;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordFragment extends BaseBindingFragment<PasswordFragmentBinding, PasswordManageContract.View, PasswordManagePresenter>
    implements PasswordManageContract.View{


    @PasswordTask
    String taskType;

    private PasswordManageListener listener;
    private TaskPasswordBase task;

    public static PasswordFragment newInstance(@PasswordTask String taskType) {
        Bundle args = new Bundle();
        args.putString(Constants.TASK_TYPE, taskType);
        PasswordFragment fragment = new PasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected PasswordManageContract.View getBaseView() {
        return this;
    }

    @Override
    protected PasswordFragmentBinding getViewBinding(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return PasswordFragmentBinding.inflate(inflater);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof PasswordManageListener) {
            setPasswordManageListener(((PasswordManageListener) context));
        }
    }

    public void setPasswordManageListener(PasswordManageListener listener) {
        this.listener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new PasswordManagePresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(view, savedInstanceState);
        Bundle dtBundle = getArguments();
        taskType = dtBundle != null ? dtBundle.getString(Constants.TASK_TYPE, PasswordTask.CREATE_NEW) : PasswordTask.CREATE_NEW;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        createUi();
        SpanUtils spanUtils = new SpanUtils(mContext);
        spanUtils.setSpan(binding.forgotPassword, getString(R.string.lupa_password), getResources().getColor(R.color.textDefault), getString(R.string.lupa_password));
        spanUtils.setSpanClickListener(() -> {
            if (listener != null) {
                listener.onForgotPassword();
            }
        });
        binding.btnSimpan.setOnClickListener(view -> task.onSubmit());
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void createUi() {
        switch (taskType) {
            case PasswordTask.CREATE_NEW:
                task = new CreateNew();
                break;
            case PasswordTask.CHANGE:
                task = new Change();
                break;
            case PasswordTask.FORGOT:
                task = new Forgot();
                break;
            case PasswordTask.CONFIRM:
                task = new Confirm();
                break;
        }
        task.onUiSet();
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            task.onPasswordChange();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private boolean checkPasswordConfirm() {
        boolean result = false;
        if (binding.edPasswordNew.hasFocus() || binding.edPasswordNewKonfirm.hasFocus()) {
            result = isValidPassword(Objects.requireNonNull(binding.edPasswordNew.getText()).toString());
            if (result) {
                binding.edPasswordNew.setError(null);
                if (!binding.edPasswordNew.getText().toString().equals(Objects.requireNonNull(binding.edPasswordNewKonfirm.getText()).toString())) {
                    binding.edPasswordNewKonfirm.setError(getString(R.string.password_validasi));
                    result = false;
                } else {
                    binding.edPasswordNewKonfirm.setError(null);
                }
            } else {
                binding.edPasswordNew.setError(getString(R.string.password_validasi_char_8));
            }
        }
        return result;
    }

    private boolean isValidPassword(final String password) {
        Pattern eightCharPattern = Pattern.compile(Constants.EIGHT_CHAR_PATTERN);
        Pattern spacePattern = Pattern.compile(Constants.SPACE_PATTERN);
        Matcher matchereightCharPattern = eightCharPattern.matcher(password);
        Matcher matcherspacePattern = spacePattern.matcher(password);
        boolean d = matchereightCharPattern.matches();
        boolean e = matcherspacePattern.matches();
        return d && e;
    }

    @Override
    public void onSetPassword(Boolean statusResult) {
        if (listener != null) {
            listener.onGetStatus(taskType, statusResult);
        }
    }

    @Override
    public void onUbahPassword(String hash, String cookie) {
        if (hash != null) {
            SmsVerificationParam params = new SmsVerificationParam(mPresenter.selectLogin().getPhoneNumber());
            params.setIsType(SmsVerificationParam.UBAH_PASSWORD_TYPE);
            params.setHash(hash);
            params.setCookie(cookie);
            SmsVerificationFragment fragmentOtp =
                    SmsVerificationFragment.newInstance(params,mPresenter.selectLogin().getEmail());
            startForResult(fragmentOtp, SmsVerificationParam.UBAH_PASSWORD_TYPE);
        } else {
            showErrorMsg(getString(R.string.failed_ubah_no_ponsel));
        }
    }

    @Override
    public void onConfirm(Boolean statusResult) {
        if (listener != null) {
            listener.onGetStatus(taskType, statusResult);
        }
        if (!statusResult) {
            showErrorMsg(getString(R.string.invalid_password));
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SmsVerificationParam.UBAH_PASSWORD_TYPE) {
                mPresenter.deleteAllTable();
                if (listener != null) {
                    listener.onGetStatus(taskType, true);
                }
            }
        }
    }

    private abstract class TaskPasswordBase {
        abstract void onUiSet();

        abstract void onPasswordChange();

        abstract void onSubmit();

        TaskPasswordBase() {
            binding.edPassword.setVisibility(View.VISIBLE);
            binding.rlPassword.setVisibility(View.VISIBLE);
            binding.tvNewPassword.setVisibility(View.VISIBLE);
            binding.edPasswordNew.setVisibility(View.VISIBLE);
            binding.tvNewPasswordKonfirm.setVisibility(View.VISIBLE);
            binding.edPasswordNewKonfirm.setVisibility(View.VISIBLE);
            binding.tvKet.setVisibility(View.VISIBLE);
            binding.btnSimpan.setText(R.string.simpan);
            binding.tvPassword.setText(R.string.kata_sandi_sebelumnya);
        }

    }

    private class CreateNew extends TaskPasswordBase {

        @Override
        void onUiSet() {
            binding.edPassword.setVisibility(View.GONE);
            binding.rlPassword.setVisibility(View.GONE);

            binding.edPasswordNew.addTextChangedListener(textWatcher);
            binding.edPasswordNewKonfirm.addTextChangedListener(textWatcher);
        }

        @Override
        void onPasswordChange() {
            binding.btnSimpan.setEnabled(checkPasswordConfirm());
        }

        @Override
        void onSubmit() {
            mPresenter.setPassword(mContext, Objects.requireNonNull(binding.edPasswordNew.getText()).toString());
        }
    }

    private class Change extends TaskPasswordBase {

        private boolean passwordConfirm = false;

        @Override
        void onUiSet() {
            binding.edPassword.addTextChangedListener(textWatcher);
            binding.edPasswordNew.addTextChangedListener(textWatcher);
            binding.edPasswordNewKonfirm.addTextChangedListener(textWatcher);
        }

        @Override
        void onPasswordChange() {
            if (binding.edPasswordNew.hasFocus() || binding.edPasswordNewKonfirm.hasFocus()) {
                passwordConfirm = checkPasswordConfirm();
            }
            binding.btnSimpan.setEnabled(passwordConfirm && !Objects.requireNonNull(binding.edPassword.getText()).toString().isEmpty());
        }

        @Override
        void onSubmit() {
            NetkromDialog netkromDialog = new NetkromDialog(mContext, 0,
                    getString(R.string.ubah_kata_sandi),
                    getString(R.string.ubah_kata_sandi_ask), getString(R.string.ubah), getString(R.string.batal), 0);
            netkromDialog.setOnButtonClick(new NetkromDialog.onButtonClick() {
                @Override
                public void onBtnYaClick() {
                    netkromDialog.dismiss();
                    mPresenter.changePassword(mContext, Objects.requireNonNull(binding.edPassword.getText()).toString(), Objects.requireNonNull(binding.edPasswordNew.getText()).toString());
                }

                @Override
                public void onBntbatalClick() {
                    netkromDialog.dismiss();
                }
            });
            netkromDialog.show();
        }
    }

    private class Forgot extends TaskPasswordBase {

        @Override
        void onUiSet() {
            binding.edPassword.setVisibility(View.GONE);
            binding.rlPassword.setVisibility(View.GONE);

            binding.edPasswordNew.addTextChangedListener(textWatcher);
            binding.edPasswordNewKonfirm.addTextChangedListener(textWatcher);
        }

        @Override
        void onPasswordChange() {
            binding.btnSimpan.setEnabled(checkPasswordConfirm());
        }

        @Override
        void onSubmit() {
            mPresenter.setPassword(mContext, Objects.requireNonNull(binding.edPasswordNew.getText()).toString());
        }
    }

    private class Confirm extends TaskPasswordBase {

        @Override
        void onUiSet() {
            binding.tvNewPassword.setVisibility(View.GONE);
            binding.edPasswordNew.setVisibility(View.GONE);
            binding.tvNewPasswordKonfirm.setVisibility(View.GONE);
            binding.edPasswordNewKonfirm.setVisibility(View.GONE);
            binding.tvKet.setVisibility(View.GONE);
            binding.btnSimpan.setText(R.string.konfirmasi);

            binding.tvPassword.setText(R.string.password_anda);
            binding.edPassword.addTextChangedListener(textWatcher);
        }

        @Override
        void onPasswordChange() {
            binding.btnSimpan.setEnabled(!Objects.requireNonNull(binding.edPassword.getText()).toString().isEmpty());
        }

        @Override
        void onSubmit() {
            mPresenter.confirmPassword(mContext, Objects.requireNonNull(binding.edPassword.getText()).toString());
        }
    }

    public interface PasswordManageListener {
        void onForgotPassword();

        void onGetStatus(@PasswordTask String task, Boolean status);
    }
}
