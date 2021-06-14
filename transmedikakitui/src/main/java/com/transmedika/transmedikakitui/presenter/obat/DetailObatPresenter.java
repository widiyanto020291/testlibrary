package com.transmedika.transmedikakitui.presenter.obat;

import com.transmedika.transmedikakitui.base.RxPresenter;
import com.transmedika.transmedikakitui.contract.obat.DetailObatContract;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.Obat;

public class DetailObatPresenter extends RxPresenter<DetailObatContract.View>
        implements DetailObatContract.Presenter {
    private final DataManager dataManager;

    public DetailObatPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.realm = dataManager.getRealm();
    }

    @Override
    public void attachView(DetailObatContract.View view) {
        super.attachView(view);
    }

    @Override
    public void insertObat(Obat obat) {
        dataManager.insertObat(obat);
    }

    @Override
    public void deleteObat(String slug) {
        dataManager.deleteObat(slug);
    }

}
