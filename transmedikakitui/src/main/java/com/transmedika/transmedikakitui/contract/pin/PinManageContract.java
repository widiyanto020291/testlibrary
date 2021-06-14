package com.transmedika.transmedikakitui.contract.pin;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;

public interface PinManageContract {
    interface View extends BaseView {
        void onSetPin(Boolean statusResult);
        void onUbahPin(Boolean statusResult);
//        void onUbahConfirm(Boolean statusResult);
        void onConfirm(Boolean statusResult);
    }

    interface Presenter extends BasePresenter<View> {
        void setPin(Context context, String pin);
        void changePin(Context context, String oldPin, String newPin);
//        void changePinConfirm(Context context, String oldPin);
        void confirmPin(Context context, String pin);
        SignIn selectLogin();
    }
}
