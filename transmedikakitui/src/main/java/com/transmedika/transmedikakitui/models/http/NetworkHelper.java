package com.transmedika.transmedikakitui.models.http;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.transmedika.transmedikakitui.BuildConfig;
import com.transmedika.transmedikakitui.utils.Constants;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public final class NetworkHelper {
    public static OkHttpClient provideClient(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        try {
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
                builder.addInterceptor(loggingInterceptor);
            }
            File cacheFile = new File(Constants.PATH_CACHE);
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
            Interceptor cacheInterceptor = new MyInterceptor(context);

            SSLContext sslContext = SSLContext.getInstance("SSL"); // Install the all-trusting trust manager
            sslContext.init(null, ConfigRest.trustAllCerts(), new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory(); // Create an ssl socket factory with our all-trusting manager


            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) ConfigRest.trustAllCerts()[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            builder.addNetworkInterceptor(cacheInterceptor);
            builder.addInterceptor(new ChuckInterceptor(context));
            builder.addInterceptor(cacheInterceptor);
            builder.cache(cache);
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(10, TimeUnit.SECONDS);
            builder.writeTimeout(25, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
