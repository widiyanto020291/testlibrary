package com.transmedika.transmedikakitui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;


public class DetailDoctorView extends LinearLayout {
    View view;
    NetkromTextView tvNama, tvStatus,
            tvSpesialis, tvDesc, tvLabelPengalaman, tvRating, tvPengalaman, tvLabelBiaya, tvHarga,
            tvHargaDisc, tvLabelStr, tvStr, tvLabelJadwal, tvJadwalKonsultasi, tvLabelAlumni, tvAlumni,
            tvLabelPraktik, tvPraktik;
    View imgStatus;
    ImageView imgProfile, imgRate, imgPengalaman, imgHarga, imgTempatPraktik, imgNoStr, imgJadwal, imgAlumni;
    NetkromButton btnChat;
    public DetailDoctorView(Context context) {
        super(context);
        init(context);
    }

    public DetailDoctorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DetailDoctorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        TransmedikaSettings transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);
        view = inflate(getContext(), R.layout.detail_doctor, this);
        tvNama = view.findViewById(R.id.tv_nama);
        tvStatus = view.findViewById(R.id.tv_status);
        tvSpesialis = view.findViewById(R.id.tv_spesialis);
        tvDesc = view.findViewById(R.id.tv_desc);
        tvLabelPengalaman = view.findViewById(R.id.tv_label_pengalaman);
        tvRating = view.findViewById(R.id.tv_rating);
        tvPengalaman = view.findViewById(R.id.tv_pengalaman);
        tvLabelBiaya = view.findViewById(R.id.tv_label_biaya);
        tvHarga = view.findViewById(R.id.tv_harga);
        tvHargaDisc = view.findViewById(R.id.tv_harga_diskon);
        tvLabelStr = view.findViewById(R.id.tv_label_str);
        tvStr = view.findViewById(R.id.tv_str);
        tvLabelJadwal = view.findViewById(R.id.tv_label_jadwal);
        tvJadwalKonsultasi = view.findViewById(R.id.tv_jadwal_konsul);
        tvLabelAlumni = view.findViewById(R.id.tv_label_alumni);
        tvAlumni = view.findViewById(R.id.tv_alumni);
        tvLabelPraktik = view.findViewById(R.id.tv_label_praktik);
        tvPraktik = view.findViewById(R.id.tv_praktik);
        btnChat = view.findViewById(R.id.btn_chat);
        imgStatus = view.findViewById(R.id.img_status);
        imgProfile = view.findViewById(R.id.img_profile);
        imgRate = view.findViewById(R.id.iv_star);
        imgPengalaman = view.findViewById(R.id.iv_pengalaman);
        imgHarga = view.findViewById(R.id.iv_rp);
        imgTempatPraktik = view.findViewById(R.id.iv_praktik);
        imgNoStr = view.findViewById(R.id.iv_str);
        imgJadwal = view.findViewById(R.id.iv_time);
        imgAlumni = view.findViewById(R.id.iv_alumni);

        if(transmedikaSettings.getDoctorPriceIcon()!=null)
            imgHarga.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                    TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorPriceIcon()),null));
        if(transmedikaSettings.getDoctorRateIcon()!=null)
            imgRate.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                    TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorRateIcon()),null));
        if(transmedikaSettings.getDoctorExperienceIcon()!=null)
            imgPengalaman.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                    TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorExperienceIcon()),null));
        if(transmedikaSettings.getDoctorStrNumberIcon()!=null)
            imgNoStr.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                    TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorStrNumberIcon()),null));
        if(transmedikaSettings.getDoctorScheduleIcon()!=null)
            imgJadwal.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                    TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorScheduleIcon()),null));
        if(transmedikaSettings.getDoctorAlumniIcon()!=null)
            imgAlumni.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                    TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorAlumniIcon()),null));
        if(transmedikaSettings.getDoctorFacilityIcon()!=null)
            imgTempatPraktik.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                    TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorFacilityIcon()),null));

        tvNama.setCustomFont(context, transmedikaSettings.getFontBold());

        tvStatus.setOnClickListener(v -> Toast.makeText(context, tvStatus.getText().toString(),Toast.LENGTH_LONG).show());
    }

    public NetkromTextView getTvNama() {
        return tvNama;
    }

    public NetkromTextView getTvStatus() {
        return tvStatus;
    }

    public NetkromTextView getTvSpesialis() {
        return tvSpesialis;
    }

    public NetkromTextView getTvDesc() {
        return tvDesc;
    }

    public NetkromTextView getTvLabelPengalaman() {
        return tvLabelPengalaman;
    }

    public NetkromTextView getTvRating() {
        return tvRating;
    }

    public NetkromTextView getTvPengalaman() {
        return tvPengalaman;
    }

    public NetkromTextView getTvLabelBiaya() {
        return tvLabelBiaya;
    }

    public NetkromTextView getTvHarga() {
        return tvHarga;
    }

    public NetkromTextView getTvHargaDisc() {
        return tvHargaDisc;
    }

    public NetkromTextView getTvLabelStr() {
        return tvLabelStr;
    }

    public NetkromTextView getTvStr() {
        return tvStr;
    }

    public NetkromTextView getTvLabelJadwal() {
        return tvLabelJadwal;
    }

    public NetkromTextView getTvJadwalKonsultasi() {
        return tvJadwalKonsultasi;
    }

    public NetkromTextView getTvLabelAlumni() {
        return tvLabelAlumni;
    }

    public NetkromTextView getTvAlumni() {
        return tvAlumni;
    }

    public NetkromTextView getTvLabelPraktik() {
        return tvLabelPraktik;
    }

    public NetkromTextView getTvPraktik() {
        return tvPraktik;
    }

    public NetkromButton getBtnChat() {
        return btnChat;
    }

    public View getImgStatus() {
        return imgStatus;
    }

    public ImageView getImgProfile() {
        return imgProfile;
    }


}
