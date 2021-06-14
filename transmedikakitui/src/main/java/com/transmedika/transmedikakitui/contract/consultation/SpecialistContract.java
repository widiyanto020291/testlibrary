package com.transmedika.transmedikakitui.contract.consultation;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;

import java.util.List;


public interface SpecialistContract {
    interface View extends BaseView {
        void specialistResp(BaseResponse<List<Specialist>> response);
        void getBroadcastEvents(BroadcastEvents.Event event);
    }

    interface Presenter extends BasePresenter<View> {
        void specialist(String auth, Context context);
        SignIn selectLogin();
    }
}
