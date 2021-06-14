package com.transmedika.transmedikakitui.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;


/**
 * Created by Widiyanto02 on 1/4/2018.
 */

public class CheckAvailableNetwork extends BroadcastReceiver {

    public static ICheckAvailableNetwork iCheckAvailableNetwork;

    public void setConnectivityListener(ICheckAvailableNetwork listener) {
        iCheckAvailableNetwork = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            boolean isConnected = isConnected(context);
            if (iCheckAvailableNetwork != null) {
                iCheckAvailableNetwork.onNetworkConnectionChanged(isConnected);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        activeNetwork.getState();
        return activeNetwork.getState() == NetworkInfo.State.CONNECTED;
    }

    public static class CheckNetworkNew {

        private final ConnectivityManager mConnectivityManager;
        private final ICheckAvailableNetwork iCheckAvailableNetwork;
        private ConnectivityManager.NetworkCallback networkCallback;

        public CheckNetworkNew(Context context, ICheckAvailableNetwork iCheckAvailableNetwork) {
            mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            this.iCheckAvailableNetwork = iCheckAvailableNetwork;
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void registerNetworkCheck() {
            if (networkCallback == null) {
                networkCallback = new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(@NonNull Network network) {
                        super.onAvailable(network);
                        iCheckAvailableNetwork.onNetworkConnectionChanged(true);
                    }

                    @Override
                    public void onLost(@NonNull Network network) {
                        super.onLost(network);
                        iCheckAvailableNetwork.onNetworkConnectionChanged(false);
                    }
                };
            } else {
                return;
            }

            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build();

            mConnectivityManager.isDefaultNetworkActive();
            mConnectivityManager.registerNetworkCallback(networkRequest, networkCallback);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network activeNetwork = mConnectivityManager.getActiveNetwork();
                if (activeNetwork != null) {
                    NetworkCapabilities capabilities = mConnectivityManager.getNetworkCapabilities(activeNetwork);
                    if (!capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                        iCheckAvailableNetwork.onNetworkConnectionChanged(false);
                    }
                } else {
                    iCheckAvailableNetwork.onNetworkConnectionChanged(false);
                }
            } else {
                if (mConnectivityManager.getAllNetworks().length > 0) {
                    boolean isConnected = false;
                    for (Network network : mConnectivityManager.getAllNetworks()) {
                        NetworkCapabilities capabilities = mConnectivityManager.getNetworkCapabilities(network);
                        if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                            Log.d("NETWORK", "registerNetworkCheck: AVAILABLE");
                            isConnected = true;
                            break;
                        } else {
                            Log.d("NETWORK", "registerNetworkCheck: " + capabilities.getTransportInfo());
                        }
                    }
                    if (!isConnected) {
                        iCheckAvailableNetwork.onNetworkConnectionChanged(false);
                    }
                } else {
                    Log.d("NETWORK", "registerNetworkCheck: NOT AVAILABLE");
                    iCheckAvailableNetwork.onNetworkConnectionChanged(false);
                }
            }
        }

        public void unregisterNetworkCheck() {
            if (networkCallback != null) {
                mConnectivityManager.unregisterNetworkCallback(networkCallback);
                networkCallback = null;
            }
        }
    }
}
