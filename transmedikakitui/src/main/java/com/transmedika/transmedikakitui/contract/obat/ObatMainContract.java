package com.transmedika.transmedikakitui.contract.obat;

import android.content.Context;

import com.tbruyelle.rxpermissions3.RxPermissions;
import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.bean.json.Obat2;
import com.transmedika.transmedikakitui.models.bean.json.Order;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.PesanObatParam;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public interface ObatMainContract {
    interface View extends BaseView {
        void getMyLocation();
        void pesanResp(BaseResponse<Order> response);
        void pesanUploadResp(BaseResponse<Order> response);
        void callBackList(List<Obat2> obat2s);
    }

    interface Presenter extends BasePresenter<View> {
        void checkPermissionLocation(RxPermissions rxPermissions, Context context);
        void pesanObat(String auth, PesanObatParam param, Context context);
        void pesanObatUpload(String auth, MultipartBody.Part[] file, RequestBody data, Context context);
        List<Obat> selectObat();
        SignIn selectLogin();
        Obat selectObat(String slug);
        void insertObat(Obat obat);
        void setCatatanOrder(String a);
    }
}
