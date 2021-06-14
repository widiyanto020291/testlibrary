package com.transmedika.transmedikakitui.presenter;

import android.Manifest;
import android.content.Context;

import com.tbruyelle.rxpermissions3.RxPermissions;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.TambahAlamatContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.Alamat;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.AlamatParam;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;


public class TambahAlamatPresenter extends RxPresenter<TambahAlamatContract.View>
        implements  TambahAlamatContract.Presenter {

    private final DataManager mDataManager;
    public TambahAlamatPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
        this.realm = mDataManager.getRealm();
    }

    @Override
    public void attachView(TambahAlamatContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent(){

    }

    @Override
    public void checkPermissionLocation(RxPermissions rxPermissions, Context context) {
        addSubscribe(rxPermissions.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        mView.getMyLocation();
                    } else {
                        mView.showErrorMsg(context.getString(R.string.app_location_permissions));
                    }
                })
        );
    }

    @Override
    public void tambahAlamat(String auth, AlamatParam param, Context context) {
        addSubscribe(mDataManager.tambahAlamat(Constants.BEARER+auth, param)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<Alamat>>(mView, context,true){
                    @Override
                    public void onNext(BaseResponse<Alamat> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.tambahAlamatResp(model);
                            }else{
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void ubahAlamat(String auth, String id, AlamatParam param, Context context) {
        addSubscribe(mDataManager.ubahAlamat(Constants.BEARER+auth, id, param)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<Alamat>>(mView, context,true){
                    @Override
                    public void onNext(BaseResponse<Alamat> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.ubahAlamatResp(model);
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