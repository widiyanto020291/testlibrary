package com.transmedika.transmedikakitui.contract;

import android.content.Context;

import com.tbruyelle.rxpermissions3.RxPermissions;
import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.Alamat;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.AlamatParam;

public interface TambahAlamatContract {
    interface View extends BaseView {
        void getMyLocation();
        void tambahAlamatResp(BaseResponse<Alamat> response);
        void ubahAlamatResp(BaseResponse<Alamat> response);
    }

    interface Presenter extends BasePresenter<View> {
        void checkPermissionLocation(RxPermissions rxPermissions, Context context);
        void tambahAlamat(String auth, AlamatParam param, Context context);
        void ubahAlamat(String auth, String id,AlamatParam param, Context context);
        SignIn selectLogin();
    }
}
