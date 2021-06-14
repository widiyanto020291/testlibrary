package com.transmedika.transmedikakitui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.databinding.ReconnectDialogBinding;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;



public class ReconnectDialog extends Dialog {

    private final ReconnectDialogBinding binding;
    private CountDownTimer countDownTimer;
    private final Context context;


    public ReconnectDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ReconnectDialogBinding.inflate(LayoutInflater.from(context));
        setCancelable(false);
        setContentView(binding.getRoot());
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TransmedikaSettings transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);

        if(transmedikaSettings.getFontBold()!=null)
            binding.tvTitle.setCustomFont(context, transmedikaSettings.getFontBold());
    }

    public void setSubTitle(String subTitle) {
        binding.tvSubTitle.setText(subTitle);
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.tvSubTitle.setText(context.getString(R.string.dua_param_string, "Sedang menghubungkan ke server chat dalam",String.format(Locale.getDefault(), "%d detik",
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))));
            }

            public void onFinish() {
                stopTimer();
                startTimer();
            }
        }.start();
    }

    @Override
    protected void onStop() {
        stopTimer();
        super.onStop();
    }

    public void stopTimer(){
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
