package com.transmedika.transmedikakitui.presenter.obat;

import android.content.Context;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.component.RxBus;
import com.transmedika.transmedikakitui.contract.obat.ObatContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BasePage;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;

import java.util.List;



public class ObatPresenter extends RxPresenter<ObatContract.View>
        implements ObatContract.Presenter {
    private final DataManager dataManager;

    public ObatPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(ObatContract.View view) {
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
    public void cariObat(String auth, String key, Context context) {
        addSubscribe(dataManager.obatOptions(Constants.BEARER+auth, key, Constants.PERPAGE)
                .compose(RxUtil.rxSchedulerHelper())
                .map(model -> {
                    if(model!=null && model.getData()!=null && model.getData().getData()!=null && model.getData().getData().size() > 0) {
                        List<Obat> obatsremote = model.getData().getData();
                        for (int i = 0; i < obatsremote.size(); i++) {
                            Obat obatRemote = obatsremote.get(i);
                            List<Obat> obatsLocal = dataManager.selectObat();
                            if (obatsLocal!=null && obatsLocal.size() > 0) {
                                for (int x = 0; x < obatsLocal.size(); x++) {
                                    Obat obatLocal = obatsLocal.get(x);
                                    if (obatLocal.equals(obatRemote)) {
                                        obatRemote.setJumlah(obatLocal.getJumlah());
                                        obatsremote.set(i, obatRemote);
                                        break;
                                    }
                                }
                            }
                        }
                        model.getData().setData(obatsremote);
                    }
                    return model;
                })
                .subscribeWith(new CommonSubscriber<BaseResponse<BasePage<List<Obat>>>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<BasePage<List<Obat>>> model) {
                        if (model != null) {
                            if (model.getCode().equals(Constants.SUCCESS_API)) {
                                if (model.getData() != null && model.getData().getData() !=null && model.getData().getData().size() > 0) {
                                    mView.cariObatResp(model);
                                } else {
                                    mView.onDataIsEmpty();
                                }
                            } else {
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void obat(String auth, String categoryId, Context context) {
        addSubscribe(dataManager.obat(Constants.BEARER+auth, categoryId, Constants.PERPAGE)
                .compose(RxUtil.rxSchedulerHelper())
                .map(model -> {
                    if(model!=null && model.getData()!=null && model.getData().getData()!=null && model.getData().getData().size() > 0) {
                        List<Obat> obatsremote = model.getData().getData();
                        for (int i = 0; i < obatsremote.size(); i++) {
                            Obat obatRemote = obatsremote.get(i);
                            List<Obat> obatsLocal = dataManager.selectObat();
                            if (obatsLocal!=null && obatsLocal.size() > 0) {
                                for (int x = 0; x < obatsLocal.size(); x++) {
                                    Obat obatLocal = obatsLocal.get(x);
                                    if (obatLocal.equals(obatRemote)) {
                                        obatRemote.setJumlah(obatLocal.getJumlah());
                                        obatsremote.set(i, obatRemote);
                                        break;
                                    }
                                }
                            }
                        }
                        model.getData().setData(obatsremote);
                    }
                    return model;
                })
                .subscribeWith(new CommonSubscriber<BaseResponse<BasePage<List<Obat>>>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<BasePage<List<Obat>>> model) {
                        if (model != null) {
                            if (model.getCode().equals(Constants.SUCCESS_API)) {
                                if (model.getData() != null && model.getData().getData() !=null && model.getData().getData().size() > 0) {
                                    mView.obatResp(model);
                                } else {
                                    mView.onDataIsEmpty();
                                }
                            } else {
                                super.onNext(model);
                            }
                        }
                    }
                }));
    }

    @Override
    public void obatDynamic(String auth, String url, Context context) {
        addSubscribe(dataManager.obatDynamic(Constants.BEARER+auth, url)
                .compose(RxUtil.rxSchedulerHelper())
                .map(model -> {
                    if(model!=null && model.getData()!=null && model.getData().getData()!=null && model.getData().getData().size() > 0) {
                        List<Obat> obatsremote = model.getData().getData();
                        for (int i = 0; i < obatsremote.size(); i++) {
                            Obat obatRemote = obatsremote.get(i);
                            List<Obat> obatsLocal = dataManager.selectObat();
                            if (obatsLocal!=null && obatsLocal.size() > 0) {
                                for (int x = 0; x < obatsLocal.size(); x++) {
                                    Obat obatLocal = obatsLocal.get(x);
                                    if (obatLocal.equals(obatRemote)) {
                                        obatRemote.setJumlah(obatLocal.getJumlah());
                                        obatsremote.set(i, obatRemote);
                                        break;
                                    }
                                }
                            }
                        }
                        model.getData().setData(obatsremote);
                    }
                    return model;
                })
                .subscribeWith(new CommonSubscriber<BaseResponse<BasePage<List<Obat>>>>(mView,context, true, false){
                    @Override
                    public void onNext(BaseResponse<BasePage<List<Obat>>> model) {
                        if (model != null) {
                            if (model.getCode().equals(Constants.SUCCESS_API)) {
                                if (model.getData() != null && model.getData().getData() !=null && model.getData().getData().size() > 0) {
                                    mView.obatDynamicResp(model);
                                } else {
                                    mView.onDataIsEmpty();
                                }
                            } else {
                                super.onNext(model);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.articleDynamicError();
                    }
                }));
    }

    @Override
    public void insertObat(Obat obat) {
        dataManager.insertObat(obat);
    }

    @Override
    public void deleteObat(String slug) {
        dataManager.deleteObat(slug);
    }

    @Override
    public SignIn selectLogin() {
        return dataManager.selectLogin();
    }
}
