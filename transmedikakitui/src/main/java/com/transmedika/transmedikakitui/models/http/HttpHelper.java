package com.transmedika.transmedikakitui.models.http;

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

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface HttpHelper {
    Flowable<BaseResponse<SignIn>> signIn(String deviceId, SignInParam param);
    Flowable<BaseResponse<List<Specialist>>> specialist(String auth);
    Flowable<BaseResponse<BasePage<List<Doctor>>>> doctor(String auth,
                                                          String id, Integer perPage,
                                                          Long medicalFacilityId,
                                                          FilterFilter filterFilter);
    Flowable<BaseResponse<BasePage<List<Doctor>>>> doctorDynamic(String auth, String url,
                                                                 FilterFilter filterFilter);
    Flowable<BaseResponse<FilterFilter>> filterDoctor(String auth);
    Flowable<BaseResponse<Doctor>> detailDoctor(String auth, String uuid);
    Flowable<BaseResponse<Konsultasi>> konsultasi(String auth, KonsultasiParam param);
    Flowable<BaseResponse<Konsultasi>> konsultasiKlinik(String auth, KonsultasiKlinikParam param);
    Flowable<BaseResponse<Profile>> profil(String auth);
    Flowable<BaseResponse<List<Profile>>> keluarga(String auth);
    Flowable<BaseResponse<GLAccount>> accountBalance(String auth);
    Flowable<BaseResponse<Voucher>> voucher(String auth, VoucherParam param);
    Flowable<BaseOResponse> statusKonsultasi(String auth, Long id, StatusKonsultasiParam param);
    Flowable<BaseResponse<SignIn>> updateDeviceId(String deviceId, String auth);
    Flowable<BaseResponse<List<Specialist>>> klinikSpesialis(String auth, Long id);
    Flowable<BaseResponse<BasePage<List<Clinic>>>> klinik(String auth, String key, Integer perPage);
    Flowable<BaseResponse<BasePage<List<Clinic>>>> klinikDynamic(String auth, String url);
    Flowable<BaseResponse<List<PertanyaanResponse>>> klinikform(String auth, FormParam formParam);
    Flowable<BaseResponse<String>> cekDeviceId(String auth);
    Flowable<BaseResponse<List<String>>> cekDeviceIdMultiple(String auth);
    Flowable<ResponseBody> downloadFile(String fileUrl);
    Flowable<BaseOResponse> postFeedbackConsultation(String auth, FeedbackParam file);
    Flowable<BaseResponse<Jawaban>> klinikFormJawaban(String auth, Long id);
    Flowable<BaseResponse<String>> uploadImage(String auth, RequestBody name, MultipartBody.Part file,
                                               RequestBody consultationid);
    Flowable<BaseResponse<Resep>> resep(String auth, Long id);
    Flowable<ResponseBody> downloadResep(String auth, String idResep);
    Flowable<BaseResponse<Konsultasi>> cekKonsultasi(String auth);
    Flowable<BaseResponse<List<KategoriObat>>> kategoriObat(String auth);
    Flowable<BaseResponse<Order>> pesanObat(String auth, PesanObatParam param);
    Flowable<BaseResponse<Order>> pesanObatResepUpload(String auth, MultipartBody.Part[] file, RequestBody param);
    Flowable<BaseResponse<BasePage<List<Obat>>>> obatOptions(String auth, String key, Integer perPage);
    Flowable<BaseResponse<BasePage<List<Obat>>>> obat(String auth, String id, Integer perPage);
    Flowable<BaseResponse<BasePage<List<Obat>>>> obatDynamic(String auth, String url);
    Flowable<BaseResponse<CariObat>> cariObat(String auth, CariObatParam param);
    Flowable<BaseResponse<List<Alamat>>> alamat(String auth);
    Flowable<BaseResponse<Alamat>> tambahAlamat(String auth, AlamatParam alamatParam);
    Flowable<BaseResponse<Alamat>> ubahAlamat(String auth, String id, @Body AlamatParam alamatParam);
    Flowable<BaseResponse<Alamat>> hapusAlamat(String auth, String id);
    Flowable<BaseOResponse> putPin(String auth, PinParam param);
    Flowable<BaseOResponse> putChangePin(String auth, PinParam param);
    Flowable<BaseOResponse> checkPin(String auth, PinParam param);
    Flowable<BaseOResponse> putPassword(String auth, PasswordParam param);
    Flowable<Response<BaseResponse<Object>>> putChangePassword(String auth, PasswordParam param);
    Flowable<BaseOResponse> checkPassword(String auth, PasswordParam param);
    Flowable<BaseOResponse> forgetPasswordRequestOtp(String auth, LupaPasswordParam param);
    Flowable<Response<BaseResponse<Object>>> forgetPasswordRequestOtpNew(String auth, LupaPasswordParam param);
    Flowable<BaseOResponse> forgetPasswordEmail(String auth, LupaPasswordParam param);
    Flowable<BaseResponse<SignIn>> smsVerification(SmsVerificationParam param);
    Flowable<BaseResponse<SignIn>> smsVerification(String cookie, SmsVerificationParam param);
    Flowable<BaseOResponse> kirimUlangOtp(OtpParam param);
}
