package com.transmedika.transmedikakitui.models.http.api;

import android.content.Context;

import com.transmedika.transmedikakitui.models.http.ConfigRest;
import com.transmedika.transmedikakitui.models.http.NetworkHelper;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;


public class OwnApisFactory {
    public static String HOST;
    public static TransmedikaApi ownApisFactory(Context context){
        TransmedikaSettings transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);
        HOST = transmedikaSettings.getBaseUrl();
        return ConfigRest.createRetrofit(NetworkHelper.provideClient(context),HOST).create(TransmedikaApi.class);
    }
}
