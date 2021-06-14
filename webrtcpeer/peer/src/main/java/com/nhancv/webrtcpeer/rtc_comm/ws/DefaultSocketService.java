//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.nhancv.webrtcpeer.rtc_comm.ws;

import android.app.Application;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class DefaultSocketService implements SocketService {
    private static final String TAG = DefaultSocketService.class.getSimpleName();
    private WebSocketClient client;
    private KeyStore keyStore;
    private LooperExecutor executor;
    private Application application;
    private SocketCallBack socketCallBack;

    public DefaultSocketService(Application application) {
        this.application = application;
        this.executor = new LooperExecutor();
        this.executor.requestStart();
    }

    public void connect(String host) {
        this.connect(host, true);
    }

    public void connect(String host, boolean force) {
        if (force) {
            this.close();
        } else if (this.isConnected()) {
            return;
        }

        URI uri;
        try {
            uri = new URI(host);
        } catch (URISyntaxException var8) {
            var8.printStackTrace();
            return;
        }

        this.client = new WebSocketClient(uri) {
            public void onOpen(ServerHandshake serverHandshake) {
                if (DefaultSocketService.this.socketCallBack != null) {
                    DefaultSocketService.this.socketCallBack.onOpen(serverHandshake);
                }

            }

            public void onMessage(String s) {
                if (DefaultSocketService.this.socketCallBack != null) {
                    DefaultSocketService.this.socketCallBack.onMessage(s);
                }

            }

            public void onClose(int i, String s, boolean b) {
                if (DefaultSocketService.this.socketCallBack != null) {
                    DefaultSocketService.this.socketCallBack.onClose(i, s, b);
                }

            }

            public void onError(Exception e) {
                if (DefaultSocketService.this.socketCallBack != null) {
                    DefaultSocketService.this.socketCallBack.onError(e);
                }

            }
        };

        try {
            String scheme = uri.getScheme();
            if (scheme.equals("https") || scheme.equals("wss")) {
                //this.setTrustedCertificate(this.application.getAssets().open("server.crt"));
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(this.keyStore);
                SSLContext sslContext = SSLContext.getInstance("TLS");
                //sslContext.init((KeyManager[])null, tmf.getTrustManagers(), (SecureRandom)null); //kuduna make ieu
                sslContext.init((KeyManager[])null, trustAllCerts, (SecureRandom)null);
                this.client.setSocketFactory(sslContext.getSocketFactory());
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        this.client.connect();
    }

    TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
    };

    public void connect(String host, SocketCallBack socketCallBack) {
        this.connect(host, socketCallBack, true);
    }

    public void connect(String host, SocketCallBack socketCallBack, boolean force) {
        this.setCallBack(socketCallBack);
        this.connect(host, force);
    }

    public void setCallBack(SocketCallBack socketCallBack) {
        this.socketCallBack = socketCallBack;
    }

    public void close() {
        if (this.isConnected()) {
            this.client.close();
        }

    }

    public boolean isConnected() {
        return this.client != null && this.client.getConnection().isOpen();
    }

    public void sendMessage(String message) {
        executor.execute(() -> {
            if (isConnected()) {
                try {
                    client.send(message);
                    //client.send(message.getBytes("UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void disconnect() {
        if(client.isOpen())
            client.close();
    }

    public void setTrustedCertificate(InputStream inputFile) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(inputFile);
            Certificate ca = cf.generateCertificate(caInput);
            String keyStoreType = KeyStore.getDefaultType();
            this.keyStore = KeyStore.getInstance(keyStoreType);
            this.keyStore.load((InputStream)null, (char[])null);
            this.keyStore.setCertificateEntry("ca", ca);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }
}
