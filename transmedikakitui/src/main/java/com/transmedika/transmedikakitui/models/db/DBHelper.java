package com.transmedika.transmedikakitui.models.db;

import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.realm.TempJawaban;

import java.util.List;

import io.realm.Realm;

public interface DBHelper {
    Realm getRealm();
    void insertLogin(SignIn login);
    SignIn selectLogin();
    void deleteLogin();
    void deleteAllTables();
    void insertObat(Obat obat);
    void deleteObat();
    void deleteObat(String slug);
    List<Obat> selectObat();
    Obat selectObat(String slug);
    void insertObat(List<Obat> obats);
    void insertJawabansTemp(TempJawaban tempJawaban);
    TempJawaban getJawabansTemp(Long klinikId, String spesialisId);
}
