package com.transmedika.transmedikakitui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;


/**
 * Created by Widiyanto02 on 6/7/2018.
 */

public class NetkromTextArea extends LinearLayout {

    private int ORIENTATION = HORIZONTAL;
    private int TEXT_LENGTH = 0;
    private int COLOR_DEFAULT = Color.BLACK;
    private int COLOR_HINT_DEFAULT = Color.GRAY;
    public NetkromEditText mNetkromEditText;
    private NetkromTextView mNetkromTextView;
    private int EDITTEXT_COLOR = COLOR_DEFAULT;
    private int EDITTEXT_HINT_COLOR = COLOR_HINT_DEFAULT;
    private int TEXTVIEW_SIZE = 0;
    private int EDITTEXT_SIZE = 0;
    private int GRAVITY = 3;
    private int INPUT_TYPE = 1;
    private int IME_ACTION = 5;
    private int TEXT_HEIGHT = -2;

    public NetkromTextArea(Context context) {
        this(context, null);
    }

    public NetkromTextArea(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetkromTextArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        int EDITTEXT_HEIGHT = TransmedikaUtils.dip2px(context, 30);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextArea);
        ORIENTATION = a.getInt(R.styleable.TextArea_orientation, ORIENTATION);
        TEXT_LENGTH = a.getInt(R.styleable.TextArea_max_length, TEXT_LENGTH);
        String textViewFont = a.getString(R.styleable.TextArea_textview_font);
        String editTextFont = a.getString(R.styleable.TextArea_edittext_font);
        EDITTEXT_COLOR = a.getColor(R.styleable.TextArea_edittext_text_color, EDITTEXT_COLOR);
        EDITTEXT_HINT_COLOR =  a.getColor(R.styleable.TextArea_edittext_hint_color, EDITTEXT_HINT_COLOR);
        TEXTVIEW_SIZE = a.getDimensionPixelSize(R.styleable.TextArea_textview_size, TEXTVIEW_SIZE);
        EDITTEXT_SIZE = a.getDimensionPixelSize(R.styleable.TextArea_edittext_size, EDITTEXT_SIZE);
        EDITTEXT_HEIGHT = a.getDimensionPixelSize(R.styleable.TextArea_edittext_height, EDITTEXT_HEIGHT);
        Drawable EDITTEXT_BACKGROUND = a.getDrawable(R.styleable.TextArea_edittext_background);
        String hintText = a.getString(R.styleable.TextArea_edittext_hint_text);
        GRAVITY = a.getInt(R.styleable.TextArea_gravity,GRAVITY);
        INPUT_TYPE = a.getInt(R.styleable.TextArea_inputType,INPUT_TYPE);
        IME_ACTION = a.getInt(R.styleable.TextArea_imeOption,IME_ACTION);
        TEXT_HEIGHT = a.getInt(R.styleable.TextArea_text_height, TEXT_HEIGHT);


        setOrientation(ORIENTATION);

        mNetkromEditText = new NetkromEditText(context);
        mNetkromTextView = new NetkromTextView(context);

        mNetkromTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,TEXTVIEW_SIZE);
        mNetkromTextView.setCustomFont(context, textViewFont);

        mNetkromEditText.setTextColor(EDITTEXT_COLOR);
        mNetkromEditText.setHintTextColor(EDITTEXT_HINT_COLOR);
        mNetkromEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,EDITTEXT_SIZE);
        mNetkromEditText.setCustomFont(context, editTextFont);
        mNetkromEditText.setBackground(EDITTEXT_BACKGROUND);
        mNetkromEditText.setHint(hintText);
        mNetkromEditText.setImeOptions(IME_ACTION);
        mNetkromEditText.setInputType(INPUT_TYPE);
        mNetkromEditText.setGravity(GRAVITY);


        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(TEXT_LENGTH);

        if(ORIENTATION == VERTICAL) {
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TransmedikaUtils.dip2px(context,TEXT_HEIGHT));
            mNetkromEditText.setLayoutParams(layoutParams);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,TransmedikaUtils.dip2px(context,5),0,0);
            mNetkromTextView.setLayoutParams(layoutParams);
            mNetkromTextView.setTextAlignment(TEXT_ALIGNMENT_VIEW_END);
        }else{
            LayoutParams layoutParamsEditText =
                    new LayoutParams(TransmedikaUtils.dip2px(context, 0), EDITTEXT_HEIGHT, 1);

            LayoutParams layoutParamsTextView =
                    new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            layoutParamsTextView.setMargins(0,0,TransmedikaUtils.dip2px(context,5),0);
            mNetkromEditText.setLayoutParams(layoutParamsEditText);
            mNetkromTextView.setLayoutParams(layoutParamsTextView);
        }

        mNetkromEditText.setHeight(EDITTEXT_HEIGHT);
        mNetkromTextView.setText(context.getString(R.string.char_note_count,0, TEXT_LENGTH));
        mNetkromEditText.setFilters(fArray);
        mNetkromEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNetkromTextView.setText(context.getString(R.string.char_note_count, mNetkromEditText.length(), TEXT_LENGTH));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addView(mNetkromEditText);
        addView(mNetkromTextView);
        a.recycle();
    }

    public String getGetTextEditText(){
        return mNetkromEditText.getText().toString();
    }

    public void setSetTextEditText(String s){
        mNetkromEditText.setText(s);
    }

    public void setErrorEdittexText(String s){
        mNetkromEditText.setError(s);
    }
}
