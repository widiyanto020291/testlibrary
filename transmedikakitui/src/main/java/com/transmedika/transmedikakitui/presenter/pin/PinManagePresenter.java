package com.transmedika.transmedikakitui.presenter.pin;

import android.content.Context;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.pin.PinManageContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.PinParam;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;

public class PinManagePresenter extends RxPresenter<PinManageContract.View>
        implements PinManageContract.Presenter {

    private final DataManager dataManager;

    public PinManagePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(PinManageContract.View view) {
        super.attachView(view);
    }

    @Override
    public void setPin(Context context, String pin) {
        addSubscribe(dataManager.putPin(Constants.BEARER + dataManager.selectLogin().getaUTHTOKEN(), new PinParam(pin))
                .compose(RxUtil.rxSchedulerHelper()).subscribeWith(new CommonSubscriber<BaseOResponse>(mView, context, true) {
                    @Override
                    public void onNext(BaseOResponse model) {
                        if (model != null) {
                            if (model.getCode().equals(Constants.SUCCESS_API)) {
                                Boolean result = (Boolean) model.getData();
                                if (result && !selectLogin().getPinStatus()) {
                                    SignIn signIn = selectLogin();
                                    signIn.setPinStatus(true);
                                    dataManager.insertLogin(signIn);
                                }
                                mView.onSetPin(result);
                            } else {
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void changePin(Context context, String oldPin, String newPin) {
        addSubscribe(dataManager.putChangePin(Constants.BEARER + dataManager.selectLogin().getaUTHTOKEN(), new PinParam(oldPin, newPin))
                .compose(RxUtil.rxSchedulerHelper()).subscribeWith(new CommonSubscriber<BaseOResponse>(mView, context, true) {
                    @Override
                    public void onNext(BaseOResponse model) {
                        if (model != null) {
                            if (model.getCode().equals(Constants.SUCCESS_API)) {
                                mView.onUbahPin((Boolean) model.getData());
                            } else {
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

//    @Override
//    public void changePinConfirm(Context context, String oldPin) {
//        CommonSubscriber<BaseResponse> checkPin = new CommonSubscriber<BaseResponse>(mView, context, true, false) {
//            @Override
//            public void onNext(BaseResponse model) {
//                if (model != null) {
//                    if (model.getCode().equals(Constants.SUCCESS_API)) {
//                        mView.onUbahConfirm((Boolean) model.getData());
//                    } else {
//                        super.onNext(model);
//                    }
//                }
//            }
//        };
//
//        addSubscribe(dataManager.checkPin(Constants.BEARER + dataManager.selectLogin().getaUTHTOKEN(), new PinParam(oldPin))
//                .compose(RxUtil.rxSchedulerHelper()).subscribeWith(checkPin));
//    }

    @Override
    public void confirmPin(Context context, String pin) {
        addSubscribe(dataManager.checkPin(Constants.BEARER + dataManager.selectLogin().getaUTHTOKEN(), new PinParam(pin))
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
