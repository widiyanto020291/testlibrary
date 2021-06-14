package com.transmedika.transmedikakitui.presenter.consultation;

import android.content.Context;


import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.consultation.JawabanContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Jawaban;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;


public class JawabanPresenter extends RxPresenter<JawabanContract.View>
        implements JawabanContract.Presenter {
    private final DataManager dataManager;

    public JawabanPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(JawabanContract.View view) {
        super.attachView(view);
    }


    @Override
    public void jawaban(String auth, Long id, Context context) {
        addSubscribe(dataManager.klinikFormJawaban(Constants.BEARER+auth, id)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<Jawaban>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<Jawaban> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.jawabanResp(model);
                            }else{
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
