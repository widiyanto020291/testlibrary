package com.transmedika.transmedikakitui.presenter.obat;

import android.Manifest;
import android.content.Context;

import com.tbruyelle.rxpermissions3.RxPermissions;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.obat.ObatMainContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.bean.json.Obat2;
import com.transmedika.transmedikakitui.models.bean.json.Order;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.PesanObatParam;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class ObatMainPresenter extends RxPresenter<ObatMainContract.View>
        implements ObatMainContract.Presenter {
    private final DataManager dataManager;

    public ObatMainPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(ObatMainContract.View view) {
        super.attachView(view);
    }


    @Override
    public List<Obat> selectObat() {
        return dataManager.selectObat();
    }

    @Override
    public SignIn selectLogin() {
        return dataManager.selectLogin();
    }

    @Override
    public Obat selectObat(String slug) {
        return dataManager.selectObat(slug);
    }

    @Override
    public void insertObat(Obat obat) {
        dataManager.insertObat(obat);
    }

    @Override
    public void setCatatanOrder(String a) {
        dataManager.setCatatanOrder(a);
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
    public void pesanObat(String auth, PesanObatParam param, Context context) {
        addSubscribe(dataManager.pesanObat(Constants.BEARER+auth, param)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<Order>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<Order> model) {
                        if (model != null) {
                            if (model.getCode().equals(Constants.SUCCESS_API)) {
                                dataManager.setCatatanOrder(null);
                                dataManager.deleteObat();
                                mView.pesanResp(model);
                            } else {
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void pesanObatUpload(String auth, MultipartBody.Part[] file, RequestBody data, Context context) {
        addSubscribe(dataManager.pesanObatResepUpload(Constants.BEARER+auth, file, data)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<BaseResponse<Order>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<Order> model) {
                        if (model != null) {
                            if (model.getCode().equals(Constants.SUCCESS_API)) {
                                dataManager.setCatatanOrder(null);
                                dataManager.deleteObat();
                                mView.pesanUploadResp(model);
                            } else {
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    public void order(List<Obat2> listFlowable){
        addSubscribe(Flowable.fromIterable(listFlowable)
                .compose(RxUtil.rxSchedulerHelper())
                .map(obat2 -> {
                    if(obat2.getAvailable() && obat2.getStatus() && obat2.getJumlah() > obat2.getStock()){
                        obat2.setJumlah(obat2.getStock().intValue());
                        obat2.setPerbaharui(false);
                    }
                    return obat2;
                })
                .toList()
                .subscribe(obats -> {
                    List<Obat> obatList = new ArrayList<>();
                    for (Obat2 obat2:obats) {
                        if(obat2.getAvailable() && obat2.getStatus() && obat2.getJumlah() > obat2.getStock()) {
                            Obat obat = dataManager.selectObat(obat2.getSlug());
                            obat.setPrices(obat2.getPrice());
                            obat.setSlug(obat2.getSlug());
                            obat.setJumlah(obat2.getJumlah());
                            obat.setMinPrices(obat2.getMinPrices());
                            obat.setMaxPrices(obat2.getMaxPrices());
                            obatList.add(obat);
                        }
                    }
                    dataManager.insertObat(obatList);
                    mView.callBackList(obats);
                }, error -> mView.showErrorMsg(error.toString())));
    }
}
