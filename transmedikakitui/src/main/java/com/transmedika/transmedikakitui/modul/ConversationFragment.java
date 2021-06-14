package com.transmedika.transmedikakitui.modul;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.LiveQueryException;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.ParseLiveQueryClientCallbacks;
import com.parse.livequery.SubscriptionHandling;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.RootBindingFragment;
import com.transmedika.transmedikakitui.component.RxBus;
import com.transmedika.transmedikakitui.contract.consultation.ConversationContract;
import com.transmedika.transmedikakitui.databinding.ActivityConversationBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.MessageTemp;
import com.transmedika.transmedikakitui.models.bean.json.BaseOResponse;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Clinic;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.Konsultasi;
import com.transmedika.transmedikakitui.models.bean.json.Profile;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.StatusKonsultasiParam;
import com.transmedika.transmedikakitui.models.bean.parse.KonsultasiParse;
import com.transmedika.transmedikakitui.models.bean.parse.Message;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.modul.consultation.ConversationAdapter;
import com.transmedika.transmedikakitui.modul.consultation.DetailCatatanDokterActivity;
import com.transmedika.transmedikakitui.modul.consultation.DetailResepActivity;
import com.transmedika.transmedikakitui.modul.consultation.DetailSpaActivity;
import com.transmedika.transmedikakitui.modul.consultation.JawabanActivity;
import com.transmedika.transmedikakitui.modul.videocall.CallingActivity;
import com.transmedika.transmedikakitui.presenter.consultation.ConversationsPresenter;
import com.transmedika.transmedikakitui.utils.CheckAvailableNetwork;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.DateUtil;
import com.transmedika.transmedikakitui.utils.ICheckAvailableNetwork;
import com.transmedika.transmedikakitui.utils.ImageLoader;
import com.transmedika.transmedikakitui.utils.MsgUiUtil;
import com.transmedika.transmedikakitui.utils.PathUtil;
import com.transmedika.transmedikakitui.utils.RxUtil;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;
import com.transmedika.transmedikakitui.widget.FeedbackKonsultasiDialog;
import com.transmedika.transmedikakitui.widget.NetkromDialog;
import com.transmedika.transmedikakitui.widget.ReconnectDialog;
import com.transmedika.transmedikakitui.worker.UploadFileChatWorkerP;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.transmedika.transmedikakitui.utils.Constants.ID_KONSULTASI;


public class ConversationFragment extends RootBindingFragment<ActivityConversationBinding, ConversationContract.View, ConversationsPresenter>
        implements ConversationContract.View, ICheckAvailableNetwork, ParseLiveQueryClientCallbacks {

    private static final String TAG = ConversationFragment.class.getSimpleName();
    private ParseUser _user, userF;
    private final List<Message> messages = new ArrayList<>();
    private ConversationAdapter chatSingleAdapter;
    private long date = 0;
    private SignIn uSignIn;
    private Doctor dokter;
    private Clinic klinik;
    private Konsultasi konsultasi = new Konsultasi();
    private Profile profil;
    private String note;
    private boolean finish;
    private Integer permissionFrom = -1;
    private static final int PICKFILE_RESULT_CODE = 90;
    private boolean resep, catatan, spa;
    private String resepId, catatanId, spaId;
    private boolean flagHistori;
    private Date tglTransaksi;
    private final ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
    private CheckAvailableNetwork.CheckNetworkNew receiver;
    private boolean initParse = false;
    private ParseQuery<ParseObject> queryConsultation;
    private ParseQuery<Message> queryChats;
    private int findFirstVisibleItemPosition;
    private int findFirstCompletelyVisibleItemPosition;
    private int findLastVisibleItemPosition;
    private int findLastCompletelyVisibleItemPosition;
    private int counter = 0;
    private FeedbackKonsultasiDialog feedbackKonsultasiDialog;
    private boolean statusFeedback = false;
    private ReconnectDialog reconnectDialog;


    public static ConversationFragment newInstance(Konsultasi konsultasi,
                                                   String note,
                                                   boolean flagHistori,
                                                   Date tglTransaksi) {
        Bundle args = new Bundle();
        ConversationFragment fragment = new ConversationFragment();
        args.putParcelable(Constants.DATA_INFO, konsultasi);
        args.putString(Constants.DATA_NOTE, note);
        args.putBoolean(Constants.FLAG_HISTORI, flagHistori);
        args.putSerializable(Constants.TGL_TRANSAKSI, tglTransaksi);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mPresenter = new ConversationsPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null) {
            konsultasi = getArguments().getParcelable(Constants.DATA_INFO);
            dokter = konsultasi.getDoctor();
            profil = konsultasi.getPatient();
            klinik = konsultasi.getKlinik();
            note = getArguments().getString(Constants.DATA_NOTE);
            konsultasi.setComplaint(note);
            if (getArguments().getBoolean(Constants.FLAG_HISTORI, false)) {
                flagHistori = getArguments().getBoolean(Constants.FLAG_HISTORI, false);
                tglTransaksi = (Date) getArguments().getSerializable(Constants.TGL_TRANSAKSI);
            }
        }
        uSignIn = mPresenter.selectLogin();
        initToolbar();
    }

    @Override
    protected ActivityConversationBinding getViewBinding(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return ActivityConversationBinding.inflate(inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void initEventAndData(Bundle bundle) {
        super.initEventAndData(bundle);
        mPresenter.checkPermission(new RxPermissions(this), mContext);
        setRv();
        getUserParse();
        setUpListener();
        if(!flagHistori) {
            if(klinik==null) {
                mPresenter.setCekKonsultasi(true);
            }else {
                mPresenter.setCekKonsultasiKlinik(true);
            }
            binding.tvDate.setText(DateUtil.dateType4(new Date()));
        }else {
            binding.tvDate.setText(DateUtil.dateType4(tglTransaksi));
        }
        binding.tvNoRm.setText(getString(R.string.dua_param_string,"No RM ", profil.getRef().getMedicalRecordNumber()!=null ? profil.getRef().getMedicalRecordNumber() : "-"));
        binding.tvPasien.setText(profil.getFullName());
        if (profil.getRef().getDob() != null)
            binding.tvUmur.setText(TransmedikaUtils.getUmur(profil.getRef().getDob(), new Date()));
        if(profil.getRef().getBodyHeight()!=null)
            binding.tvTb.setText(String.valueOf(profil.getRef().getBodyHeight()).concat("Cm"));
        if(profil.getRef().getBodyHeight()!=null)
            binding.tvBb.setText(String.valueOf(profil.getRef().getBodyWeight()).concat("Kg"));
        binding.tvKeluhan.setText(note);
        if(klinik!=null){
            binding.tvLblKeluhan.setVisibility(View.GONE);
            binding.tvKeluhan.setVisibility(View.GONE);
            binding.btnTampilkanJawaban.setVisibility(View.VISIBLE);
        }else {
            binding.tvLblKeluhan.setVisibility(View.VISIBLE);
            binding.tvKeluhan.setVisibility(View.VISIBLE);
            binding.btnTampilkanJawaban.setVisibility(View.GONE);
        }

        if(flagHistori){
            binding.layoutInputArea.setVisibility(View.GONE);
            binding.commonToolbar.getMenu().findItem(R.id.call).setVisible(false);
            binding.commonToolbar.getMenu().findItem(R.id.action_end).setVisible(false);
            binding.commonToolbar.getMenu().findItem(R.id.action_menu).setVisible(false);
        }
    }

    @NonNull
    @NotNull
    @Override
    protected ConversationContract.View getBaseView() {
        return this;
    }

    private void setUpListener(){
        setOnRefreshClickListener(this::getUserParse);
        binding.edChat.addTextChangedListener(getmWatchPencarian);
        binding.llProfile.setOnClickListener(v -> {
            if(binding.llShowHide.getVisibility() == View.VISIBLE){
                binding.llShowHide.setVisibility(View.GONE);
                binding.imgShowHide.setRotation(-90);
            }else {
                binding.llShowHide.setVisibility(View.VISIBLE);
                binding.imgShowHide.setRotation(90);
            }
        });
        binding.fbGotoDown.setOnClickListener(v -> binding.rv.smoothScrollToPosition(0));
        binding.fbSend.setOnClickListener(v -> {
            String textMsg = Objects.requireNonNull(binding.edChat.getText()).toString();
            if(!TextUtils.isEmpty(Objects.requireNonNull(binding.edChat.getText()).toString()) && binding.edChat.getText().toString().trim().length() > 0)
                sendMessage(UUID.randomUUID().toString(), Message.TEXT_TYPE, textMsg, null);
        });
        binding.imgEmoticon.setOnClickListener(v -> {
            permissionFrom = 1;
            mPresenter.checkPermission(new RxPermissions(ConversationFragment.this), mContext);
        });

        binding.btnTampilkanJawaban.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putLong(ID_KONSULTASI, konsultasi.getConsultationId());
            Intent i = new Intent(mContext, JawabanActivity.class);
            i.putExtra(Constants.DATA_USER, b);
            startActivity(i);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void registerReceiver(){
        receiver = new CheckAvailableNetwork.CheckNetworkNew(mContext, this);
        receiver.registerNetworkCheck();
    }

    private final TextWatcher getmWatchPencarian = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            /*RxUtil.runOnUi(o -> {
                updateTyping("Sedang Mengetik");
            });*/
        }

        @Override
        public void afterTextChanged(Editable s) {
            //mPresenter.stopTyping();
        }
    };

    @Override
    public void jump() {
        //RxUtil.runOnUi(o -> updateTyping(null));
    }

    @Override
    public void uploadResp(BaseResponse<String> response) {
        Log.e(TAG, response.getData());
    }

    @Override
    public void donwloadResp(String path) {
        File file = new File(path);

        Uri uri = FileProvider.getUriForFile(mActivity, transmedikaSettings.getApplicationId() + ".fileprovider", file);
        String mime = mActivity.getContentResolver().getType(uri);

        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        newIntent.setDataAndType(uri,mime);
        newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            MsgUiUtil.showSnackBar(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0),
                    getString(R.string.tidak_ada_aplikasi_buka_file),mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void statusKonsultasiResp(BaseOResponse response) {
        finish = true;
        feedbackConsultation();
        selesaiKonsultasi();
        if(klinik==null) {
            mPresenter.setCekKonsultasi(false);
        }else {
            mPresenter.setCekKonsultasiKlinik(false);
        }
        broadcastKonsultasi();
    }

    @Override
    public void cekDeviceIdResp(BaseResponse<String> response) {
        /*if(response.getData()!=null) {
            Bundle b = new Bundle();
            b.putParcelable("UserTarget", getProfilFromDokter(response.getData()));
            b.putString(ID_KONSULTASI, String.valueOf(konsultasi.getConsultationId()));
            Intent i = new Intent(mContext, CallingActivity.class);
            i.putExtra("DATA", b);
            startActivity(i);
        }*/
    }

    @Override
    public void cekDeviceIdMultipleResp(BaseResponse<List<String>> response) {
        if(response.getData()!=null) {
            Bundle b = new Bundle();
            b.putParcelable("UserTarget", getProfilFromDokter(response.getData()));
            b.putString(ID_KONSULTASI, String.valueOf(konsultasi.getConsultationId()));
            Intent i = new Intent(mContext, CallingActivity.class);
            i.putExtra("DATA", b);
            startActivity(i);
        }
    }

    private void broadcastKonsultasi(){
        BroadcastEvents.Event event = new BroadcastEvents.Event();
        if(klinik==null) {
            event.setInitString(Constants.FLAG_KONSULTASI_NO);
        }else {
            event.setInitString(Constants.FLAG_KONSULTASI_KLINIK_NO);
        }
        RxBus.getDefault().post(new BroadcastEvents(event));
    }

    private void setRv() {
        chatSingleAdapter = new ConversationAdapter(mContext, messages, _user, dokter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, true);
        binding.rv.setLayoutManager(linearLayoutManager);
        binding.rv.setItemAnimator(null);
        binding.rv.setAdapter(chatSingleAdapter);
        chatSingleAdapter.setOnItemClickListeners(new ConversationAdapter.OnItemClickListeners() {
            @Override
            public void onDetailObatClick(Message message, int pos) {
                Bundle b = new Bundle();
                b.putParcelable(Constants.DATA_KONSULTASI, konsultasi);
                Intent i = new Intent(mContext, DetailResepActivity.class);
                i.putExtra(Constants.DATA_USER, b);
                i.putExtra(ID_KONSULTASI, konsultasi.getConsultationId());
                startActivity(i);
            }

            @Override
            public void onFileClick(Message message, int pos) {
                if(TransmedikaUtils.getFilenameUrlExt(message.getText(),'.').equalsIgnoreCase("doc")||
                        TransmedikaUtils.getFilenameUrlExt(message.getText(),'.').equalsIgnoreCase("docx")||
                        TransmedikaUtils.getFilenameUrlExt(message.getText(),'.').equalsIgnoreCase("pdf")){
                    //doWorkDonwload(message);
                    setUsingDialog(true);
                    mPresenter.download(message.getText(), userF.getString("name"), mContext);
                }else {
                    MsgUiUtil.showSnackBar(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0),
                            message.getText(),mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
                }
            }

            @Override
            public void onImageClick(Message message, int pos) {
                if(TransmedikaUtils.getFilenameUrlExt(message.getText(),'.').equalsIgnoreCase("jpg")||
                        TransmedikaUtils.getFilenameUrlExt(message.getText(),'.').equalsIgnoreCase("png")||
                        TransmedikaUtils.getFilenameUrlExt(message.getText(),'.').equalsIgnoreCase("jpeg")){

                    //doWorkDonwload(message);
                    setUsingDialog(true);
                    mPresenter.download(message.getText(), userF.getString("name"), mContext);
                }else {
                    MsgUiUtil.showSnackBar(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0),
                            message.getText(),mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
                }
            }

            @Override
            public void onPhrYa(Message message, int pos) {
                if(!finish) {
                    updateConsultation(message.getMessageId(), Message.PHR_REQ_STATUS_ALLOWED);

                    message.setText(Message.PHR_REQ_STATUS_ALLOWED);
                    messages.set(pos, message);
                    chatSingleAdapter.notifyItemChanged(pos, message);
                }
            }

            @Override
            public void onPhrTidak(Message message, int pos) {
                if(!finish) {
                    updateConsultation(message.getMessageId(), Message.PHR_REQ_STATUS_DENIED);
                    message.setText(Message.PHR_REQ_STATUS_DENIED);
                    messages.set(pos, message);
                    chatSingleAdapter.notifyItemChanged(pos, message);
                }
            }

            @Override
            public void uploadUlang(Message message, int pos) {
                upload(message.getFileLocal(), true, message.getMessageId());
            }

            @Override
            public void onDetailSpaClick(Message message, int pos) {
                Bundle b = new Bundle();
                b.putParcelable(Constants.DATA, dokter);
                b.putParcelable(Constants.DATA_KONSULTASI, konsultasi);
                b.putParcelable(Constants.DATA_PROFILE, profil);
                b.putString(Constants.DATA_MSG, message.getText());
                b.putSerializable(Constants.DATA_DATE, message.getCreatedAt());
                Intent i = new Intent(mContext, DetailSpaActivity.class);
                i.putExtra(Constants.DATA_USER, b);
                startActivity(i);
            }

            @Override
            public void onDetailNoteClick(Message message, int pos) {
                Bundle b = new Bundle();
                b.putParcelable(Constants.DATA, dokter);
                b.putParcelable(Constants.DATA_KONSULTASI, konsultasi);
                b.putParcelable(Constants.DATA_PROFILE, profil);
                b.putString(Constants.DATA_MSG, message.getText());
                b.putSerializable(Constants.DATA_DATE, message.getCreatedAt());
                Intent i = new Intent(mContext, DetailCatatanDokterActivity.class);
                i.putExtra(Constants.DATA_USER, b);
                startActivity(i);
            }

            @Override
            public void showInfoError(Message message, int pos) {
                MsgUiUtil.showSnackBar(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0),
                        message.getText(),mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
            }
        });
        binding.rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                findFirstCompletelyVisibleItemPosition  = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                findLastCompletelyVisibleItemPosition  = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                Log.i("findFirstVisible", String.valueOf(findFirstVisibleItemPosition));
                Log.i("findFirstCompletely", String.valueOf(findFirstCompletelyVisibleItemPosition));
                Log.i("findLastVisible", String.valueOf(findLastVisibleItemPosition));
                Log.i("findLastCompletely", String.valueOf(findLastCompletelyVisibleItemPosition));

                if (findFirstCompletelyVisibleItemPosition == 0 && binding.flFabDown.getVisibility() == View.VISIBLE) {
                    counter = 0;
                    binding.flFabDown.setVisibility(View.GONE);
                    binding.tvUnread.setVisibility(View.GONE);
                } else if (!messages.isEmpty() && findFirstVisibleItemPosition >= 1 && binding.flFabDown.getVisibility() == View.GONE) {
                    binding.flFabDown.setVisibility(View.VISIBLE);
                } else if (counter > 0 && (findFirstCompletelyVisibleItemPosition + 1) == counter) {
                    counter--;
                    binding.tvUnread.setText(String.valueOf(counter));
                }
            }
        });
    }

    /*private void doWorkDonwload(Message message){
        Data.Builder dataP = new Data.Builder();
        dataP.putString(Constants.DATA_URL, message.getText());
        dataP.putString(Constants.DATA_PASIEN, userF.getString("name"));

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(DownloadFileChatWorkerP.class)
                .setInputData(dataP.build())
                .build();

        WorkManager.getInstance(getApplicationContext()).enqueue(oneTimeWorkRequest);
    }*/

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void getUserParse(){
        showLoading();
        ParseUser.becomeInBackground(uSignIn.getTokenParse(), (user, exx) -> {
            if(user!=null && exx == null){
                _user = user;
                chatSingleAdapter.setUser(_user);
                ParseQuery<ParseObject> queryF = ParseQuery.getQuery("_User");
                queryF.getInBackground(dokter.getObjectId(), (objectX, eX) -> {
                    userF = (ParseUser) objectX;
                    initParse = true;
                    registerReceiver();
                });
            }else {
                showErrorMsg(exx.getMessage());
            }
        });

        parseLiveQueryClient.registerListener(this);
    }

    private void cekCounter(){
        counter++;
        if(findFirstVisibleItemPosition >=1 && counter > 0){
            binding.tvUnread.setVisibility(View.VISIBLE);
            binding.tvUnread.setText(String.valueOf(counter));
        }else {
            counter = 0;
            binding.rv.scrollToPosition(0);
        }
    }

    private void updateConversationsLiveQuery(){
        queryChats = ParseQuery.getQuery(Message.class);
        queryChats.whereEqualTo("uid", _user);
        queryChats.whereEqualTo("user", userF);
        queryChats.whereEqualTo("consultation_id", String.valueOf(konsultasi.getConsultationId()));
        queryChats.setLimit(1000);
        //queryChats.addAscendingOrder("createdAt");
        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(queryChats);

        subscriptionHandling.handleSubscribe(query ->
                query.findInBackground((data, e) -> {
                    if(initParse){
                        if (e != null) {
                            showErrorMsg(e.getMessage());
                        } else {
                            if (data.size() > 0) {
                                messages.clear();
                                for (Message message : data) {
                                    insertMessage(message);
                                }

                                if (messages.size() > 0) {
                                    chatSingleAdapter.notifyDataSetChanged();
                                }
                            }
                            initParse = false;
                            hideLoading();
                        }
                    }else {
                        if(!flagHistori) {
                            List<Message> messagesTmp = new ArrayList<>(data);
                            messagesTmp.removeAll(messages);

                            List<Message> messagesOld = new ArrayList<>(data);
                            messagesOld.removeAll(messagesTmp);

                            messages.clear();

                            for (Message message : messagesOld) {
                                insertMessage(message);
                            }
                            chatSingleAdapter.notifyDataSetChanged();

                            //cek new message from other
                            for (Message message : messagesTmp) {
                                insertMessage(message);
                                chatSingleAdapter.notifyItemInserted(0);
                                cekCounter();
                            }
                        }
                        hideLoading();
                    }
                }));

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> RxUtil.runOnUi(o -> {
            if(!object.getSenderId().equals(_user)) {

                insertMessage(object);
                chatSingleAdapter.notifyItemInserted(0);
                cekCounter();
                updateToRead();

                if (object.getKind().equals(Message.SESSION_END_TYPE)) {
                    binding.layoutInputArea.setVisibility(View.GONE);
                }
            }else{
                int pos = messages.indexOf(object);
                messages.set(pos, object);
                chatSingleAdapter.notifyItemChanged(pos, object);
            }
        }));

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.UPDATE, (query, object) ->
                RxUtil.runOnUi(o -> {
                    if(object.getSenderId().equals(_user)) {
                        int pos = messages.indexOf(object);
                        messages.set(pos, object);
                        chatSingleAdapter.notifyItemChanged(pos, object);
                    } else if (object.getSenderId().equals(userF)) {
                        if (Message.IMAGE_TYPE.equalsIgnoreCase(object.getKind()) || Message.FILE_TYPE.equalsIgnoreCase(object.getKind()) || Message.VIDEO_TYPE.equalsIgnoreCase(object.getKind())) {
                            int pos = messages.indexOf(object);
                            if (pos != -1) {
                                messages.set(pos, object);
                                chatSingleAdapter.notifyItemChanged(pos, object);
                            }
                        }
                    }
                }));

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.ENTER, (query, object) ->
                RxUtil.runOnUi(o -> Log.e("","")));

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.LEAVE, (query, object) ->
                RxUtil.runOnUi(o -> Log.e("","")));
    }

    private void konsultasiLiveQuery(){
        queryConsultation = ParseQuery.getQuery("Consultations");
        queryConsultation.whereEqualTo("consultation_id", konsultasi.getConsultationId());
        SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(queryConsultation);

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.UPDATE, (query, object) -> {
            if(Objects.equals(object.getString("status"), KonsultasiParse.SESI_BERAKHIR)) {
                RxUtil.runOnUi(o -> {
                    finish = true;
                    feedbackConsultation();
                    broadcastKonsultasi();
                    if(klinik==null) {
                        mPresenter.setCekKonsultasi(false);
                    }else {
                        mPresenter.setCekKonsultasiKlinik(false);
                    }
                    binding.rv.setVisibility(View.VISIBLE);
                    binding.layoutInputArea.setVisibility(View.GONE);
                    binding.commonToolbar.getMenu().findItem(R.id.call).setVisible(false);
                    binding.commonToolbar.getMenu().findItem(R.id.action_end).setVisible(false);
                    binding.commonToolbar.getMenu().findItem(R.id.action_menu).setVisible(false);
                });
            }
        });

    }

    private void selesaiKonsultasi(){
        sendMessage(UUID.randomUUID().toString(), Message.SESSION_END_TYPE, "Konsultasi sudah selesai",null);
    }

    /*private void updateTyping(){
        ParseQuery<ParseObject> queryChats = ParseQuery.getQuery("Chats");
        queryChats.whereEqualTo("uid", _user);
        SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(queryChats);
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.UPDATE, (query, object) -> {
            RxUtil.runOnUi(o -> {
                if(Objects.requireNonNull(object.getParseUser("user")).getObjectId().equals(userF.getObjectId())) {
                    if (object.getString("typing") != null) {
                        binding.commonToolbar.tvToolbarSubTitle.setVisibility(View.VISIBLE);
                        binding.commonToolbar.tvToolbarSubTitle.setText(object.getString("typing"));
                    } else {
                        binding.commonToolbar.tvToolbarSubTitle.setVisibility(View.GONE);
                    }
                }
            });
        });
    }*/

    private Message sendMessage(String msgId, String typeMsg, String textMsg, String fileLocal){

        Message message = new Message();
        message.setMessageId(msgId);
        message.setSenderId(_user);
        message.setUser(userF);
        message.setText(textMsg);
        message.setDate(new Date());
        message.setUid(_user);
        message.setStatus(Message.NOT_SEND_STATUS);
        message.setKind(typeMsg);
        message.setKonsultasiId(String.valueOf(konsultasi.getConsultationId()));
        if(fileLocal!=null)
            message.setFileLocal(fileLocal);

        insertMessage(message);
        chatSingleAdapter.notifyItemInserted(0);
        binding.rv.scrollToPosition(0);
        binding.edChat.setText(null);
        message.saveEventually(e -> {
            if(e == null){
                RxUtil.runOnUi(o -> {
                    /*int pos = messages.indexOf(message);
                    String status = messages.get(pos).getStatus();
                    if(status.equals(Message.NOT_SEND_STATUS)){
                        message.setStatus(Message.SENT_STATUS);
                    }else {
                        message.setStatus(status);
                    }

                    messages.set(pos, message);
                    chatSingleAdapter.notifyItemChanged(pos, message);*/
                });
            }else {
                Log.d(TAG,"Gagal Kirim Pesan, "+e.getMessage());
            }
        });
        return message;
    }

    protected void initToolbar(){
        binding.commonToolbar.inflateMenu(R.menu.conversation_menu);
        binding.commonToolbar.getRlBack().setOnClickListener(v -> {
            if(finish || flagHistori){
                mActivity.finish();
            }else {
                dialogExit();
            }
        });
        binding.commonToolbar.getTvTitle().setText(dokter.getFullName());
        ImageLoader.loadAll(mActivity, dokter.getProfileDoctor(), binding.commonToolbar.getImgProfile(), R.drawable.bg_circle_place_holder);
        binding.commonToolbar.getTvSubTitle().setVisibility(View.GONE);
        binding.commonToolbar.setOnMenuItemClickListener(
                item -> {
                    int id = item.getItemId();
                    if(id == R.id.call){
                        permissionFrom = 0;
                        mPresenter.checkPermission(new RxPermissions(this), mContext);
                    }else if(id == R.id.action_end){
                        if(finish){
                            mActivity.finish();
                        }else {
                            dialogExit();
                        }
                    }/*else if(id == R.id.action_menu){
                        MessagePopUpWindow popUpWindow = new MessagePopUpWindow(mContext);
                        popUpWindow.showAsDropDown(vAcnhor);
                        popUpWindow.setOnItemMenuClickListener(new MessagePopUpWindow.OnItemMenuClickListener() {
                            @Override
                            public void onAkhiriClick() {
                                if(finish){
                                    finish();
                                }else {
                                    dialogExit();
                                }
                            }

                            @Override
                            public void onVideoCallClick() {
                                permissionFrom = 0;
                                mPresenter.checkPermission(new RxPermissions(activity), mContext);
                            }
                        });
                    }*/
                    return true;
                });
    }

    private void akhiriSesi(){
        setUsingDialog(true);
        StatusKonsultasiParam param = new StatusKonsultasiParam();
        param.setStatus(KonsultasiParse.SESI_BERAKHIR);
        mPresenter.statusKonsultasi(uSignIn.getaUTHTOKEN(), konsultasi.getConsultationId(), param, mContext);
    }

    private void insertMessage(Message message){
        if (!DateUtil.isSameDay(date, message.getDate().getTime())) {
            date = message.getDate().getTime();
            Message msgHeader = new Message();
            msgHeader.setKind(Message.DATE_TYPE);
            msgHeader.setDate(message.getDate());
            messages.add(0,msgHeader);
        }

        messages.add(0,message);

        if(message.getKind().equals(Message.RESEP_OBAT_TYPE)){
            if(resep){
                Message messageXX = new Message();
                messageXX.setMessageId(resepId);
                int pos = messages.indexOf(messageXX);
                messages.remove(messageXX);
                chatSingleAdapter.notifyItemRemoved(pos);
            }
            resepId = message.getMessageId();
            resep = true;
        }

        if(message.getKind().equals(Message.RESEP_OBAT_SPA)){
            if(spa){
                Message messageXX = new Message();
                messageXX.setMessageId(spaId);
                int pos = messages.indexOf(messageXX);
                messages.remove(messageXX);
                chatSingleAdapter.notifyItemRemoved(pos);
            }

            spaId = message.getMessageId();
            spa = true;
        }

        if(message.getKind().equals(Message.RESEP_OBAT_CATATAN)){
            if(catatan){
                Message messageXX = new Message();
                messageXX.setMessageId(catatanId);
                int pos = messages.indexOf(messageXX);
                messages.remove(messageXX);
                chatSingleAdapter.notifyItemRemoved(pos);
            }

            catatanId = message.getMessageId();
            catatan = true;
        }

        if (message.getKind().equals(Message.SESSION_END_TYPE)) {
            if(binding.layoutInputArea.getVisibility() == View.VISIBLE)
                binding.layoutInputArea.setVisibility(View.GONE);
        }
    }

    private void updateToRead(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("uidF", userF.getObjectId());
        params.put("uid", _user.getObjectId());
        params.put("consultation_id", String.valueOf(konsultasi.getConsultationId()));
        try {
            ParseCloud.callFunction("updateToReaded", params);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /*private void updateTyping(String status){
        HashMap<String, Object> params = new HashMap<>();
        params.put("uidF", userF.getObjectId());
        params.put("uid", _user.getObjectId());
        params.put("status", status);
        try {
            ParseCloud.callFunction("updateTyping", params);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

    private void updateConsultation(String messageId, String status){
        HashMap<String, Object> params = new HashMap<>();
        params.put("message_id", messageId);
        params.put("status", status);
        params.put("consultation_id", konsultasi.getConsultationId());
        try {
            ParseCloud.callFunction("updateConsultation", params);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        if(finish || flagHistori){
            return super.onBackPressedSupport();
        }else {
            dialogExit();
            return true;
        }
    }

    @Override
    public void getBroadcastEvents(BroadcastEvents.Event event) {
        if(event.getInitString().equals(Constants.ICOMING_CALL)){
            /*Bundle b = new Bundle();
            b.putParcelable("USER", getProfilFromDokter());
            b.putString(ID_KONSULTASI, String.valueOf(konsultasi.getConsultationId()));
            Intent intent = new Intent(mActivity, AnswerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("DATA", b);
            mContext.getApplicationContext().startActivity(intent);*/
            if (getActivity() instanceof ConversationMainActivity) {
                ConversationMainActivity cActivity = ((ConversationMainActivity) getActivity());
                cActivity.init(false);
            }
        }else if(event.getInitString().equals(Constants.msg)){
            MessageTemp messageTemp = (MessageTemp) event.getObject();
            for (Message message : messages){
                if(message.getMessageId().equals(messageTemp.getMsgId())){
                    message.setText(messageTemp.getUrl());
                    message.saveEventually();
                    int i = messages.indexOf(message);
                    if(i > -1){
                        chatSingleAdapter.notifyItemChanged(i, message);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void start() {
        if(permissionFrom == 1){
            Intent chooseFile = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            String[] mimetypes = {"image/jpg", "image/png", "image/jpeg", "application/pdf",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "application/msword"};
            chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            chooseFile.setAction(Intent.ACTION_OPEN_DOCUMENT); //Intent.ACTION_GET_CONTENT itu ngebuka file browser lain juga jd pake action open document untuk sembunyikn yang lain
            chooseFile = Intent.createChooser(chooseFile, "Pilih file");
            startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
        }else if(permissionFrom == 0){
            //mPresenter.cekDeviceId(uSignIn.getaUTHTOKEN(), mContext);
            setUsingDialog(true);
            mPresenter.cekDeviceIdMultiple(uSignIn.getaUTHTOKEN(), mContext);
        }
    }

    private Profile getProfilFromDokter(List<String> currentDeviceIds) {
        Profile mProfil = getProfilFromDokter();
        mProfil.setDeviceIds(currentDeviceIds);
        return mProfil;
    }

    /*private Profil getProfilFromDokter(String currentDeviceIds) {
        Profil mProfil = getProfilFromDokter();
        mProfil.setDeviceId(currentDeviceIds);
        return mProfil;
    }*/

    private Profile getProfilFromDokter(){
        Profile mProfil = new Profile();
        mProfil.setUuid(dokter.getUuid());
        mProfil.setEmail(dokter.getEmail());
        mProfil.setFullName(dokter.getFullName());
        mProfil.setProfilePicture(dokter.getProfileDoctor());
        mProfil.setPhoneNumber(dokter.getPhoneNumber());
        mProfil.setStatus(dokter.getStatus());
        return mProfil;
    }

    private void dialogExit() {
        NetkromDialog netkromDialog = new NetkromDialog(mContext, 0,
                getString(R.string.keluar_conversations),
                getString(R.string.akhiri_conversations), getString(R.string.konfirmasi), getString(R.string.batal),0);
        netkromDialog.setOnButtonClick(new NetkromDialog.onButtonClick() {
            @Override
            public void onBtnYaClick() {
                akhiriSesi();
                netkromDialog.dismiss();
            }

            @Override
            public void onBntbatalClick() {
                netkromDialog.dismiss();
            }
        });
        netkromDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == PICKFILE_RESULT_CODE) {
                if (resultCode == RESULT_OK && data != null) {
                    Uri uri = data.getData();
                    String path = PathUtil.getPath(mContext, uri, true);
                    upload(path, false, null);
                }
            }
        }
    }

    private void upload(String path, boolean reupload, String msgId){

        if(path!=null) {
            if(msgId==null) {
                msgId = UUID.randomUUID().toString();
            }
            Data.Builder dataP = new Data.Builder();
            dataP.putString(Constants.DATA, path);
            dataP.putString(Constants.DATA_MSG, msgId);
            dataP.putLong(Constants.DATA_KONSULTASI, konsultasi.getConsultationId());
            dataP.putString(Constants.DATA_PROFILE, Constants.BEARER + uSignIn.getaUTHTOKEN());


            //String typeFile = PathUtil.getMimType(mContext, uri);

            String typeFile = com.transmedika.transmedikakitui.utils.TransmedikaUtils.getExt(path);
            if (typeFile.equalsIgnoreCase("jpg") || typeFile.equalsIgnoreCase("png") || typeFile.equalsIgnoreCase("jpeg")) {
                dataP.putString("type", Message.IMAGE_TYPE);
                if(!reupload) {
                    sendMessage(msgId, Message.IMAGE_TYPE, getString(R.string.uploading), path)
                            .saveEventually(e -> {
                                if (e == null) {
                                    jobUpload(dataP);
                                }
                            });
                }else {
                    jobUpload(dataP);
                }
            } else if (typeFile.equalsIgnoreCase("pdf") || typeFile.equalsIgnoreCase("doc") || typeFile.equalsIgnoreCase("docx")) {
                dataP.putString("type", Message.FILE_TYPE);
                if(!reupload) {
                    sendMessage(msgId, Message.FILE_TYPE, getString(R.string.uploading), path)
                            .saveEventually(e -> {
                                if (e == null) {
                                    jobUpload(dataP);
                                }
                            });
                }else {
                    jobUpload(dataP);
                }
            }
        }else {
            MsgUiUtil.showSnackBar(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0),
                    "Maaf lokasi file tidak ditemukan di tempat penyimpanan",mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
        }
    }

    private void jobUpload(Data.Builder dataP){
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(UploadFileChatWorkerP.class)
                .setInputData(dataP.build())
                .build();

        WorkManager.getInstance(mActivity.getApplicationContext()).enqueue(oneTimeWorkRequest);
    }

    @Override
    public void onDestroy() {
        if(receiver!=null) {
            receiver.unregisterNetworkCheck();
        }
        parseLiveQueryClient.unsubscribe(queryConsultation);
        parseLiveQueryClient.unsubscribe(queryChats);
        parseLiveQueryClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected){
            if(initParse) {
                updateConversationsLiveQuery();
                konsultasiLiveQuery();
                if(!flagHistori)
                    updateToRead();
            }else {
                if(!flagHistori)
                    MsgUiUtil.showSnackBar(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0),
                            getString(R.string.kembali_online),mContext, R.drawable.ic_check_24dp, R.color.green, Snackbar.LENGTH_LONG);
            }
        }else {
            if(!flagHistori)
                MsgUiUtil.showSnackBar(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0),
                        getString(R.string.tidak_ada_koneksi),mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.setBusyTelepon(false);
        //updateDeviceId();
    }

    /*private void updateDeviceId(){
        if(uSignIn!=null) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnSuccessListener(ConversationsSingleOnlyParseActivity.this, instanceIdResult -> {
                        Data.Builder dataP = new Data.Builder();
                        dataP.putString(Constants.DATA, instanceIdResult.getToken());

                        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(UpdateDeviceWorker.class)
                                .setInputData(dataP.build())
                                .build();

                        WorkManager.getInstance(getApplicationContext()).enqueue(oneTimeWorkRequest);
                    });
        }

    }*/

    private void feedbackConsultation() {
        if (!statusFeedback && feedbackKonsultasiDialog == null) {
            feedbackKonsultasiDialog = new FeedbackKonsultasiDialog(mContext);
            feedbackKonsultasiDialog.setDialogListener(feedbackParam -> {
                feedbackParam.setConsultationId(konsultasi.getConsultationId());
                mPresenter.postFeedback(mContext, uSignIn.getaUTHTOKEN(), feedbackParam);
            });
            feedbackKonsultasiDialog.show();
        }
    }

    @Override
    public void onFeedbackResp(BaseOResponse response) {
        if (response.getData() != null) {
            statusFeedback = true;
            NetkromDialog netkromDialog = new NetkromDialog(mContext, R.drawable.ic_info_checklis, getString(R.string.feedback_success), true,  Constants.ALERT_CORRECT);
            netkromDialog.show();
            netkromDialog.setOnCancelListener(dialogInterface -> {
                if (feedbackKonsultasiDialog != null && feedbackKonsultasiDialog.isShowing()) {
                    feedbackKonsultasiDialog.cancel();
                }
            });

            sendLocalBroadcast();
        }
    }

    private void sendLocalBroadcast(){
        BroadcastEvents.Event event = new BroadcastEvents.Event();
        event.setInitString(Constants.BROADCAST_FINISH_KONSULTASI);
        RxBus.getDefault().post(new BroadcastEvents(event));
    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        if (feedbackKonsultasiDialog != null && feedbackKonsultasiDialog.isShowing()) {
            feedbackKonsultasiDialog.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        parseLiveQueryClient.connectIfNeeded();
    }

    @Override
    public void jumpReconnect() {
        RxUtil.runOnUi(o -> {
            resep = false;
            catatan = false;
            spa = false;
            if(parseLiveQueryClient!=null && reconnectDialog.isShowing()){
                parseLiveQueryClient.reconnect();
            }
        });
    }

    @Override
    public void onLiveQueryClientConnected(ParseLiveQueryClient client) {
        Log.i(TAG, "ParseLiveQueryClient "+"status connected");
        RxUtil.runOnUi(o -> {
            if(reconnectDialog!=null)
                if(reconnectDialog.isShowing())
                    reconnectDialog.dismiss();
        });
    }

    @Override
    public void onLiveQueryClientDisconnected(ParseLiveQueryClient client, boolean userInitiated) {
        Log.i(TAG, "ParseLiveQueryClient "+"status disconnected");
        RxUtil.runOnUi(o -> {
            if(reconnectDialog == null) {
                reconnectDialog = new ReconnectDialog(mContext);
            }
            if(!reconnectDialog.isShowing()) {
                reconnectDialog.show();
            }
            mPresenter.reconnect();
        });
    }

    @Override
    public void onLiveQueryError(ParseLiveQueryClient client, LiveQueryException reason) {
        Log.i(TAG, "ParseLiveQueryClient "+"status Error "+ reason.getMessage());
    }

    @Override
    public void onSocketError(ParseLiveQueryClient client, Throwable reason) {
        Log.i(TAG, "ParseLiveQueryClient "+"status Socket Error "+ reason.getMessage());
    }
}
