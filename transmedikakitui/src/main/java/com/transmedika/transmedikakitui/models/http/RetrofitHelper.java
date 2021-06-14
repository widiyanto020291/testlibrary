package com.transmedika.transmedikakitui.models.http;


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
import com.transmedika.transmedikakitui.models.http.api.OwnApisFactory;
import com.transmedika.transmedikakitui.models.http.api.TransmedikaApi;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class RetrofitHelper implements HttpHelper {

    private final TransmedikaApi mTransmedikaApi;

    public static RetrofitHelper getRetrofitHelperInstance(Context context) {
        return new RetrofitHelper(context);
    }

    public RetrofitHelper(Context context){
        mTransmedikaApi = OwnApisFactory.ownApisFactory(context);
    }


    @Override
    public Flowable<BaseResponse<SignIn>> signIn(String deviceId, SignInParam param) {
        return mTransmedikaApi.signIn(deviceId,param);
    }

    @Override
    public Flowable<BaseResponse<List<Specialist>>> specialist(String auth) {
        return mTransmedikaApi.specialist(auth);
    }

    @Override
    public Flowable<BaseResponse<BasePage<List<Doctor>>>> doctor(String auth, String id, Integer perPage, Long medicalFacilityId, FilterFilter filterFilter) {
        return mTransmedikaApi.doctor(auth, id, perPage, medicalFacilityId, filterFilter);
    }

    @Override
    public Flowable<BaseResponse<BasePage<List<Doctor>>>> doctorDynamic(String auth, String url, FilterFilter filterFilter) {
        return mTransmedikaApi.doctorDynamic(auth, url, filterFilter);
    }

    @Override
    public Flowable<BaseResponse<FilterFilter>> filterDoctor(String auth) {
        return mTransmedikaApi.filterDoctor(auth);
    }

    @Override
    public Flowable<BaseResponse<Doctor>> detailDoctor(String auth, String uuid) {
        return mTransmedikaApi.detailDoctor(auth, uuid);
    }

    @Override
    public Flowable<BaseResponse<Konsultasi>> konsultasi(String auth, KonsultasiParam param) {
        return mTransmedikaApi.konsultasi(auth, param);
    }

    @Override
    public Flowable<BaseResponse<Konsultasi>> konsultasiKlinik(String auth, KonsultasiKlinikParam param) {
        return mTransmedikaApi.konsultasiKlinik(auth, param);
    }

    @Override
    public Flowable<BaseResponse<Profile>> profil(String auth) {
        return mTransmedikaApi.profil(auth);
    }

    @Override
    public Flowable<BaseResponse<List<Profile>>> keluarga(String auth) {
        return mTransmedikaApi.keluarga(auth);
    }

    @Override
    public Flowable<BaseResponse<GLAccount>> accountBalance(String auth) {
        return mTransmedikaApi.accountBalance(auth);
    }

    @Override
    public Flowable<BaseResponse<Voucher>> voucher(String auth, VoucherParam param) {
        return mTransmedikaApi.voucher(auth, param);
    }

    @Override
    public Flowable<BaseOResponse> statusKonsultasi(String auth, Long id, StatusKonsultasiParam param) {
        return mTransmedikaApi.statusKonsultasi(auth, id, param);
    }

    @Override
    public Flowable<BaseResponse<SignIn>> updateDeviceId(String deviceId, String auth) {
        return mTransmedikaApi.updateDeviceId(deviceId, auth);
    }

    @Override
    public Flowable<BaseResponse<List<Specialist>>> klinikSpesialis(String auth, Long id) {
        return mTransmedikaApi.klinikSpesialis(auth, id);
    }

    @Override
    public Flowable<BaseResponse<BasePage<List<Clinic>>>> klinik(String auth, String key, Integer perPage) {
        return mTransmedikaApi.klinik(auth, key, perPage);
    }

    @Override
    public Flowable<BaseResponse<BasePage<List<Clinic>>>> klinikDynamic(String auth, String url) {
        return mTransmedikaApi.klinikDynamic(auth, url);
    }

    @Override
    public Flowable<BaseResponse<List<PertanyaanResponse>>> klinikform(String auth, FormParam formParam) {
        return mTransmedikaApi.klinikform(auth, formParam);
    }

    @Override
    public Flowable<BaseResponse<String>> cekDeviceId(String auth) {
        return mTransmedikaApi.cekDeviceId(auth);
    }

    @Override
    public Flowable<BaseResponse<List<String>>> cekDeviceIdMultiple(String auth) {
        return mTransmedikaApi.cekDeviceIdMultiple(auth);
    }

    @Override
    public Flowable<ResponseBody> downloadFile(String fileUrl) {
        return mTransmedikaApi.downloadFile(fileUrl);
    }

    @Override
    public Flowable<BaseOResponse> postFeedbackConsultation(String auth, FeedbackParam file) {
        return mTransmedikaApi.postFeedbackConsultation(auth, file);
    }

    @Override
    public Flowable<BaseResponse<Jawaban>> klinikFormJawaban(String auth, Long id) {
        return mTransmedikaApi.klinikFormJawaban(auth, id);
    }

    @Override
    public Flowable<BaseResponse<String>> uploadImage(String auth, RequestBody name, MultipartBody.Part file, RequestBody consultationid) {
        return mTransmedikaApi.uploadImage(auth, name, file, consultationid);
    }

    @Override
    public Flowable<BaseResponse<Resep>> resep(String auth, Long id) {
        return mTransmedikaApi.resep(auth, id);
    }

    @Override
    public Flowable<ResponseBody> downloadResep(String auth, String idResep) {
        return mTransmedikaApi.downloadResep(auth, idResep);
    }

    @Override
    public Flowable<BaseResponse<Konsultasi>> cekKonsultasi(String auth) {
        return mTransmedikaApi.cekKonsultasi(auth);
    }

    @Override
    public Flowable<BaseResponse<List<KategoriObat>>> kategoriObat(String auth) {
        return mTransmedikaApi.kategoriObat(auth);
    }

    @Override
    public Flowable<BaseResponse<Order>> pesanObat(String auth, PesanObatParam param) {
        return mTransmedikaApi.pesanObat(auth, param);
    }

    @Override
    public Flowable<BaseResponse<Order>> pesanObatResepUpload(String auth, MultipartBody.Part[] file, RequestBody param) {
        return mTransmedikaApi.pesanObatResepUpload(auth, file, param);
    }

    @Override
    public Flowable<BaseResponse<BasePage<List<Obat>>>> obatOptions(String auth, String key, Integer perPage) {
        return mTransmedikaApi.obatOptions(auth, key, perPage);
    }

    @Override
    public Flowable<BaseResponse<BasePage<List<Obat>>>> obat(String auth, String id, Integer perPage) {
        return mTransmedikaApi.obat(auth, id, perPage);
    }

    @Override
    public Flowable<BaseResponse<BasePage<List<Obat>>>> obatDynamic(String auth, String url) {
        return mTransmedikaApi.obatDynamic(auth, url);
    }

    @Override
    public Flowable<BaseResponse<CariObat>> cariObat(String auth, CariObatParam param) {
        return mTransmedikaApi.cariObat(auth, param);
    }

    @Override
    public Flowable<BaseResponse<List<Alamat>>> alamat(String auth) {
        return mTransmedikaApi.alamat(auth);
    }

    @Override
    public Flowable<BaseResponse<Alamat>> tambahAlamat(String auth, AlamatParam alamatParam) {
        return mTransmedikaApi.tambahAlamat(auth, alamatParam);
    }

    @Override
    public Flowable<BaseResponse<Alamat>> ubahAlamat(String auth, String id, AlamatParam alamatParam) {
        return mTransmedikaApi.ubahAlamat(auth, id, alamatParam);
    }

    @Override
    public Flowable<BaseResponse<Alamat>> hapusAlamat(String auth, String id) {
        return mTransmedikaApi.hapusAlamat(auth, id);
    }

    @Override
    public Flowable<BaseOResponse> putPin(String auth, PinParam param) {
        return mTransmedikaApi.putPin(auth, param);
    }

    @Override
    public Flowable<BaseOResponse> putChangePin(String auth, PinParam param) {
        return mTransmedikaApi.putChangePin(auth, param);
    }

    @Override
    public Flowable<BaseOResponse> checkPin(String auth, PinParam param) {
        return mTransmedikaApi.checkPin(auth, param);
    }

    @Override
    public Flowable<BaseOResponse> putPassword(String auth, PasswordParam param) {
        return mTransmedikaApi.putPassword(auth, param);
    }

    @Override
    public Flowable<Response<BaseResponse<Object>>> putChangePassword(String auth, PasswordParam param) {
        return mTransmedikaApi.putChangePassword(auth, param);
    }

    @Override
    public Flowable<BaseOResponse> checkPassword(String auth, PasswordParam param) {
        return mTransmedikaApi.checkPassword(auth, param);
    }

    @Override
    public Flowable<BaseOResponse> forgetPasswordRequestOtp(String auth, LupaPasswordParam param) {
        return mTransmedikaApi.forgotPasswordByPhoneOld(auth, param);
    }

    @Override
    public Flowable<Response<BaseResponse<Object>>> forgetPasswordRequestOtpNew(String auth, LupaPasswordParam param) {
        return mTransmedikaApi.forgotPasswordByPhone(auth, param);
    }

    @Override
    public Flowable<BaseOResponse> forgetPasswordEmail(String auth, LupaPasswordParam param) {
        return mTransmedikaApi.forgotPasswordByEmail(auth, param);
    }

    @Override
    public Flowable<BaseResponse<SignIn>> smsVerification(SmsVerificationParam param) {
        return mTransmedikaApi.smsVerification(param);
    }

    @Override
    public Flowable<BaseResponse<SignIn>> smsVerification(String cookie, SmsVerificationParam param) {
        return mTransmedikaApi.smsVerification(cookie, param);
    }

    @Override
    public Flowable<BaseOResponse> kirimUlangOtp(OtpParam param) {
        return mTransmedikaApi.kirimUlangOtp(param);
    }
}
