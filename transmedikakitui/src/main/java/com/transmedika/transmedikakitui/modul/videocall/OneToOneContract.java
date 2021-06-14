package com.transmedika.transmedikakitui.modul.videocall;


import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.modul.videocall.kurento.models.response.ServerResponse;

import org.webrtc.EglBase;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;



public interface OneToOneContract {
    interface View extends BaseView {
        void getBroadcastEvents(BroadcastEvents.Event event);
        void logAndToast(String msg);
        void disconnect();
        VideoCapturer createVideoCapturer();
        EglBase.Context getEglBaseContext();
        VideoRenderer.Callbacks getLocalProxyRenderer();
        VideoRenderer.Callbacks getRemoteProxyRenderer();
        void setSwappedFeeds(boolean swappedFeed);
        void socketConnect(boolean success);
        void registerStatus(boolean success, ServerResponse.RegistrationPayload registrationPayload);
        void transactionToCalling(boolean isHost);
        void incomingCalling(String fromPeer, ServerResponse.RegistrationPayload registrationPayload);
        void stopCalling();
        void startCallIng();
        void notifyMe(ServerResponse.RegistrationPayload registrationPayload);
        void missedCall(String s);
        void callingResponse(String s);
        void cancelCallingResponse();
        void rejectCallingResponse(String s);
        void dcMissedCall();
        void busy(String s);
        void rejectedFromCalle();
        void goToincommingCallNeverReceive();
    }
    interface Presenter extends BasePresenter<View> {
        SignIn selectLogin();
        void startTimeMissedCall();
        void startTimeIfIncommingCallNeverReceive();
    }
}
