package com.transmedika.transmedikakitui.modul.password;

import android.app.Activity;
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
import com.transmedika.transmedikakitui.contract.password.ForgetPasswordEmailContract;
import com.transmedika.transmedikakitui.databinding.ForgetPasswordEmailFragmentBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.presenter.password.ForgetPasswordEmailPresenter;
import com.transmedika.transmedikakitui.utils.SpanUtils;
import com.transmedika.transmedikakitui.widget.NetkromDialog;

import java.util.Objects;

public class ForgetPasswordEmailFragment extends BaseBindingFragment<ForgetPasswordEmailFragmentBinding, ForgetPasswordEmailContract.View, ForgetPasswordEmailPresenter>
        implements ForgetPasswordEmailContract.View {

    private final Handler emailSearchHandler = new Handler();
    long delay = 1000; //milliseconds
    final long[] lastTextEdit = {0};

    public static ForgetPasswordEmailFragment newInstance() {
        Bundle bundle = new Bundle();
        ForgetPasswordEmailFragment fragment = new ForgetPasswordEmailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected ForgetPasswordEmailFragmentBinding getViewBinding(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ForgetPasswordEmailFragmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected ForgetPasswordEmailContract.View getBaseView() {
        return this;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new ForgetPasswordEmailPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        if (mPresenter.selectLogin() != null) {
            String email = mPresenter.selectLogin().getEmail();
            String[] emailS = email.split("@");
            String name = (emailS[0].length() >= 3) ? emailS[0].substring(0, 3).concat("*****") : emailS[0].concat("*****");
            String provider = (emailS[1].length() >= 3) ? emailS[1].substring(0, 3).concat("***") : emailS[1].concat("***");
            binding.tvDesc.setText(getString(R.string.forgot_password_desc_signed, name.concat("@").concat(provider)));
        }

        SpanUtils spSign = new SpanUtils(mContext);
        spSign.setSpan(binding.tvRemember, getString(R.string.remember_password), getResources().getColor(R.color.textDefault), getString(R.string.remember_password));
        spSign.setSpanClickListener(() -> {
            Bundle b = new Bundle();
            b.putBoolean("remember", true);
            setFragmentResult(Activity.RESULT_CANCELED, b);
            pop();
        });

        binding.edEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailSearchHandler.removeCallbacks(checkerTime);
                binding.edEmail.setError(null);
                binding.btnSend.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                lastTextEdit[0] = System.currentTimeMillis();
                emailSearchHandler.postDelayed(checkerTime, delay - 250);
                if (binding.pbValidasi.getVisibility() == View.GONE) {
                    binding.pbValidasi.setVisibility(View.VISIBLE);
                }
            }

        });

        binding.btnSend.setOnClickListener(view -> mPresenter.sendEmail(mContext, Objects.requireNonNull(binding.edEmail.getText()).toString()));
    }

    Runnable checkerTime = () -> {
        if (System.currentTimeMillis() >= (lastTextEdit[0] + delay - 500)) {
            boolean status = true;

            String textEmail = Objects.requireNonNull(binding.edEmail.getText()).toString();
            if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                binding.edEmail.setError(getString(R.string.email_validasi));
                status = false;
            }

            if (TextUtils.isEmpty(textEmail)) {
                binding.edEmail.setError(getString(R.string.email_required));
                status = false;
            }

            binding.btnSend.setEnabled(status);
            binding.pbValidasi.setVisibility(View.GONE);
        }

        if(transmedikaSettings.getFontBold()!=null){
            binding.tvLabelTitle.setCustomFont(mContext, transmedikaSettings.getFontBold());
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        emailSearchHandler.removeCallbacks(checkerTime);
    }

    @Override
    public void onSuccessSendEmail(Boolean statusResult) {
        if (statusResult) {
            String title = getString(R.string.success_send_email).concat("\n\n").concat(getString(R.string.reset_password_email_message, Objects.requireNonNull(binding.edEmail.getText()).toString()));
            NetkromDialog netkromDialog = new NetkromDialog(mContext, R.drawable.ic_info_checklis, title, true);
            netkromDialog.setOnCancelListener(dialogInterface -> {
                setFragmentResult(Activity.RESULT_OK, new Bundle());
                pop();
            });
            netkromDialog.show();
        } else {
            showErrorMsg(getString(R.string.failed_send_email));
        }
    }
}

