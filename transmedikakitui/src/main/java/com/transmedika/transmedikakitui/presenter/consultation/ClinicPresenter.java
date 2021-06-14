package com.transmedika.transmedikakitui.presenter.consultation;

import android.content.Context;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.component.RxBus;
import com.transmedika.transmedikakitui.contract.consultation.ClinicContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BasePage;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Clinic;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;

import java.util.List;



public class ClinicPresenter extends RxPresenter<ClinicContract.View>
        implements ClinicContract.Presenter {
    private final DataManager dataManager;

    public ClinicPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(ClinicContract.View view) {
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
    public void klinik(String auth, String key, Context context) {
        addSubscribe(dataManager.klinik(Constants.BEARER+auth, key, Constants.PERPAGE)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<BasePage<List<Clinic>>>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<BasePage<List<Clinic>>> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                if(model.getData()!=null && model.getData().getData()!=null && model.getData().getData().size() > 0) {
                                    mView.klinikResp(model);
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
    public void klinikDynamic(String auth, String url, Context context) {
        addSubscribe(dataManager.klinikDynamic(Constants.BEARER+auth, url)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<BasePage<List<Clinic>>>>(mView,context, true, false){
                    @Override
                    public void onNext(BaseResponse<BasePage<List<Clinic>>> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                if(model.getData()!=null && model.getData().getData()!=null && model.getData().getData().size() > 0) {
                                    mView.klinikDynamicResp(model);
                                }else {
                                    mView.onDataIsEmpty();
                                }
                            }else{
                                super.onNext(model);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.klinikDynamicError();
                    }
                }));
    }

    @Override
    public SignIn selectLogin() {
        return dataManager.selectLogin();
    }
}
