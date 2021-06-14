package com.transmedika.transmedikakitui.contract.obat;

import android.content.Context;

import com.tbruyelle.rxpermissions3.RxPermissions;
import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.CariObat;
import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.CariObatParam;

import java.util.List;


public interface HasilCariObatContract {
    interface View extends BaseView {
        void cariObatResp(BaseResponse<CariObat> response);
        void jump();
    }

    interface Presenter extends BasePresenter<View> {
        void cariObat(String auth, CariObatParam param, Context context);
        void insertObat(Obat obat);
        void deleteObat(String slug);
        List<Obat> selectObat();
        SignIn selectLogin();
        Obat selectObat(String slug);
        String getCatatanOrder();
        void setCatatanOrder(String b);
        void checkPermission(RxPermissions rxPermissions, Context context);
    }
}
