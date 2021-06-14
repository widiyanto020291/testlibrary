package com.transmedika.transmedikakitui.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;


public class SpanUtils {
    private final Context context;
    private SpanClickListener spanClickListener;
    private final TransmedikaSettings transmedikaSettings;


    public SpanUtils(Context context) {
        this.context = context;
        transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);
    }

    public SpanUtils(Context context, SpanClickListener spanClickListener){
        this(context);
        this.spanClickListener = spanClickListener;
    }

    public void setSpan(TextView view, String fulltext, int color, String partString) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(fulltext);
        int part = fulltext.indexOf(partString);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                if(spanClickListener!=null)
                    spanClickListener.onSpancClick();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                Typeface tf = Typeface.createFromAsset(context.getAssets(), transmedikaSettings.getFontBold());
                ds.setUnderlineText(true);
                ds.setTypeface(tf);
                ds.setColor(color);
            }
        }, part, part+partString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(spannableString);
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setSpanWithoutUnderline(TextView view, String fulltext, int color, String partString) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(fulltext);
        int part = fulltext.indexOf(partString);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                if(spanClickListener!=null)
                    spanClickListener.onSpancClick();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                Typeface tf = Typeface.createFromAsset(context.getAssets(), transmedikaSettings.getFontBold());
                ds.setUnderlineText(false);
                ds.setTypeface(tf);
                ds.setColor(color);
            }
        }, part, part+partString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(spannableString);
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setSpanMore(TextView view, String fulltext, int color, String partString) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(fulltext);
        int part = fulltext.indexOf(partString);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                if(spanClickListener!=null)
                    spanClickListener.onSpancClick();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                TextPaint paint = view.getPaint();
                float width = paint.measureText(partString);

                Shader textShader = new LinearGradient(0, 0, width, view.getTextSize(),
                        new int[]{
                                Color.parseColor("#EAF0F4"),
                                Color.parseColor("#3c4f5e"),
                        }, null, Shader.TileMode.MIRROR);
                ds.setShader(textShader);
            }
        }, part, part + partString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(spannableString);
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setSpanClickListener(SpanClickListener spanClickListener) {
        this.spanClickListener = spanClickListener;
    }

    public interface SpanClickListener{
        void onSpancClick();
    }
}
