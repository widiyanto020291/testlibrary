package com.transmedika.transmedikakitui.presenter.consultation;

import android.content.Context;
import android.util.Log;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.consultation.DetailResepContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.bean.json.Resep;
import com.transmedika.transmedikakitui.models.bean.json.ResepObat;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
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

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.ResponseBody;


public class DetailResepPresenter extends RxPresenter<DetailResepContract.View>
        implements DetailResepContract.Presenter {
    private final DataManager dataManager;

    public DetailResepPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(DetailResepContract.View view) {
        super.attachView(view);
    }


    @Override
    public void detailResep(String auth, Long id, Context context) {
        addSubscribe(dataManager.resep(Constants.BEARER+auth,id)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<Resep>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<Resep> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                dataManager.deleteObat();
                                mView.detailResepResp(model);
                            }else{
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void downLoadResep(String auth, String idResep, Context context) {
        addSubscribe(dataManager.downloadResep(Constants.BEARER+auth, idResep)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<ResponseBody>(mView,context, true){
                    @Override
                    public void onNext(ResponseBody model) {
                        if(model!=null && model.contentLength() >= 0) {
                            boolean b = writeResponseBodyToDisk(model, idResep, context);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String idResep, Context context) {
        boolean valid = false;
        try {
            File netkromDir = new File(TransmedikaUtils.cleanName("RESEP",context));
            boolean isDirectoryCreated= netkromDir.exists();
            if (!isDirectoryCreated) {
                isDirectoryCreated = netkromDir.mkdirs();
            }

            if(isDirectoryCreated) {
                File fileResep = new File(TransmedikaUtils.cleanName("RESEP",context), idResep.concat(".png"));
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    byte[] fileReader = new byte[4096];
                    long fileSize = body.contentLength();
                    long fileSizeDownloaded = 0;
                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(fileResep);
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
                    mView.donwloadResepResp(fileResep.getPath());
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
    public SignIn selectLogin() {
        return dataManager.selectLogin();
    }

    @Override
    public void insertObat(Obat obat) {
        dataManager.insertObat(obat);
    }

    @Override
    public void order(List<ResepObat> listFlowable){
        addSubscribe(Flowable.fromIterable(listFlowable)
                .compose(RxUtil.rxSchedulerHelper())
                .map(resepObat -> {
                    Obat obat = new Obat();
                    obat.setPrescriptionId(resepObat.getPrescriptionId());
                    obat.setJumlah(resepObat.getQty());
                    obat.setSlug(resepObat.getSlug());
                    obat.setName(resepObat.getMedicinesName());
                    obat.setMaxPrices((long)0);
                    obat.setMinPrices((long)0);
                    obat.setUnit(resepObat.getUnit());
                    return obat;
                })
                .toList()
                .subscribe(obats -> {
                    dataManager.insertObat(obats);
                    mView.success();
                }, error -> mView.showErrorMsg(error.toString())));
    }
}
