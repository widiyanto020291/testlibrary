package com.transmedika.transmedikakitui.presenter;

import android.content.Context;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.SignInContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.SignInParam;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;

public class SignInPresenter extends RxPresenter<SignInContract.View>
        implements SignInContract.Presenter {
    private final DataManager dataManager;

    public SignInPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }


    @Override
    public void attachView(SignInContract.View view) {
        super.attachView(view);
    }

    @Override
    public void signIn(String deviceId, SignInParam param, Context context) {
        addSubscribe(dataManager.signIn(deviceId, param)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<SignIn>>(mView, context, true) {
                    @Override
                    public void onNext(BaseResponse<SignIn> model) {
                        if (model != null) {
                            if (model.getCode().equals(Constants.SUCCESS_API)) {
                                SignIn signIn = model.getData();
                                dataManager.insertLogin(signIn);
                                mView.signIn(model);
                            } else {
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }
}
