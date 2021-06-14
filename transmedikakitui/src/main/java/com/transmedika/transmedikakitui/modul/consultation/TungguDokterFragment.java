package com.transmedika.transmedikakitui.modul.consultation;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.parse.ParseQuery;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseBindingFragment;
import com.transmedika.transmedikakitui.contract.consultation.TungguDokterContract;
import com.transmedika.transmedikakitui.databinding.TungguDokterBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.Konsultasi;
import com.transmedika.transmedikakitui.models.bean.json.Profile;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.StatusKonsultasiParam;
import com.transmedika.transmedikakitui.models.bean.parse.KonsultasiParse;
import com.transmedika.transmedikakitui.modul.ConversationMainActivity;
import com.transmedika.transmedikakitui.presenter.TungguDokterPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.RxUtil;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;
import com.transmedika.transmedikakitui.widget.NetkromDialog;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.transmedika.transmedikakitui.utils.Constants.DATA_KONSULTASI;


public class TungguDokterFragment extends BaseBindingFragment<TungguDokterBinding, TungguDokterContract.View, TungguDokterPresenter>
    implements TungguDokterContract.View{

    private static final String TAG = TungguDokterFragment.class.getSimpleName();
    private CountDownTimer countDownTimer;
    private boolean finish;
    private SignIn signIn;
    private Konsultasi konsultasi;
    private Profile profil;
    private boolean handleStatus = false;

    @Override
    protected TungguDokterContract.View getBaseView() {
        return this;
    }

    public static TungguDokterFragment newInstance(Konsultasi konsultasi) {

        Bundle args = new Bundle();

        TungguDokterFragment fragment = new TungguDokterFragment();
        args.putParcelable(DATA_KONSULTASI, konsultasi);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new TungguDokterPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(view, savedInstanceState);
        if(getArguments()!=null){
            konsultasi = getArguments().getParcelable(DATA_KONSULTASI);
            profil = konsultasi.getPatient();
        }
        signIn = mPresenter.selectLogin();
    }


    @Override
    protected TungguDokterBinding getViewBinding(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return TungguDokterBinding.inflate(inflater);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        if(transmedikaSettings.getButtonPrimaryLightRoundBackground()!=null){
            binding.btnBatal.setBackground(ResourcesCompat.getDrawable(getResources(),
                    TransmedikaUtils.setDrawable(mContext, transmedikaSettings.getButtonPrimaryLightRoundBackground()),null));
        }
        startTimer();
        getUserParse();
        TransmedikaUtils.updateDeviceId(mContext);
        binding.btnBatal.setOnClickListener(v -> backHandler());
        if(transmedikaSettings.getWaitingDoctorResource()!=null){
            binding.imgWaitingDoctor.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                    TransmedikaUtils.setDrawable(mContext, transmedikaSettings.getWaitingDoctorResource()),null));
        }
    }

    private void getUserParse(){
        getLiveQuery();
    }

    private void getLiveQuery(){
        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        ParseQuery<KonsultasiParse> queryChats = ParseQuery.getQuery(KonsultasiParse.class);
        queryChats.whereEqualTo("consultation_id", konsultasi.getConsultationId());
        SubscriptionHandling<KonsultasiParse> subscriptionHandling = parseLiveQueryClient.subscribe(queryChats);

        subscriptionHandling.handleSubscribe(query -> query.findInBackground((data, e) ->
                RxUtil.runOnUi(o -> {
                    if (!data.isEmpty()) {
                        Log.d(TAG, "getLiveQuery: FROM INIT");
                        onGetHandle(data.get(0));
                    }
                })
        ));

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.UPDATE, (query, object) ->
                RxUtil.runOnUi(o -> {
                    Log.d(TAG, "getLiveQuery: FROM UPDATE");
                    onGetHandle(object);
                }));
    }

    private void onGetHandle(KonsultasiParse object) {
        if (!handleStatus) {
            if (Objects.equals(object.getStatus(), KonsultasiParse.ON_CHAT)) {
                handleStatus = true;
                Bundle b = new Bundle();
                b.putParcelable(Constants.DATA_INFO, konsultasi);
                Intent i = new Intent(mContext, ConversationMainActivity.class);
                i.putExtra(Constants.DATA,b);
                i.putExtra(Constants.ID_KONSULTASI, konsultasi.getConsultationId());
                i.putExtra(Constants.DATA_NOTE, konsultasi.getComplaint());
                i.putExtra(Constants.DATA_REF, profil.getRef());
                startActivity(i);
                mActivity.finish();
            } else if (Objects.equals(object.getStatus(), KonsultasiParse.REJECTED_BY_DOCTOR)) {
                handleStatus = true;
                finish = true;
                countDownTimer.cancel();
                binding.tvDokterSiap.setVisibility(View.GONE);
                binding.tvTime.setText(getString(R.string.tidak_ada_response)
                        .concat(System.lineSeparator()
                                .concat(getString(R.string.hubungi_dokter_lain))));
                if(transmedikaSettings.getButtonPrimaryRoundBackground()!=null){
                    binding.btnBatal.setBackground(ResourcesCompat.getDrawable(getResources(),
                            TransmedikaUtils.setDrawable(mContext, transmedikaSettings.getButtonPrimaryRoundBackground()),null));
                }
                binding.btnBatal.setText(getString(R.string.kembali));
                binding.btnBatal.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            }
        }
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.tvTime.setText(String.format(Locale.getDefault(),"%s %02d:%02d",
                        getString(R.string.dalam),
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                binding.tvTime.setText(getString(R.string.tidak_ada_response)
                        .concat(System.lineSeparator()
                                .concat(getString(R.string.hubungi_dokter_lain))));
                if(transmedikaSettings.getButtonPrimaryRoundBackground()!=null){
                    binding.btnBatal.setBackground(ResourcesCompat.getDrawable(getResources(),
                            TransmedikaUtils.setDrawable(mContext, transmedikaSettings.getButtonPrimaryRoundBackground()),null));
                }
                binding.btnBatal.setText(getString(R.string.kembali));
                binding.btnBatal.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                binding.tvDokterSiap.setVisibility(View.GONE);
                finish = true;
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        backHandler();
        return true;
    }

    private void backHandler(){
        if(!finish) {
            dialogExit();
        }else {
            req(KonsultasiParse.NOT_ANSWERED);
        }
    }

    @Override
    public void statusKonsultasiResp(BaseOResponse response) {
        mActivity.finish();
    }

    private void dialogExit() {
        NetkromDialog netkromDialog = new NetkromDialog(mContext, 0,
                getString(R.string.batal_konsultasi),
                getString(R.string.batal_konsultasi_), getString(R.string.konfirmasi), getString(R.string.batal),0);
        netkromDialog.setOnButtonClick(new NetkromDialog.onButtonClick() {
            @Override
            public void onBtnYaClick() {
                netkromDialog.dismiss();
                if(finish){
                    TungguDokterFragment.super.onBackPressedSupport();
                }else {
                    req(KonsultasiParse.CANCEL_BY_PATIEN);
                }
            }

            @Override
            public void onBntbatalClick() {
                netkromDialog.dismiss();
            }
        });
        netkromDialog.show();
    }

    private void req(String status){
        StatusKonsultasiParam param = new StatusKonsultasiParam();
        param.setStatus(status);
        mPresenter.statusKonsultasi(signIn.getaUTHTOKEN(), konsultasi.getConsultationId(), param, mContext);
    }
}
