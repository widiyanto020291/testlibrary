package com.transmedika.transmedikakitui.contract.password;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;


public interface ForgetPasswordEmailContract {
    interface View extends BaseView {
        void onSuccessSendEmail(Boolean statusResult);
    }

    interface Presenter extends BasePresenter<View> {
        void sendEmail(Context context, String email);
        SignIn selectLogin();
    }
}
