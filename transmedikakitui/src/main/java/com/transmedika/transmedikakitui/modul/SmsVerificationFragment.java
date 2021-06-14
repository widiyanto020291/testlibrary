package com.transmedika.transmedikakitui.modul;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.readystatesoftware.chuck.internal.ui.MainActivity;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseBindingFragment;
import com.transmedika.transmedikakitui.contract.SmsVerificationContract;
import com.transmedika.transmedikakitui.databinding.SmsVerificationFragmentBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.OtpParam;
import com.transmedika.transmedikakitui.models.bean.json.param.SmsVerificationParam;
import com.transmedika.transmedikakitui.presenter.SmsVerificationPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.MsgUiUtil;
import com.transmedika.transmedikakitui.utils.SpanUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SmsVerificationFragment extends BaseBindingFragment<SmsVerificationFragmentBinding, SmsVerificationContract.View, SmsVerificationPresenter>
        implements SmsVerificationContract.View {

    private String phone_number = "";
    private String email;
    private String message = "";
    private SmsVerificationParam smsVerificationParam;
    private String from;

    private CountDownTimer countDownTimer;

    public static SmsVerificationFragment newInstance(String phone_number, String email, String from) {
        Bundle args = new Bundle();
        SmsVerificationFragment fragment = new SmsVerificationFragment();
        fragment.setArguments(args);
        fragment.setPhoneNumber(phone_number);
        fragment.setEmail(email);
        fragment.setFrom(from);
        return fragment;
    }

    public static SmsVerificationFragment newInstance(String phone_number, String email) {
        Bundle args = new Bundle();
        SmsVerificationFragment fragment = new SmsVerificationFragment();
        fragment.setArguments(args);
        fragment.setPhoneNumber(phone_number);
        fragment.setEmail(email);
        return fragment;
    }

    public static SmsVerificationFragment newInstance(SmsVerificationParam param, String email) {
        Bundle args = new Bundle();
        SmsVerificationFragment fragment = new SmsVerificationFragment();
        args.putParcelable("params", param);
        fragment.setEmail(email);
        fragment.setArguments(args);
        return fragment;
    }

    public static SmsVerificationFragment newInstance(SmsVerificationParam param) {
        Bundle args = new Bundle();
        SmsVerificationFragment fragment = new SmsVerificationFragment();
        args.putParcelable("params", param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected SmsVerificationContract.View getBaseView() {
        return this;
    }

    @Override
    public void onViewCreated(@NotNull @NonNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new SmsVerificationPresenter(DataManager.getDataManagerInstance(mContext));

        SmsListener smsReceiver = new SmsListener();
        smsReceiver.setFragment(this);
        mActivity.registerReceiver(smsReceiver, new IntentFilter(
                "android.provider.Telephony.SMS_RECEIVED"));

        super.onViewCreated(view, savedInstanceState);

        Bundle arg = getArguments();
        smsVerificationParam = arg.getParcelable("params");
        if (smsVerificationParam != null) {
            setPhoneNumber(smsVerificationParam.getPhone_number());
        }

        if(this.email!=null){
            binding.tvLblEmail.setVisibility(View.VISIBLE);
            binding.tvEmail.setVisibility(View.VISIBLE);
            binding.tvEmail.setText(this.email);
        }

        binding.tvPhobe.setText(phone_number);
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setMessage(String message) {
        binding.edOtp.setText(message.replace("Autentikasi registrasi aplikasi Dokter Keluarga - ", "").trim());
        this.message = message;
    }

    @Override
    protected SmsVerificationFragmentBinding getViewBinding(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return SmsVerificationFragmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        setUpListener();
        setToolBar(getString(R.string.verifikasi));
        disableResendToken(true);
        startTimer();
//        if (initOtp) {
//            OtpParam param = new OtpParam(phone_number);
//            mPresenter.kirimUlangOtp(param, mContext);
//            new Handler().postDelayed(() -> {
//                setFragmentResult(Activity.RESULT_OK, new Bundle());
//                pop();
//            }, 1500);
//        }

        if(transmedikaSettings.getFontBold()!=null){
            binding.tvPhobe.setCustomFont(mContext, transmedikaSettings.getFontBold());
            binding.tvEmail.setCustomFont(mContext, transmedikaSettings.getFontBold());
        }

        if(transmedikaSettings.getFontLight()!=null){
            binding.lblMasukanOtp.setCustomFont(mContext, transmedikaSettings.getFontLight());
            binding.tvLblEmail.setCustomFont(mContext, transmedikaSettings.getFontLight());
        }
    }

    private void setUpListener() {
        binding.btnVerify.setOnClickListener(v -> {
            if (valid()) {
                final SmsVerificationParam param;
                if (smsVerificationParam != null) {
                    param = smsVerificationParam;
                } else {
                    param = new SmsVerificationParam(phone_number);
                }
                String otp = binding.otpView.getTextOtp();
                param.setVerification_code(otp);
                param.setRefType(Constants.PATIENT);
                mPresenter.smsVerification(param, mContext);
            }
        });
    }

    private void setToolBar(String title) {
        binding.toolbar.setNavigationOnClickListener(v -> pop());
        binding.toolbar.setTitle(title);
    }

    private void disableResendToken(boolean b) {
        if (b) {
            binding.tvResend.setEnabled(false);
            binding.tvResend.setClickable(false);
            binding.tvResend.setFocusable(false);
            binding.tvResend.setTextColor(ContextCompat.getColor(mContext, R.color.red_dark));
        } else {
            binding.tvResend.setEnabled(true);
            binding.tvResend.setClickable(true);
            binding.tvResend.setFocusable(true);
            binding.tvResend.setTextColor(ContextCompat.getColor(mContext, R.color.textDefault));
            SpanUtils spanUtils = new SpanUtils(mContext);
            spanUtils.setSpan(binding.tvResend, getString(R.string.krim_ulang), getResources().getColor(R.color.textDefault), getString(R.string.krim_ulang));
            spanUtils.setSpanClickListener(() -> {
                OtpParam param = new OtpParam(phone_number);
                mPresenter.kirimUlangOtp(param, mContext);
            });
        }
    }

    private boolean valid() {
        boolean valid = true;
        if (TextUtils.isEmpty(binding.otpView.getTextOtp()) || !binding.otpView.isFull()) {
            valid = false;
            binding.otpView.setError("Kode verifikasi harus diisi");
        }
        return valid;
    }

    @Override
    public void smsVerificationResp(BaseResponse<SignIn> response) {
        if (response.getData() != null) {
            if(this.from!=null){
                if(this.from.equals(Constants.FROM_REGISTER)){
                    Intent i = new Intent(mContext, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }else {
                setFragmentResult(Activity.RESULT_OK, new Bundle());
                pop();
            }
        } else {
            Toast.makeText(getActivity(), response.getMessages(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void kirimUlangOtpresp(BaseOResponse response) {
        startTimer();
    }



    private void startTimer() {
        disableResendToken(true);
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.tvResend.setText(getString(R.string.timer, String.valueOf(millisUntilFinished / 1000)));
                binding.tvResend.setText(String.format(Locale.getDefault(), "%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                disableResendToken(false);
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}
