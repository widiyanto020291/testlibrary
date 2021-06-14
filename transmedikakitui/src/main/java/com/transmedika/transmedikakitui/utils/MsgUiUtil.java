package com.transmedika.transmedikakitui.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.snackbar.Snackbar;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.dialog.NetkromProgressDialog;
import com.transmedika.transmedikakitui.widget.SnackbarView;


/**
 * Created by Widiyanto02 on 1/4/2018.
 */

public class MsgUiUtil {
    private NetkromProgressDialog pgCommon;
    private Context context;

    public MsgUiUtil(Context context) {
        this.context = context;
    }

    public static void showSnackBar(View view, String msg, Context context, int imgDrawable, int bgColor, int duration) {
        Snackbar snackbar = Snackbar.make(view, msg, duration);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView = layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        SnackbarView snackView = new SnackbarView(context);
        LinearLayout llMain = snackView.findViewById(R.id.ll_main);
        ImageView imageView = snackView.findViewById(R.id.img_warning);
        llMain.setBackgroundColor(ResourcesCompat.getColor(context.getResources(),bgColor,null));
        imageView.setImageResource(imgDrawable);
        snackView.setMessage(msg);
        layout.setPadding(0,0,0,0);
        layout.addView(snackView, 0);
        snackbar.show();
    }

    public void showPgCommon(){
        pgCommon = new NetkromProgressDialog(context);
        if(pgCommon.getWindow()!=null && !pgCommon.isShowing()) {
            pgCommon.setCancelable(true);
            pgCommon.show();
        }
    }

    public void dismisPgCommon(){
        if(pgCommon!=null)
            if(pgCommon.isShowing())
                pgCommon.dismiss();
    }

    public NetkromProgressDialog getPgCommon(){
        return pgCommon;
    }
}
