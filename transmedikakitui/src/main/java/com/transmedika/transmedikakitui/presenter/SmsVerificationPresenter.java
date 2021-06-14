package com.transmedika.transmedikakitui.presenter;


import android.content.Context;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.SmsVerificationContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.OtpParam;
import com.transmedika.transmedikakitui.models.bean.json.param.SmsVerificationParam;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;


public class SmsVerificationPresenter extends RxPresenter<SmsVerificationContract.View>
        implements SmsVerificationContract.Presenter {
    private final DataManager dataManager;

    public SmsVerificationPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(SmsVerificationContract.View view) {
        super.attachView(view);
    }


    @Override
    public void smsVerification(SmsVerificationParam param, Context context) {
        addSubscribe(dataManager.smsVerification(param)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<SignIn>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<SignIn> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                SignIn signIn = model.getData();
                                dataManager.insertLogin(signIn);
                                mView.smsVerificationResp(model);
                            }else{
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void kirimUlangOtp(OtpParam param, Context context) {
        addSubscribe(dataManager.kirimUlangOtp(param)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseOResponse>(mView,context, true){
                    @Override
                    public void onNext(BaseOResponse model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.kirimUlangOtpresp(model);
                            }else{
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }
}
