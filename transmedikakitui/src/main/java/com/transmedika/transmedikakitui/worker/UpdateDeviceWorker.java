package com.transmedika.transmedikakitui.worker;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subscribers.ResourceSubscriber;
import io.realm.Realm;

import static com.transmedika.transmedikakitui.utils.TransmedikaUtils.getRealm;


public class UpdateDeviceWorker extends Worker {

    private final DataManager mDataManager;
    private Disposable disposable;
    private final WorkerParameters workerParameters;
    private Realm mRealm;

    public UpdateDeviceWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        this.workerParameters = workerParams;
        this.mDataManager = DataManager.getDataManagerInstance(appContext);
    }



    @NonNull
    @Override
    public Result doWork() {
        String deviceId = this.workerParameters.getInputData().getString(Constants.DATA);
        new Handler(Looper.getMainLooper()).post(() -> {
                mRealm = getRealm();
                mRealm.executeTransactionAsync(realm -> {
                    SignIn bean = realm.where(SignIn.class).findFirst();
                if(bean!=null) {
                    disposable = mDataManager.updateDeviceId(deviceId, Constants.BEARER.concat(bean.getaUTHTOKEN()))
                            .compose(RxUtil.rxSchedulerHelper())
                            .subscribeWith(new ResourceSubscriber<BaseResponse<SignIn>>() {

                                @Override
                                public void onNext(BaseResponse<SignIn> model) {
                                    Log.i(UpdateDeviceWorker.class.getSimpleName(), "Update deviceId Sukses");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(UpdateDeviceWorker.class.getSimpleName(), "Update deviceId Gagal ".concat(e.toString()));
                                }

                                @Override
                                public void onComplete() {
                                    if (!disposable.isDisposed()) {
                                        disposable.dispose();
                                    }
                                }
                            });
                }
            });
        });
        return Result.success();
    }



    @Override
    public void onStopped() {
        if(disposable!=null)
            disposable.isDisposed();

        if(mRealm!=null)
            mRealm.close();
        super.onStopped();
    }
}
