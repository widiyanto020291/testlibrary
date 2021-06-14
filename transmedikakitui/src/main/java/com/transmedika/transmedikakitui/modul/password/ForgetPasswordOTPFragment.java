package com.transmedika.transmedikakitui.modul.password;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseBindingFragment;
import com.transmedika.transmedikakitui.contract.password.ForgetPasswordOtpContract;
import com.transmedika.transmedikakitui.databinding.ForgetPasswordOtpFragmentBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.param.SmsVerificationParam;
import com.transmedika.transmedikakitui.modul.SmsVerificationFragment;
import com.transmedika.transmedikakitui.presenter.password.ForgetPasswordOtpPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.SpanUtils;
import com.transmedika.transmedikakitui.widget.NetkromDialog;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPasswordOTPFragment extends BaseBindingFragment<ForgetPasswordOtpFragmentBinding, ForgetPasswordOtpContract.View, ForgetPasswordOtpPresenter>
        implements ForgetPasswordOtpContract.View {

    private final Handler inputHandler = new Handler();
    long delay = 1000; //milliseconds
    final long[] lastTextEdit = {0};
    private boolean statusNoPhone = false;
    private boolean passwordConfirm = false;
    boolean isLogin;

    public static ForgetPasswordOTPFragment newInstance() {
        Bundle bundle = new Bundle();
        ForgetPasswordOTPFragment fragment = new ForgetPasswordOTPFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected ForgetPasswordOtpFragmentBinding getViewBinding(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ForgetPasswordOtpFragmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected ForgetPasswordOtpContract.View getBaseView() {
        return this;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new ForgetPasswordOtpPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(view, savedInstanceState);
        isLogin = mPresenter.selectLogin() != null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        if (isLogin) {
            String phoneNumber = mPresenter.selectLogin().getPhoneNumber();
            binding.tvDesc.setText(getString(R.string.forgot_password_otp_desc_signed, phoneNumber.substring(phoneNumber.length() - 4)));
        }

        SpanUtils spSign = new SpanUtils(mContext);
        spSign.setSpan(binding.tvRemember, getString(R.string.remember_password), getResources().getColor(R.color.textDefault), getString(R.string.remember_password));
        spSign.setSpanClickListener(() -> {
            Bundle b = new Bundle();
            b.putBoolean("remember", true);
            setFragmentResult(Activity.RESULT_CANCELED, b);
            pop();
        });

        binding.edPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputHandler.removeCallbacks(checkerTime);
                binding.edPhone.setError(null);
                statusNoPhone = false;
                binding.btnSend.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                lastTextEdit[0] = System.currentTimeMillis();
                inputHandler.postDelayed(checkerTime, delay - 250);
                if (binding.pbValidasi.getVisibility() == View.GONE) {
                    binding.pbValidasi.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.edPasswordNew.addTextChangedListener(textWatcherPassword);
        binding.edPasswordNewKonfirm.addTextChangedListener(textWatcherPassword);

        binding.btnSend.setOnClickListener(view -> {
//            if (isLogin) {
            mPresenter.requestOtp(mContext, Objects.requireNonNull(binding.edPhone.getText()).toString(), Objects.requireNonNull(binding.edPasswordNew.getText()).toString(), Objects.requireNonNull(binding.edPasswordNewKonfirm.getText()).toString());
//            } else {
//                mPresenter.requestOtp(mContext, binding.edPhone.getText().toString());
//            }
        });

        if(transmedikaSettings.getFontBold()!=null){
            binding.tvLabelTitle.setCustomFont(mContext, transmedikaSettings.getFontBold());
        }
    }

    Runnable checkerTime = () -> {
        if (System.currentTimeMillis() >= (lastTextEdit[0] + delay - 500)) {
            String textValue = Objects.requireNonNull(Objects.requireNonNull(binding.edPhone.getText()).toString());
            if (TextUtils.isEmpty(textValue)) {
                binding.edPhone.setError(getString(R.string.ponsel_required));
            } else if (!Patterns.PHONE.matcher(textValue).matches()) {
                binding.edPhone.setError(getString(R.string.ponsel_validasi));
            } else {
                statusNoPhone = true;
            }

            binding.btnSend.setEnabled(passwordConfirm && statusNoPhone);
            binding.pbValidasi.setVisibility(View.GONE);
        }
    };

    private final TextWatcher textWatcherPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (binding.edPasswordNew.hasFocus() || binding.edPasswordNewKonfirm.hasFocus()) {
                passwordConfirm = checkPasswordConfirm();
            }
            binding.btnSend.setEnabled(passwordConfirm && statusNoPhone);
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
    public void onDetach() {
        super.onDetach();
        inputHandler.removeCallbacks(checkerTime);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                NetkromDialog netkromDialog = new NetkromDialog(mContext, R.drawable.ic_info_checklis, getString(R.string.success_change_password_relogin), true);
                netkromDialog.setOnCancelListener(dialogInterface -> {
                    if (isLogin) {
                        /*Intent intent = SignInActivity.toLoginIntent(mContext);
                        startActivity(intent);*/
                    } else {
                        mActivity.finish();
                    }
                });
                mPresenter.deleteAllTables();
                netkromDialog.show();
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.IS_SIGNED, true);
                setFragmentResult(Activity.RESULT_OK, bundle);
                pop();
            }
        }
    }

    @Override
    public void onSuccessResetPasswordOtp(String hash, String cookie) {
        if (hash != null && !hash.isEmpty()) {
            SmsVerificationParam params = new SmsVerificationParam(Objects.requireNonNull(binding.edPhone.getText()).toString());
            params.setIsType(2);
            params.setHash(hash);
            params.setCookie(cookie);
            SmsVerificationFragment fragmentOtp =
                    SmsVerificationFragment.newInstance(params);
            startForResult(fragmentOtp, 2);
        } else {
            showErrorMsg(getString(R.string.failed_request_otp));
        }
    }

    @Override
    public void onSuccessSendOtp(Boolean statusResult) {
        if (statusResult) {
            SmsVerificationParam params = new SmsVerificationParam(Objects.requireNonNull(binding.edPhone.getText()).toString());
//            params.setIsType(SmsVerificationParam.RESET_PASSWORD_TYPE);
            SmsVerificationFragment fragmentOtp =
                    SmsVerificationFragment.newInstance(params);
            startForResult(fragmentOtp, 1);
        } else {
            showErrorMsg(getString(R.string.failed_request_otp));
        }
    }
}
