package com.transmedika.transmedikakitui.contract.obat;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.Obat;

public interface DetailObatContract {
    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<View> {
        void insertObat(Obat obat);
        void deleteObat(String slug);
    }
}
