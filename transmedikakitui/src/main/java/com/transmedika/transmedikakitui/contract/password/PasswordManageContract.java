package com.transmedika.transmedikakitui.contract.password;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;


public interface PasswordManageContract {
    interface View extends BaseView {
        void onSetPassword(Boolean statusResult);
        void onUbahPassword(String hash, String cookie);
        void onConfirm(Boolean statusResult);
    }

    interface Presenter extends BasePresenter<View> {
        void setPassword(Context context, String password);
        void changePassword(Context context, String oldPassword, String newPassword);
        void confirmPassword(Context context, String password);
        void deleteAllTable();
        SignIn selectLogin();
    }
}
