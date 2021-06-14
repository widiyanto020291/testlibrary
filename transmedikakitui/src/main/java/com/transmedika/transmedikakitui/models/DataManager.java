package com.transmedika.transmedikakitui.models;


import android.content.Context;

import com.transmedika.transmedikakitui.models.bean.json.Alamat;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.BasePage;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.CariObat;
import com.transmedika.transmedikakitui.models.bean.json.Clinic;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.FilterFilter;
import com.transmedika.transmedikakitui.models.bean.json.GLAccount;
import com.transmedika.transmedikakitui.models.bean.json.Jawaban;
import com.transmedika.transmedikakitui.models.bean.json.KategoriObat;
import com.transmedika.transmedikakitui.models.bean.json.Konsultasi;
import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.bean.json.Order;
import com.transmedika.transmedikakitui.models.bean.json.PertanyaanResponse;
import com.transmedika.transmedikakitui.models.bean.json.Profile;
import com.transmedika.transmedikakitui.models.bean.json.Resep;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.models.bean.json.Voucher;
import com.transmedika.transmedikakitui.models.bean.json.param.AlamatParam;
import com.transmedika.transmedikakitui.models.bean.json.param.CariObatParam;
import com.transmedika.transmedikakitui.models.bean.json.param.FeedbackParam;
import com.transmedika.transmedikakitui.models.bean.json.param.FormParam;
import com.transmedika.transmedikakitui.models.bean.json.param.KonsultasiKlinikParam;
import com.transmedika.transmedikakitui.models.bean.json.param.KonsultasiParam;
import com.transmedika.transmedikakitui.models.bean.json.param.LupaPasswordParam;
import com.transmedika.transmedikakitui.models.bean.json.param.OtpParam;
import com.transmedika.transmedikakitui.models.bean.json.param.PasswordParam;
import com.transmedika.transmedikakitui.models.bean.json.param.PesanObatParam;
import com.transmedika.transmedikakitui.models.bean.json.param.PinParam;
import com.transmedika.transmedikakitui.models.bean.json.param.SignInParam;
import com.transmedika.transmedikakitui.models.bean.json.param.SmsVerificationParam;
import com.transmedika.transmedikakitui.models.bean.json.param.StatusKonsultasiParam;
import com.transmedika.transmedikakitui.models.bean.json.param.VoucherParam;
import com.transmedika.transmedikakitui.models.bean.realm.TempJawaban;
import com.transmedika.transmedikakitui.models.db.DBHelper;
import com.transmedika.transmedikakitui.models.db.RealmHelper;
import com.transmedika.transmedikakitui.models.http.HttpHelper;
import com.transmedika.transmedikakitui.models.http.RetrofitHelper;
import com.transmedika.transmedikakitui.models.prefs.ImplPreferencesHelper;
import com.transmedika.transmedikakitui.models.prefs.PreferencesHelper;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.realm.Realm;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class DataManager implements HttpHelper, DBHelper, PreferencesHelper {

    private final HttpHelper retrofitHelper;
    private final PreferencesHelper implPreferencesHelper;
    private final RealmHelper realmHelper;

    public static DataManager getDataManagerInstance(Context context) {
        return new DataManager(context);
    }

    public DataManager(Context context){
        retrofitHelper = RetrofitHelper.getRetrofitHelperInstance(context);
        implPreferencesHelper = ImplPreferencesHelper.getPreferenceHelperInstance();
        realmHelper = RealmHelper.getRealmHelper();
    }

    public RealmHelper getRealmHelper() {
        return realmHelper;
    }

    @Override
    public void insertLogin(SignIn login) {
        realmHelper.insertLogin(login);
    }

    @Override
    public SignIn selectLogin() {
        return realmHelper.selectLogin();
    }

    @Override
    public void deleteLogin() {
        realmHelper.deleteLogin();
    }

    @Override
    public void deleteAllTables() {
        realmHelper.deleteAllTables();
    }

    @Override
    public void insertObat(Obat obat) {
        realmHelper.insertObat(obat);
    }

    @Override
    public void deleteObat() {
        realmHelper.deleteObat();
    }

    @Override
    public void deleteObat(String slug) {
        realmHelper.deleteObat(slug);
    }

    @Override
    public List<Obat> selectObat() {
        return realmHelper.selectObat();
    }

    @Override
    public Obat selectObat(String slug) {
        return realmHelper.selectObat(slug);
    }

    @Override
    public void insertObat(List<Obat> obats) {
        realmHelper.insertObat(obats);
    }

    @Override
    public void insertJawabansTemp(TempJawaban tempJawaban) {
        realmHelper.insertJawabansTemp(tempJawaban);
    }

    @Override
    public TempJawaban getJawabansTemp(Long klinikId, String spesialisId) {
        return realmHelper.getJawabansTemp(klinikId, spesialisId);
    }

    @Override
    public Realm getRealm() {
        return realmHelper.getRealm();
    }

    @Override
    public Flowable<BaseResponse<SignIn>> signIn(String deviceId, SignInParam param) {
        return retrofitHelper.signIn(deviceId, param);
    }

    @Override
    public Flowable<BaseResponse<List<Specialist>>> specialist(String auth) {
        return retrofitHelper.specialist(auth);
    }

    @Override
    public Flowable<BaseResponse<BasePage<List<Doctor>>>> doctor(String auth, String id, Integer perPage, Long medicalFacilityId, FilterFilter filterFilter) {
        return retrofitHelper.doctor(auth, id, perPage, medicalFacilityId, filterFilter);
    }

    @Override
    public Flowable<BaseResponse<BasePage<List<Doctor>>>> doctorDynamic(String auth, String url, FilterFilter filterFilter) {
        return retrofitHelper.doctorDynamic(auth, url, filterFilter);
    }

    @Override
    public Flowable<BaseResponse<FilterFilter>> filterDoctor(String auth) {
        return retrofitHelper.filterDoctor(auth);
    }

    @Override
    public Flowable<BaseResponse<Doctor>> detailDoctor(String auth, String uuid) {
        return retrofitHelper.detailDoctor(auth, uuid);
    }

    @Override
    public Flowable<BaseResponse<Konsultasi>> konsultasi(String auth, KonsultasiParam param) {
        return retrofitHelper.konsultasi(auth, param);
    }

    @Override
    public Flowable<BaseResponse<Konsultasi>> konsultasiKlinik(String auth, KonsultasiKlinikParam param) {
        return retrofitHelper.konsultasiKlinik(auth, param);
    }

    @Override
    public Flowable<BaseResponse<Profile>> profil(String auth) {
        return retrofitHelper.profil(auth);
    }

    @Override
    public Flowable<BaseResponse<List<Profile>>> keluarga(String auth) {
        return retrofitHelper.keluarga(auth);
    }

    @Override
    public Flowable<BaseResponse<GLAccount>> accountBalance(String auth) {
        return retrofitHelper.accountBalance(auth);
    }

    @Override
    public Flowable<BaseResponse<Voucher>> voucher(String auth, VoucherParam param) {
        return retrofitHelper.voucher(auth, param);
    }

    @Override
    public Flowable<BaseOResponse> statusKonsultasi(String auth, Long id, StatusKonsultasiParam param) {
        return retrofitHelper.statusKonsultasi(auth, id, param);
    }

    @Override
    public Flowable<BaseResponse<SignIn>> updateDeviceId(String deviceId, String auth) {
        return retrofitHelper.updateDeviceId(deviceId, auth);
    }

    @Override
    public Flowable<BaseResponse<List<Specialist>>> klinikSpesialis(String auth, Long id) {
        return retrofitHelper.klinikSpesialis(auth, id);
    }

    @Override
    public Flowable<BaseResponse<BasePage<List<Clinic>>>> klinik(String auth, String key, Integer perPage) {
        return retrofitHelper.klinik(auth, key, perPage);
    }

    @Override
    public Flowable<BaseResponse<BasePage<List<Clinic>>>> klinikDynamic(String auth, String url) {
        return retrofitHelper.klinikDynamic(auth, url);
    }

    @Override
    public Flowable<BaseResponse<List<PertanyaanResponse>>> klinikform(String auth, FormParam formParam) {
        return retrofitHelper.klinikform(auth, formParam);
    }

    @Override
    public Flowable<BaseResponse<String>> cekDeviceId(String auth) {
        return retrofitHelper.cekDeviceId(auth);
    }

    @Override
    public Flowable<BaseResponse<List<String>>> cekDeviceIdMultiple(String auth) {
        return retrofitHelper.cekDeviceIdMultiple(auth);
    }

    @Override
    public Flowable<ResponseBody> downloadFile(String fileUrl) {
        return retrofitHelper.downloadFile(fileUrl);
    }

    @Override
    public Flowable<BaseOResponse> postFeedbackConsultation(String auth, FeedbackParam file) {
        return retrofitHelper.postFeedbackConsultation(auth, file);
    }

    @Override
    public Flowable<BaseResponse<Jawaban>> klinikFormJawaban(String auth, Long id) {
        return retrofitHelper.klinikFormJawaban(auth, id);
    }

    @Override
    public Flowable<BaseResponse<String>> uploadImage(String auth, RequestBody name, MultipartBody.Part file, RequestBody consultationid) {
        return retrofitHelper.uploadImage(auth, name, file, consultationid);
    }

    @Override
    public Flowable<BaseResponse<Resep>> resep(String auth, Long id) {
        return retrofitHelper.resep(auth, id);
    }

    @Override
    public Flowable<ResponseBody> downloadResep(String auth, String idResep) {
        return retrofitHelper.downloadResep(auth, idResep);
    }

    @Override
    public Flowable<BaseResponse<Konsultasi>> cekKonsultasi(String auth) {
        return retrofitHelper.cekKonsultasi(auth);
    }

    @Override
    public Flowable<BaseResponse<List<KategoriObat>>> kategoriObat(String auth) {
        return retrofitHelper.kategoriObat(auth);
    }

    @Override
    public Flowable<BaseResponse<Order>> pesanObat(String auth, PesanObatParam param) {
        return retrofitHelper.pesanObat(auth, param);
    }

    @Override
    public Flowable<BaseResponse<Order>> pesanObatResepUpload(String auth, MultipartBody.Part[] file, RequestBody param) {
        return retrofitHelper.pesanObatResepUpload(auth, file, param);
    }

    @Override
    public Flowable<BaseResponse<BasePage<List<Obat>>>> obatOptions(String auth, String key, Integer perPage) {
        return retrofitHelper.obatOptions(auth, key, perPage);
    }

    @Override
    public Flowable<BaseResponse<BasePage<List<Obat>>>> obat(String auth, String id, Integer perPage) {
        return retrofitHelper.obat(auth, id, perPage);
    }

    @Override
    public Flowable<BaseResponse<BasePage<List<Obat>>>> obatDynamic(String auth, String url) {
        return retrofitHelper.obatDynamic(auth, url);
    }

    @Override
    public Flowable<BaseResponse<CariObat>> cariObat(String auth, CariObatParam param) {
        return retrofitHelper.cariObat(auth, param);
    }

    @Override
    public Flowable<BaseResponse<List<Alamat>>> alamat(String auth) {
        return retrofitHelper.alamat(auth);
    }

    @Override
    public Flowable<BaseResponse<Alamat>> tambahAlamat(String auth, AlamatParam alamatParam) {
        return retrofitHelper.tambahAlamat(auth, alamatParam);
    }

    @Override
    public Flowable<BaseResponse<Alamat>> ubahAlamat(String auth, String id, AlamatParam alamatParam) {
        return retrofitHelper.ubahAlamat(auth, id, alamatParam);
    }

    @Override
    public Flowable<BaseResponse<Alamat>> hapusAlamat(String auth, String id) {
        return retrofitHelper.hapusAlamat(auth, id);
    }

    @Override
    public Flowable<BaseOResponse> putPin(String auth, PinParam param) {
        return retrofitHelper.putPin(auth, param);
    }

    @Override
    public Flowable<BaseOResponse> putChangePin(String auth, PinParam param) {
        return retrofitHelper.putChangePin(auth, param);
    }

    @Override
    public Flowable<BaseOResponse> checkPin(String auth, PinParam param) {
        return retrofitHelper.checkPin(auth, param);
    }

    @Override
    public Flowable<BaseOResponse> putPassword(String auth, PasswordParam param) {
        return retrofitHelper.putPassword(auth, param);
    }

    @Override
    public Flowable<Response<BaseResponse<Object>>> putChangePassword(String auth, PasswordParam param) {
        return retrofitHelper.putChangePassword(auth, param);
    }

    @Override
    public Flowable<BaseOResponse> checkPassword(String auth, PasswordParam param) {
        return retrofitHelper.checkPassword(auth, param);
    }

    @Override
    public Flowable<BaseOResponse> forgetPasswordRequestOtp(String auth, LupaPasswordParam param) {
        return retrofitHelper.forgetPasswordRequestOtp(auth, param);
    }

    @Override
    public Flowable<Response<BaseResponse<Object>>> forgetPasswordRequestOtpNew(String auth, LupaPasswordParam param) {
        return retrofitHelper.forgetPasswordRequestOtpNew(auth, param);
    }

    @Override
    public Flowable<BaseOResponse> forgetPasswordEmail(String auth, LupaPasswordParam param) {
        return retrofitHelper.forgetPasswordEmail(auth, param);
    }

    @Override
    public Flowable<BaseResponse<SignIn>> smsVerification(SmsVerificationParam param) {
        return retrofitHelper.smsVerification(param);
    }

    @Override
    public Flowable<BaseResponse<SignIn>> smsVerification(String cookie, SmsVerificationParam param) {
        return retrofitHelper.smsVerification(cookie, param);
    }

    @Override
    public Flowable<BaseOResponse> kirimUlangOtp(OtpParam param) {
        return retrofitHelper.kirimUlangOtp(param);
    }

    @Override
    public boolean sibuk() {
        return implPreferencesHelper.sibuk();
    }

    @Override
    public void setSibuk(boolean b) {
        implPreferencesHelper.setSibuk(b);
    }

    @Override
    public boolean cekKonsultasi() {
        return implPreferencesHelper.cekKonsultasi();
    }

    @Override
    public void setCekKonsultasi(boolean b) {
        implPreferencesHelper.setCekKonsultasi(b);
    }

    @Override
    public boolean cekKonsultasiKlinik() {
        return implPreferencesHelper.cekKonsultasiKlinik();
    }

    @Override
    public void setCekKonsultasiKlinik(boolean b) {
        implPreferencesHelper.setCekKonsultasiKlinik(b);
    }

    @Override
    public String getCatatanOrder() {
        return implPreferencesHelper.getCatatanOrder();
    }

    @Override
    public void setCatatanOrder(String b) {
        implPreferencesHelper.setCatatanOrder(b);
    }
}
