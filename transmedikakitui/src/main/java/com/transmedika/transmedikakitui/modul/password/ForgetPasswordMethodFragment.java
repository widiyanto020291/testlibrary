package com.transmedika.transmedikakitui.modul.password;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.transmedika.transmedikakitui.base.SimpleBindingFragment;
import com.transmedika.transmedikakitui.databinding.ForgotPasswordMethodBinding;
import com.transmedika.transmedikakitui.utils.Constants;

public class ForgetPasswordMethodFragment extends SimpleBindingFragment<ForgotPasswordMethodBinding> {
    public final static int EMAIL = 1;
    public final static int OTP = 2;

    ForgetPasswordMethodListener listener;

    public static ForgetPasswordMethodFragment newInstance() {
        Bundle args = new Bundle();
        ForgetPasswordMethodFragment fragment = new ForgetPasswordMethodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(ForgetPasswordMethodListener listener) {
        this.listener = listener;
    }

    @Override
    protected ForgotPasswordMethodBinding getViewBinding(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ForgotPasswordMethodBinding.inflate(inflater, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ForgetPasswordMethodListener) {
            setListener(((ForgetPasswordMethodListener) context));
        }
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        if(transmedikaSettings.getFontBold()!=null){
            binding.tvTitle.setCustomFont(mContext, transmedikaSettings.getFontBold());
        }
        binding.clMail.setOnClickListener(view -> startForResult(ForgetPasswordEmailFragment.newInstance(), EMAIL));
        binding.clOtp.setOnClickListener(view -> startForResult(ForgetPasswordOTPFragment.newInstance(), OTP));
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EMAIL) {
                if (listener != null) {
                    listener.onSuccessSendEmail(true);
                }
            } else if (requestCode == OTP) {
                if (listener != null) {
                    listener.onSuccessRequestOtp(true, data.getBoolean(Constants.IS_SIGNED, true));
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            boolean d = data != null && data.getBoolean("remember", false);
            if (d) {
                listener.onRememberPassword();
            }
        }
    }

    public interface ForgetPasswordMethodListener {
        void onSuccessSendEmail(boolean status);

        void onSuccessRequestOtp(boolean status, boolean isSigned);

        void onRememberPassword();
    }
}