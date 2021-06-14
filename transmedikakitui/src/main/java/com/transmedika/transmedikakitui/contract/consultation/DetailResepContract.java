package com.transmedika.transmedikakitui.contract.consultation;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.bean.json.Resep;
import com.transmedika.transmedikakitui.models.bean.json.ResepObat;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;

import java.util.List;

public interface DetailResepContract {
    interface View extends BaseView {
        void detailResepResp(BaseResponse<Resep> response);
        void success();
        void donwloadResepResp(String path);
    }

    interface Presenter extends BasePresenter<View> {
        void order(List<ResepObat> listFlowable);
        void detailResep(String auth, Long id, Context context);
        void downLoadResep(String auth, String idResep, Context context);
        SignIn selectLogin();
        void insertObat(Obat obat);
    }
}
