package com.transmedika.transmedikakitui.contract;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.PertanyaanResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.FormParam;
import com.transmedika.transmedikakitui.models.bean.realm.TempJawaban;

import java.util.List;




public interface PertanyaanContract {
    interface View extends BaseView {
        void pertanyaanResp(BaseResponse<List<PertanyaanResponse>> response);

    }

    interface Presenter extends BasePresenter<View> {
        void pertanyaan(String auth, FormParam formParam, Context context);
        TempJawaban getTempJawaban(Long klinikId, String spesialisSlug);
        void insertTempJawaban(TempJawaban tempJawaban);
        SignIn selectLogin();
    }
}
