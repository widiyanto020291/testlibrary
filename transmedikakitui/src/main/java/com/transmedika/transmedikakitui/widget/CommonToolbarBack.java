package com.transmedika.transmedikakitui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;


public class CommonToolbarBack extends Toolbar {
    View view;
    NetkromTextView tvTitle;
    private String title;

    public CommonToolbarBack(@NonNull Context context) {
        super(context);
    }

    public CommonToolbarBack(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CommonToolbarBack(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonToolbarBack);
        title = a.getString(R.styleable.CommonToolbarBack_title);
        boolean withMenu = a.getBoolean(R.styleable.CommonToolbarBack_withMenu, false);

        TransmedikaSettings transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);
        view = inflate(getContext(), R.layout.common_toolbar, this);
        tvTitle = view.findViewById(R.id.tv_title);
        if (transmedikaSettings.getToolbarCenterTitle()) {
            if(!withMenu) {
                tvTitle.setPadding(TransmedikaUtils.dip2px(context, -64), 0, 0, 0);
            }else {
                tvTitle.setPadding(TransmedikaUtils.dip2px(context,-28),0,0,0);
            }
            tvTitle.setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
        }else {
            tvTitle.setGravity(Gravity.START|Gravity.CENTER_VERTICAL);
        }

        setNavigationIcon(ResourcesCompat.getDrawable(getResources(),
                TransmedikaUtils.setDrawable(context, transmedikaSettings.getBackIcon()),null));
        tvTitle.setText(this.title);
        tvTitle.setCustomFont(context, transmedikaSettings.getFontRegular());
        tvTitle.setTextColor(Color.parseColor(transmedikaSettings.getTitleToolbarColor()));

        if(transmedikaSettings.getToolbarColor()!=null)
            setBackgroundColor(Color.parseColor(transmedikaSettings.getToolbarColor()));
        a.recycle();
    }

    public void setTitle(String title) {
        this.title = title;
        tvTitle.setText(this.title);
    }

    public NetkromTextView getTvTitle() {
        return tvTitle;
    }
}
