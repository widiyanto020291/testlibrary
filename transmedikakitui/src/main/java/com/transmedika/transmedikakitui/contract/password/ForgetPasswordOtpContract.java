package com.transmedika.transmedikakitui.contract.password;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;


public interface ForgetPasswordOtpContract {
    interface View extends BaseView {
        void onSuccessSendOtp(Boolean statusResult);
        void onSuccessResetPasswordOtp(String hash, String cookie);
    }

    interface Presenter extends BasePresenter<View> {
        void requestOtp(Context context, String noPonsel, String password, String confirmPassword);
        void requestOtp(Context context, String noPonsel);
        void deleteAllTables();
        SignIn selectLogin();
    }
}
