package com.transmedika.transmedikakitui.presenter.obat;

import android.Manifest;
import android.content.Context;

import com.tbruyelle.rxpermissions3.RxPermissions;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.obat.HasilCariObatContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.CariObat;
import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.bean.json.Obat2;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.CariObatParam;
import com.transmedika.transmedikakitui.utils.CommonSubscriber;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;

import java.util.List;


public class HasilCariObatPresenter extends RxPresenter<HasilCariObatContract.View>
        implements HasilCariObatContract.Presenter {
    private final DataManager dataManager;

    public HasilCariObatPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(HasilCariObatContract.View view) {
        super.attachView(view);
    }


    @Override
    public void cariObat(String auth, CariObatParam param, Context context) {
        addSubscribe(dataManager.cariObat(Constants.BEARER+auth, param)
                .compose(RxUtil.rxSchedulerHelper())
                .map(model -> {
                    if(model.getData()!=null && model.getData().getObats().size() > 0) {
                        List<Obat2> obatsremote = model.getData().getObats();
                        boolean yes = false;
                        for (int i = 0; i < obatsremote.size(); i++) {
                            Obat2 obatRemote = obatsremote.get(i);
                            Obat obatsLocal = dataManager.selectObat(obatRemote.getSlug());
                            if(obatsLocal!=null){
                                if(obatRemote.getAvailable() && (obatsLocal.getJumlah() > (obatRemote.getStock()))){
                                    obatRemote.setPerbaharui(true);
                                    yes = true;
                                }
                            }

                        }
                        if(yes) {
                            model.getData().setPerbaharui(yes);
                        }
                        model.getData().setObats(obatsremote);
                    }
                    return model;
                })
                .subscribeWith(new CommonSubscriber<BaseResponse<CariObat>>(mView,context, true){
                    @Override
                    public void onNext(BaseResponse<CariObat> model) {
                        if (model != null) {
                            if (model.getCode().equals(Constants.SUCCESS_API)) {
                                if (model.getData() != null && model.getData().getObats().size() > 0) {
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
    public void insertObat(Obat obat) {
        dataManager.insertObat(obat);
    }

    @Override
    public void deleteObat(String slug) {
        dataManager.deleteObat(slug);
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
    public String getCatatanOrder() {
        return dataManager.getCatatanOrder();
    }

    @Override
    public void setCatatanOrder(String b) {
        dataManager.setCatatanOrder(b);
    }

    @Override
    public void checkPermission(RxPermissions rxPermissions, Context context) {
        addSubscribe(rxPermissions.request(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        mView.jump();
                    } else {
                        mView.showErrorMsg(context.getString(R.string.izin_camera_strorage_1));
                    }
                })
        );
    }
}
