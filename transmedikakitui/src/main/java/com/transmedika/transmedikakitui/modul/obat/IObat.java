package com.transmedika.transmedikakitui.modul.obat;

public interface IObat {
    void calculateObat();
    void calculateRealObat(int jml, long total, Long idCari);
    void calculatePembayaran(long total, String kodeVoucher, long totalVoucher);
}
