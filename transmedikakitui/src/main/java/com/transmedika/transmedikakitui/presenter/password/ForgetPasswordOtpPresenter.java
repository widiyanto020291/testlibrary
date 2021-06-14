package com.transmedika.transmedikakitui.presenter.password;

import android.content.Context;


import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.password.ForgetPasswordOtpContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.LupaPasswordParam;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import retrofit2.Response;

public class ForgetPasswordOtpPresenter extends RxPresenter<ForgetPasswordOtpContract.View> implements ForgetPasswordOtpContract.Presenter {

    private final DataManager dataManager;

    public ForgetPasswordOtpPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(ForgetPasswordOtpContract.View view) {
        super.attachView(view);
    }

    @Override
    public void requestOtp(Context context, String noPonsel) {
        LupaPasswordParam param = new LupaPasswordParam();
        param.setPhoneNumber(noPonsel);
        String auth = dataManager.selectLogin() != null ? Constants.BEARER + dataManager.selectLogin().getaUTHTOKEN() : "";
        addSubscribe(dataManager.forgetPasswordRequestOtp(auth, param)
                .compose(RxUtil.rxSchedulerHelper()).subscribeWith(new CommonSubscriber<BaseOResponse>(mView, context, true) {
                    @Override
                    public void onNext(BaseOResponse model) {
                        if (model != null) {
                            if (model.getCode().equals(Constants.SUCCESS_API)) {
                                boolean result = false;
                                if (model.getData() instanceof Boolean) {
                                    result = ((Boolean) model.getData());
                                } else {
                                    result = model.getSuccess();
                                }
                                mView.onSuccessSendOtp(result);
                            } else {
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void requestOtp(Context context, String noPonsel, String password, String confirmPassword) {
        LupaPasswordParam param = new LupaPasswordParam();
        param.setPassword(password);
        param.setPasswordConfirmation(confirmPassword);
        param.setPhoneNumber(noPonsel);
        String auth = dataManager.selectLogin() != null ? Constants.BEARER + dataManager.selectLogin().getaUTHTOKEN() : "";
        addSubscribe(dataManager.forgetPasswordRequestOtpNew(auth, param)
                .compose(RxUtil.rxSchedulerHelper()).subscribeWith(new CommonSubscriber<Response<BaseResponse<Object>>>(mView, context, true) {
                    @Override
                    public void onNext(Response<BaseResponse<Object>> response) {
                        if (response != null) {
                            BaseResponse<Object> model = response.body();
                            if (model != null ) {
                                if (model.getCode().equals(Constants.SUCCESS_API)) {
                                    String cookie = TransmedikaUtils.cookie(response.headers());
                                    mView.onSuccessResetPasswordOtp((String) model.getData(), cookie);
                                } else {
                                    super.onNext(response);
                                }
                            } else {
                                onErrorResponse(response);
                            }
                        }
                    }
                }));
    }

    @Override
    public void deleteAllTables() {
        dataManager.deleteAllTables();
    }

    @Override
    public SignIn selectLogin() {
        return dataManager.selectLogin();
    }
}
