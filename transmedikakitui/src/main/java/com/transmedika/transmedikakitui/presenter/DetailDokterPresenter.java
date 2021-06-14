package com.transmedika.transmedikakitui.presenter;

import android.content.Context;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.DetailDoctorContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;


public class DetailDokterPresenter extends RxPresenter<DetailDoctorContract.View>
        implements DetailDoctorContract.Presenter {
    private final DataManager dataManager;

    public DetailDokterPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(DetailDoctorContract.View view) {
        super.attachView(view);
    }


    @Override
    public void detailDokter(String auth, String uuidDokter, Context context) {
        addSubscribe(dataManager.detailDoctor(Constants.BEARER+auth, uuidDokter)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<Doctor>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<Doctor> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.detailDokterResp(model);
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
