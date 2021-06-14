package com.transmedika.transmedikakitui.contract;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.Alamat;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;

import java.util.List;


public interface AlamatContract {
    interface View extends BaseView {
        void alamatResp(BaseResponse<List<Alamat>> response);
        void hapusAlamatResp(BaseResponse<Alamat> response);
    }

    interface Presenter extends BasePresenter<View> {
        void alamat(String auth, Context context);
        void hapusAlamat(String auth, String id, Context context);
        SignIn selectLogin();
    }
}
