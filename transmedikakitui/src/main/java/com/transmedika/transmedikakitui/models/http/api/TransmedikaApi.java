package com.transmedika.transmedikakitui.models.http.api;

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
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Widiyanto02 on 9/6/2017.
 */

public interface TransmedikaApi {

    @Headers({"Content-Type: application/json"})
    @POST("auth/login")
    Flowable<BaseResponse<SignIn>> signIn(@Header("device-id") String deviceId,
                                          @Body SignInParam param);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("specialists")
    Flowable<BaseResponse<List<Specialist>>> specialist(@Header ("Authorization") String auth);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("doctor")
    Flowable<BaseResponse<BasePage<List<Doctor>>>> doctor(@Header ("Authorization") String auth,
                                                          @Query("specialist") String id,
                                                          @Query("per_page") Integer perPage,
                                                          @Query("medical_facility_id") Long medicalFacilityId,
                                                          @Body FilterFilter filterFilter);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Flowable<BaseResponse<BasePage<List<Doctor>>>> doctorDynamic(@Header ("Authorization") String auth,
                                                                 @Url String url,
                                                                 @Body FilterFilter filterFilter);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("parameter-filter-doctor")
    Flowable<BaseResponse<FilterFilter>> filterDoctor(@Header ("Authorization") String auth);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("doctors/{uuid}")
    Flowable<BaseResponse<Doctor>> detailDoctor(@Header ("Authorization") String auth,
                                                @Path("uuid") String uuid);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("consultation")
    Flowable<BaseResponse<Konsultasi>> konsultasi(@Header ("Authorization") String auth,
                                                  @Body KonsultasiParam param);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("consultation-clinic")
    Flowable<BaseResponse<Konsultasi>> konsultasiKlinik(@Header ("Authorization") String auth,
                                                        @Body KonsultasiKlinikParam param);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("profile")
    Flowable<BaseResponse<Profile>> profil(@Header ("Authorization") String auth);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("family")
    Flowable<BaseResponse<List<Profile>>> keluarga(@Header ("Authorization") String auth);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("doctors/{uuid}")
    Flowable<BaseResponse<Doctor>> detailDokter(@Header ("Authorization") String auth,
                                                @Path("uuid") String uuid);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("account-balance")
    Flowable<BaseResponse<GLAccount>> accountBalance(@Header ("Authorization") String auth);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("voucher-exist")
    Flowable<BaseResponse<Voucher>> voucher(@Header ("Authorization") String auth,
                                            @Body VoucherParam param);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @PUT("consultation/{konsultasi_id}")
    Flowable<BaseOResponse> statusKonsultasi(@Header ("Authorization") String auth,
                                             @Path("konsultasi_id") Long id,
                                             @Body StatusKonsultasiParam param);

    @Headers({"Content-Type: application/json"})
    @PUT("auth/device-id")
    Flowable<BaseResponse<SignIn>> updateDeviceId(@Header("device-id") String deviceId,
                                                  @Header ("Authorization") String auth);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("specialists-medical-facility/{id}")
    Flowable<BaseResponse<List<Specialist>>> klinikSpesialis(@Header ("Authorization")
                                                                     String auth, @Path("id") Long id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("medical-facilities-public")
    Flowable<BaseResponse<BasePage<List<Clinic>>>> klinik(@Header ("Authorization") String auth,
                                                          @Query("search") String key,
                                                          @Query("per_page") Integer perPage);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Flowable<BaseResponse<BasePage<List<Clinic>>>> klinikDynamic(@Header ("Authorization") String auth,
                                                                 @Url String url);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("medical-facility-form")
    Flowable<BaseResponse<List<PertanyaanResponse>>> klinikform(@Header ("Authorization") String auth,
                                                                @Body FormParam formParam);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("auth/check-device-id")
    Flowable<BaseResponse<String>> cekDeviceId(@Header ("Authorization") String auth);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("auth/check-device-id-multiple")
    Flowable<BaseResponse<List<String>>> cekDeviceIdMultiple(@Header ("Authorization") String auth);

    @Streaming
    @GET
    Flowable<ResponseBody> downloadFile(@Url String fileUrl);

    @Headers({"Content-Type: application/json"})
    @POST("rating")
    Flowable<BaseOResponse> postFeedbackConsultation(@Header ("Authorization") String auth,
                                                     @Body FeedbackParam file);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("medical-form-patients/{id}")
    Flowable<BaseResponse<Jawaban>> klinikFormJawaban(@Header ("Authorization") String auth,
                                                      @Path("id") Long id);

    @Multipart
    @POST("chat/upload")
    Flowable<BaseResponse<String>> uploadImage(@Header ("Authorization") String auth,
                                               @Part("file") RequestBody name,
                                               @Part MultipartBody.Part file,
                                               @Part("consultation_id") RequestBody consultationid);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("prescription/{konsultasi_id}")
    Flowable<BaseResponse<Resep>> resep(@Header ("Authorization") String auth,
                                        @Path("konsultasi_id") Long id);

    @Streaming
    @GET("prescription/generate/{id_resep}")
    Flowable<ResponseBody> downloadResep(@Header ("Authorization") String auth,
                                         @Path("id_resep") String idResep);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("check-consultation-available")
    Flowable<BaseResponse<Konsultasi>> cekKonsultasi(@Header ("Authorization") String auth);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("medicine-categories")
    Flowable<BaseResponse<List<KategoriObat>>> kategoriObat(@Header ("Authorization") String auth);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("orders")
    Flowable<BaseResponse<Order>> pesanObat(@Header ("Authorization") String auth,
                                            @Body PesanObatParam param);

    @Headers({"Accept: application/json"})
    @Multipart
    @POST("orders")
    Flowable<BaseResponse<Order>> pesanObatResepUpload(@Header ("Authorization") String auth,
                                                       @Part MultipartBody.Part[] file,
                                                       @Part("data") RequestBody param);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("medicine-options")
    Flowable<BaseResponse<BasePage<List<Obat>>>> obatOptions(@Header ("Authorization") String auth,
                                                             @Query("key") String key,
                                                             @Query("per_page") Integer perPage);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("medicine/{category_id}")
    Flowable<BaseResponse<BasePage<List<Obat>>>> obat(@Header ("Authorization") String auth,
                                                      @Path("category_id") String id,
                                                      @Query("per_page") Integer perPage);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET
    Flowable<BaseResponse<BasePage<List<Obat>>>> obatDynamic(@Header ("Authorization") String auth,
                                                             @Url String url);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("medicine-stocks-pharmacy")
    Flowable<BaseResponse<CariObat>> cariObat(@Header ("Authorization") String auth,
                                              @Body CariObatParam param);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("patient-address")
    Flowable<BaseResponse<List<Alamat>>> alamat(@Header ("Authorization") String auth);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("patient-address")
    Flowable<BaseResponse<Alamat>> tambahAlamat(@Header ("Authorization") String auth, @Body AlamatParam alamatParam);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @PUT("patient-address/{id}")
    Flowable<BaseResponse<Alamat>> ubahAlamat(@Header ("Authorization") String auth, @Path("id") String id, @Body AlamatParam alamatParam);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @DELETE("patient-address/{id}")
    Flowable<BaseResponse<Alamat>> hapusAlamat(@Header ("Authorization") String auth, @Path("id") String id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @PUT("auth/pin")
    Flowable<BaseOResponse> putPin(@Header ("Authorization") String auth, @Body PinParam param);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @PUT("profile/pin")
    Flowable<BaseOResponse> putChangePin(@Header ("Authorization") String auth, @Body PinParam param);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("auth/check-pin")
    Flowable<BaseOResponse> checkPin(@Header ("Authorization") String auth, @Body PinParam param);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @PUT("profile/reset-password")
    Flowable<BaseOResponse> putPassword(@Header ("Authorization") String auth, @Body PasswordParam param);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @PUT("profile/password")
    Flowable<Response<BaseResponse<Object>>> putChangePassword(@Header ("Authorization") String auth, @Body PasswordParam param);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("auth/check-password")
    Flowable<BaseOResponse> checkPassword(@Header ("Authorization") String auth, @Body PasswordParam param);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @PUT("profile/reset-password-by-phone")
    Flowable<BaseOResponse> forgotPasswordByPhoneOld(@Header ("Authorization") String auth, @Body LupaPasswordParam param);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("forgot-password-by-phone")
    Flowable<Response<BaseResponse<Object>>> forgotPasswordByPhone(@Header ("Authorization") String auth, @Body LupaPasswordParam param);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("profile/reset-password-by-email")
    Flowable<BaseOResponse> forgotPasswordByEmail(@Header ("Authorization") String auth, @Body LupaPasswordParam param);

    @Headers({"Content-Type: application/json"})
    @POST("verify-otp")
    Flowable<BaseResponse<SignIn>> smsVerification(@Body SmsVerificationParam param);

    @Headers({"Content-Type: application/json"})
    @POST("verify-otp")
    Flowable<BaseResponse<SignIn>> smsVerification(@Header("Cookie") String cookie, @Body SmsVerificationParam param);

    @Headers({"Content-Type: application/json"})
    @POST("auth/resend-otp")
    Flowable<BaseOResponse> kirimUlangOtp(@Body OtpParam param);
}

