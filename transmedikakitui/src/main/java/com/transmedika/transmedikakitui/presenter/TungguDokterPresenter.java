package com.transmedika.transmedikakitui.presenter;

import android.content.Context;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.consultation.TungguDokterContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.StatusKonsultasiParam;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;


public class TungguDokterPresenter extends RxPresenter<TungguDokterContract.View>
        implements TungguDokterContract.Presenter {

    private final DataManager mDataManager;
    private static final int COUNT_DOWN_TIME = 1000;

    public TungguDokterPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
        this.realm = mDataManager.getRealm();
    }

    @Override
    public void statusKonsultasi(String auth, Long idKonsultasi, StatusKonsultasiParam param, Context context) {
        addSubscribe(mDataManager.statusKonsultasi(Constants.BEARER+auth,idKonsultasi, param)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseOResponse>(mView,context, true){
                    @Override
                    public void onNext(BaseOResponse model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.statusKonsultasiResp(model);
                            }else{
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public SignIn selectLogin() {
        return mDataManager.selectLogin();
    }
}
