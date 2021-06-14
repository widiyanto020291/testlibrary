//package com.transmedika.transmedikakitui.modul.videocall;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.media.AudioManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.PowerManager;
//import android.os.VibrationEffect;
//import android.os.Vibrator;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.FrameLayout;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.content.res.AppCompatResources;
//import androidx.core.content.res.ResourcesCompat;
//
//import com.nhancv.webrtcpeer.rtc_plugins.ProxyRenderer;
//import com.transmedika.models.DataManager;
//import com.transmedika.models.bean.json.Profile;
//import com.transmedika.models.bean.json.SignIn;
//import com.transmedika.models.events.BroadcastEvents;
//import com.transmedika.transmedikacommon.Constants;
//import com.transmedika.transmedikakit.utils.ImageLoader;
//import com.transmedika.transmedikakitui.R;
//import com.transmedika.transmedikakitui.base.BaseBindingActivity;
//import com.transmedika.transmedikakitui.databinding.KurActivityAnswerBinding;
//import com.transmedika.transmedikakitui.modul.videocall.kurento.models.response.ServerResponse;
//import com.transmedika.transmedikakitui.widget.NetkromDialog;
//
//import org.jetbrains.annotations.NotNull;
//import org.webrtc.Camera1Enumerator;
//import org.webrtc.Camera2Enumerator;
//import org.webrtc.CameraEnumerator;
//import org.webrtc.EglBase;
//import org.webrtc.Logging;
//import org.webrtc.RendererCommon;
//import org.webrtc.VideoCapturer;
//import org.webrtc.VideoRenderer;
//
//import java.util.Calendar;
//import java.util.Objects;
//
//
//public class AnswerActivity extends BaseBindingActivity<KurActivityAnswerBinding, OneToOneContract.View,OneToOnePresenter>
//        implements OneToOneContract.View {
//
//    private static final String TAG = AnswerActivity.class.getSimpleName();
//    private int _xDelta;
//    private int _yDelta;
//
//    private EglBase rootEglBase;
//    private ProxyRenderer localProxyRenderer;
//    private ProxyRenderer remoteProxyRenderer;
//    private Toast logToast;
//    private boolean isSwappedFeeds;
//    private Profile userF;
//    private SignIn user;
//
//    //private MediaPlayer mediaPlayer;
//    private String currentStatus = "";
//    private Vibrator vibrator;
//    private boolean front = true;
//    private boolean video = true;
//    private String consultationId;
//    private ServerResponse.RegistrationPayload registrationPayloadFrom;
//    private ServerResponse.RegistrationPayload registrationPayloadTo;
//    private NetkromDialog netkromDialog;
//
//    protected void init() {
//
//        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
//
//        //config peer
//        localProxyRenderer = new ProxyRenderer();
//        remoteProxyRenderer = new ProxyRenderer();
//        rootEglBase = EglBase.create();
//
//        binding.vGLSurfaceViewCallFull.init(rootEglBase.getEglBaseContext(), null);
//        binding.vGLSurfaceViewCallFull.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
//        binding.vGLSurfaceViewCallFull.setEnableHardwareScaler(false);
//
//        binding.vGLSurfaceViewCallPip.init(rootEglBase.getEglBaseContext(), null);
//        binding.vGLSurfaceViewCallPip.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
//        binding.vGLSurfaceViewCallPip.setEnableHardwareScaler(true);
//        binding.vGLSurfaceViewCallPip.setZOrderMediaOverlay(true);
//
//        // Swap feeds on pip view click.
//        binding.vGLSurfaceViewCallPip.setOnClickListener(view -> setSwappedFeeds(!isSwappedFeeds));
//
//        setSwappedFeeds(true);
//        mPresenter.connectServer(false);
//    }
//
//    @Override
//    public void setSwappedFeeds(boolean isSwappedFeeds) {
//        Logging.d(TAG, "setSwappedFeeds: " + isSwappedFeeds);
//        this.isSwappedFeeds = isSwappedFeeds;
//        localProxyRenderer.setTarget(isSwappedFeeds ? binding.vGLSurfaceViewCallFull : binding.vGLSurfaceViewCallPip);
//        remoteProxyRenderer.setTarget(isSwappedFeeds ? binding.vGLSurfaceViewCallPip : binding.vGLSurfaceViewCallFull);
//        binding.vGLSurfaceViewCallFull.setMirror(isSwappedFeeds);
//        binding.vGLSurfaceViewCallPip.setMirror(!isSwappedFeeds);
//    }
//
//    @Override
//    public void socketConnect(boolean success) {
//        if (success) {
//            Log.i("Test", "Connected");
//            mPresenter.register(user.getEmail());
//            //mPresenter.register("dokterumum1@dokter.com");
//        }
//    }
//
//    @Override
//    public void disconnect() {
//        localProxyRenderer.setTarget(null);
//        binding.vGLSurfaceViewCallFull.release();
//        binding.vGLSurfaceViewCallPip.release();
//
//        finish();
//    }
//
//    @Override
//    protected KurActivityAnswerBinding getViewBinding(@NonNull LayoutInflater inflater) {
//        return KurActivityAnswerBinding.inflate(inflater);
//    }
//
//    @NonNull
//    @NotNull
//    @Override
//    protected OneToOneContract.View getBaseView() {
//        return this;
//    }
//
//    @Override
//    protected void onViewCreated(Bundle bundle) {
//        Window window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
//                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                "MyApp::MyWakelockTag");
//        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
//
//        mPresenter = new OneToOnePresenter(DataManager.getDataManagerInstance(mContext), getApplication());
//        super.onViewCreated(bundle);
//        user = mPresenter.selectLogin();
//    }
//
//    @Override
//    protected void initEventAndData(Bundle bundle) {
//        Bundle b = getIntent().getBundleExtra("DATA");
//        if (b != null) {
//            userF = b.getParcelable("USER");
//            consultationId = b.getString(Constants.ID_KONSULTASI);
//        }
//        setUpListener();
//        binding.flEndCall.show();
//        if (userF != null) {
//            ImageLoader.load(this, Objects.requireNonNull(userF).getProfilePicture(), binding.cvImg);
//            binding.tvName.setText(userF.getFullName());
//        }
//
//        /*FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, SystemUtil.dip2px(mContext, 144));
//        layoutParams.setMargins(SystemUtil.dip2px(mContext, 12), SystemUtil.dip2px(mContext, 12), 0, 0);*/
//        //binding.vGLSurfaceViewCallPip.setLayoutParams(layoutParams);
//        binding.vGLSurfaceViewCallPip.setOnTouchListener(new OnTounch());
//
//        init();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    private void setUpListener(){
//        binding.flJawab.setOnClickListener(v -> {
//            /*if(mediaPlayer!=null && mediaPlayer.isPlaying()) {
//                mediaPlayer.stop();
//            }*/
//            if (vibrator != null && vibrator.hasVibrator()) {
//                vibrator.cancel();
//            }
//            //transactionToCalling( false);
//            mPresenter.createOffer();
//            binding.flJawab.hide();
//        });
//
//        binding.flEndCall.setOnClickListener(v -> {
//            if (currentStatus.equals(Constants.CURRENT_RINGING)) {
//                mPresenter.rejectCalling(registrationPayloadFrom, registrationPayloadTo);
//            } else if(currentStatus.equals(Constants.CURRENT_COMMUNOICATION)){
//                mPresenter.stop();
//                mPresenter.disconnect();
//            } else {
//                mPresenter.disconnect();
//            }
//        });
//
//        binding.flSwitch.setOnClickListener(v -> {
//            mPresenter.sw();
//            if (front) {
//                binding.flSwitch.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_camera_front));
//                front = false;
//            } else {
//                binding.flSwitch.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_camera_rear));
//                front = true;
//            }
//        });
//
//        binding.flDisableVideo.setOnClickListener(v -> {
//            if (video) {
//                binding.flDisableVideo.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_videocam_on));
//                video = false;
//            } else {
//                binding.flDisableVideo.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_videocam_off));
//                video = true;
//            }
//            mPresenter.disableVideo(video);
//        });
//    }
//
//
//    @Override
//    public void getBroadcastEvents(BroadcastEvents.Event event) {
//        if (event.getInitString().equals(Constants.CURRENT_BUSY)) {
//            if (event.getObject() instanceof SignIn) {
//                SignIn userf = (SignIn) event.getObject();
//                mPresenter.busy(user.getEmail(), userf.getEmail());
//            }
//        }
//    }
//
//    @Override
//    public void logAndToast(String msg) {
//        Log.d(TAG, msg);
//        if (logToast != null) {
//            logToast.cancel();
//        }
//        logToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
//        logToast.show();
//    }
//
//    @Override
//    public VideoCapturer createVideoCapturer() {
//        VideoCapturer videoCapturer;
//        if (useCamera2()) {
//            if (!captureToTexture()) {
//                return null;
//            }
//            videoCapturer = createCameraCapturer(new Camera2Enumerator(this));
//        } else {
//            videoCapturer = createCameraCapturer(new Camera1Enumerator(captureToTexture()));
//        }
//        return videoCapturer;
//    }
//
//    @Override
//    public EglBase.Context getEglBaseContext() {
//        return rootEglBase.getEglBaseContext();
//    }
//
//    @Override
//    public VideoRenderer.Callbacks getLocalProxyRenderer() {
//        return localProxyRenderer;
//    }
//
//    @Override
//    public VideoRenderer.Callbacks getRemoteProxyRenderer() {
//        return remoteProxyRenderer;
//    }
//
//    @Override
//    public void registerStatus(boolean success, ServerResponse.RegistrationPayload registrationPayload) {
//        this.registrationPayloadFrom = registrationPayload;
//        //mPresenter.notifyCaller(user.getEmail(), userF.getEmail());
//        mPresenter.notifyCaller(registrationPayload, userF.getEmail());
//        mPresenter.startTimeIfIncommingCallNeverReceive();
//    }
//
//    @Override
//    public void transactionToCalling(boolean isHost) {
//        mPresenter.initPeerConfig(isHost, userF.getUuid(), user.getUuid(),consultationId, registrationPayloadFrom, registrationPayloadTo);
//        mPresenter.startCall();
//    }
//
//    @Override
//    public void incomingCalling(String fromPeer, ServerResponse.RegistrationPayload registrationPayload) {
//        Log.i(TAG,"IncomingCall");
//        this.registrationPayloadTo = registrationPayload;
//        currentStatus = Constants.CURRENT_RINGING;
//        /*mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.yelyel);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();*/
//        binding.llCall.setVisibility(View.VISIBLE);
//
//        long[] pattern = {
//                0, 500, 800, 500, 800, 500, 800, 500, 800, 500, 800
//        };
//        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //vibrator.vibrate(VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE));
//            vibrator.vibrate(VibrationEffect.createWaveform(pattern,0));
//        } else {
//            vibrator.vibrate(pattern,0);
//        }
//
//
//        binding.tvCall.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.vcall_text_background,null));
//        binding.tvName.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.vcall_text_background,null));
//        transactionToCalling(false);
//    }
//
//    @Override
//    public void stopCalling() {
//        mPresenter.disconnect();
//    }
//
//    @Override
//    public void startCallIng() {
//        binding.llProfile.setVisibility(View.GONE);
//        binding.flJawab.hide();
//        binding.flEndCall.show();
//        //flDisableVideo.show();
//        binding.flSwitch.show();
//        currentStatus = Constants.CURRENT_COMMUNOICATION;
//    }
//
//    @Override
//    public void notifyMe(ServerResponse.RegistrationPayload registrationPayload) {
//        this.registrationPayloadTo = registrationPayload;
//        mPresenter.startTimeMissedCall();
//        //handling error ketika user yang menelepon udah gak aktif tp response dari server dianggap aktive jadi,
//        // cek jika 2 detik tidak ada response maka dianggap mc ajah.
//    }
//
//    @Override
//    public void missedCall(String s) {
//        Log.i("Test", "Missed Call");
//        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
//        mPresenter.disconnect();
//    }
//
//    @Override
//    public void callingResponse(String s) {
//
//    }
//
//    @Override
//    public void cancelCallingResponse() {
//
//    }
//
//    @Override
//    public void rejectCallingResponse(String s) {
//        mPresenter.disconnect();
//    }
//
//    @Override
//    public void dcMissedCall() {
//        if (!currentStatus.equals(Constants.CURRENT_RINGING)) {
//            Toast.makeText(mContext, userF.getEmail() + " Missed Call", Toast.LENGTH_LONG).show();
//            mPresenter.disconnect();
//        }
//    }
//
//    @Override
//    public void busy(String s) {
//        Toast.makeText(mContext, userF.getFullName() + " Sedang sibuk, coba lagi nanti", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void rejectedFromCalle() {
//
//    }
//
//    @Override
//    public void goToincommingCallNeverReceive() {
//        if(!currentStatus.equals(Constants.CURRENT_RINGING) && !currentStatus.equals(Constants.CURRENT_COMMUNOICATION)) {
//            if(netkromDialog == null)
//                netkromDialog = new NetkromDialog(mContext, R.drawable.ic_gagal,
//                        "Terjadi kesalahan saat menerima telepon. Silahkan melakukan panggilan ulang",
//                        true, Constants.ALERT_FAILED);
//
//            netkromDialog.setOnCancelListener(dialog ->  {
//                if (registrationPayloadTo!=null) {
//                    mPresenter.rejectCalling(registrationPayloadFrom, registrationPayloadTo);
//                }else {
//                    mPresenter.disconnect();
//                }
//            });
//            if (!netkromDialog.isShowing())
//                netkromDialog.show();
//        }
//    }
//
//    private boolean useCamera2() {
//        return Camera2Enumerator.isSupported(this) && mPresenter.getDefaultConfig().isUseCamera2();
//    }
//
//    private boolean captureToTexture() {
//        return mPresenter.getDefaultConfig().isCaptureToTexture();
//    }
//
//    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
//        final String[] deviceNames = enumerator.getDeviceNames();
//        // First, try to find front facing camera
//        for (String deviceName : deviceNames) {
//            if (enumerator.isFrontFacing(deviceName)) {
//                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
//
//                if (videoCapturer != null) {
//                    return videoCapturer;
//                }
//            }
//        }
//
//        // Front facing camera not found, try something else
//        for (String deviceName : deviceNames) {
//            if (!enumerator.isFrontFacing(deviceName)) {
//                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
//
//                if (videoCapturer != null) {
//                    return videoCapturer;
//                }
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    public void onBackPressedSupport() {
//        super.onBackPressedSupport();
//        if (currentStatus.equals(Constants.CURRENT_RINGING)) {
//            mPresenter.rejectCalling(registrationPayloadFrom, registrationPayloadTo);
//        }else if(currentStatus.equals(Constants.CURRENT_COMMUNOICATION)) {
//            mPresenter.stop();
//            mPresenter.disconnect();
//        }else {
//            mPresenter.disconnect();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        /*if(mediaPlayer!=null && mediaPlayer.isPlaying()) {
//            mediaPlayer.stop();
//        }*/
//        if (vibrator != null && vibrator.hasVibrator()) {
//            vibrator.cancel();
//        }
//        super.onDestroy();
//    }
//
//    private final class OnTounch implements View.OnTouchListener {
//        private static final int MAX_CLICK_DURATION = 200;
//        private long startClickTime;
//
//        @SuppressLint("ClickableViewAccessibility")
//        @Override
//        public boolean onTouch(View view, MotionEvent event) {
//            final int X = (int) event.getRawX();
//            final int Y = (int) event.getRawY();
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN & MotionEvent.ACTION_MASK:
//                    startClickTime = Calendar.getInstance().getTimeInMillis();
//                    FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();
//                    _xDelta = X - lParams.leftMargin;
//                    _yDelta = Y - lParams.topMargin;
//                    break;
//                case MotionEvent.ACTION_UP:
//                    long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
//                    if (clickDuration < MAX_CLICK_DURATION) {
//                        setSwappedFeeds(!isSwappedFeeds);
//                    }
//                    break;
//                case MotionEvent.ACTION_POINTER_DOWN:
//                    break;
//                case MotionEvent.ACTION_MOVE & MotionEvent.ACTION_MASK:
//                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view
//                            .getLayoutParams();
//                    layoutParams.leftMargin = X - _xDelta;
//                    layoutParams.topMargin = Y - _yDelta;
//                    layoutParams.rightMargin = -250;
//                    layoutParams.bottomMargin = -250;
//                    view.setLayoutParams(layoutParams);
//                    break;
//            }
//            binding.main.invalidate();
//            return true;
//        }
//    }
//}
