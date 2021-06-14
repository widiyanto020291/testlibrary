package com.transmedika.transmedikakitui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;


/**
 * Created by Widiyanto02 on 8/1/2017.
 */
public class NetkromCheckBox extends AppCompatCheckBox {

    public NetkromCheckBox(Context context) {
        super(context);
    }

    public NetkromCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public NetkromCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TransmedikaSettings transmedikaSettings = TransmedikaUtils.transmedikaSettings(ctx);
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.NetkromCheckBox);
        String customFont = a.getString(R.styleable.NetkromCheckBox_customFont);
        WidgetUtils.setCustomFont(ctx, transmedikaSettings.getFontRegular(),this);
        a.recycle();
    }

    public void setCustomFont(Context ctx, String asset){
        WidgetUtils.setCustomFont(ctx, asset, this);
    }
}
