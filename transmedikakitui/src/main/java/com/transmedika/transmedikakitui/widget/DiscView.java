package com.transmedika.transmedikakitui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Voucher;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.util.Objects;


public class DiscView extends LinearLayout {
    View view;
    NetkromEditText mEdPromo;
    NetkromTextView mTvBiaya;
    NetkromTextView mTvDisc;
    NetkromTextView mTvTotal;
    NetkromTextView mTvLblDisc1;
    NetkromTextView mTvLblDisc2;
    LinearLayout llDisc;
    private Long potongan = (long)0, estimasi;
    private DiscListener discListener;
    TransmedikaSettings transmedikaSettings;

    public DiscView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public DiscView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DiscView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context){
        transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);
        view = inflate(getContext(), R.layout.disc_view, this);
        mEdPromo = view.findViewById(R.id.ed_promo);
        mTvBiaya = view.findViewById(R.id.tv_biaya);
        mTvDisc = view.findViewById(R.id.tv_disc);
        mTvTotal = view.findViewById(R.id.tv_total);
        mTvLblDisc1 = view.findViewById(R.id.tv_lbl_disc1);
        mTvLblDisc2 = view.findViewById(R.id.tv_lbl_disc2);
        llDisc = view.findViewById(R.id.ll_disc);
        mEdPromo.setOnClickListener(v -> dialogDisc(context));
    }

    private void dialogDisc(Context context){
        View view = View.inflate(getContext(), R.layout.dialog_voucher, null);
        NetkromEditText mCodeVoucher = view.findViewById(R.id.ed_code_voucher);
        NetkromButton btnGunakan = view.findViewById(R.id.btn_gunakan);
        mCodeVoucher.setText(Objects.requireNonNull(mEdPromo.getText()).toString());
        if(transmedikaSettings.getButtonPrimaryBackground()!=null) {
           btnGunakan.setBackground(ResourcesCompat.getDrawable(getResources(),
                    TransmedikaUtils.setDrawable(context, transmedikaSettings.getButtonPrimaryBackground()), null));
        }
        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.DialogStyle);
        btnGunakan.setOnClickListener(v -> {
            //hideSoftInput();
            if (validVoucher(mCodeVoucher)) {
                dialog.dismiss();
                this.mEdPromo.setText(Objects.requireNonNull(mCodeVoucher.getText()).toString());
                discListener.voucher(mCodeVoucher);
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    private boolean validVoucher(NetkromEditText mEdCodeVoucher){
        boolean valid = true;
        if(TextUtils.isEmpty(Objects.requireNonNull(mEdCodeVoucher.getText()).toString())){
            valid = false;
            mEdCodeVoucher.requestFocus();
            /*MsgUiUtil.showSnackBar(((ViewGroup) Objects.requireNonNull((View) findViewById(android.R.id.content))).getChildAt(0),
                    "Masukan kode voucher",getContext(), R.drawable.ic_check_24dp, R.color.green, Snackbar.LENGTH_LONG);*/
        }
        return valid;
    }

    public void showData(){
        mTvBiaya.setText(TransmedikaUtils.numberFormat().format(estimasi));
        mTvDisc.setText(TransmedikaUtils.numberFormat().format(potongan));
        mTvTotal.setText(TransmedikaUtils.numberFormat().format(estimasi));
        if(discListener!=null)
            discListener.updateHarga(estimasi, null, 0);
    }

    public void setDataDisc(BaseResponse<Voucher> response){
        if(response.getData()!=null){
            mTvLblDisc1.setVisibility(View.VISIBLE);
            mTvLblDisc1.setTextColor(ContextCompat.getColor(getContext(),R.color.green));
            if(response.getData().getType().equals(1)){ // nominal
                mTvLblDisc2.setText(getContext().getString(R.string.potongan));
                mTvDisc.setText(TransmedikaUtils.numberFormat().format(response.getData().getNominal()));
                potongan = response.getData().getNominal();
                mTvTotal.setText(TransmedikaUtils.numberFormat().format(estimasi - potongan));
                mTvLblDisc1.setText(getContext().getString(R.string.menghemat).concat(TransmedikaUtils.numberFormat().format(response.getData().getNominal())));
                if(discListener!=null)
                    discListener.updateHarga(estimasi, response.getData().getCode(), potongan);
            }

            if(response.getData().getType().equals(2)){ // persen
                mTvLblDisc2.setText(getContext().getString(R.string.potongan).concat(String.valueOf(response.getData().getNominal())).concat("%"));
                mTvDisc.setText(TransmedikaUtils.numberFormat().format((estimasi * response.getData().getNominal()) / 100));
                potongan = (estimasi * response.getData().getNominal()) / 100;
                mTvTotal.setText(TransmedikaUtils.numberFormat().format(estimasi - potongan));
                mTvLblDisc1.setText(getContext().getString(R.string.menghemat).concat(TransmedikaUtils.numberFormat().format(potongan)));
                if(discListener!=null)
                    //discListener.updateHarga(estimasi - potongan,response.getData().getCode(),potongan);
                    discListener.updateHarga(estimasi,response.getData().getCode(),potongan);
            }
        }else {
            potongan = (long)0;
            mTvLblDisc2.setText(getContext().getString(R.string.potongan));
            mTvDisc.setText(TransmedikaUtils.numberFormat().format(0));
            mTvTotal.setText(TransmedikaUtils.numberFormat().format(estimasi));
            mTvLblDisc1.setVisibility(View.VISIBLE);
            mTvLblDisc1.setText(response.getMessages());
            mTvLblDisc1.setTextColor(ResourcesCompat.getColor(getResources(),R.color.red_dark, null));
            if(discListener!=null)
                discListener.updateHarga(estimasi, null, 0);
        }
    }

    public Long getPotongan() {
        return potongan;
    }

    public Long getEstimasi() {
        return estimasi;
    }

    public void setEstimasi(Long estimasi) {
        this.estimasi = estimasi;
    }

    public void setDiscListener(DiscListener discListener) {
        this.discListener = discListener;
    }

    public interface DiscListener{
        void updateHarga(long estimasi, String kodeVoucher, long totalVoucher);
        void voucher(NetkromEditText voucherCode);
    }

    public LinearLayout getLlDisc() {
        return llDisc;
    }
}
