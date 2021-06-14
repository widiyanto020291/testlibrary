package com.transmedika.transmedikakitui.presenter;

import android.content.Context;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.AlamatContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.Alamat;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;

import java.util.List;


public class AlamatPresenter extends RxPresenter<AlamatContract.View>
        implements AlamatContract.Presenter {
    private final DataManager dataManager;

    public AlamatPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(AlamatContract.View view) {
        super.attachView(view);
    }


    @Override
    public void alamat(String auth, Context context) {
        addSubscribe(dataManager.alamat(Constants.BEARER+auth)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<List<Alamat>>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<List<Alamat>> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                if(model.getData()!=null && model.getData().size() > 0) {
                                    mView.alamatResp(model);
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
    public void hapusAlamat(String auth, String id, Context context) {
        addSubscribe(dataManager.hapusAlamat(Constants.BEARER+auth, id)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<Alamat>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<Alamat> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.hapusAlamatResp(model);
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
