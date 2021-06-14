package com.transmedika.transmedikakitui.modul.videocall.kurento.models.response;

/**
 * Created by nhancao on 6/19/17.
 */

public enum IdResponse {

    REGISTER_RESPONSE("registerResponse"),
    PRESENTER_RESPONSE("presenterResponse"),
    ICE_CANDIDATE("iceCandidate"),
    VIEWER_RESPONSE("viewerResponse"),
    STOP_COMMUNICATION("stopCommunication"),
    CLOSE_ROOM_RESPONSE("closeRoomResponse"),
    INCOMING_CALL("incomingCall"),
    START_COMMUNICATION("startCommunication"),
    CALL_RESPONSE("callResponse"),
    CALLING_RESPONSE("callingResponse"),
    NOTIFY_CALLER_ACTIVE("notifyCallerActive"),
    NOTIFY_CALLER_MISSED("notifyCallerNotActive"),
    CANCEL_CALLING_RESPONSE("cancelCallingResponse"),
    REJECT_CALLING_RESPONSE("rejectCallingResponse"),
    BUSY_RESPONSE("busyResponse"),
    UN_KNOWN("unknown"),
    CALL_HANDLED_FROM_OTHER_DEVICE("incomingCallHandledFromOtherDevice");

    private String id;

    IdResponse(String id) {
        this.id = id;
    }

    public static IdResponse getIdRes(String idRes) {
        for (IdResponse idResponse : IdResponse.values()) {
            if (idRes.equals(idResponse.getId())) {
                return idResponse;
            }
        }
        return UN_KNOWN;
    }

    public String getId() {
        return id;
    }
}
