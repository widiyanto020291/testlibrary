package com.transmedika.transmedikakitui.contract;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.KategoriObat;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;

import java.util.List;


public interface KategoriObatContract {
    interface View extends BaseView {
        void kategoriObatResp(BaseResponse<List<KategoriObat>> response);
    }

    interface Presenter extends BasePresenter<View> {
        void kategoriObat(String auth, Context context);
        SignIn selectLogin();
    }
}
