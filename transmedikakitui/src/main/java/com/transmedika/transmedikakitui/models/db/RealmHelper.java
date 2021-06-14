package com.transmedika.transmedikakitui.models.db;

import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.realm.TempJawaban;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RealmHelper implements DBHelper {

    private final Realm mRealm;
    public static final String DB_NAME = "trasnmedika_realm.realm";

    public RealmHelper() {
        mRealm = TransmedikaUtils.getRealm();
    }

    public static RealmHelper getRealmHelper() {
        return new RealmHelper();
    }

    @Override
    public Realm getRealm() {
        return mRealm;
    }

    @Override
    public void insertLogin(SignIn login) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(login);
        mRealm.commitTransaction();
    }

    @Override
    public SignIn selectLogin() {
        SignIn bean = mRealm.where(SignIn.class).findFirst();
        if (bean == null)
            return null;
        return mRealm.copyFromRealm(bean);
    }

    @Override
    public void deleteLogin() {
        mRealm.beginTransaction();
        mRealm.delete(SignIn.class);
        mRealm.commitTransaction();
    }

    @Override
    public void deleteAllTables() {
        mRealm.beginTransaction();
        mRealm.deleteAll();
        mRealm.commitTransaction();
    }

    @Override
    public void insertObat(Obat obat) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(obat);
        mRealm.commitTransaction();
    }

    @Override
    public void deleteObat() {
        mRealm.beginTransaction();
        mRealm.delete(Obat.class);
        mRealm.commitTransaction();
    }

    @Override
    public void deleteObat(String slug) {
        Obat obat = mRealm.where(Obat.class)
                .equalTo("slug", slug)
                .findFirst();
        mRealm.beginTransaction();
        if (obat != null) {
            obat.deleteFromRealm();
        }
        mRealm.commitTransaction();
    }

    @Override
    public List<Obat> selectObat() {
        RealmQuery<Obat> results = mRealm.where(Obat.class);
        RealmResults<Obat> obats = results.findAll();
        return mRealm.copyFromRealm(obats);
    }

    @Override
    public Obat selectObat(String slug) {
        Obat bean = mRealm.where(Obat.class)
                .equalTo("slug", slug)
                .findFirst();
        if (bean == null)
            return null;
        return mRealm.copyFromRealm(bean);
    }

    @Override
    public void insertObat(List<Obat> obats) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(obats);
        mRealm.commitTransaction();
    }

    @Override
    public void insertJawabansTemp(TempJawaban tempJawaban) {
        mRealm.beginTransaction();
        mRealm.delete(Obat.class);
        mRealm.commitTransaction();

        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(tempJawaban);
        mRealm.commitTransaction();
    }

    @Override
    public TempJawaban getJawabansTemp(Long klinikId, String spesialisId) {
        TempJawaban bean = mRealm.where(TempJawaban.class)
                .equalTo("klinikId", klinikId)
                .equalTo("specializeId", spesialisId)
                .findFirst();
        if (bean == null)
            return null;
        return mRealm.copyFromRealm(bean);
    }
}
