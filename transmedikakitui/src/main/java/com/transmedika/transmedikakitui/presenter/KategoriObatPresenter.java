package com.transmedika.transmedikakitui.presenter;

import android.content.Context;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.KategoriObatContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.KategoriObat;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;

import java.util.List;



public class KategoriObatPresenter extends RxPresenter<KategoriObatContract.View>
        implements KategoriObatContract.Presenter {
    private final DataManager dataManager;

    public KategoriObatPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(KategoriObatContract.View view) {
        super.attachView(view);
    }


    @Override
    public void kategoriObat(String auth, Context context) {
        addSubscribe(dataManager.kategoriObat(Constants.BEARER+auth)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<List<KategoriObat>>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<List<KategoriObat>> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.kategoriObatResp(model);
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
