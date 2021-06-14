package com.transmedika.transmedikakitui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;


import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.utils.ImageLoader;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import org.jetbrains.annotations.NotNull;

public class CommonToolbarMessage extends Toolbar {
    View view;
    NetkromTextView tvTitle, tvSubTitle;
    RelativeLayout rlBack;
    ImageView imgBack;
    CircleImageView imgProfile;
    private Context context;

    public CommonToolbarMessage(@NonNull @NotNull Context context) {
        super(context);
        init(context);
    }

    public CommonToolbarMessage(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public CommonToolbarMessage(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    private void init(Context context){
        TransmedikaSettings transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);
        view = inflate(getContext(), R.layout.common_toolbar_message, this);
        tvTitle = view.findViewById(R.id.tv_toolbar_title);
        tvSubTitle = view.findViewById(R.id.tv_toolbar_sub_title);
        rlBack = view.findViewById(R.id.rl_back);
        imgBack = view.findViewById(R.id.img_back);
        imgProfile = view.findViewById(R.id.civ_profile);

        if(transmedikaSettings.getBackIcon()!=null){
            imgBack.setImageDrawable(ResourcesCompat.getDrawable(getResources(),TransmedikaUtils.setDrawable(context,transmedikaSettings.getBackIcon()),null));
        }

        if(transmedikaSettings.getFontLight()!=null){
            tvSubTitle.setCustomFont(context, transmedikaSettings.getFontLight());
        }

        if(transmedikaSettings.getToolbarColor()!=null)
            setBackgroundColor(Color.parseColor(transmedikaSettings.getToolbarColor()));
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setSubTitle(String subTitle) {
        tvSubTitle.setText(subTitle);
    }

    public void setImageUrl(String imgUrl) {
        ImageLoader.load(context, imgUrl, imgProfile,R.drawable.bg_circle_place_holder);
    }

    public RelativeLayout getRlBack() {
        return rlBack;
    }

    public NetkromTextView getTvTitle() {
        return tvTitle;
    }

    public NetkromTextView getTvSubTitle() {
        return tvSubTitle;
    }

    public CircleImageView getImgProfile() {
        return imgProfile;
    }
}
