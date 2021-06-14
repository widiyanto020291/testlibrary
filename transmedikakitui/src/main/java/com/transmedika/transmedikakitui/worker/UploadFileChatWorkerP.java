package com.transmedika.transmedikakitui.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.TypeAdapter;
import com.parse.ParseQuery;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.component.RxBus;
import com.transmedika.transmedikakitui.models.bean.MessageTemp;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.parse.Message;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.models.http.RetrofitHelper;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subscribers.ResourceSubscriber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.HttpException;


public class UploadFileChatWorkerP extends Worker {

    private final RetrofitHelper retrofitHelper;
    private final WorkerParameters workerParameters;
    private Disposable disposable;
    private final Context context;
    private String path;

    public UploadFileChatWorkerP(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        this.context = appContext;
        this.workerParameters = workerParams;
        retrofitHelper = RetrofitHelper.getRetrofitHelperInstance(appContext);
    }

    @NonNull
    @Override
    public Result doWork() {
        Long konsultasiId = this.workerParameters.getInputData().getLong(Constants.DATA_KONSULTASI, 0);
        String msgId = this.workerParameters.getInputData().getString(Constants.DATA_MSG);
        this.path = Objects.requireNonNull(this.workerParameters.getInputData().getString(Constants.DATA));
        File file = new File(path);
        String type = this.workerParameters.getInputData().getString("type");

        File fileBukti;
        if ("image".equalsIgnoreCase(type)) {
            fileBukti = getTempFileImageForUpload(file);
            long fileSizeA = file.length() / 1024;
            long fileSizeB = fileBukti.length() / 1024;
            Log.d("COPY", "copyFile: SIZE " + fileSizeA + "To: " + fileSizeB);
        } else {
            fileBukti = file;
        }

        MultipartBody.Part partBukti = MultipartBody.Part.createFormData("file", TransmedikaUtils.getFileName(fileBukti),
                RequestBody.create(MediaType.parse("file/*"), fileBukti));
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), TransmedikaUtils.getFileName(fileBukti));
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(konsultasiId));
        disposable = retrofitHelper.uploadImage(this.workerParameters.getInputData().getString(Constants.DATA_PROFILE),name, partBukti, id)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new ResourceSubscriber<BaseResponse<String>>() {

                    @Override
                    public void onNext(BaseResponse<String> model) {
                        if(model.getCode().equals(Constants.SUCCESS_API)) {
                            query(msgId, konsultasiId, model.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e instanceof HttpException){
                            ResponseBody body = Objects.requireNonNull(((HttpException) e).response()).errorBody();
                            TypeAdapter<BaseOResponse> adapter = TransmedikaUtils.gsonBuilder().getAdapter(BaseOResponse.class);
                            try {
                                BaseOResponse errorParser = adapter.fromJson(Objects.requireNonNull(body).string());
                                query(msgId, konsultasiId, context.getString(R.string.error_http_2, ((HttpException) e).code(), errorParser.getMessages()));
                            } catch (IOException exc) {
                                query(msgId, konsultasiId, context.getString(R.string.error_http_2, ((HttpException) e).code(), e.getMessage()));
                                exc.printStackTrace();
                            }
                        }else {
                            query(msgId, konsultasiId, context.getString(R.string.dua_param_string,"Gagal Upload", e.getMessage()));
                        }
                    }

                    @Override
                    public void onComplete() {
                        if(!disposable.isDisposed()){
                            disposable.dispose();
                        }
                    }
                });

        return Result.success();
    }

    private File getTempFileImageForUpload(File oriFile) {
        String fileName = TransmedikaUtils.createMediaFileName("IMAGE", context.getString(R.string.app_name), TransmedikaUtils.getFileExtension(oriFile));
        File temp = TransmedikaUtils.createTempFile(context, fileName);
        try {
            if (temp.createNewFile()) {
                TransmedikaUtils.copyFile(context, oriFile, temp);
                return temp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return oriFile;
    }

    private void query(String msgId, Long konsultasiId, String url){
        ParseQuery<Message> queryChats = ParseQuery.getQuery(Message.class);
        queryChats.whereEqualTo("messageId", msgId);
        queryChats.whereEqualTo("consultation_id", String.valueOf(konsultasiId));
        queryChats.findInBackground((data, e) -> {
            if (e != null) {
                BroadcastEvents.Event event = new BroadcastEvents.Event();
                event.setObject(new MessageTemp(msgId,konsultasiId,url));
                event.setInitString(Constants.msg);
                RxBus.getDefault().post(new BroadcastEvents(event));
            }else {
                for (Message message : data) {
                    message.setText(url);
                    message.setFileLocal(path);
                    message.saveEventually();
                }
            }
        });
    }

    @Override
    public void onStopped() {
        super.onStopped();
        disposable.isDisposed();
    }

    /*@NonNull
    @Override
    public Single<Result> createWork() {
        File fileBukti = new File(Objects.requireNonNull(this.workerParameters.getInputData().getString(Constants.DATA)));
        MultipartBody.Part partBukti = MultipartBody.Part.createFormData("file", SystemUtil.getFileName(fileBukti),
                RequestBody.create(MediaType.parse("file/*"), fileBukti));
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), SystemUtil.getFileName(fileBukti));
        return dataManager.uploadImage(this.workerParameters.getInputData().getString(Constants.DATA_PROFILE),name, partBukti)
                .doOnSuccess(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Throwable {
                        String resul = result.toString();
                        Log.e("","");
                    }
                }).map(new Function<Result, Result>() {
                    @Override
                    public Result apply(Result result) throws Throwable {
                        return Result.success();
                    }
                }).onErrorReturn(new Function<Throwable, Result>() {
                    @Override
                    public Result apply(Throwable throwable) throws Throwable {
                        return Result.failure();
                    }
                });
    }*/


}
