package com.transmedika.transmedikakitui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;


public class SnackbarView extends LinearLayout {

    View view;
    NetkromTextView tvMessage;

    public SnackbarView(Context context) {
        super(context);
        init(context);
    }

    public SnackbarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SnackbarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        TransmedikaSettings transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);
        view = inflate(getContext(), R.layout.snackbar_view, this);
        tvMessage = view.findViewById(R.id.tv_msg);

        tvMessage.setCustomFont(context, transmedikaSettings.getFontRegular());
        tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tvMessage.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white,null));
    }

    public void setMessage(String a){
        tvMessage.setText(a);
    }
}
