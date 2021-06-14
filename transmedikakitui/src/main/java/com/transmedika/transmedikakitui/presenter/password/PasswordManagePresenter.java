package com.transmedika.transmedikakitui.presenter.password;

import android.content.Context;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.password.PasswordManageContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.PasswordParam;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import retrofit2.Response;


public class PasswordManagePresenter extends RxPresenter<PasswordManageContract.View> implements PasswordManageContract.Presenter {

    private final DataManager dataManager;

    public PasswordManagePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(PasswordManageContract.View view) {
        super.attachView(view);
    }

    @Override
    public void setPassword(Context context, String password) {
        PasswordParam passwordParam = new PasswordParam(password, password);
        passwordParam.setUuid(selectLogin().getUuid());
        final String bearer;
        if (dataManager.selectLogin() != null) {
            bearer = Constants.BEARER + dataManager.selectLogin().getaUTHTOKEN();
        } else {
            bearer = Constants.BEARER;
        }
        addSubscribe(dataManager.putPassword(bearer, passwordParam)
                .compose(RxUtil.rxSchedulerHelper()).subscribeWith(new CommonSubscriber<BaseOResponse>(mView, context, true) {
                    @Override
                    public void onNext(BaseOResponse model) {
                        if (model != null) {
                            if (model.getCode().equals(Constants.SUCCESS_API)) {
                                if (model.getData() != null) {
                                    dataManager.deleteAllTables();
                                    mView.onSetPassword(true);
                                }
                            } else {
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void changePassword(Context context, String oldPassword, String newPassword) {
        PasswordParam passwordParam = new PasswordParam(oldPassword, newPassword, newPassword);
        passwordParam.setPhoneNumber(selectLogin().getPhoneNumber());
        addSubscribe(dataManager.putChangePassword(Constants.BEARER + dataManager.selectLogin().getaUTHTOKEN(), passwordParam)
                .compose(RxUtil.rxSchedulerHelper()).subscribeWith(new CommonSubscriber<Response<BaseResponse<Object>>>(mView, context, true) {
                    @Override
                    public void onNext(Response<BaseResponse<Object>> response) {
                        if (response != null) {
                            BaseResponse<Object> model = response.body();
                            if (model != null) {
                                if (model.getCode().equals(Constants.SUCCESS_API)) {
                                    String cookie = TransmedikaUtils.cookie(response.headers());
                                    if (model.getData() != null && model.getData() instanceof String) {
                                        mView.onUbahPassword(((String) model.getData()), cookie);
                                    } else {
                                        mView.onUbahPassword(null, cookie);
                                    }
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
    public void deleteAllTable() {
        dataManager.deleteAllTables();
    }

    @Override
    public void confirmPassword(Context context, String password) {
        addSubscribe(dataManager.checkPassword(Constants.BEARER + dataManager.selectLogin().getaUTHTOKEN(), new PasswordParam(password))
                .compose(RxUtil.rxSchedulerHelper()).subscribeWith(new CommonSubscriber<BaseOResponse>(mView, context, true) {
                    @Override
                    public void onNext(BaseOResponse model) {
                        if (model != null) {
                            if (model.getCode().equals(Constants.SUCCESS_API)) {
                                mView.onConfirm((Boolean) model.getData());
                            } else {
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public SignIn selectLogin() {
        return dataManager.selectLogin();
    }
}
