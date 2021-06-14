package com.transmedika.transmedikakitui.contract;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.SignInParam;

public interface SignInContract {
    interface View extends BaseView {
        void signIn(BaseResponse<SignIn> response);
    }

    interface Presenter extends BasePresenter<View> {
        void signIn(String deviceId, SignInParam param, Context context);
    }
}
