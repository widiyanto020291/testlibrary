package com.transmedika.transmedikakitui.contract.consultation;

import android.content.Context;

import com.tbruyelle.rxpermissions3.RxPermissions;
import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.FeedbackParam;
import com.transmedika.transmedikakitui.models.bean.json.param.StatusKonsultasiParam;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface ConversationContract {
    interface View extends BaseView {
        void getBroadcastEvents(BroadcastEvents.Event event);
        void start();
        void jump();
        void uploadResp(BaseResponse<String> response);
        void donwloadResp(String path);
        void statusKonsultasiResp(BaseOResponse response);
        void cekDeviceIdResp(BaseResponse<String> response);
        void cekDeviceIdMultipleResp(BaseResponse<List<String>> response);
        void onFeedbackResp(BaseOResponse response);
        void jumpReconnect();
    }

    interface Presenter extends BasePresenter<View> {
        void cekDeviceId(String auth, Context context);
        void cekDeviceIdMultiple(String auth, Context context);
        void statusKonsultasi(String auth, Long idKonsultasi, StatusKonsultasiParam param, Context context);
        void stopTyping();
        void upload(String auth, RequestBody name, MultipartBody.Part file, Context context);
        void download(String url, String namaPasien, Context context);
        void postFeedback(Context context, String auth, FeedbackParam param);
        SignIn selectLogin();
        void checkPermission(RxPermissions rxPermissions, Context context);
        void setCekKonsultasi(boolean b);
        void setCekKonsultasiKlinik(boolean b);
        void setBusyTelepon(boolean b);
        void reconnect();
    }
}
