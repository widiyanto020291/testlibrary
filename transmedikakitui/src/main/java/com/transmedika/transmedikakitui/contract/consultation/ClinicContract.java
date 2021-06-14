package com.transmedika.transmedikakitui.contract.consultation;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BasePage;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Clinic;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;

import java.util.List;



public interface ClinicContract {
    interface View extends BaseView {
        void getBroadcastEvents(BroadcastEvents.Event event);
        void klinikDynamicResp(BaseResponse<BasePage<List<Clinic>>> response);
        void klinikResp(BaseResponse<BasePage<List<Clinic>>> response);
        void klinikDynamicError();
        void spesialisResp(BaseResponse<List<Specialist>> response);

    }

    interface Presenter extends BasePresenter<View> {
        void spesialis(String auth, Long id, Context context);
        void klinik(String auth, String key, Context context);
        void klinikDynamic(String auth, String url, Context context);
        SignIn selectLogin();
    }
}
