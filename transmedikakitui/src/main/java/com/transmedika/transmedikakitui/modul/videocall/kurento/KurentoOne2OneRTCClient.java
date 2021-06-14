package com.transmedika.transmedikakitui.modul.videocall.kurento;

import android.util.Log;

import com.nhancv.webrtcpeer.rtc_comm.ws.BaseSocketCallback;
import com.nhancv.webrtcpeer.rtc_comm.ws.SocketService;
import com.nhancv.webrtcpeer.rtc_peer.RTCClient;
import com.transmedika.transmedikakitui.modul.videocall.kurento.models.response.ServerResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;


/**
 * Created by nhancao on 7/18/17.
 */

public class KurentoOne2OneRTCClient implements RTCClient {
    private static final String TAG = KurentoOne2OneRTCClient.class.getSimpleName();

    private SocketService socketService;
    private boolean isHost;
    private String callerUUID;
    private String calleeUUID;
    private String consultationId;
    private ServerResponse.RegistrationPayload registrationPayloadFrom;
    private ServerResponse.RegistrationPayload registrationPayloadTo;

    public KurentoOne2OneRTCClient(SocketService socketService, boolean isHost) {
        this.socketService = socketService;
        this.isHost = isHost;
    }

    public KurentoOne2OneRTCClient(SocketService socketService, boolean isHost,
                                   String callerUUID, String calleeUUID, String consultationId, ServerResponse.RegistrationPayload registrationPayloadFrom,
                                   ServerResponse.RegistrationPayload registrationPayloadTo) {
        this.socketService = socketService;
        this.isHost = isHost;
        this.callerUUID = callerUUID;
        this.calleeUUID = calleeUUID;
        this.consultationId = consultationId;
        this.registrationPayloadFrom = registrationPayloadFrom;
        this.registrationPayloadTo = registrationPayloadTo;
    }

    public void connectToRoom(String host, BaseSocketCallback socketCallback) {
        socketService.connect(host, socketCallback);
    }

    @Override
    public void sendOfferSdp(SessionDescription sdp) {
        try {
            if (isHost) {

                JSONObject objRegistrationPayloadFrom = new JSONObject();
                objRegistrationPayloadFrom.put("name", registrationPayloadFrom.getName());
                objRegistrationPayloadFrom.put("sessionId", registrationPayloadFrom.getSessionId());

                JSONObject objRegistrationPayloadTo = new JSONObject();
                objRegistrationPayloadTo.put("name", registrationPayloadTo.getName());
                objRegistrationPayloadTo.put("sessionId", registrationPayloadTo.getSessionId());

                JSONObject obj = new JSONObject();
                obj.put("id", "call");
                obj.put("from", objRegistrationPayloadFrom);
                obj.put("to", objRegistrationPayloadTo);
                obj.put("sdpOffer", sdp.description);

                socketService.sendMessage(obj.toString());

            } else {
                JSONObject objRegistrationPayloadFrom = new JSONObject();
                objRegistrationPayloadFrom.put("name", registrationPayloadTo.getName());
                objRegistrationPayloadFrom.put("sessionId", registrationPayloadTo.getSessionId());

                JSONObject obj = new JSONObject();
                obj.put("id", "incomingCallResponse");
                obj.put("from", objRegistrationPayloadFrom);
                obj.put("callResponse", "accept");
                obj.put("sdpOffer", sdp.description);

                JSONObject objK = new JSONObject();
                objK.put("consultationId", this.consultationId);
                objK.put("callerUUID", this.callerUUID);
                objK.put("calleeUUID", this.calleeUUID);

                obj.put("consultationPayload", objK);

                socketService.sendMessage(obj.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendAnswerSdp(SessionDescription sdp) {

    }

    @Override
    public void sendLocalIceCandidate(IceCandidate iceCandidate) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", "onIceCandidate");
            JSONObject candidate = new JSONObject();
            candidate.put("candidate", iceCandidate.sdp);
            candidate.put("sdpMid", iceCandidate.sdpMid);
            candidate.put("sdpMLineIndex", iceCandidate.sdpMLineIndex);
            obj.put("candidate", candidate);

            socketService.sendMessage(obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendLocalIceCandidateRemovals(IceCandidate[] candidates) {
        Log.e(TAG, "sendLocalIceCandidateRemovals: ");
    }

}
