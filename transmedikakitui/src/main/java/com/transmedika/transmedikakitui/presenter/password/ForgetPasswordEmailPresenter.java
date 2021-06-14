package com.transmedika.transmedikakitui.presenter.password;

import android.content.Context;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.password.ForgetPasswordEmailContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.LupaPasswordParam;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;


public class ForgetPasswordEmailPresenter extends RxPresenter<ForgetPasswordEmailContract.View> implements ForgetPasswordEmailContract.Presenter {

    private final DataManager dataManager;

    public ForgetPasswordEmailPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(ForgetPasswordEmailContract.View view) {
        super.attachView(view);
    }


    @Override
    public void sendEmail(Context context, String email) {
        LupaPasswordParam param = new LupaPasswordParam();
        param.setEmail(email);
        final String bearer;
        if (dataManager.selectLogin() != null) {
            bearer = Constants.BEARER + dataManager.selectLogin().getaUTHTOKEN();
        } else {
            bearer = Constants.BEARER;
        }
        addSubscribe(dataManager.forgetPasswordEmail(bearer, param)
                .compose(RxUtil.rxSchedulerHelper()).subscribeWith(new CommonSubscriber<BaseOResponse>(mView, context, true) {
                    @Override
                    public void onNext(BaseOResponse model) {
                        if (model != null) {
                            if (model.getCode().equals(Constants.SUCCESS_API)) {
                                boolean result;
                                if (model.getData() instanceof Boolean) {
                                    result = ((Boolean) model.getData());
                                } else {
                                    result = model.getSuccess();
                                }
                                mView.onSuccessSendEmail(result);
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
