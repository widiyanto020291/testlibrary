package com.transmedika.transmedikakitui.presenter.consultation;

import android.content.Context;


import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.component.RxBus;
import com.transmedika.transmedikakitui.contract.consultation.SpesialisKlinikContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;

import java.util.List;



public class SpesialisKlinikPresenter extends RxPresenter<SpesialisKlinikContract.View>
        implements SpesialisKlinikContract.Presenter {
    private final DataManager dataManager;

    public SpesialisKlinikPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(SpesialisKlinikContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(BroadcastEvents.class)
                .compose(RxUtil.rxSchedulerHelper())
                .map(BroadcastEvents::getEvent)
                .subscribeWith(new CommonSubscriber<BroadcastEvents.Event>(mView, false, false) {
                    @Override
                    public void onNext(BroadcastEvents.Event event) {
                        mView.getBroadcastEvents(event);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        registerEvent();
                    }
                })
        );
    }


    @Override
    public void spesialis(String auth, Long id, Context context) {
        addSubscribe(dataManager.klinikSpesialis(Constants.BEARER+auth, id)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<List<Specialist>>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<List<Specialist>> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.spesialisResp(model);
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
