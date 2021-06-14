package com.transmedika.transmedikakitui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.transmedika.transmedikakitui.R;


/**
 * Created by Widiyanto02 on 8/1/2017.
 */
public class NetkromTextViewAutoComplete extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {
    public NetkromTextViewAutoComplete(Context context) {
        super(context);
    }

    public NetkromTextViewAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public NetkromTextViewAutoComplete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.NetkromTextViewAutoComplete);
        String customFont = a.getString(R.styleable.NetkromTextViewAutoComplete_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public void setCustomFont(Context ctx, String asset){
        WidgetUtils.setCustomFont(ctx, asset, this);
    }
}
