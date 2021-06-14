package com.nhancv.webrtcpeer.rtc_comm.ws;

import java.io.InputStream;

/**
 * Created by nhancao on 6/19/17.
 */

public interface SocketService {

    void connect(String var1);

    void connect(String var1, boolean var2);

    void connect(String var1, SocketCallBack var2);

    void connect(String var1, SocketCallBack var2, boolean var3);

    void setCallBack(SocketCallBack var1);

    void close();

    boolean isConnected();

    void sendMessage(String var1);

    void disconnect();

}
