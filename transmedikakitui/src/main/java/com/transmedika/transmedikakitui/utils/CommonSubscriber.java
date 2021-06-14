package com.transmedika.transmedikakitui.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.http.exception.ApiException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Objects;

import io.reactivex.rxjava3.subscribers.ResourceSubscriber;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public abstract class CommonSubscriber<T> extends ResourceSubscriber<T> {
    private static final String TAG = CommonSubscriber.class.getSimpleName();
    private BaseView mView;
    private boolean isShowErrorState = true;
    private Context mContext;
    private boolean showLoading = true;
    private TransmedikaSettings transmedikaSettings;

    protected CommonSubscriber(Context mContext){
        transmedikaSettings = TransmedikaUtils.transmedikaSettings(mContext);
    }

    protected CommonSubscriber(BaseView view, Context mContext, boolean isShowErrorState){
        this(mContext);
        this.mView = view;
        this.isShowErrorState = isShowErrorState;
        this.mContext = mContext;
    }

    public CommonSubscriber(BaseView mView, boolean isShowErrorState, boolean showLoading) {
        this.mView = mView;
        this.isShowErrorState = isShowErrorState;
        this.showLoading = showLoading;
    }

    public CommonSubscriber(BaseView mView, Context mContext, boolean isShowErrorState, boolean showLoading) {
        this(mContext);
        this.mContext = mContext;
        this.mView = mView;
        this.isShowErrorState = isShowErrorState;
        this.showLoading = showLoading;
    }

    public CommonSubscriber(BaseView mView, boolean isShowErrorState) {
        this.mView = mView;
        this.isShowErrorState = isShowErrorState;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(showLoading)
            mView.showLoading();
    }


    @Override
    public void onComplete() {
        hideLoading();
    }

    private void hideLoading(){
        if(isShowErrorState)
            mView.hideLoading();
    }

    @Override
    public void onNext(T t) {
        if (mView == null) {
            return;
        }

        if(t instanceof BaseResponse){
            Log.d(TAG, "base response");
        }
    }

    public void onErrorResponse(Response<BaseResponse<Object>> response) {
        try {
            final String error = Objects.requireNonNull(response.errorBody()).string();
            BaseOResponse baseResponse = new Gson().fromJson(error, BaseOResponse.class);
            if (baseResponse != null && baseResponse.getMessages() != null) {
                mView.showErrorMsg(baseResponse.getMessages());
            } else if (response.raw().code() == 500) {
                mView.showErrorMsg("Server error, code: " + response.raw().code());
            } else {
                throw new Exception(error);
            }
        } catch (Exception e) {
            onError(e);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mView == null) {
            return;
        }

        hideLoading();

        if (e instanceof ApiException) {
            mView.showErrorMsg(e.toString());
            Log.d(TAG, e.toString());
        } else if(e instanceof SocketTimeoutException) {
            mView.showErrorMsg(mContext.getString(R.string.request_timeout));
            Log.d(TAG, e.toString());
        } else if(e instanceof ConnectException) {
            mView.showErrorMsg(mContext.getString(R.string.check_network));
            Log.d(TAG, e.toString());
        } else if(e instanceof UnknownHostException){
            mView.showErrorMsg(mContext.getString(R.string.network_unknown_host));
            Log.d(TAG, e.toString());
        } else if(e instanceof SocketException){
            mView.showErrorMsg(mContext.getString(R.string.network_disconnected));
            Log.d(TAG, e.toString());
        } else if (e instanceof HttpException) {
            if(((HttpException) e).code() == Constants.CODE_404) {
                mView.showErrorMsg(mContext.getString(R.string.host_not_found, transmedikaSettings.getBaseUrl()));
            }else if(((HttpException) e).code() == Constants.CODE_500) {
                mView.showErrorMsg(e.toString());
            }else if(((HttpException) e).code() == Constants.CODE_401) {
                mView.sessionExpired();
                generalErrorMessage(e);
            }else {
                generalErrorMessage(e);
            }
            Log.d(TAG, e.toString());
        } else {
            mView.showErrorMsg(e.toString());
            Log.d(TAG, e.toString());
        }
    }

    private void generalErrorMessage(Throwable e){
        ResponseBody body = Objects.requireNonNull(((HttpException) e).response()).errorBody();
        TypeAdapter<BaseOResponse> adapter = TransmedikaUtils.gsonBuilder().getAdapter(BaseOResponse.class);
        try {
            BaseOResponse errorParser = adapter.fromJson(Objects.requireNonNull(body).string());
            mView.showErrorMsg(errorParser.getMessages());
        } catch (IOException exc) {
            mView.showErrorMsg(mContext.getString(R.string.error_http, ((HttpException) e).code(), e.getMessage()));
            exc.printStackTrace();
        }
    }
}