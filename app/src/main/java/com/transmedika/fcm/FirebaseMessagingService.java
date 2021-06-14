package com.transmedika.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.RemoteMessage;
import com.transmedika.examplesdk.R;
import com.transmedika.transmedikakitui.component.RxBus;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private NotificationManager notifManager;
    private String photo;
    private String source; // ?
    private RemoteMessage remoteMessage;
    private int NOTIFY_ID;
    private DataManager mDataManager;
    //private String idSlug;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({NotifType.VIDEO_CALL, NotifType.ORDER, NotifType.NOTIFICATIONS, NotifType.MESSAGES})
    public @interface NotifType {
        String VIDEO_CALL = "VIDEO_CALL";
        String ORDER = "ORDER";

        String NOTIFICATIONS = "NOTIFICATIONS";

        String MESSAGES = "MESSAGES";
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        mDataManager = DataManager.getDataManagerInstance(getApplicationContext());
        this.remoteMessage = remoteMessage;
        String id = "pasien";
        source = remoteMessage.getData().get("app");
        String contextText = remoteMessage.getData().get("body");
        String titleText = remoteMessage.getData().get("title");
        photo = remoteMessage.getData().get("image");
        //idSlug = remoteMessage.getData().get("id_slug");

        NOTIFY_ID = (int)System.currentTimeMillis();
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            assert notifManager != null;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, id, importance);
                mChannel.enableVibration(true);
                mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                        new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                                .build());
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);
            assert contextText != null;
            builder.setContentTitle(titleText)
                    .setSmallIcon(R.drawable.ic_tick)
                    .setContentText(HtmlCompat.fromHtml(contextText, HtmlCompat.FROM_HTML_MODE_LEGACY))
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .setColor(getApplicationContext().getColor(R.color.colorPrimary))
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
                    .setTicker(contextText);
        } else {
            builder = new NotificationCompat.Builder(this, id);
            builder.setContentTitle(titleText)
                    .setSmallIcon(R.drawable.ic_tick)
                    .setContentText(Html.fromHtml(contextText))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setTicker(contextText)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        notif(photo, builder);
    }

    private void notif(String largeImage, NotificationCompat.Builder builder){
        if (largeImage != null) {
            new Handler(Looper.getMainLooper()).post(() -> Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(photo)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap largeIcon, Transition<? super Bitmap> transition) {
                            notifikasiAllApp(largeIcon, builder);
                        }
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            notifikasiAllApp(null, builder);
                        }
                    }));
        } else {
            notifikasiAllApp(null, builder);
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        TransmedikaUtils.updateDeviceIdJob(s, getApplicationContext());
    }

    private void notifikasiAllApp(Bitmap largeIcon, NotificationCompat.Builder builder){
        new Handler(Looper.getMainLooper()).post(() -> {
            if (largeIcon != null) {
                NotificationCompat.Style style = new NotificationCompat.BigPictureStyle().bigPicture(largeIcon);
                builder.setStyle(style);
            }

            if (source.equalsIgnoreCase(NotifType.VIDEO_CALL)) {
                SignIn userF = TransmedikaUtils.gsonBuilder().fromJson(remoteMessage.getData().get("me"), SignIn.class);
                if (!mDataManager.sibuk()) {
                    mDataManager.setSibuk(true);
                    sendLocalBroadcast(userF, Constants.ICOMING_CALL);
                } else {
                    sendLocalBroadcast(userF, Constants.CURRENT_BUSY);
                }
            }else{
                    Notification notification = builder.build();
                    notifManager.notify(NOTIFY_ID, notification);
                }
        });
    }

    private void sendLocalBroadcast(SignIn userF, String s){
        BroadcastEvents.Event event = new BroadcastEvents.Event();
        event.setObject(userF);
        event.setInitString(s);
        RxBus.getDefault().post(new BroadcastEvents(event));
    }
}
