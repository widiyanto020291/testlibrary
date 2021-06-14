package com.transmedika.transmedikakitui.models.prefs;

public interface PreferencesHelper {
    boolean sibuk();
    void setSibuk(boolean b);

    boolean cekKonsultasi();
    void setCekKonsultasi(boolean b);

    boolean cekKonsultasiKlinik();
    void setCekKonsultasiKlinik(boolean b);

    String getCatatanOrder();
    void setCatatanOrder(String b);
}
