package com.transmedika.transmedikakitui.contract.consultation;

import android.content.Context;

import com.transmedika.transmedikakitui.base.BasePresenter;
import com.transmedika.transmedikakitui.base.BaseView;
import com.transmedika.transmedikakitui.models.bean.join.KeluargaJoin;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Konsultasi;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Voucher;
import com.transmedika.transmedikakitui.models.bean.json.param.KonsultasiKlinikParam;
import com.transmedika.transmedikakitui.models.bean.json.param.KonsultasiParam;
import com.transmedika.transmedikakitui.models.bean.json.param.VoucherParam;

public interface DetailDokterKonsultasiContract {
    interface View extends BaseView {
        void konsultasiResp(BaseResponse<Konsultasi> response);
        void konsultasiKlinikResp(BaseResponse<Konsultasi> response);
        void keluargaResp(KeluargaJoin join);
        void voucherResp(BaseResponse<Voucher> response);
    }

    interface Presenter extends BasePresenter<View> {
        void konsultasi(String auth, KonsultasiParam param, Context context);
        void konsultasiKlinik(String auth, KonsultasiKlinikParam param, Context context);
        void keluarga(String auth, String uuidDokter, Context context);
        void voucher(String auth, VoucherParam param, Context context);
        SignIn selectLogin();
    }
}
