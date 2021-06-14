package com.nhancv.webrtcpeer.rtc_comm.ws;


import org.java_websocket.handshake.ServerHandshake;

/**
 * Created by nhancao on 6/20/17.
 */

public class BaseSocketCallback implements SocketCallBack {

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("onOpen");
    }

    @Override
    public void onMessage(String serverResponse) {
        System.out.println("onMessage: " + serverResponse);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("onClose: " + s);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
