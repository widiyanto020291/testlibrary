package com.transmedika.transmedikakitui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;


import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.databinding.ProgressDialogViewNBinding;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NetkromProgressDialog extends Dialog {
    private final List<Integer> imgList = new ArrayList<>();
    private final Handler mHandler;
    private final ProgressDialogViewNBinding binding;

    public NetkromProgressDialog(@NonNull Context context) {
        super(context);

        TransmedikaSettings transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);
        if(transmedikaSettings.getDialogResource()!=null && transmedikaSettings.getDialogResource().size() > 0) {
            for (String a : transmedikaSettings.getDialogResource()){
                imgList.add(TransmedikaUtils.setDrawable(context, a));
            }
        }else {
            imgList.add(R.drawable.ic_dialog_1);
            imgList.add(R.drawable.ic_dialog_2);
            imgList.add(R.drawable.ic_dialog_3);
            imgList.add(R.drawable.ic_dialog_4);
            imgList.add(R.drawable.ic_dialog_5);
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ProgressDialogViewNBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        mHandler = new Handler(Looper.myLooper());
        startRepeatingTask();
        binding.img.getLayoutParams().height = Math.round(TransmedikaUtils.dip2px(context, 100f));
        binding.img.getLayoutParams().width = Math.round(TransmedikaUtils.dip2px(context, 100f));
        binding.img.requestLayout();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRepeatingTask();
    }

    private final Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if(imgList.size() > 0) {
                    Random rnd = new Random();
                    int randomNum = rnd.nextInt(imgList.size() - 1);
                    binding.img.setImageResource(imgList.get(randomNum));
                }
            } finally {
                int mInterval = 500;
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    private void startRepeatingTask() {
        mStatusChecker.run();
    }

    private void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
}
