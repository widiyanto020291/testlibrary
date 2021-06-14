package com.transmedika.transmedikakitui.modul.consultation;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.widget.NestedScrollView;

import com.transmedika.transmedikakitui.modul.DetailBaseDokterActivity;
import com.transmedika.transmedikakitui.utils.Constants;

public class DetailDoctorActivity extends DetailBaseDokterActivity {
    private static final String TAG = DetailDoctorActivity.class.getSimpleName();
    private final Rect scrollBounds = new Rect();
    @Override
    protected void onChatClick() {
        Bundle b = new Bundle();
        b.putParcelable(Constants.DATA, dokter);
        b.putParcelable(Constants.KLINIK, klinik);
        b.putParcelable(Constants.SPESIALIS, spesialis);
        Intent i = new Intent(mContext, ChatStepActivity.class);
        i.putExtra(Constants.DATA_USER, b);
        startActivity(i);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        super.initEventAndData(bundle);
        setToolBar(dokter.getSpecialist());
        binding.viewMain.getHitRect(scrollBounds);

        binding.viewMain.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                Log.i(TAG, "Scroll DOWN");
                isVisible();
            }

            if (scrollY < oldScrollY) {
                Log.i(TAG, "Scroll UP");
                isVisible();
            }

            if (scrollY == 0) {
                Log.i(TAG, "TOP SCROLL");
            }

            if (scrollY == ( v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight() )) {
                Log.i(TAG, "BOTTOM SCROLL");
            }
        });
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        isVisible();
    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        binding.llFloating.setVisibility(View.GONE);
    }

    private void isVisible(){
        if (binding.detailDoctorView.getBtnChat().getLocalVisibleRect(scrollBounds)) {
            binding.llFloating.setVisibility(View.GONE);
        } else {
            binding.llFloating.setVisibility(View.VISIBLE);
        }
    }
}
