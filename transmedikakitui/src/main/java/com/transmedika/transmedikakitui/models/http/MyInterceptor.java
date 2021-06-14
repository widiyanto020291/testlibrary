package com.transmedika.transmedikakitui.models.http;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.crashlytics.CustomKeysAndValues;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.transmedika.transmedikakitui.BuildConfig;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.models.bean.json.Password;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.DateUtil;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Calendar;
import java.util.Objects;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import retrofit2.HttpException;

public class MyInterceptor implements Interceptor {

    private static final String TAG = MyInterceptor.class.getSimpleName();

    private long maxContentLength = 250000L;
    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private final Context context;

    public MyInterceptor(Context context) {
        this.context = context;
    }

    public void setMaxContentLength(long maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (TransmedikaUtils.isNetworkConnected(context)) {
            int maxAge = 0;
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Pragma")
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28;
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
        }

        // Error send to FirebaseCrashlytics
        if (!BuildConfig.DEBUG) {
            if (response.code() != 200 && response.code() != 413 && response.code() != 412) { //413 dan 412 itu response validasi dari login
                try {
                    throw new HttpException(retrofit2.Response.error(response.body() != null ? response.body() : ResponseBody.create(MediaType.get("text"), "No Data"), response));
                } catch (HttpException e) {
                    String sb = "[" + DateUtil.calToString(Calendar.getInstance(), Constants.DATE_TIME_ZONE_1) + "]" +
                            " -CODE: " + response.code() +
                            " -METHOD: " + response.request().method() +
                            " -END_POINT: " + getEndPoint(response.request().url().toString());
                    Log.e(TAG, "Error response: " + sb);
                    FirebaseCrashlytics.getInstance().log(sb);
                    FirebaseCrashlytics.getInstance().setCustomKeys(responsBodyCreate(response, request));
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
            }
        }
        
        return response;
    }

    public boolean isJSONValid(String a) {
        try {
            new JSONObject(a);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private CustomKeysAndValues responsBodyCreate(Response response, Request request) {
        ResponseBody responseBody = response.body();
        RequestBody requestBody = request.body();

        CustomKeysAndValues.Builder builder = new CustomKeysAndValues.Builder()
                .putString("METHOD", response.request().method())
                .putString("URL", getEndPoint(response.request().url().toString()))
                .putLong("time", System.currentTimeMillis())
                .putInt("code", response.code())
                .putString("message", response.message());

        /* Trace Request jika ada error */
        boolean hasRequestBody = requestBody != null;

        Headers headers = request.headers();
        String token;
        if(headers.size() > 0) {
            token = headers.get("Authorization");
            if(token!=null) {
                builder.putString("requestHeader", request.headers().toString().replace(token, "Bearer *******"));
            }else {
                builder.putString("requestHeader", request.headers().toString());
            }
        }
        if (hasRequestBody) {
            if (requestBody.contentType() != null) {
                builder.putString("requestContentType", Objects.requireNonNull(requestBody.contentType()).toString());
            }
            try {
                if (requestBody.contentLength() != -1) {
                    builder.putLong("requestContentLength", requestBody.contentLength());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (hasRequestBody && !bodyHasUnsupportedEncoding(request.headers())) {
            BufferedSource source = getNativeSource(new Buffer(), bodyGzipped(request.headers()));
            Buffer buffer = source.getBuffer();
            try {
                requestBody.writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (isPlaintext(buffer)) {
                String reqBodyString = readFromBuffer(buffer, charset);
                if(reqBodyString!=null){
                    if(isJSONValid(reqBodyString)) {
                        Password passwordM = TransmedikaUtils.gsonBuilder().fromJson(reqBodyString, Password.class);
                        if(passwordM!=null) {
                            String password = passwordM.getPassword();
                            String oldPassword = passwordM.getOldPassword();
                            String newPin = passwordM.getNewPin();
                            String pin = passwordM.getPin();
                            if (reqBodyString.contains("password"))
                                if(password!=null)
                                    reqBodyString = reqBodyString.replace(password, "*****");
                            if(reqBodyString.contains("old_password"))
                                if(oldPassword!=null)
                                    reqBodyString = reqBodyString.replace(oldPassword, "*****");
                            if(reqBodyString.contains("pin"))
                                if(pin!=null)
                                    reqBodyString = reqBodyString.replace(pin, "*****");
                            if(reqBodyString.contains("new_pin"))
                                if(newPin!=null)
                                    reqBodyString = reqBodyString.replace(newPin, "*****");
                        }
                    }
                    builder.putString("requestBody", reqBodyString);
                }

            }
        }

        /* Trace Response jika ada error */
        if (responseBody != null && HttpHeaders.hasBody(response) && !bodyHasUnsupportedEncoding(response.headers())) {
            try {
                BufferedSource source = getNativeSource(response);
                if (source == null) {
                    builder.putString("responseBody", "Unexpected content");
                    return builder.build();
                }
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.getBuffer();
                Charset charset = StandardCharsets.UTF_8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    try {
                        charset = contentType.charset(StandardCharsets.UTF_8);
                    } catch (UnsupportedCharsetException e) {
                        Log.w(TAG, "responsBodyCreate: ", e);
                        builder.putString("responseBody", "Unexpected content");
                        return builder.build();
                    }
                }
                if (isPlaintext(buffer)) {
                    builder.putString("responseBody", readFromBuffer(buffer.clone(), charset));
                }
                builder.putLong("responseContentLength", buffer.size());
            } catch (IOException e) {
                Log.w(TAG, "responsBodyCreate: ", e);
            }
        }
        return builder.build();
    }

    private String readFromBuffer(Buffer buffer, Charset charset) {
        long bufferSize = buffer.size();
        long maxBytes = Math.min(bufferSize, maxContentLength);
        String body = "";
        try {
            body = buffer.readString(maxBytes, charset);
        } catch (EOFException e) {
            body += "\n\n--- Unexpected end of content ---";/*context.getString(R.string.chuck_body_unexpected_eof);*/
        }
        if (bufferSize > maxContentLength) {
            body += "\n\n--- Content truncated ---";/*context.getString(R.string.chuck_body_content_truncated);*/
        }
        return body;
    }


    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private BufferedSource getNativeSource(BufferedSource input, boolean isGzipped) {
        if (isGzipped) {
            GzipSource source = new GzipSource(input);
            return Okio.buffer(source);
        } else {
            return input;
        }
    }

    private BufferedSource getNativeSource(Response response) throws IOException {
        if (bodyGzipped(response.headers())) {
            BufferedSource source = response.peekBody(maxContentLength).source();
            if (source.getBuffer().size() < maxContentLength) {
                GzipSource gzipSource = new GzipSource(source);
                return Okio.buffer(gzipSource);
            } else {
                Log.w(TAG, "gzip encoded response was too long");
            }
        }
        if (response.body() != null) {
            return response.body().source();
        }
        return null;
    }

    private boolean bodyGzipped(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return "gzip".equalsIgnoreCase(contentEncoding);
    }

    public String getEndPoint(String url) {
        Uri uri = Uri.parse(url);
        return uri.getPath() + ((uri.getQuery() != null) ? "?" + uri.getQuery() : "");
    }

    private boolean bodyHasUnsupportedEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null &&
                !contentEncoding.equalsIgnoreCase("identity") &&
                !contentEncoding.equalsIgnoreCase("gzip");
    }

}
