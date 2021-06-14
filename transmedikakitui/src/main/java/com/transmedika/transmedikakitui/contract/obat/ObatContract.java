package com.transmedika.transmedikakitui.contract.obat;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BasePage;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;

import java.util.List;


public interface ObatContract {
    interface View extends BaseView {
        void getBroadcastEvents(BroadcastEvents.Event event);
        void obatResp(BaseResponse<BasePage<List<Obat>>> response);
        void obatDynamicResp(BaseResponse<BasePage<List<Obat>>> response);
        void cariObatResp(BaseResponse<BasePage<List<Obat>>> response);
        void articleDynamicError();

    }

    interface Presenter extends BasePresenter<View> {
        void cariObat(String auth, String key, Context context);
        void obat(String auth, String categoryId, Context context);
        void obatDynamic(String auth, String url, Context context);
        void insertObat(Obat obat);
        void deleteObat(String slug);
        SignIn selectLogin();
    }
}
