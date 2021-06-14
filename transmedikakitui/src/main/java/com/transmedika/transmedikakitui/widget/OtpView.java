package com.transmedika.transmedikakitui.widget;

import android.app.Service;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import org.jetbrains.annotations.NotNull;


public class OtpView extends ConstraintLayout {
    View view;

    ConstraintLayout mClOtp1;
    ConstraintLayout mClOtp2;
    ConstraintLayout mClOtp3;
    ConstraintLayout mClOtp4;
    ConstraintLayout mClOtp5;
    ConstraintLayout mClOtp6;

    NetkromTextView mTvOtp1;
    NetkromTextView mTvOtp2;
    NetkromTextView mTvOtp3;
    NetkromTextView mTvOtp4;
    NetkromTextView mTvOtp5;
    NetkromTextView mTvOtp6;
    EditText mEtOtp;
    NetkromTextView mTvLabel;
    ImageView mIvVisibility;
    NetkromTextView mTvError;

    Boolean show = false;
    Boolean showHideIcon = true;
    String title;
    @ColorInt int color = Color.BLACK;
    @ColorInt int colorLine = Color.parseColor("#C4C4C4");
    @ColorInt int colorFocus = Color.BLUE;
    final @ColorInt int colorError = Color.RED;

    ConstraintLayout[] pinContainerViews;
    NetkromTextView[] pinViews;
    final StringBuilder sb = new StringBuilder();

    private ChangeTextListener changeTextListener;

    public OtpView(@NonNull Context context) {
        super(context);
    }

    public OtpView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public OtpView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public OtpView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    public void setChangeTextListener(ChangeTextListener changeTextListener) {
        this.changeTextListener = changeTextListener;
    }

    public String getTextOtp() {
        return String.valueOf(mEtOtp.getText());
    }

    public Boolean isFull() {
        return mEtOtp.length() == pinViews.length;
    }

    public void clearPin() {
        for (NetkromTextView tv : pinViews) {
            tv.setText("");
            tv.setVisibility(View.GONE);
        }
        unFocusedAll();
        hideSoftKeyboard(mEtOtp);
    }

    private void initView(Context context, AttributeSet attrs) {
        TransmedikaSettings transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);
        view = inflate(context, R.layout.item_otp, this);
        mClOtp1 = view.findViewById(R.id.cl_otp_1);
        mClOtp2 = view.findViewById(R.id.cl_otp_2);
        mClOtp3 = view.findViewById(R.id.cl_otp_3);
        mClOtp4 = view.findViewById(R.id.cl_otp_4);
        mClOtp5 = view.findViewById(R.id.cl_otp_5);
        mClOtp6 = view.findViewById(R.id.cl_otp_6);
        mTvOtp1 = view.findViewById(R.id.tv_char_1);
        mTvOtp2 = view.findViewById(R.id.tv_char_2);
        mTvOtp3 = view.findViewById(R.id.tv_char_3);
        mTvOtp4 = view.findViewById(R.id.tv_char_4);
        mTvOtp5 = view.findViewById(R.id.tv_char_5);
        mTvOtp6 = view.findViewById(R.id.tv_char_6);
        mTvLabel = view.findViewById(R.id.tv_label);
        mIvVisibility = view.findViewById(R.id.iv_visible);
        mEtOtp = view.findViewById(R.id.et_otp_in);
        mTvError = view.findViewById(R.id.tv_error);

        pinContainerViews = new ConstraintLayout[]{mClOtp1, mClOtp2, mClOtp3, mClOtp4, mClOtp5, mClOtp6};
        pinViews = new NetkromTextView[]{mTvOtp1, mTvOtp2, mTvOtp3, mTvOtp4, mTvOtp5, mTvOtp6};

        if(transmedikaSettings.getFontBold()!=null){
            mTvLabel.setCustomFont(context, transmedikaSettings.getFontBold());
        }

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PinView);
            show = a.getBoolean(R.styleable.PinView_show, false);
            title = a.getString(R.styleable.PinView_title);
            showHideIcon = a.getInt(R.styleable.PinView_showHideIconVisibility, 0) == 1;
            String errorMessage = a.getString(R.styleable.PinView_error);
            setColorLine(a.getColor(R.styleable.PinView_colorLine, ContextCompat.getColor(getContext(), R.color.black)));
            setColorFocus(a.getColor(R.styleable.PinView_colorFocus, ContextCompat.getColor(getContext(), R.color.colorPrimary)));
            setColorPin(a.getColor(R.styleable.PinView_colorPin, Color.BLACK));

            titleIconState();
            hideShowPin();
            setError(errorMessage);
            a.recycle();
        }


        view.setOnClickListener(view -> {
            if (!mEtOtp.hasFocus()) {
                mEtOtp.setFocusable(true);
                mEtOtp.setFocusableInTouchMode(true);
                mEtOtp.requestFocus();
            } else {
                showSoftKeyboard(mEtOtp);
            }
        });

        mIvVisibility.setOnClickListener(view -> setShow(!show));

        mEtOtp.setOnFocusChangeListener((view, b) -> {
            if (view.hasFocus()) {
                checkLastest();
                showSoftKeyboard(((EditText) view));
            } else {
                unFocusedAll();
//                hideSoftKeyboard(((EditText) view));
            }
        });

        mEtOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                int beforeLength = sb.length();
                int afterLength = cs.length();
                if (beforeLength < afterLength) {
                    pinViews[beforeLength].setVisibility(View.VISIBLE);
                    pinViews[beforeLength].setText(cs.subSequence(beforeLength, afterLength));
                    sb.append(cs.subSequence(beforeLength, afterLength));
                    nextFocus();
                } else if (beforeLength > afterLength) {
                    int sizeDiff = beforeLength - afterLength;
                    if (sizeDiff > 1) {
                        for (int j = 0; j < sizeDiff; j++) {
                            int size = sb.length();
                            if (size > 0) {
                                pinViews[size - 1].setText("");
                                pinViews[size - 1].setVisibility(View.GONE);
                                prevFocus2();
                                sb.deleteCharAt(afterLength);
                            }
                        }
                    } else {
                        pinViews[afterLength].setText("");
                        pinViews[afterLength].setVisibility(View.GONE);
                        prevFocus2();
                        sb.deleteCharAt(afterLength);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (changeTextListener != null)
                    changeTextListener.onChange(OtpView.this, editable, editable.length() == pinViews.length);
            }
        });

//        mEtOtp.setOnKeyListener((view, keyCode, event) -> {
//            if (event != null) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_DEL) {
//                        int length = mEtOtp.length();
//                        if (length > 0 && sb.length() > 0) {
//                            pinViews[length - 1].setText("");
//                            pinViews[length - 1].setVisibility(View.GONE);
//                            sb.deleteCharAt(length - 1);
//                            prevFocus();
//                        }
//                    }
//                }
//            }
//            return false;
//        });
    }

    public void setColorPin(@ColorInt int color) {
        this.color = color;
        setColorPinAll(color);
    }

    private void setColorPinAll(@ColorInt int color) {
        for (NetkromTextView tv : pinViews) {
            tv.setTextColor(color);
        }
        hideShowPin();
    }

    public void setColorLine(@ColorInt int colorLine) {
        this.colorLine = colorLine;
        setAllColorLine(colorLine);
    }

    private void setAllColorLine(@ColorInt int colorLine) {
        for (ConstraintLayout pinContainerView : pinContainerViews) {
            viewLineBackgroundColor(pinContainerView, colorLine);
        }
    }

    public void setColorFocus(@ColorInt int colorFocus) {
        this.colorFocus = colorFocus;
    }

    private void checkLastest() {
        unFocusedAll();
        int size = mEtOtp.getText().toString().length();
        if (size == 0) {
            viewFocused(pinContainerViews[0]);
        } else if (size == pinViews.length) {
            viewFocused(pinContainerViews[5]);
        } else {
            viewFocused(pinContainerViews[size]);
        }
    }

    private void unFocusedAll() {
        for (ConstraintLayout pinContainerView : pinContainerViews) {
            viewUnFocused(pinContainerView);
        }
    }

    private void nextFocus() {
        int size = mEtOtp.getText().length();
        if (size == pinViews.length) {
            viewUnFocused(pinContainerViews[pinViews.length - 1]);
            hideSoftKeyboard(mEtOtp);
            mEtOtp.setFocusable(false);
            mEtOtp.setFocusableInTouchMode(false);
        } else {
            viewUnFocused(pinContainerViews[size - 1]);
            viewFocused(pinContainerViews[size]);
        }
    }

    private void prevFocus() {
        int size = mEtOtp.getText().length();
        if (size > 0 && size < pinViews.length) {
            viewUnFocused(pinContainerViews[size]);
            viewFocused(pinContainerViews[size - 1]);
        }
    }

    private void prevFocus2() {
        int size = sb.length();
        if (size > 0 && size < pinViews.length) {
            viewUnFocused(pinContainerViews[size]);
            viewFocused(pinContainerViews[size - 1]);
        }
    }

    private void viewFocused(@NotNull ConstraintLayout view) {
        view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ed_normal_selected));
        //viewLineBackgroundColor(view, colorFocus);
    }

    private void viewUnFocused(@NotNull ConstraintLayout view) {
        view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ed_normal));
        //viewLineBackgroundColor(view, colorLine);
    }

    private void viewLineBackgroundColor(@NotNull ConstraintLayout view, @ColorInt int color) {
        View viewLine = view.findViewWithTag("line");
        viewLine.setBackgroundColor(color);
    }

    public void setShow(Boolean show) {
        this.show = show;
        hideShowPin();
    }

    public void setTitle(String title) {
        this.title = title;
        titleIconState();
    }

    public void setShowHideIcon(Boolean showHideIcon) {
        this.showHideIcon = showHideIcon;
        titleIconState();
    }

    public void setError(String error) {
        if (error != null && !error.isEmpty()) {
            if (mTvError.getVisibility() != View.VISIBLE) {
                mTvError.setText(error);
                mTvError.setVisibility(View.VISIBLE);
                setColorPinAll(colorError);
                setAllColorLine(colorError);
            }
        } else {
            if (mTvError.getVisibility() != View.GONE) {
                mTvError.setVisibility(View.GONE);
                setColorPinAll(color);
                setAllColorLine(colorLine);
            }
        }
    }

    private void titleIconState() {
        if (title != null && !title.isEmpty()) {
            mTvLabel.setText(title);
            if (mTvLabel.getVisibility() == View.GONE || mTvLabel.getVisibility() == View.INVISIBLE) {
                mTvLabel.setVisibility(View.VISIBLE);
            }
            if (!showHideIcon) {
                mIvVisibility.setVisibility(View.INVISIBLE);
            } else {
                mIvVisibility.setVisibility(View.VISIBLE);
            }
        } else {
            mTvLabel.setVisibility(View.GONE);
            if (!showHideIcon) {
                mIvVisibility.setVisibility(View.GONE);
            } else {
                mIvVisibility.setVisibility(View.VISIBLE);
            }
        }
    }

    private void hideShowPin() {
        mIvVisibility.setImageDrawable(ContextCompat.getDrawable(getContext(), show ? R.drawable.ic_visibility_24dp : R.drawable.ic_visibility_off_24dp));
        setPinBackground(show ? ContextCompat.getColorStateList(getContext(), android.R.color.transparent) : mTvError.getVisibility() == View.VISIBLE ? ColorStateList.valueOf(colorError): ColorStateList.valueOf(color));
    }

    private void setPinBackground(ColorStateList colorStateList) {
        mTvOtp1.setBackgroundTintList(colorStateList);
        mTvOtp2.setBackgroundTintList(colorStateList);
        mTvOtp3.setBackgroundTintList(colorStateList);
        mTvOtp4.setBackgroundTintList(colorStateList);
        mTvOtp5.setBackgroundTintList(colorStateList);
        mTvOtp6.setBackgroundTintList(colorStateList);
    }

    private void showSoftKeyboard(EditText editText) {
        if (editText == null) return;
        InputMethodManager imm = ((InputMethodManager) getContext().getSystemService(Service.INPUT_METHOD_SERVICE));
        imm.showSoftInput(editText, 0);
    }

    private void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;
        InputMethodManager imm = ((InputMethodManager) getContext().getSystemService(Service.INPUT_METHOD_SERVICE));
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public interface ChangeTextListener {
        void onChange(View view, CharSequence charSequence, Boolean isEnd);
    }
}
