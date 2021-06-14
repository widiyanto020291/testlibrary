package com.transmedika.transmedikakitui.contract;

import android.content.Context;


import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;


public interface DetailDoctorContract {
    interface View extends BaseView {
        void detailDokterResp(BaseResponse<Doctor> response);
    }

    interface Presenter extends BasePresenter<View> {
        void detailDokter(String auth, String uuidDokter, Context context);
        SignIn selectLogin();
    }
}
