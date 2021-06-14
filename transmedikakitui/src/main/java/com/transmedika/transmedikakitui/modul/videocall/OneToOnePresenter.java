package com.transmedika.transmedikakitui.modul.videocall;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nhancv.webrtcpeer.rtc_comm.ws.BaseSocketCallback;
import com.nhancv.webrtcpeer.rtc_comm.ws.DefaultSocketService;
import com.nhancv.webrtcpeer.rtc_comm.ws.SocketService;
import com.nhancv.webrtcpeer.rtc_peer.PeerConnectionClient;
import com.nhancv.webrtcpeer.rtc_peer.PeerConnectionParameters;
import com.nhancv.webrtcpeer.rtc_peer.SignalingEvents;
import com.nhancv.webrtcpeer.rtc_peer.SignalingParameters;
import com.nhancv.webrtcpeer.rtc_peer.config.DefaultConfig;
import com.nhancv.webrtcpeer.rtc_plugins.RTCAudioManager;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.component.RxBus;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.modul.videocall.kurento.KurentoOne2OneRTCClient;
import com.transmedika.transmedikakitui.modul.videocall.kurento.models.CallPush;
import com.transmedika.transmedikakitui.modul.videocall.kurento.models.CandidateModel;
import com.transmedika.transmedikakitui.modul.videocall.kurento.models.response.ServerResponse;
import com.transmedika.transmedikakitui.modul.videocall.kurento.models.response.TypeResponse;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.RxUtil;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;
import org.webrtc.StatsReport;
import org.webrtc.VideoCapturer;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Flowable;

public class OneToOnePresenter extends RxPresenter<OneToOneContract.View>
        implements OneToOneContract.Presenter, SignalingEvents, PeerConnectionClient.PeerConnectionEvents {
    public static final String STREAM_HOST = "wss://rtc.mybeam.me/one2one";
    private static final String TAG = OneToOnePresenter.class.getSimpleName();
    private final Application application;
    private final SocketService socketService;
    private final Gson gson;

    private PeerConnectionClient peerConnectionClient;
    private KurentoOne2OneRTCClient rtcClient;
    private PeerConnectionParameters peerConnectionParameters;
    private DefaultConfig defaultConfig;
    private RTCAudioManager audioManager;
    private SignalingParameters signalingParameters;
    private boolean iceConnected;
    private final DataManager dataManager;
    private boolean directCall;


    @Override
    public void attachView(OneToOneContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(BroadcastEvents.class)
                .compose(RxUtil.rxSchedulerHelper())
                .map(BroadcastEvents::getEvent)
                .subscribeWith(new CommonSubscriber<BroadcastEvents.Event>(mView, false, false) {
                    @Override
                    public void onNext(BroadcastEvents.Event event) {
                        mView.getBroadcastEvents(event);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        registerEvent();
                    }
                })
        );
    }

    public OneToOnePresenter(DataManager dataManager, Application application) {
        this.dataManager = dataManager;
        this.application = application;
        this.socketService = new DefaultSocketService(application);
        this.gson = new Gson();
    }

    public void initPeerConfig(boolean isHost, String callerUUID,
                               String calleeUUID, String consultationId, ServerResponse.RegistrationPayload registrationPayloadFrom, ServerResponse.RegistrationPayload registrationPayloadTo) {
        rtcClient = new KurentoOne2OneRTCClient(socketService, isHost,
                callerUUID, calleeUUID, consultationId, registrationPayloadFrom, registrationPayloadTo);
        defaultConfig = new DefaultConfig();
        peerConnectionParameters = defaultConfig.createPeerConnectionParams();
        peerConnectionClient = PeerConnectionClient.getInstance();
        peerConnectionClient.createPeerConnectionFactory(
                application.getApplicationContext(), peerConnectionParameters, this);
    }

    public void disconnect() {
        if (rtcClient != null) {
            rtcClient = null;
        }
        if (peerConnectionClient != null) {
            peerConnectionClient.close();
            peerConnectionClient = null;
        }

        if (audioManager != null) {
            audioManager.stop();
            audioManager = null;
        }

        socketService.close();
        mView.disconnect();
    }


    public void connectServer(boolean directCall_) {
        this.directCall = directCall_;
        socketService.connect(OneToOnePresenter.STREAM_HOST, new BaseSocketCallback() {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                super.onOpen(serverHandshake);
                RxUtil.runOnUi(o -> {
                    //mView.logAndToast("Connected");
                    mView.socketConnect(true);
                });
            }

            @Override
            public void onMessage(String serverResponse_) {
                super.onMessage(serverResponse_);
                try {
                    ServerResponse serverResponse = gson.fromJson(serverResponse_, ServerResponse.class);

                    switch (serverResponse.getIdRes()) {
                        case REGISTER_RESPONSE: // update disini
                            if (serverResponse.getTypeRes() == TypeResponse.REJECTED) {
                                mView.logAndToast(application.getApplicationContext().getString(R.string.gagal_membuat_sesi));
                            } else {
                                RxUtil.runOnUi(o -> mView.registerStatus(true, serverResponse.getRegistrationPayload()));
                            }
                            break;
                        case INCOMING_CALL:
                            RxUtil.runOnUi(o -> {
                                dataManager.setSibuk(true);
                                mView.incomingCalling(serverResponse.getFrom().toString(), serverResponse.getFrom());
                            });
                            break;
                        case CALL_RESPONSE:
                            if (serverResponse.getTypeRes() == TypeResponse.REJECTED) {
                                RxUtil.runOnUi(o -> {
                                    dataManager.setSibuk(true);
                                    mView.rejectedFromCalle();
                                });
                            } else {
                                SessionDescription sdp = new SessionDescription(SessionDescription.Type.ANSWER,
                                                                                serverResponse.getSdpAnswer());
                                onRemoteDescription(sdp);
                                RxUtil.runOnUi(o -> {
                                    dataManager.setSibuk(true);
                                    mView.startCallIng();
                                });
                            }

                            break;

                        case ICE_CANDIDATE:
                            CandidateModel candidateModel = serverResponse.getCandidate();
                            onRemoteIceCandidate(
                                    new IceCandidate(candidateModel.getSdpMid(), candidateModel.getSdpMLineIndex(),
                                                     candidateModel.getSdp()));
                            break;

                        case START_COMMUNICATION:
                            SessionDescription sdp = new SessionDescription(SessionDescription.Type.ANSWER,
                                                                            serverResponse.getSdpAnswer());
                            onRemoteDescription(sdp);
                            RxUtil.runOnUi(o -> {
                                dataManager.setSibuk(true);
                                mView.startCallIng();
                            });
                            break;
                        case NOTIFY_CALLER_ACTIVE:
                            RxUtil.runOnUi(o -> {
                                dataManager.setSibuk(true);
                                mView.notifyMe(serverResponse.getFrom());
                            });
                        case CALLING_RESPONSE: // hanya dapetin response ajh ketika server kirim notifikasi ke fcm.
                            RxUtil.runOnUi(o -> {
                                dataManager.setSibuk(true);
                                mView.callingResponse(serverResponse.getResponse());
                            });
                            break;
                        case CANCEL_CALLING_RESPONSE:
                            RxUtil.runOnUi(o -> {
                                dataManager.setSibuk(false);
                                mView.cancelCallingResponse();
                                stop();
                            });
                            break;
                        case REJECT_CALLING_RESPONSE:
                            RxUtil.runOnUi(o -> { //stop
                                dataManager.setSibuk(false);
                                mView.rejectCallingResponse(serverResponse.getResponse());
                                stop();
                            });
                            break;
                        case NOTIFY_CALLER_MISSED:
                            RxUtil.runOnUi(o -> { //stop
                                dataManager.setSibuk(false);
                                mView.missedCall(serverResponse.getResponse());
                                stop();
                            });
                            break;
                        case CALL_HANDLED_FROM_OTHER_DEVICE:
                            RxUtil.runOnUi(o -> { //stop
                                mView.socketConnect(false);
                                disconnect();
                                stop();
                            });
                            break;
                        case BUSY_RESPONSE:
                            RxUtil.runOnUi(o -> mView.busy(serverResponse.getResponse()));
                            break;
                        case STOP_COMMUNICATION:
                            RxUtil.runOnUi(o -> {
                                dataManager.setSibuk(false);
                                mView.stopCalling();
                            });
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                super.onClose(i, s, b);
                RxUtil.runOnUi(o -> {
                    dataManager.setSibuk(false);
                    mView.socketConnect(false);
                    disconnect();
                });
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                RxUtil.runOnUi(o -> {
                    dataManager.setSibuk(false);
                    mView.socketConnect(false);
                    disconnect();
                });
            }
        });

    }

    public void busy(String from, String to) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", "busy");
            obj.put("from", from);
            obj.put("to", to);

            socketService.sendMessage(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void register(String name) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", "register");
            obj.put("name", name);

            socketService.sendMessage(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void callingMobileDevice(String fromPeer, String toPeer, CallPush firebasePost){
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", "calling");
            obj.put("from", fromPeer);
            obj.put("to", toPeer);

            //update by widy
            JSONObject objData = new JSONObject();
            objData.put("body", firebasePost.getData().getBody());
            objData.put("title", firebasePost.getData().getTitle());
            objData.put("app", firebasePost.getData().getApp());
            objData.put("status", firebasePost.getData().getStatus());
            objData.put("me", firebasePost.getData().getMe());
            objData.put("friend", firebasePost.getData().getFriend());

            JSONObject objFcm = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for(String a:firebasePost.getTo()){
                jsonArray.put(a);
            }
            objFcm.put("to", jsonArray);
            objFcm.put("data", objData);
            obj.put("fcm", objFcm);

            String objStr = obj.toString();
            socketService.sendMessage(objStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void notifyCaller(ServerResponse.RegistrationPayload from, String to){
        try {

            JSONObject objRegistrationPayloadFrom = new JSONObject();
            objRegistrationPayloadFrom.put("name", from.getName());
            objRegistrationPayloadFrom.put("sessionId", from.getSessionId());

            JSONObject objRegistrationPayloadTo = new JSONObject();
            objRegistrationPayloadTo.put("name", to);
            objRegistrationPayloadTo.put("sessionId", null);

            JSONObject obj = new JSONObject();
            obj.put("id", "notifyCaller");
            obj.put("from", objRegistrationPayloadFrom);
            obj.put("to", objRegistrationPayloadTo);

            socketService.sendMessage(obj.toString());
            Log.i("Test", "Notify Caller");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", "stop");

            socketService.sendMessage(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void cancelCalling(ServerResponse.RegistrationPayload from, String to) {
        try {
            JSONObject objRegistrationPayloadFrom = new JSONObject();
            objRegistrationPayloadFrom.put("name", from.getName());
            objRegistrationPayloadFrom.put("sessionId", from.getSessionId());

            JSONObject objRegistrationPayloadTo = new JSONObject();
            objRegistrationPayloadTo.put("name", to);

            JSONObject obj = new JSONObject();
            obj.put("id", "cancelCalling");
            obj.put("from", objRegistrationPayloadFrom);
            obj.put("to", objRegistrationPayloadTo);
            socketService.sendMessage(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void rejectCalling(ServerResponse.RegistrationPayload from, ServerResponse.RegistrationPayload to) {
        try {
            JSONObject objRegistrationPayloadFrom = new JSONObject();
            objRegistrationPayloadFrom.put("name", from.getName());
            objRegistrationPayloadFrom.put("sessionId", from.getSessionId());

            JSONObject objRegistrationPayloadTo = new JSONObject();
            objRegistrationPayloadTo.put("name", to.getName());
            objRegistrationPayloadTo.put("sessionId", to.getSessionId());

            JSONObject obj = new JSONObject();
            obj.put("id", "rejectCalling");
            obj.put("from", objRegistrationPayloadFrom);
            obj.put("to", objRegistrationPayloadTo);
            socketService.sendMessage(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void startCall() {
        if (rtcClient == null) {
            Log.e(TAG, "AppRTC client is not allocated for a call.");
            return;
        }

        SignalingParameters parameters = new SignalingParameters(
                new LinkedList<PeerConnection.IceServer>() {
                    {
                        add(new PeerConnection.IceServer("turn:167.71.204.0:3478","netkrom","bull3tX"));
                        add(new PeerConnection.IceServer("turn:159.65.9.21:3478", "netkrom","bull3tX"));
                        add(new PeerConnection.IceServer("turn:159.89.202.60:3478", "netkrom","bull3tX"));
                        add(new PeerConnection.IceServer("turn:174.138.20.143:3478", "netkrom","bull3tX"));
                        add(new PeerConnection.IceServer("turn:139.59.99.228:3478", "netkrom","bull3tX"));
                        add(new PeerConnection.IceServer("turn:174.138.30.241:3478", "netkrom","bull3tX"));
                    }
                }, true, null, null, null, null, null);
        onSignalConnected(parameters);

        // Create and audio manager that will take care of audio routing,
        // audio modes, audio device enumeration etc.
        audioManager = RTCAudioManager.create(application.getApplicationContext());
        // Store existing audio settings and change audio mode to
        // MODE_IN_COMMUNICATION for best possible VoIP performance.
        Log.d(TAG, "Starting the audio manager...");
        audioManager.start((audioDevice, availableAudioDevices) ->
                                   Log.d(TAG, "onAudioManagerDevicesChanged: " + availableAudioDevices + ", "
                                              + "selected: " + audioDevice));
    }

    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    private void callConnected() {
        if (peerConnectionClient == null) {
            Log.w(TAG, "Call is connected in closed or error state");
            return;
        }
        // Enable statistics callback.
        peerConnectionClient.enableStatsEvents(true, 1000);
        mView.setSwappedFeeds(false);
    }

    @Override
    public void onSignalConnected(SignalingParameters params) {
        RxUtil.runOnUi(o -> {
            signalingParameters = params;
            VideoCapturer videoCapturer = null;
            if (peerConnectionParameters.videoCallEnabled) {
                videoCapturer = mView.createVideoCapturer();
            }
            peerConnectionClient
                    .createPeerConnection(mView.getEglBaseContext(), mView.getLocalProxyRenderer(),
                                          mView.getRemoteProxyRenderer(), videoCapturer,
                                          signalingParameters);

            if(directCall) {
                peerConnectionClient.createOffer();
            }
        });
    }

    public void createOffer(){
        RxUtil.runOnUi(o -> peerConnectionClient.createOffer());
    }

    @Override
    public void onRemoteDescription(SessionDescription sdp) {
        RxUtil.runOnUi(o -> {
            if (peerConnectionClient == null) {
                Log.e(TAG, "Received remote SDP for non-initilized peer connection.");
                return;
            }
            peerConnectionClient.setRemoteDescription(sdp);
            if (!signalingParameters.initiator) {
                mView.logAndToast("Creating ANSWER...");
                // Create answer. Answer SDP will be sent to offering client in
                // PeerConnectionEvents.onLocalDescription event.
                peerConnectionClient.createAnswer();
            }
        });
    }

    @Override
    public void onRemoteIceCandidate(IceCandidate candidate) {
        RxUtil.runOnUi(o -> {
            if (peerConnectionClient == null) {
                Log.e(TAG, "Received ICE candidate for a non-initialized peer connection.");
                return;
            }
            peerConnectionClient.addRemoteIceCandidate(candidate);
        });
    }

    @Override
    public void onRemoteIceCandidatesRemoved(IceCandidate[] candidates) {
        RxUtil.runOnUi(o -> {
            if (peerConnectionClient == null) {
                Log.e(TAG, "Received ICE candidate removals for a non-initialized peer connection.");
                return;
            }
            peerConnectionClient.removeRemoteIceCandidates(candidates);
        });
    }

    @Override
    public void onChannelClose() {
        RxUtil.runOnUi(o -> {
            mView.logAndToast("Remote end hung up; dropping PeerConnection");
            disconnect();
        });
    }

    @Override
    public void onChannelError(String description) {
        Log.e(TAG, "onChannelError: " + description);
    }

    @Override
    public void onLocalDescription(SessionDescription sdp) {
        RxUtil.runOnUi(o -> {
            if (rtcClient != null) {
                if (signalingParameters.initiator) {
                    rtcClient.sendOfferSdp(sdp);
                } else {
                    rtcClient.sendAnswerSdp(sdp);
                }
            }
            if (peerConnectionParameters.videoMaxBitrate > 0) {
                Log.d(TAG, "Set video maximum bitrate: " + peerConnectionParameters.videoMaxBitrate);
                peerConnectionClient.setVideoMaxBitrate(peerConnectionParameters.videoMaxBitrate);
            }
        });
    }

    @Override
    public void onIceCandidate(IceCandidate candidate) {
        RxUtil.runOnUi(o -> {
            if (rtcClient != null) {
                rtcClient.sendLocalIceCandidate(candidate);
            }
        });
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] candidates) {
        RxUtil.runOnUi(o -> {
            if (rtcClient != null) {
                rtcClient.sendLocalIceCandidateRemovals(candidates);
            }
        });
    }

    @Override
    public void onIceConnected() {
        RxUtil.runOnUi(o -> {
            iceConnected = true;
            callConnected();
        });
    }

    @Override
    public void onIceDisconnected() {
        RxUtil.runOnUi(o -> {
            mView.logAndToast("ICE disconnected");
            iceConnected = false;
            //disconnect();
        });
    }

    @Override
    public void onPeerConnectionClosed() {

    }

    @Override
    public void onPeerConnectionStatsReady(StatsReport[] reports) {
        RxUtil.runOnUi(o -> {
            if (iceConnected) {
                Log.e(TAG, "run: " + reports);
            }
        });
    }

    @Override
    public void onPeerConnectionError(String description) {
        Log.e(TAG, "onPeerConnectionError: " + description);
        RxUtil.runOnUi(o -> {
            mView.logAndToast(description);
            disconnect();
        });
    }

    @Override
    public SignIn selectLogin() {
        return dataManager.selectLogin();
    }

    @Override
    public void startTimeMissedCall() {
        addSubscribe(Flowable.timer(2000, TimeUnit.MILLISECONDS)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(aLong -> mView.dcMissedCall())
        );
    }

    @Override
    public void startTimeIfIncommingCallNeverReceive() {
        addSubscribe(Flowable.timer(5000, TimeUnit.MILLISECONDS)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(aLong -> mView.goToincommingCallNeverReceive())
        );
    }

    public void sw(){
        if(peerConnectionClient!=null)
            peerConnectionClient.switchCamera();
    }

    public void disableVideo(boolean a){
        if(peerConnectionClient!=null)
            peerConnectionClient.setVideoEnabled(a);
    }
}
