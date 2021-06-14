package com.transmedika.transmedikakitui.modul;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseBindingActivity;
import com.transmedika.transmedikakitui.databinding.CommonFrameFragmentBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.Konsultasi;
import com.transmedika.transmedikakitui.models.bean.json.Profile;
import com.transmedika.transmedikakitui.models.bean.json.Ref;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.modul.videocall.OneToOneContract;
import com.transmedika.transmedikakitui.modul.videocall.OneToOnePresenter;
import com.transmedika.transmedikakitui.modul.videocall.kurento.models.response.ServerResponse;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.widget.NetkromDialog;

import org.jetbrains.annotations.NotNull;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.EglBase;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;

import java.util.Date;
import java.util.Objects;

import me.yokeyword.fragmentation.SupportFragment;

public class ConversationMainActivity extends BaseBindingActivity<CommonFrameFragmentBinding, OneToOneContract.View, OneToOnePresenter>
    implements OneToOneContract.View{

    private Konsultasi konsultasi = new Konsultasi();
    private Doctor dokter;
    private Profile profil;
    private String note;
    private boolean flagHistori;
    private Date tglTransaksi;
    private SignIn user;

    private String currentStatus = "";
    private ServerResponse.RegistrationPayload registrationPayloadFrom;
    private ServerResponse.RegistrationPayload registrationPayloadTo;

    private NetkromDialog netkromDialog;
    private boolean isPop = false;

    @NonNull
    @NotNull
    @Override
    protected OneToOneContract.View getBaseView() {
        return this;
    }

    @Override
    protected CommonFrameFragmentBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return CommonFrameFragmentBinding.inflate(inflater);
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        mPresenter = new OneToOnePresenter(DataManager.getDataManagerInstance(mContext), getApplication());
        super.onViewCreated(bundle);
        Bundle b = getIntent().getBundleExtra(Constants.DATA);
        if(b != null) {
            konsultasi = b.getParcelable(Constants.DATA_INFO);
            dokter = konsultasi.getDoctor();
            profil = konsultasi.getPatient();
            Ref ref = getIntent().getParcelableExtra(Constants.DATA_REF);
            profil.setRef(ref);
            Long idKonsultasi = getIntent().getLongExtra(Constants.ID_KONSULTASI,0);
            konsultasi.setConsultationId(idKonsultasi);
            note = getIntent().getStringExtra(Constants.DATA_NOTE);
            konsultasi.setComplaint(note);
            if (getIntent().getBooleanExtra(Constants.FLAG_HISTORI, false)) {
                flagHistori = getIntent().getBooleanExtra(Constants.FLAG_HISTORI, false);
                tglTransaksi = (java.util.Date) getIntent().getSerializableExtra(Constants.TGL_TRANSAKSI);
            }
        }
        user = mPresenter.selectLogin();

        if (findFragment(ConversationFragment.class) == null) {
            if(dokter!=null) {
                loadRootFragment(R.id.fl_container, ConversationFragment.newInstance(konsultasi, note, flagHistori, tglTransaksi));
            }
        }
    }

    @Override
    protected void initEventAndData(Bundle bundle) {

    }

    protected void init(boolean a) {
        isPop = false;
        mPresenter.connectServer(a);
    }

    @Override
    public void getBroadcastEvents(BroadcastEvents.Event event) {

    }

    @Override
    public void logAndToast(String msg) {

    }

    @Override
    public void disconnect() {
        if(!isPop) {
            SupportFragment currentFragment = (SupportFragment) getSupportFragmentManager().findFragmentById(R.id.fl_container);
            if (currentFragment instanceof AnswerFragment) {
                ((AnswerFragment) currentFragment).disconnect();
                pop();
                isPop = true;
            }
        }
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


    private boolean useCamera2() {
        return Camera2Enumerator.isSupported(this) && mPresenter.getDefaultConfig().isUseCamera2();
    }

    private boolean captureToTexture() {
        return mPresenter.getDefaultConfig().isCaptureToTexture();
    }

    @Override
    public EglBase.Context getEglBaseContext() {
        return ((AnswerFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fl_container))).getRootEglBase().getEglBaseContext();
    }

    @Override
    public VideoRenderer.Callbacks getLocalProxyRenderer() {
        return ((AnswerFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fl_container))).getLocalProxyRenderer();
    }

    @Override
    public VideoRenderer.Callbacks getRemoteProxyRenderer() {
        return ((AnswerFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fl_container))).getRemoteProxyRenderer();
    }

    @Override
    public void setSwappedFeeds(boolean swappedFeed) {
        SupportFragment currentFragment = (SupportFragment) getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (currentFragment instanceof AnswerFragment) {
            ((AnswerFragment) currentFragment).setSwappedFeeds(swappedFeed);
        }
    }

    @Override
    public void socketConnect(boolean success) {
        if (success) {
            mPresenter.register(user.getEmail());
        }
    }

    @Override
    public void registerStatus(boolean success, ServerResponse.RegistrationPayload registrationPayload) {
        this.registrationPayloadFrom = registrationPayload;
        mPresenter.notifyCaller(registrationPayload, dokter.getEmail());
        mPresenter.startTimeIfIncommingCallNeverReceive();
    }

    @Override
    public void transactionToCalling(boolean isHost) {
        mPresenter.initPeerConfig(isHost, dokter.getUuid(), user.getUuid(), String.valueOf(konsultasi.getConsultationId()), registrationPayloadFrom, registrationPayloadTo);
        mPresenter.startCall();
    }

    @Override
    public void incomingCalling(String fromPeer, ServerResponse.RegistrationPayload registrationPayload) {
        this.registrationPayloadTo = registrationPayload;
        currentStatus = Constants.CURRENT_RINGING;
        start(AnswerFragment.newInstance(profil));
    }

    @Override
    public void stopCalling() {
        mPresenter.disconnect();
    }

    @Override
    public void startCallIng() {
        ((AnswerFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fl_container))).startCallIng();
    }

    @Override
    public void notifyMe(ServerResponse.RegistrationPayload registrationPayload) {
        this.registrationPayloadTo = registrationPayload;
        mPresenter.startTimeMissedCall();
    }

    @Override
    public void missedCall(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
        mPresenter.disconnect();
    }

    @Override
    public void callingResponse(String s) {

    }

    @Override
    public void cancelCallingResponse() {

    }

    @Override
    public void rejectCallingResponse(String s) {
        mPresenter.disconnect();
    }

    @Override
    public void dcMissedCall() {
        if (!currentStatus.equals(Constants.CURRENT_RINGING)) {
            Toast.makeText(mContext, profil.getEmail() + " Missed Call", Toast.LENGTH_LONG).show();
            mPresenter.disconnect();
        }
    }

    @Override
    public void busy(String s) {

    }

    @Override
    public void rejectedFromCalle() {

    }

    @Override
    public void goToincommingCallNeverReceive() {
        if(!currentStatus.equals(Constants.CURRENT_RINGING) && !currentStatus.equals(Constants.CURRENT_COMMUNOICATION)) {
            if(netkromDialog == null)
                netkromDialog = new NetkromDialog(mContext, R.drawable.ic_gagal,
                        "Terjadi kesalahan saat menerima telepon. Silahkan melakukan panggilan ulang",
                        true, Constants.ALERT_FAILED);

            netkromDialog.setOnCancelListener(dialog ->  {
                if (registrationPayloadTo!=null) {
                    mPresenter.rejectCalling(registrationPayloadFrom, registrationPayloadTo);
                }else {
                    mPresenter.disconnect();
                }
            });
            if (!netkromDialog.isShowing())
                netkromDialog.show();
        }
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public OneToOnePresenter mPresenter(){
        return mPresenter;
    }

    public ServerResponse.RegistrationPayload getRegistrationPayloadFrom() {
        return registrationPayloadFrom;
    }

    public ServerResponse.RegistrationPayload getRegistrationPayloadTo() {
        return registrationPayloadTo;
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        if (currentStatus.equals(Constants.CURRENT_RINGING)) {
            mPresenter.rejectCalling(registrationPayloadFrom, registrationPayloadTo);
        }else if(currentStatus.equals(Constants.CURRENT_COMMUNOICATION)) {
            mPresenter.stop();
            mPresenter.disconnect();
        }else {
            mPresenter.disconnect();
        }
    }
}
