package com.transmedika.transmedikakitui.modul.videocall.kurento.models.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.transmedika.transmedikakitui.modul.videocall.kurento.models.CandidateModel;
import com.transmedika.transmedikakitui.modul.videocall.kurento.models.IdModel;

import java.io.Serializable;



public class ServerResponse extends IdModel implements Serializable {
    @SerializedName("response")
    private String response;
    @SerializedName("sdpAnswer")
    private String sdpAnswer;
    @SerializedName("candidate")
    private CandidateModel candidate;
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private boolean success;
    @SerializedName("from")
    private RegistrationPayload from;
    @SerializedName("registrationPayload") RegistrationPayload registrationPayload;

    public IdResponse getIdRes() {
        return IdResponse.getIdRes(getId());
    }

    public TypeResponse getTypeRes() {
        return TypeResponse.getType(getResponse());
    }

    public String getResponse() {
        return response;
    }

    public String getSdpAnswer() {
        return sdpAnswer;
    }

    public CandidateModel getCandidate() {
        return candidate;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public RegistrationPayload getFrom() {
        return from;
    }

    public RegistrationPayload getRegistrationPayload() {
        return registrationPayload;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class RegistrationPayload{
        @SerializedName("name") private String name;
        @SerializedName("sessionId") private String sessionId;

        public RegistrationPayload() {
        }

        public String getName() {
            return name;
        }

        public String getSessionId() {
            return sessionId;
        }
    }
}
