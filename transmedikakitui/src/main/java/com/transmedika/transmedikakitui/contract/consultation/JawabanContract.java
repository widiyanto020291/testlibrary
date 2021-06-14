package com.transmedika.transmedikakitui.contract.consultation;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Jawaban;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;


public interface JawabanContract {
    interface View extends BaseView {
        void jawabanResp(BaseResponse<Jawaban> response);

    }

    interface Presenter extends BasePresenter<View> {
        void jawaban(String auth, Long id, Context context);
        SignIn selectLogin();
    }
}
