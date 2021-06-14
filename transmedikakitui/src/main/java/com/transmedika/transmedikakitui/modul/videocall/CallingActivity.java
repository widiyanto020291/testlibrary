package com.transmedika.transmedikakitui.modul.videocall;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import com.nhancv.webrtcpeer.rtc_plugins.ProxyRenderer;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseBindingActivity;
import com.transmedika.transmedikakitui.databinding.KurActivityCallingBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.Profile;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.modul.videocall.kurento.models.CallPush;
import com.transmedika.transmedikakitui.modul.videocall.kurento.models.Data;
import com.transmedika.transmedikakitui.modul.videocall.kurento.models.response.ServerResponse;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.ImageLoader;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import org.jetbrains.annotations.NotNull;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.EglBase;
import org.webrtc.Logging;
import org.webrtc.RendererCommon;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;

import java.util.Calendar;
import java.util.Objects;


public class CallingActivity extends BaseBindingActivity<KurActivityCallingBinding, OneToOneContract.View,OneToOnePresenter>
        implements OneToOneContract.View {

    private static final String TAG = CallingActivity.class.getSimpleName();
    private int _xDelta;
    private int _yDelta;

    private EglBase rootEglBase;
    private ProxyRenderer localProxyRenderer;
    private ProxyRenderer remoteProxyRenderer;
    private Toast logToast;
    private boolean isSwappedFeeds;
    private SignIn user;
    private String currentStatus = "";
    private Profile targetProfile;
    private boolean front = true;
    private String consultationId;
    private ServerResponse.RegistrationPayload registrationPayloadFrom;
    private ServerResponse.RegistrationPayload registrationPayloadTo;

    protected void init() {
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        //config peer
        localProxyRenderer = new ProxyRenderer();
        remoteProxyRenderer = new ProxyRenderer();
        rootEglBase = EglBase.create();


        binding.vGLSurfaceViewCallFull.init(rootEglBase.getEglBaseContext(), null);
        binding.vGLSurfaceViewCallFull.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        binding.vGLSurfaceViewCallFull.setEnableHardwareScaler(false);

        binding.vGLSurfaceViewCallPip.init(rootEglBase.getEglBaseContext(), null);
        binding.vGLSurfaceViewCallPip.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        binding.vGLSurfaceViewCallPip.setEnableHardwareScaler(true);
        binding.vGLSurfaceViewCallPip.setZOrderMediaOverlay(true);

        // Swap feeds on pip view click.
        binding.vGLSurfaceViewCallPip.setOnClickListener(view -> setSwappedFeeds(!isSwappedFeeds));

        setSwappedFeeds(true);
        mPresenter.connectServer(true);
        binding.tvCall.setText(R.string.mencoba_terhubung_ke_socket);

        localProxyRenderer.setTarget(binding.vGLSurfaceViewCallFull);
    }

    @Override
    public void setSwappedFeeds(boolean isSwappedFeeds) {
        Logging.d(TAG, "setSwappedFeeds: " + isSwappedFeeds);
        this.isSwappedFeeds = isSwappedFeeds;
        localProxyRenderer.setTarget(isSwappedFeeds ? binding.vGLSurfaceViewCallFull : binding.vGLSurfaceViewCallPip);
        remoteProxyRenderer.setTarget(isSwappedFeeds ? binding.vGLSurfaceViewCallPip : binding.vGLSurfaceViewCallFull);
        binding.vGLSurfaceViewCallFull.setMirror(isSwappedFeeds);
        binding.vGLSurfaceViewCallPip.setMirror(!isSwappedFeeds);
    }

    @Override
    public void socketConnect(boolean success) {
        if (success && user != null) {
            binding.tvCall.setText(R.string.membuat_sesi);
            mPresenter.register(user.getEmail());
        }
    }

    @Override
    public void disconnect() {
        localProxyRenderer.setTarget(null);
        binding.vGLSurfaceViewCallFull.release();
        binding.vGLSurfaceViewCallPip.release();
        finish();
    }

    @Override
    protected KurActivityCallingBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return KurActivityCallingBinding.inflate(inflater);
    }

    @NonNull
    @NotNull
    @Override
    protected OneToOneContract.View getBaseView() {
        return this;
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        // These flags ensure that the activity can be launched when the screen is locked.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);

        mPresenter = new OneToOnePresenter(DataManager.getDataManagerInstance(mContext), getApplication());
        super.onViewCreated(bundle);
        user = mPresenter.selectLogin();
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        Bundle b = getIntent().getBundleExtra("DATA");
        if (b != null) {
            targetProfile = b.getParcelable("UserTarget");
            consultationId = b.getString(Constants.ID_KONSULTASI);
        }
        setUpListener();
        if (targetProfile != null) {
            binding.tvName.setText(targetProfile.getFullName());
            ImageLoader.load(this, Objects.requireNonNull(targetProfile).getProfilePicture(), binding.cvImg);
            if (targetProfile.getDeviceIds() == null) {
                errorDeviceIds();
            }else if(targetProfile.getDeviceIds().size() == 0){
                errorDeviceIds();
            }
            init();
        }

        //binding.flEndCall.hide();

        //FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, SystemUtil.dip2px(mContext, 144));
        //layoutParams.setMargins(SystemUtil.dip2px(mContext, 12), SystemUtil.dip2px(mContext, 12), 0, 0);
        //vGLSurfaceViewCallPip.setLayoutParams(layoutParams);
        binding.vGLSurfaceViewCallPip.setOnTouchListener(new OnTounch());
    }

    private void errorDeviceIds(){
        Toast.makeText(mContext, "Target user tidak punya device id", Toast.LENGTH_LONG).show();
        finish();
    }

    private void setUpListener(){
        binding.flEndCall.setOnClickListener(v -> {
            if (currentStatus.equals(Constants.CURRENT_RINGING)) {
                mPresenter.cancelCalling(registrationPayloadFrom, targetProfile.getEmail());
            }else if(currentStatus.equals(Constants.CURRENT_COMMUNOICATION)){
                mPresenter.stop();
                mPresenter.disconnect();
            }else {
                mPresenter.disconnect();
            }
        });

        binding.flSwitch.setOnClickListener(v -> {
            mPresenter.sw();
            if (front) {
                binding.flSwitch.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_camera_front));
                front = false;
            } else {
                binding.flSwitch.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_camera_rear));
                front = true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        if (currentStatus.equals(Constants.CURRENT_RINGING)) {
            mPresenter.cancelCalling(registrationPayloadFrom, targetProfile.getEmail());
        }else if(currentStatus.equals(Constants.CURRENT_COMMUNOICATION)){
            mPresenter.stop();
            mPresenter.disconnect();
        }else {
            mPresenter.disconnect();
        }
    }

    @Override
    public void getBroadcastEvents(BroadcastEvents.Event event) {
        if (event.getInitString().equals(Constants.CURRENT_BUSY)) {
            if (event.getObject() instanceof SignIn) {
                SignIn userf = (SignIn) event.getObject();
                mPresenter.busy(user.getEmail(), userf.getEmail());
            }
        }
    }

    @Override
    public void logAndToast(String msg) {
        Log.d(TAG, msg);
        if (logToast != null) {
            logToast.cancel();
        }
        logToast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        logToast.show();
    }

    @Override
    public VideoCapturer createVideoCapturer() {
        VideoCapturer videoCapturer;
        if (useCamera2()) {
            if (!captureToTexture()) {
                return null;
            }
            videoCapturer = createCameraCapturer(new Camera2Enumerator(this));
        } else {
            videoCapturer = createCameraCapturer(new Camera1Enumerator(captureToTexture()));
        }
        return videoCapturer;
    }

    @Override
    public EglBase.Context getEglBaseContext() {
        return rootEglBase.getEglBaseContext();
    }

    @Override
    public VideoRenderer.Callbacks getLocalProxyRenderer() {
        return localProxyRenderer;
    }

    @Override
    public VideoRenderer.Callbacks getRemoteProxyRenderer() {
        return remoteProxyRenderer;
    }

    @Override
    public void registerStatus(boolean success, ServerResponse.RegistrationPayload registrationPayload) {
        this.registrationPayloadFrom = registrationPayload;
        CallPush calllTni = new CallPush();
        Data data = new Data();
        data.setApp("VIDEO_CALL");
        data.setBody(user.getEmail() + " menelepon");
        data.setStatus("CALLING");
        data.setTitle("CALL");
        data.setMe(TransmedikaUtils.gsonBuilder().toJson(user));
        data.setFriend(TransmedikaUtils.gsonBuilder().toJson(user));
        calllTni.setTo(targetProfile.getDeviceIds());
        calllTni.setData(data);
        mPresenter.callingMobileDevice(user.getEmail(), targetProfile.getEmail(), calllTni);

        binding.tvCall.setText(R.string.sedang_menghubungkan);
    }

    @Override
    public void transactionToCalling(boolean isHost) {
        mPresenter.initPeerConfig(isHost, user.getUuid(), targetProfile.getUuid(), consultationId, registrationPayloadFrom, registrationPayloadTo);
        mPresenter.startCall();
    }

    @Override
    public void incomingCalling(String fromPeer, ServerResponse.RegistrationPayload registrationPayload) {
        this.registrationPayloadTo = registrationPayload;
        //transactionToCalling(fromPeer, user.getEmail(), false);
    }

    @Override
    public void stopCalling() {
        mPresenter.disconnect();
    }

    @Override
    public void startCallIng() {
        binding.llCall.setVisibility(View.GONE);
        binding.flSwitch.show();
        binding.flEndCall.show();
        currentStatus = Constants.CURRENT_COMMUNOICATION;
    }

    @Override
    public void notifyMe(ServerResponse.RegistrationPayload registrationPayload) {
        this.registrationPayloadTo = registrationPayload;
        binding.tvCall.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.vcall_text_background,null));
        binding.tvName.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.vcall_text_background,null));
        binding.tvCall.setText(R.string.berdering);
        transactionToCalling(true);
        currentStatus = Constants.CURRENT_RINGING;
    }

    @Override
    public void missedCall(String s) {

    }

    @Override
    public void callingResponse(String s) {
        if (s.equals("accepted")) {
            currentStatus = Constants.CURRENT_CALLING;
            binding.flEndCall.show();
        } else if (s.equals("rejected")) {
            logAndToast(s);
            disconnect();
        }
    }

    @Override
    public void cancelCallingResponse() {
        mPresenter.disconnect();
    }

    @Override
    public void rejectCallingResponse(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
        mPresenter.disconnect();
    }

    @Override
    public void dcMissedCall() {

    }

    @Override
    public void busy(String s) {
        binding.tvCall.setText(targetProfile.getFullName().concat(" sedang sibuk"));
    }

    @Override
    public void rejectedFromCalle() {
        logAndToast(getString(R.string.dua_param_string,targetProfile.getFullName(),getString(R.string.menolak_panggilan)));
        mPresenter.disconnect();
    }

    @Override
    public void goToincommingCallNeverReceive() {

    }

    private boolean useCamera2() {
        return Camera2Enumerator.isSupported(this) && mPresenter.getDefaultConfig().isUseCamera2();
    }

    private boolean captureToTexture() {
        return mPresenter.getDefaultConfig().isCaptureToTexture();
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();
        // First, try to find front facing camera
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // Front facing camera not found, try something else
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }

    private final class OnTounch implements View.OnTouchListener {
        private static final int MAX_CLICK_DURATION = 200;
        private long startClickTime;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN & MotionEvent.ACTION_MASK:
                    startClickTime = Calendar.getInstance().getTimeInMillis();
                    FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    break;
                case MotionEvent.ACTION_UP:
                    long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                    if (clickDuration < MAX_CLICK_DURATION) {
                        setSwappedFeeds(!isSwappedFeeds);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE & MotionEvent.ACTION_MASK:
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view
                            .getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = -250;
                    layoutParams.bottomMargin = -250;
                    view.setLayoutParams(layoutParams);
                    break;
            }
            binding.main.invalidate();
            return true;
        }
    }
}
