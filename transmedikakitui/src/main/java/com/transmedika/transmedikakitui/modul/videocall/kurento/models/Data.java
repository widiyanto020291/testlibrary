
package com.transmedika.transmedikakitui.modul.videocall.kurento.models;

import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("body")
    private String body;
    @SerializedName("title")
    private String title;
    @SerializedName("app")
    private String app;
    @SerializedName("status")
    private String status;
    @SerializedName("me")
    private String me;
    @SerializedName("friend")
    private String friend;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMe() {
        return me;
    }

    public void setMe(String me) {
        this.me = me;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }
}
