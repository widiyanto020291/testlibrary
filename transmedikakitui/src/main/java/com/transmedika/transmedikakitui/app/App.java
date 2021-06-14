package com.transmedika.transmedikakitui.app;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.work.Configuration;
import androidx.work.WorkManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.parse.Parse;
import com.parse.ParseObject;
import com.transmedika.transmedikakitui.models.bean.parse.KonsultasiParse;
import com.transmedika.transmedikakitui.models.bean.parse.Message;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import io.realm.Realm;

public class App extends Application {
    private static App instance;
    private TransmedikaSettings transmedikaSettings;
    public static synchronized App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        transmedikaSettings = TransmedikaUtils.transmedikaSettings(instance);
        setUpFabric();
        /*setUpLeakcanary();
        setUpBlockLeaknary();*/
        setUpRealm();
        setUpSubClass();
        setUpParse();
        setUpWorker();
    }

    private void setUpWorker(){
        Configuration myConfig = new Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .build();

        WorkManager.initialize(this, myConfig);
    }

    private void setUpSubClass(){
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(KonsultasiParse.class);
    }

    protected void setUpParse(){
        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(transmedikaSettings.getParseAppId())
                .clientKey(transmedikaSettings.getParseClientId())
                .server(transmedikaSettings.getParseHost())
                .enableLocalDataStore()
                .build()
        );
    }

    protected void setUpRealm(){
        Realm.init(getApplicationContext());
    }

    protected void setUpFabric(){
        FirebaseApp.initializeApp(this);
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
    }

    /*protected void setUpLeakcanary(){
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    protected void setUpBlockLeaknary(){
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }*/

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private boolean mainActiviyActive;

    public static void setInstance(App instance) {
        App.instance = instance;
    }

    public boolean isMainActiviyActive() {
        return mainActiviyActive;
    }

    public void setMainActiviyActive(boolean mainActiviyActive) {
        this.mainActiviyActive = mainActiviyActive;
    }
}
