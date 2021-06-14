package com.transmedika.transmedikakitui.presenter.consultation;

import android.content.Context;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.component.RxBus;
import com.transmedika.transmedikakitui.contract.consultation.SpecialistContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;

import java.util.List;


public class SpecialistPresenter extends RxPresenter<SpecialistContract.View>
        implements SpecialistContract.Presenter {
    private final DataManager dataManager;

    public SpecialistPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(SpecialistContract.View view) {
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
    public void specialist(String auth, Context context) {
        addSubscribe(dataManager.specialist(Constants.BEARER+auth)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<List<Specialist>>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<List<Specialist>> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                if(model.getData()!=null && model.getData().size() > 0) {
                                    mView.specialistResp(model);
                                }else {
                                    mView.onDataIsEmpty();
                                }
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
