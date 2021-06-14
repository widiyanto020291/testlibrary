package com.transmedika.transmedikakitui.contract.consultation;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.join.DokterJoin;
import com.transmedika.transmedikakitui.models.bean.json.BasePage;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.FilterFilter;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;

import java.util.List;



public interface DoctorContract {
    interface View extends BaseView {
        void dokterResp(BaseResponse<BasePage<List<Doctor>>> response);
        void dokterDynamicResp(BaseResponse<BasePage<List<Doctor>>> response);
        void doctorDynamicError();
        void dokterJoinResp(DokterJoin response);
        void getBroadcastEvents(BroadcastEvents.Event event);
    }

    interface Presenter extends BasePresenter<View> {
        void dokterJoin(String auth, String id, Long medicalFacilityId, Context context);
        void dokter(String auth, String id, Long medicalFacilityId, FilterFilter filterFilter, Context context);
        void dokterDynamic(String auth, String url, FilterFilter filterFilter, Context context);
        SignIn selectLogin();
    }
}
