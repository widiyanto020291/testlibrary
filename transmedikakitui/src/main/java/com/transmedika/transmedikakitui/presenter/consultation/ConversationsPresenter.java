package com.transmedika.transmedikakitui.presenter.consultation;

import android.Manifest;
import android.content.Context;
import android.util.Log;

import com.tbruyelle.rxpermissions3.RxPermissions;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.component.RxBus;
import com.transmedika.transmedikakitui.contract.consultation.ConversationContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.FeedbackParam;
import com.transmedika.transmedikakitui.models.bean.json.param.StatusKonsultasiParam;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class ConversationsPresenter extends RxPresenter<ConversationContract.View>
        implements ConversationContract.Presenter {
    private final DataManager mDataManager;
    private static final int COUNT_DOWN_TIME = 4000;
    private static final int RECONNECT_COUNT_DOWN_TIME = 10000;

    public ConversationsPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
        this.realm = mDataManager.getRealm();
    }

    @Override
    public void attachView(ConversationContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(BroadcastEvents.class)
                .compose(RxUtil.rxSchedulerHelper())
                .map(BroadcastEvents::getEvent)
                .subscribeWith(new CommonSubscriber<BroadcastEvents.Event>(mView, false, false) {
                    @Override
                    public void onNext(BroadcastEvents.Event event) {
                        mView.getBroadcastEvents(event);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        registerEvent();
                    }
                })
        );
    }

    @Override
    public void stopTyping() {
        startCountDown();
    }

    @Override
    public void upload(String auth, RequestBody name, MultipartBody.Part file, Context context) {
        /*addSubscribe(mDataManager.uploadImage(Constants.BEARER+auth,name, file)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<String>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<String> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.uploadResp(model);
                            }else{
                                super.onNext(model);
                            }
                        }
                    }
                }));*/
    }

    @Override
    public void cekDeviceId(String auth, Context context) {
        addSubscribe(mDataManager.cekDeviceId(Constants.BEARER+auth)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<String>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<String> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.cekDeviceIdResp(model);
                            }else{
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void cekDeviceIdMultiple(String auth, Context context) {
        addSubscribe(mDataManager.cekDeviceIdMultiple(Constants.BEARER+auth)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<List<String>>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<List<String>> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.cekDeviceIdMultipleResp(model);
                            }else{
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void statusKonsultasi(String auth, Long idKonsultasi, StatusKonsultasiParam param, Context context) {
        addSubscribe(mDataManager.statusKonsultasi(Constants.BEARER+auth,idKonsultasi, param)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseOResponse>(mView,context, true){
                    @Override
                    public void onNext(BaseOResponse model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.statusKonsultasiResp(model);
                            }else{
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void download(String url, String namaPasien, Context context) {
        addSubscribe(mDataManager.downloadFile(url)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<ResponseBody>(mView,context, true){
                    @Override
                    public void onNext(ResponseBody model) {
                        if(model.contentLength() > 0) {
                            boolean b = writeResponseBodyToDisk(model, url, namaPasien, context);
                        }
                    }
                }));
    }

    private void startCountDown() {
        addSubscribe(Flowable.timer(COUNT_DOWN_TIME, TimeUnit.MILLISECONDS)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(aLong -> mView.jump())
        );
    }

    @Override
    public void reconnect() {
        addSubscribe(Flowable.timer(RECONNECT_COUNT_DOWN_TIME, TimeUnit.MILLISECONDS)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(aLong -> mView.jumpReconnect())
        );
    }

    @Override
    public SignIn selectLogin() {
        return mDataManager.selectLogin();
    }

    @Override
    public void checkPermission(RxPermissions rxPermissions, Context context) {
        addSubscribe(rxPermissions.request(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        mView.start();
                    } else {
                        mView.showErrorMsg("mebutuhkan akses kamera");
                    }
                })
        );
    }

    @Override
    public void setCekKonsultasi(boolean b) {
        mDataManager.setCekKonsultasi(b);
    }

    @Override
    public void setCekKonsultasiKlinik(boolean b) {
        mDataManager.setCekKonsultasiKlinik(b);
    }

    @Override
    public void setBusyTelepon(boolean b) {
        mDataManager.setSibuk(b);
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String url, String namaPasien, Context context) {
        boolean valid = false;
        try {
            File netkromDir = new File(TransmedikaUtils.cleanName(namaPasien,context));
            boolean isDirectoryCreated= netkromDir.exists();
            if (!isDirectoryCreated) {
                isDirectoryCreated = netkromDir.mkdirs();
            }

            if(isDirectoryCreated) {
                File buktiBayar = new File(TransmedikaUtils.cleanName(namaPasien,context), TransmedikaUtils.getFilenameUrlExt(url,'/'));
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    byte[] fileReader = new byte[4096];
                    long fileSize = body.contentLength();
                    long fileSizeDownloaded = 0;
                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(buktiBayar);
                    while (true) {
                        int read = inputStream.read(fileReader);
                        if (read == -1) {
                            break;
                        }
                        outputStream.write(fileReader, 0, read);
                        fileSizeDownloaded += read;
                        Log.d("Worker Download", "file download: " + fileSizeDownloaded + " of " + fileSize);
                    }
                    outputStream.flush();
                    mView.donwloadResp(buktiBayar.getPath());
                    valid = true;
                } catch (IOException e) {
                    mView.showErrorMsg(e.toString());
                    valid = false;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            }
        } catch (IOException e) {
            mView.showErrorMsg(e.toString());
            valid = false;
        }
        return valid;
    }

    @Override
    public void postFeedback(Context context, String auth, FeedbackParam param) {
        addSubscribe(mDataManager.postFeedbackConsultation(Constants.BEARER+auth, param)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseOResponse>(mView, context, true){
                    @Override
                    public void onNext(BaseOResponse model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.onFeedbackResp(model);
                            }else{
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }
}
