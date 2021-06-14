package com.transmedika.transmedikakitui.models.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.transmedika.transmedikakitui.app.App;
import com.transmedika.transmedikakitui.utils.Constants;


public class ImplPreferencesHelper implements PreferencesHelper {

    private static final boolean DEFAULT_SIBUK = false;
    private static final boolean DEFAULT_KONSULTASI = false;
    private static final boolean DEFAULT_KONSULTASI_KLINIK = false;
    private static final String DEFAULT_CATATAN_ORDER = null;

    public static final String SP_SIBUK = "SP_SIBUK";
    public static final String SP_KONSULTASI = "SP_KONSULTASI";
    public static final String SP_KONSULTASI_KLINIK = "SP_KONSULTASI_KLINIK";

    public static final String SHAREDPREFERENCES_NAME = "transmedika_prefs";
    private final SharedPreferences mSPrefs;
    private static final ImplPreferencesHelper implPreferencesHelper;

    public ImplPreferencesHelper() {
        mSPrefs = App.getInstance().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    static{
        implPreferencesHelper = new ImplPreferencesHelper();
    }

    public static ImplPreferencesHelper getPreferenceHelperInstance() {
        return implPreferencesHelper;
    }


    @Override
    public boolean sibuk() {
        return mSPrefs.getBoolean(SP_SIBUK, DEFAULT_SIBUK);
    }

    @Override
    public void setSibuk(boolean b) {
        mSPrefs.edit().putBoolean(SP_SIBUK, b).apply();
    }

    @Override
    public boolean cekKonsultasi() {
        return mSPrefs.getBoolean(SP_KONSULTASI, DEFAULT_KONSULTASI);
    }

    @Override
    public void setCekKonsultasi(boolean b) {
        mSPrefs.edit().putBoolean(SP_KONSULTASI, b).apply();
    }

    @Override
    public boolean cekKonsultasiKlinik() {
        return mSPrefs.getBoolean(SP_KONSULTASI_KLINIK, DEFAULT_KONSULTASI_KLINIK);
    }

    @Override
    public void setCekKonsultasiKlinik(boolean b) {
        mSPrefs.edit().putBoolean(SP_KONSULTASI_KLINIK, b).apply();
    }

    @Override
    public String getCatatanOrder() {
        return mSPrefs.getString(Constants.SP_CATATAN, DEFAULT_CATATAN_ORDER);
    }

    @Override
    public void setCatatanOrder(String b) {
        mSPrefs.edit().putString(Constants.SP_CATATAN, b).apply();
    }
}
