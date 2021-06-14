package com.transmedika.transmedikakitui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.databinding.NetkromDialogBinding;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;


public class NetkromDialog extends Dialog {

    private onButtonClick onButtonClick;
    private final NetkromDialogBinding binding;

    public ImageView getImg() {
        return binding.img;
    }

    public NetkromDialog(@NonNull Context context, int imgParam, String titleParam, boolean animation, String assets) {
        this(context, imgParam, null, titleParam, null, 0);
        if (animation) {
            binding.img.getLayoutParams().height = Math.round(dipToPixels(context, 100f));
            binding.img.getLayoutParams().width = Math.round(dipToPixels(context, 100f));
            binding.img.requestLayout();
        }
        binding.img.setAnimation(assets);
        binding.llButtonMain.setVisibility(View.GONE);
        binding.rootView.setClickable(true);
        binding.rootView.setFocusable(true);
        binding.rootView.setOnClickListener(view -> cancel());
        setCanceledOnTouchOutside(true);
    }

    public NetkromDialog(@NonNull Context context, int imgParam, String titleParam, boolean isBigImage) {
        this(context, imgParam, null, titleParam, null, 0);
        if (isBigImage) {
            binding.img.getLayoutParams().height = Math.round(dipToPixels(context, 100f));
            binding.img.getLayoutParams().width = Math.round(dipToPixels(context, 100f));
            binding.img.requestLayout();
        }
        binding.llButtonMain.setVisibility(View.GONE);
        binding.rootView.setClickable(true);
        binding.rootView.setFocusable(true);
        binding.rootView.setOnClickListener(view -> cancel());
        setCanceledOnTouchOutside(true);
    }

    public NetkromDialog(@NonNull Context context, int imgParam, String titleParam,
                         String subTitleParam, String btnYaParam, int imgBgParam) {
        this(context, imgParam, titleParam, subTitleParam, btnYaParam, null, imgBgParam);
        setCanceledOnTouchOutside(false);
    }

    public NetkromDialog(@NonNull Context context, int imgParam, String titleParam,
                         String subTitleParam, String btnYaParam, String btnBatalParam, int imgBgParam) {
        this(context, imgParam, titleParam, subTitleParam, null, btnYaParam, btnBatalParam, imgBgParam);
    }

    public NetkromDialog(@NonNull Context context, int imgParam, String titleParam,
                         String subTitleParam, String ket, String btnYaParam, String btnBatalParam, int imgBgParam) {
        super(context);
        TransmedikaSettings transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = NetkromDialogBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        binding.tvTitle.setCustomFont(context, transmedikaSettings.getFontBold());

        if (titleParam == null) {
            binding.tvTitle.setVisibility(View.GONE);
        }

        if (subTitleParam == null) {
            binding.tvSubTitle.setVisibility(View.GONE);
        }

        if (ket == null) {
            binding.tvKet.setVisibility(View.GONE);
        } else {
            binding.tvKet.setText(ket);
        }

        if (imgParam == 0) {
            binding.img.setVisibility(View.GONE);
        } else {
            binding.img.setImageDrawable(AppCompatResources.getDrawable(context, imgParam));
        }

        if (imgBgParam == 0) {
            binding.btnYa.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_rec_red));
        } else {
            binding.btnYa.setBackground(ContextCompat.getDrawable(context, imgBgParam));
        }

        binding.tvTitle.setText(titleParam);
        binding.tvSubTitle.setText(subTitleParam);
        if (btnBatalParam == null || btnBatalParam.isEmpty()) {
            binding.btnBatal.setVisibility(View.GONE);
        } else {
            binding.btnBatal.setText(btnBatalParam);
        }

        if (btnYaParam == null) {
            binding.btnYa.setVisibility(View.GONE);
        } else {
            binding.btnYa.setText(btnYaParam);
        }

        binding.btnYa.setOnClickListener(v -> onButtonClick.onBtnYaClick());
        binding.btnBatal.setOnClickListener(v -> onButtonClick.onBntbatalClick());
    }

    public void setOnButtonClick(NetkromDialog.onButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public interface onButtonClick {
        void onBtnYaClick();

        void onBntbatalClick();
    }
}
