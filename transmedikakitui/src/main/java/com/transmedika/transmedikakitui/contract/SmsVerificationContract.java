package com.transmedika.transmedikakitui.contract;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.OtpParam;
import com.transmedika.transmedikakitui.models.bean.json.param.SmsVerificationParam;


public interface SmsVerificationContract {
    interface View extends BaseView {
        void smsVerificationResp(BaseResponse<SignIn> response);
        void kirimUlangOtpresp(BaseOResponse response);
    }

    interface Presenter extends BasePresenter<View> {
        void smsVerification(SmsVerificationParam param, Context context);
        void kirimUlangOtp(OtpParam param, Context context);
    }
}
