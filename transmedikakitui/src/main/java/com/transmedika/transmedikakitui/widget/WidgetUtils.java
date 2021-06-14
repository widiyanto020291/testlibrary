package com.transmedika.transmedikakitui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

public class WidgetUtils {
    private static final String TAG = WidgetUtils.class.getSimpleName();
    public static void setCustomFont(Context ctx, String asset, TextView view) {
        if(asset!=null) {
            Typeface tf;
            try {
                tf = Typeface.createFromAsset(ctx.getAssets(), asset);
                view.setTypeface(tf);
            } catch (Exception e) {
                Log.e(TAG, "Could not get typeface: " + e.getMessage());
            }
        }
    }
}
