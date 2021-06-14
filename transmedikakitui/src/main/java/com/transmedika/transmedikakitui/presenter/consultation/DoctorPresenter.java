package com.transmedika.transmedikakitui.presenter.consultation;

import android.content.Context;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.component.RxBus;
import com.transmedika.transmedikakitui.contract.consultation.DoctorContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.join.DokterJoin;
import com.transmedika.transmedikakitui.models.bean.json.BasePage;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.FilterFilter;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;


public class DoctorPresenter extends RxPresenter<DoctorContract.View>
        implements DoctorContract.Presenter {
    private final DataManager dataManager;

    public DoctorPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(DoctorContract.View view) {
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
    public void dokterJoin(String auth, String id, Long medicalFacilityId, Context context) {
        Flowable<BaseResponse<BasePage<List<Doctor>>>> doctor = dataManager.doctor(Constants.BEARER+auth,id, Constants.PERPAGE, medicalFacilityId, new FilterFilter())
                .compose(RxUtil.rxSchedulerHelper());

        Flowable<BaseResponse<FilterFilter>> filter = dataManager.filterDoctor(Constants.BEARER+auth)
                .compose(RxUtil.rxSchedulerHelper());


        addSubscribe(Flowable.zip(doctor, filter, DokterJoin::new)
                .subscribeWith(new CommonSubscriber<DokterJoin>(mView, context, true) {
                    @Override
                    public void onNext(DokterJoin model) {
                        if(model !=null) {
                            if (model.getDoctors().getCode().equals(Constants.SUCCESS_API) &&
                                    model.getFilters().getCode().equals(Constants.SUCCESS_API)) {
                                if(model.getDoctors()!=null && model.getDoctors().getData()!=null && model.getDoctors().getData().getData()!=null && model.getDoctors().getData().getData().size() > 0){
                                    mView.dokterJoinResp(model);
                                }else {
                                    mView.onDataIsEmpty();
                                }
                            }else {
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void dokter(String auth, String id, Long medicalFacilityId, FilterFilter filterFilter, Context context) {
        addSubscribe(dataManager.doctor(Constants.BEARER+auth,id, Constants.PERPAGE, medicalFacilityId, filterFilter)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<BasePage<List<Doctor>>>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<BasePage<List<Doctor>>> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                if(model.getData()!=null && model.getData().getData()!=null && model.getData().getData().size() > 0) {
                                    mView.dokterResp(model);
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
    public void dokterDynamic(String auth, String url, FilterFilter filterFilter, Context context) {
        addSubscribe(dataManager.doctorDynamic(Constants.BEARER+auth, url, filterFilter)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<BasePage<List<Doctor>>>>(mView,context, true, false){
                    @Override
                    public void onNext(BaseResponse<BasePage<List<Doctor>>> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                if(model.getData()!=null && model.getData().getData()!=null && model.getData().getData().size() > 0) {
                                    mView.dokterDynamicResp(model);
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
                        mView.doctorDynamicError();
                    }
                }));
    }

    @Override
    public SignIn selectLogin() {
        return dataManager.selectLogin();
    }
}
