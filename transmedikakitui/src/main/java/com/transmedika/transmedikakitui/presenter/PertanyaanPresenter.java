package com.transmedika.transmedikakitui.presenter;

import android.content.Context;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.PertanyaanContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.PertanyaanResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.FormParam;
import com.transmedika.transmedikakitui.models.bean.realm.TempJawaban;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;

import java.util.List;


public class PertanyaanPresenter extends RxPresenter<PertanyaanContract.View>
        implements PertanyaanContract.Presenter {
    private final DataManager dataManager;

    public PertanyaanPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(PertanyaanContract.View view) {
        super.attachView(view);
    }

    @Override
    public void pertanyaan(String auth, FormParam formParam, Context context) {
        addSubscribe(dataManager.klinikform(Constants.BEARER+auth, formParam)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<List<PertanyaanResponse>>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<List<PertanyaanResponse>> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                if(model.getData()!=null && model.getData()!=null && model.getData().size() > 0) {
                                    mView.pertanyaanResp(model);
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
    public TempJawaban getTempJawaban(Long klinikId, String spesialisSlug) {
        return dataManager.getJawabansTemp(klinikId, spesialisSlug);
    }

    @Override
    public void insertTempJawaban(TempJawaban tempJawaban) {
        dataManager.insertJawabansTemp(tempJawaban);
    }

    @Override
    public SignIn selectLogin() {
        return dataManager.selectLogin();
    }
}
