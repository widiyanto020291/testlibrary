package com.transmedika.transmedikakitui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.transmedika.transmedikakitui.models.bean.json.Profile;
import com.transmedika.transmedikakitui.modul.consultation.KeluargaAdapter;

import java.util.Date;
import java.util.List;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.utils.ImageLoader;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;


public class ProfileHeaderView extends ConstraintLayout {

    View view;
    Spinner spKeluarga;
    NetkromTextView mTvBB;
    NetkromTextView mTvTb;
    NetkromTextView mTvUmur;
    NetkromButton btnEdit;
    ImageView imgProfile, ivArrowDown;

    private KeluargaAdapter keluargaAdapter;
    private Profile keluarga;
    private UbahHeightWeightListener ubahHeightWeightListener;
    private HeaderListener headerListener;

    public ProfileHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public ProfileHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ProfileHeaderView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        view = inflate(getContext(), R.layout.item_pasien_header, this);
        spKeluarga = view.findViewById(R.id.sp_keluarga);
        mTvBB = view.findViewById(R.id.tv_bb);
        mTvTb = view.findViewById(R.id.tv_tb);
        mTvUmur = view.findViewById(R.id.tv_umur);
        imgProfile = view.findViewById(R.id.img_profile);
        btnEdit = view.findViewById(R.id.btn_edit);
        ivArrowDown = view.findViewById(R.id.iv_arrow_down);
        btnEdit.setOnClickListener(v -> {
            headerListener.onEditClickListener();
        });

        TransmedikaSettings transmedikaSettings = TransmedikaUtils.transmedikaSettings(context);

        if(transmedikaSettings.getDoctorEditButtonBackground()!=null){
            btnEdit.setBackground(ResourcesCompat.getDrawable(getResources(),
                    TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorEditButtonBackground()),null));
        }

        Drawable searchIc = transmedikaSettings.getDoctorEditButtonIcon() != null ?
                ResourcesCompat.getDrawable(context.getResources(), TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorEditButtonIcon()), null) :
        AppCompatResources.getDrawable(context, R.drawable.ic_edit_white);

        btnEdit.setCompoundDrawablesWithIntrinsicBounds(searchIc, null, null, null);
    }

    public void setUbahHeightWeightListener(UbahHeightWeightListener ubahHeightWeightListener) {
        this.ubahHeightWeightListener = ubahHeightWeightListener;
    }

    public void setKeluargaData(List<Profile> keluargas) {
        this.keluargaAdapter = new KeluargaAdapter(getContext(), keluargas);
        spKeluarga.setAdapter(this.keluargaAdapter);
        spKeluarga.setSelection(0);
        spKeluarga.setOnItemSelectedListener(keluargaClickListener);
    }

    public void keluargaNotifyChange() {
        keluargaAdapter.notifyDataSetChanged();
    }

    public void setKeluargaSpinnerValue(Profile keluarga) {
        this.keluarga = keluarga;
        dataSelected(keluarga);
    }

    public Profile getKeluargaSpinnerValue() {
        return keluarga;
    }

    private final AdapterView.OnItemSelectedListener keluargaClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            keluarga = (Profile) adapterView.getSelectedItem();
            dataSelected(keluarga);
            if(headerListener!=null)
                headerListener.onDataSelected(keluarga);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    public void dataSelected(Profile profil) {
        if (profil.getRef() != null) {
            if (profil.getRef().getBodyHeight() != null) {
                mTvTb.setText(String.valueOf(profil.getRef().getBodyHeight()).concat("Cm"));
            } else {
                mTvTb.setText(getContext().getString(R.string.strip));
            }
            if (profil.getRef().getBodyHeight() != null) {
                mTvBB.setText(String.valueOf(profil.getRef().getBodyWeight()).concat("Kg"));
            } else {
                mTvBB.setText(getContext().getString(R.string.strip));
            }
            if (profil.getRef().getDob() != null) {
                mTvUmur.setText(TransmedikaUtils.getUmur(profil.getRef().getDob(), new Date()));
            } else {
                mTvUmur.setText(getContext().getString(R.string.strip));
            }
            ImageLoader.load(getContext(), profil.getProfilePicture(), imgProfile);
        }
    }

    public interface UbahHeightWeightListener {
        void onSubmit(String bb, String tb);
    }

    public interface HeaderListener{
        void onEditClickListener();
        void onDataSelected(Profile profil);
    }

    public void setDisableBtnEdit(int disableBtnEdit) {
        btnEdit.setVisibility(disableBtnEdit);
    }

    public ImageView getIvArrowDown() {
        return ivArrowDown;
    }

    public NetkromButton getBtnEdit() {
        return btnEdit;
    }

    public Spinner getSpKeluarga() {
        return spKeluarga;
    }

    public void setHeaderListener(HeaderListener headerListener) {
        this.headerListener = headerListener;
    }
}
