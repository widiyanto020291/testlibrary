package com.transmedika.transmedikakitui.contract.consultation;

import android.content.Context;


import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.StatusKonsultasiParam;


public interface TungguDokterContract {
    interface View extends BaseView {
        void statusKonsultasiResp(BaseOResponse response);
    }

    interface Presenter extends BasePresenter<View> {
        void statusKonsultasi(String auth, Long idKonsultasi, StatusKonsultasiParam param, Context context);
        SignIn selectLogin();
    }
}
