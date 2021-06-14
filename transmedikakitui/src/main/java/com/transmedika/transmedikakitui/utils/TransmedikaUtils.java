package com.transmedika.transmedikakitui.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.transmedika.transmedikakitui.models.db.EncodeRealm;
import com.transmedika.transmedikakitui.models.db.RealmHelper;
import com.transmedika.transmedikakitui.utils.gson.DateSerializeDeserialize;
import com.transmedika.transmedikakitui.worker.UpdateDeviceWorker;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TransmedikaUtils {
    private static final String TAG = TransmedikaUtils.class.getSimpleName();
    public static void updateDeviceIdJob(String newToken, Context context){
        Data.Builder dataP = new Data.Builder();
        dataP.putString(Constants.DATA, newToken);

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(UpdateDeviceWorker.class)
                .setInputData(dataP.build())
                .build();

        WorkManager.getInstance(context).enqueue(oneTimeWorkRequest);
    }

    public static void updateDeviceId(Context mContext){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }
                    String s = task.getResult().getToken();
                    updateDeviceIdJob(s, mContext);
                });
    }

    public static String cleanName(String nama, Context context){
        String cleanName = nama.replaceAll("[-!$%^&*()#@_+|~=`{}\\[\\]:\";'<>?,.]","").toUpperCase();
        return context.getExternalCacheDir() + Constants.NETKROM_DIR+ "/"+cleanName+"/";
        //return Constants.NETKROM_PATH + Constants.NETKROM_DIR+ "/"+cleanName+"/";
    }

    public static String getExt(String a){
        int found = a.lastIndexOf('.') + 1;
        return (found > 0 ? a.substring(found) : "");
    }

    public static String getFileName(File file){
        return file.getName();
    }

    public static MultipartBody.Part partImage(File file){
        return MultipartBody.Part.createFormData("image", TransmedikaUtils.getFileName(file),
                RequestBody.create(MediaType.parse("file/*"), file));
    }

    public static String createMediaFileName(String prefix, String suffix, String extFile) {
        String timeStamp = new SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.getDefault()
        ).format(new Date());
        return prefix + "_" + timeStamp + "_" + suffix + extFile;
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        return (lastIndexOf == -1) ? "" : name.substring(lastIndexOf);
    }

    public static File createTempFile(Context context, String fileName) {
        return new File(context.getCacheDir(), fileName);
    }

    public static void copyFile(Context context, File ori, File copy) {
        try {
            if (ori.exists()) {
                Bitmap compressed = compressFile(context, new File(ori.getPath()));
                //Convert bitmap to byte array
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                compressed.compress(Bitmap.CompressFormat.JPEG, 70 /*ignored for PNG*/, bos);

                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(copy);
                fos.write(bitmapdata);
                bos.flush();
                bos.close();
                fos.flush();
                fos.close();
                long fileSize = copy.length() / 1024;
                Log.d("COPY", "copyFile: " + fileSize);
                Log.v("COPY", "Copy file successful.");
            } else {
                Log.v("COPY", "Copy file failed. Source file missing.");
            }
        } catch (IOException e) {
            Log.w("COPY", "onActivityResult: ", e);
        }
    }

    public static Bitmap compressFile(Context context, File file) {
        try {
            String dest = context.getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES
            ).getAbsolutePath();
            return new Compressor(context)
                    .setMaxWidth(720)
                    .setMaxHeight(560)
                    .setQuality(70)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(dest)
                    .compressToBitmap(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeFile(file.getPath());
    }

    public static int setDrawable(Context context, String drawableName) {
        return context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
    }

    public static int setLayout(Context context, String layoutName) {
        return context.getResources().getIdentifier(layoutName, "layout", context.getPackageName());
    }

    public static int dip2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int getActionbarSize(Context context){
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static String getAssetsJSON(Context context, String filePath) {
        BufferedReader reader = null;
        StringBuilder dataString = new StringBuilder();
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(filePath), StandardCharsets.UTF_8));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                dataString.append(mLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dataString.toString();
    }

    public static TransmedikaSettings transmedikaSettings(Context context){
        return new Gson()
                .fromJson(getAssetsJSON(context,"transmedika/transmedika-settings.json"),
                        TransmedikaSettings.class);
    }

    public static Gson gsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Date.class, new DateSerializeDeserialize());

        return gsonBuilder
                //.setLenient()
                .create();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean isNetworkConnected(Context context) {
        boolean exist = false;
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network activeNetwork = mConnectivityManager.getActiveNetwork();
            if (activeNetwork != null) {
                NetworkCapabilities capabilities = mConnectivityManager.getNetworkCapabilities(activeNetwork);
                exist = capabilities!=null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            } else {
                exist = false;
            }
        } else {
            if (mConnectivityManager.getAllNetworks().length > 0) {
                boolean isConnected = false;
                for (Network network : mConnectivityManager.getAllNetworks()) {
                    NetworkCapabilities capabilities = mConnectivityManager.getNetworkCapabilities(network);
                    if (capabilities!=null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                        Log.d("NETWORK", "registerNetworkCheck: AVAILABLE");
                        isConnected = true;
                        break;
                    } else {
                        if (capabilities!=null)
                            Log.d("NETWORK", "registerNetworkCheck: " + capabilities.getTransportInfo());
                    }
                }
                exist = isConnected;
            } else {
                Log.d("NETWORK", "registerNetworkCheck: NOT AVAILABLE");
            }
        }

        return exist;
    }

    public static NumberFormat numberFormat() {
        Locale localeID = new Locale("in", "ID");
        return NumberFormat.getCurrencyInstance(localeID);
    }

    public static void toggleSoftKeyBoard(Activity activity, boolean hide) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            return;
        }
        if(inputManager!=null) {
            if (hide) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            } else {
                inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    public static void toggleSoftKeyBoard(Context activity, boolean hide, View view) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view == null) {
            return;
        }
        if(inputManager!=null) {
            if (hide) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            } else {
                inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    public static String getUmur(Date lahir, Date current) {
        org.joda.time.Period period = new org.joda.time.Period(lahir.getTime(), current.getTime());
        return period.getYears()+" Tahun";
        //return period.getYears()+" Tahun, "+period.getMonths()+" Bulan";
    }

    public static Integer getUmurNumber(Date lahir, Date current) {
        org.joda.time.Period period = new org.joda.time.Period(lahir.getTime(), current.getTime());
        return period.getYears();
    }


    public static Realm getRealm() {
        return Realm.getInstance(realmConfiguration());
    }

    private static RealmConfiguration realmConfiguration(){
        return new RealmConfiguration.Builder()
                .encryptionKey(EncodeRealm.encrypt(Constants.KEY64))
                .name(RealmHelper.DB_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    public static Typeface getFace(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
        return tf;
    }

    public static String getFilenameUrlExt(String a, Character pembatas){
        int found = a.lastIndexOf(pembatas) + 1;
        return (found > 0 ? a.substring(found) : a);
    }

    public static String cookie(Headers respHader){
        String[] headers = respHader.toString().split(System.lineSeparator());
        String cookie = null;
        for (String header : headers) {
            if(header!=null) {
                String[] spString = header.split(":");
                if(spString.length > 0) {
                    if (spString[0].equals("set-cookie") && spString[1].contains("laravel_session")) {
                        cookie = header.replace("set-cookie: ", "");
                        break;
                    }
                }
            }
        }
        return cookie;
    }

}
