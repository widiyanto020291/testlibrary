package com.transmedika.transmedikakitui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.models.bean.json.GLAccount;
import com.transmedika.transmedikakitui.utils.SpanUtils;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;


public class SaldoView extends LinearLayout {
    View view;
    NetkromTextView mTvSaldo;
    NetkromTextView mTvSaldoTidakCukup;
    RadioButton rbJenisBayar;
    ImageView imgDompet;
    private GLAccount glAccount;
    private Double gl;
    private SaloListener saloListener;
    private boolean saldoCukup;

    public SaldoView(Context context) {
        super(context);
        initView(context);
    }

    public SaldoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SaldoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        TransmedikaSettings transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);
        view = inflate(getContext(), R.layout.saldo_view, this);
        mTvSaldo = view.findViewById(R.id.tv_saldo);
        mTvSaldoTidakCukup = view.findViewById(R.id.tv_saldo_tidak_cukup);
        rbJenisBayar = view.findViewById(R.id.rb_jenis_bayar);
        imgDompet = view.findViewById(R.id.img_dompet);

        if(transmedikaSettings.getPocketIcon()!=null){
            imgDompet.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                    TransmedikaUtils.setDrawable(context,transmedikaSettings.getPocketIcon()),null));
        }
    }

    public void setShowData(GLAccount glAccount, double total, double potongan){
        if (glAccount.getGLAccountBalance() == null){
            glAccount.setGLAccountBalance("0");
        }
        saloListener.disableBtnV(false);
        this.glAccount = glAccount;
        gl = Double.valueOf(glAccount.getGLAccountBalance());
        mTvSaldo.setText(TransmedikaUtils.numberFormat().format(gl));
        cekSaldo(total, potongan);
    }


    public void cekSaldo(double total, double potongan){
        mTvSaldo.setText(TransmedikaUtils.numberFormat().format(gl));
        if(gl < (total - potongan)){
            mTvSaldoTidakCukup.setVisibility(View.VISIBLE);
            SpanUtils utils = new SpanUtils(getContext());
            utils.setSpan(mTvSaldoTidakCukup,getContext().getString(R.string.saldo_tidak_cukup),
                    getResources().getColor(R.color.red),getContext().getString(R.string.saldo_tidak_cukup_));
            saloListener.disableBtnV(true);
            saldoCukup = false;
        }else {
            saldoCukup = true;
            saloListener.disableBtnV(false);
        }
    }

    public void setSaloListener(SaloListener saloListener) {
        this.saloListener = saloListener;
    }

    public interface SaloListener{
        void disableBtnV(boolean b);
    }

    public RadioButton getRbJenisBayar() {
        return rbJenisBayar;
    }

    public GLAccount getGlAccount() {
        return glAccount;
    }

    public boolean isSaldoCukup() {
        return saldoCukup;
    }
}
