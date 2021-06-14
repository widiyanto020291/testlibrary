package com.transmedika.examplesdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Konsultasi;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.SignInParam;
import com.transmedika.transmedikakitui.modul.ConversationMainActivity;
import com.transmedika.transmedikakitui.modul.consultation.ClinicActivity;
import com.transmedika.transmedikakitui.modul.consultation.SpesialisKlinikActivity;
import com.transmedika.transmedikakitui.modul.obat.KategoriObatActivity;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.MsgUiUtil;
import com.transmedika.transmedikakitui.utils.RxUtil;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subscribers.ResourceSubscriber;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    DataManager dataManager;
    CompositeDisposable mCompositeDisposable;
    MsgUiUtil msgUiUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msgUiUtil = new MsgUiUtil(MainActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(TransmedikaUtils.setDrawable(getApplicationContext(),"ic_arrow_back"));
        dataManager = new DataManager(getApplicationContext());

        if(dataManager.selectLogin()!=null){
            Intent i = new Intent(MainActivity.this, ClinicActivity.class);
            startActivity(i);

            finish();
        }else {
            login();
        }

        /*findViewById(R.id.tv_cek).setOnClickListener(v -> {
            if (dataManager.cekKonsultasiKlinik()) {
                cekKonsultasi();
            } else {
                Intent iKlinik = new Intent(MainActivity.this, ClinicActivity.class);
                startActivity(iKlinik);
            }
        });*/

        //Intent i = new Intent(MainActivity.this, KategoriObatActivity.class);
        //startActivity(i);

        //login();
    }

    private void cekKonsultasi(){
        addSubscribe(dataManager.cekKonsultasi(Constants.BEARER+dataManager.selectLogin().getaUTHTOKEN())
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new ResourceSubscriber<BaseResponse<Konsultasi>>(){

                    @Override
                    protected void onStart() {
                        super.onStart();
                        msgUiUtil.showPgCommon();
                    }

                    @Override
                    public void onNext(BaseResponse<Konsultasi> model) {
                        if(model!=null){
                            if(model.getCode().equals(Constants.SUCCESS_API)){
                                Bundle b = new Bundle();
                                Konsultasi konsultasi = model.getData();
                                b.putParcelable(Constants.DATA_INFO, konsultasi);
                                Intent i = new Intent(MainActivity.this, ConversationMainActivity.class);
                                i.putExtra(Constants.DATA,b);
                                i.putExtra(Constants.ID_KONSULTASI, model.getData().getConsultationId());
                                i.putExtra(Constants.DATA_NOTE, model.getData().getComplaint());
                                i.putExtra(Constants.DATA_REF, model.getData().getPatient().getRef());
                                startActivity(i);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        msgUiUtil.dismisPgCommon();
                        MsgUiUtil.showSnackBar(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0),
                                t.getMessage(),MainActivity.this, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
                    }

                    @Override
                    public void onComplete() {
                        msgUiUtil.dismisPgCommon();
                    }
                }));
    }


    /** Contoh memanggil method login pada sdk Transmedika **/
    private void login(){
        SignInParam param = new SignInParam();
        param.setUsername("087822204189");
        param.setPassword("123456");
        param.setRefType("patient");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }
                    String token = task.getResult().getToken();
                    addSubscribe(dataManager.signIn(token, param)
                            .compose(RxUtil.rxSchedulerHelper())
                            .subscribeWith(new ResourceSubscriber<BaseResponse<SignIn>>() {

                                @Override
                                protected void onStart() {
                                    super.onStart();
                                    msgUiUtil.showPgCommon();
                                }

                                @Override
                                public void onNext(BaseResponse<SignIn> model) {
                                    if (model != null) {
                                        if (model.getCode().equals(Constants.SUCCESS_API)) {
                                            SignIn signIn = model.getData();
                                            if (signIn.getaUTHTOKEN().isEmpty() || signIn.getUuid() == null) {
                                                signIn.setName(param.getUsername());
                                                signIn.setSosmed(true);
                                            } else {
                                                dataManager.insertLogin(signIn);

                                                Intent i = new Intent(MainActivity.this, SpesialisKlinikActivity.class);
                                                startActivity(i);

                                                finish();
                                                //cekKonsultasi();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable t) {
                                    msgUiUtil.dismisPgCommon();
                                    MsgUiUtil.showSnackBar(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0),
                                            t.getMessage(),MainActivity.this, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
                                }

                                @Override
                                public void onComplete() {
                                    msgUiUtil.dismisPgCommon();
                                }
                            }));
                }).addOnFailureListener(e -> MsgUiUtil.showSnackBar(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0),
                        e.toString(), MainActivity.this, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG));

    }




    private void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    @Override
    protected void onDestroy() {
        unSubscribe();
        super.onDestroy();
    }

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
    }
}