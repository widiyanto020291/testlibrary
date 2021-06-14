package com.transmedika.transmedikakitui.modul;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import com.nhancv.webrtcpeer.rtc_plugins.ProxyRenderer;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.SimpleBindingFragment;
import com.transmedika.transmedikakitui.databinding.KurActivityAnswerBinding;
import com.transmedika.transmedikakitui.models.bean.json.Profile;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.ImageLoader;

import org.webrtc.EglBase;
import org.webrtc.RendererCommon;

import java.util.Calendar;
import java.util.Objects;

public class AnswerFragment extends SimpleBindingFragment<KurActivityAnswerBinding> {
    private EglBase rootEglBase;
    private ProxyRenderer localProxyRenderer;
    private ProxyRenderer remoteProxyRenderer;
    private boolean isSwappedFeeds;
    private Vibrator vibrator;
    private boolean front = true;
    private boolean video = true;
    private Profile userF;
    private int _xDelta;
    private int _yDelta;

    public static AnswerFragment newInstance(Profile userF) {
        Bundle args = new Bundle();
        AnswerFragment fragment = new AnswerFragment();
        args.putParcelable(Constants.DATA_USER, userF);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected KurActivityAnswerBinding getViewBinding(LayoutInflater inflater,
                                                      @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                                                      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return KurActivityAnswerBinding.inflate(inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments()!=null){
            userF = getArguments().getParcelable(Constants.DATA_USER);
        }
        binding.llCall.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        initVibrate();
        init();
        setUpListener();

        if (userF != null) {
            ImageLoader.load(mActivity, Objects.requireNonNull(userF).getProfilePicture(), binding.cvImg);
            binding.tvName.setText(userF.getFullName());
            binding.tvCall.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.vcall_text_background,null));
            binding.tvName.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.vcall_text_background,null));

        }

        if(getActivity() instanceof ConversationMainActivity)
            ((ConversationMainActivity) getActivity()).transactionToCalling(false);

        binding.vGLSurfaceViewCallPip.setOnTouchListener(new OnTounch());

    }

    private void initVibrate(){
        long[] pattern = {
                0, 500, 800, 500, 800, 500, 800, 500, 800, 500, 800
        };
        vibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //vibrator.vibrate(VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE));
            vibrator.vibrate(VibrationEffect.createWaveform(pattern,0));
        } else {
            vibrator.vibrate(pattern,0);
        }
    }

    void init(){
        mActivity.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        //config peer
        localProxyRenderer = new ProxyRenderer();
        remoteProxyRenderer = new ProxyRenderer();
        rootEglBase = EglBase.create();

        binding.vGLSurfaceViewCallFull.init(rootEglBase.getEglBaseContext(), null);
        binding.vGLSurfaceViewCallFull.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        //binding.vGLSurfaceViewCallFull.setEnableHardwareScaler(false);

        binding.vGLSurfaceViewCallPip.init(rootEglBase.getEglBaseContext(), null);
        binding.vGLSurfaceViewCallPip.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        //binding.vGLSurfaceViewCallPip.setEnableHardwareScaler(true);
        binding.vGLSurfaceViewCallPip.setZOrderMediaOverlay(true);

        setSwappedFeeds(true);
        binding.vGLSurfaceViewCallPip.setOnClickListener(view -> setSwappedFeeds(!isSwappedFeeds));
    }


    public void setSwappedFeeds(boolean isSwappedFeeds) {
        this.isSwappedFeeds = isSwappedFeeds;
        localProxyRenderer.setTarget(isSwappedFeeds ? binding.vGLSurfaceViewCallFull : binding.vGLSurfaceViewCallPip);
        remoteProxyRenderer.setTarget(isSwappedFeeds ? binding.vGLSurfaceViewCallPip : binding.vGLSurfaceViewCallFull);
        binding.vGLSurfaceViewCallFull.setMirror(isSwappedFeeds);
        binding.vGLSurfaceViewCallPip.setMirror(!isSwappedFeeds);
    }

    private void setUpListener(){
        if (getActivity() instanceof ConversationMainActivity) {
            ConversationMainActivity cActivity = ((ConversationMainActivity) getActivity());
            binding.flJawab.setOnClickListener(v -> {
                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.cancel();
                }
                cActivity.mPresenter().createOffer();
                binding.flJawab.hide();
                binding.vSeparate.setVisibility(View.GONE);
            });

            binding.flEndCall.setOnClickListener(v -> {
                if (cActivity.getCurrentStatus().equals(Constants.CURRENT_RINGING)) {
                    cActivity.mPresenter().rejectCalling(cActivity.getRegistrationPayloadFrom(), cActivity.getRegistrationPayloadTo());
                } else if (cActivity.getCurrentStatus().equals(Constants.CURRENT_COMMUNOICATION)) {
                    cActivity.mPresenter().stop();
                    cActivity.mPresenter().disconnect();
                } else {
                    cActivity.mPresenter().disconnect();
                }
            });

            binding.flSwitch.setOnClickListener(v -> {
                cActivity.mPresenter().sw();
                if (front) {
                    binding.flSwitch.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_camera_front));
                    front = false;
                } else {
                    binding.flSwitch.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_camera_rear));
                    front = true;
                }
            });

            binding.flDisableVideo.setOnClickListener(v -> {
                if (video) {
                    binding.flDisableVideo.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_videocam_on));
                    video = false;
                } else {
                    binding.flDisableVideo.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_videocam_off));
                    video = true;
                }
                cActivity.mPresenter().disableVideo(video);
            });
        }
    }

    public void startCallIng() {
        binding.llProfile.setVisibility(View.GONE);
        binding.flJawab.hide();
        binding.flEndCall.show();
        //flDisableVideo.show();
        binding.flSwitch.show();
    }

    public void disconnect(){
        localProxyRenderer.setTarget(null);
        binding.vGLSurfaceViewCallFull.release();
        binding.vGLSurfaceViewCallPip.release();
    }

    public EglBase getRootEglBase() {
        return rootEglBase;
    }

    public ProxyRenderer getLocalProxyRenderer() {
        return localProxyRenderer;
    }

    public ProxyRenderer getRemoteProxyRenderer() {
        return remoteProxyRenderer;
    }

    @Override
    public void onDestroy() {
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.cancel();
        }
        super.onDestroy();
    }

    @Override
    public boolean onBackPressedSupport() {
        return super.onBackPressedSupport();
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
