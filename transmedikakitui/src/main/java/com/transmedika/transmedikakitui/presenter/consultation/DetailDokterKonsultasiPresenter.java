package com.transmedika.transmedikakitui.presenter.consultation;

import android.content.Context;


import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.consultation.DetailDokterKonsultasiContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.join.KeluargaJoin;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.GLAccount;
import com.transmedika.transmedikakitui.models.bean.json.Konsultasi;
import com.transmedika.transmedikakitui.models.bean.json.Profile;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Voucher;
import com.transmedika.transmedikakitui.models.bean.json.param.KonsultasiKlinikParam;
import com.transmedika.transmedikakitui.models.bean.json.param.KonsultasiParam;
import com.transmedika.transmedikakitui.models.bean.json.param.VoucherParam;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;


public class DetailDokterKonsultasiPresenter extends RxPresenter<DetailDokterKonsultasiContract.View>
        implements DetailDokterKonsultasiContract.Presenter {
    private final DataManager dataManager;

    public DetailDokterKonsultasiPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(DetailDokterKonsultasiContract.View view) {
        super.attachView(view);
    }


    @Override
    public void konsultasi(String auth, KonsultasiParam param, Context context) {
        addSubscribe(dataManager.konsultasi(Constants.BEARER+auth,param)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<Konsultasi>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<Konsultasi> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.konsultasiResp(model);
                            }else{
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void konsultasiKlinik(String auth, KonsultasiKlinikParam param, Context context) {
        addSubscribe(dataManager.konsultasiKlinik(Constants.BEARER+auth,param)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<Konsultasi>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<Konsultasi> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.konsultasiKlinikResp(model);
                            }else{
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void keluarga(String auth, String uuidDokter, Context context) {
        Flowable<BaseResponse<Profile>> profil = dataManager.profil(Constants.BEARER+auth)
                .compose(RxUtil.rxSchedulerHelper());

        Flowable<BaseResponse<List<Profile>>> keluarga = dataManager.keluarga(Constants.BEARER+auth)
                .compose(RxUtil.rxSchedulerHelper());

        Flowable<BaseResponse<Doctor>> dokter = dataManager.detailDoctor(Constants.BEARER+auth, uuidDokter)
                .compose(RxUtil.rxSchedulerHelper());

        Flowable<BaseResponse<GLAccount>> glAccount = dataManager.accountBalance(Constants.BEARER+auth)
                .compose(RxUtil.rxSchedulerHelper());

        addSubscribe(Flowable.zip(profil, keluarga, dokter, glAccount, KeluargaJoin::new)
                .subscribeWith(new CommonSubscriber<KeluargaJoin>(mView, context, true) {
            @Override
            public void onNext(KeluargaJoin model) {
                if(model !=null) {
                    if (model.getProfil().getCode().equals(Constants.SUCCESS_API) &&
                            model.getKeluargas().getCode().equals(Constants.SUCCESS_API) &&
                                model.getDokter().getCode().equals(Constants.SUCCESS_API) &&
                                    model.getGlAccount().getCode().equals(Constants.SUCCESS_API)) {
                        mView.keluargaResp(model);
                    }else {
                        super.onNext(model);
                    }
                }
            }
        }));
    }

    @Override
    public void voucher(String auth, VoucherParam param, Context context) {
        addSubscribe(dataManager.voucher(Constants.BEARER+auth, param)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<Voucher>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<Voucher> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                mView.voucherResp(model);
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
