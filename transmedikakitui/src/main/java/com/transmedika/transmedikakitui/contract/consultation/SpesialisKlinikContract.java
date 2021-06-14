package com.transmedika.transmedikakitui.contract.consultation;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;

import java.util.List;



public interface SpesialisKlinikContract {
    interface View extends BaseView {
        void spesialisResp(BaseResponse<List<Specialist>> response);
        void getBroadcastEvents(BroadcastEvents.Event event);
    }

    interface Presenter extends BasePresenter<View> {
        void spesialis(String auth, Long id, Context context);
        SignIn selectLogin();
    }
}
