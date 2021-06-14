package com.transmedika.transmedikakitui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.core.content.res.ResourcesCompat;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.models.bean.json.JenisPembayaran;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;


public class OtherPaymentView extends LinearLayout {
    View view;
    RelativeLayout rlLine;
    RadioButton radioButton;
    NetkromTextView tvNamaPembayaran;
    LinearLayout llMetodeLainnya;
    LinearLayout llMetodeLainnyaTerpilih;
    OtherPaymentListener otherPaymentListener;
    ImageView imgDompet, imgArrow;
    public OtherPaymentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public OtherPaymentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public OtherPaymentView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context){
        TransmedikaSettings transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);
        view = inflate(getContext(), R.layout.other_payment_view, this);
        rlLine = view.findViewById(R.id.line_pembayaran_lainnya);
        radioButton = view.findViewById(R.id.rb_jenis_bayar);
        tvNamaPembayaran = view.findViewById(R.id.tv_payment_name);
        llMetodeLainnya = view.findViewById(R.id.ll_metode_lainnya);
        llMetodeLainnyaTerpilih = view.findViewById(R.id.ll_metode_lainnya_terpilih);

        llMetodeLainnya.setOnClickListener(v -> otherPaymentListener.onLlMetodeLainnyaClick());

        imgDompet = view.findViewById(R.id.img_dompet);
        imgArrow = view.findViewById(R.id.img_arrow);

        if(transmedikaSettings.getPocketIcon()!=null){
            imgDompet.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                    TransmedikaUtils.setDrawable(context,transmedikaSettings.getPocketIcon()),null));
        }

        if(transmedikaSettings.getOtherPaymentArrowIcon()!=null){
            imgArrow.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                    TransmedikaUtils.setDrawable(context,transmedikaSettings.getOtherPaymentArrowIcon()),null));
        }
    }

    public void setOtherPaymentListener(OtherPaymentListener otherPaymentListener) {
        this.otherPaymentListener = otherPaymentListener;
    }

    public interface OtherPaymentListener{
        void onLlMetodeLainnyaClick();
    }

    public void showPembayaranTerpilih(boolean b, JenisPembayaran jenisPembayaran){
        if(b) {
            llMetodeLainnyaTerpilih.setVisibility(View.VISIBLE);
            rlLine.setVisibility(View.VISIBLE);
            tvNamaPembayaran.setText(jenisPembayaran.getAccountName());
        }else {
            llMetodeLainnyaTerpilih.setVisibility(View.GONE);
            rlLine.setVisibility(View.GONE);
        }
    }

    public RadioButton getRadioButton() {
        return radioButton;
    }
}
