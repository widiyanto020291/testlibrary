package com.transmedika.transmedikakitui.modul.consultation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseUser;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseBindingViewHolder;
import com.transmedika.transmedikakitui.databinding.ItemChatJadwalBinding;
import com.transmedika.transmedikakitui.databinding.ItemChatObatBinding;
import com.transmedika.transmedikakitui.databinding.ItemChatPhrBinding;
import com.transmedika.transmedikakitui.databinding.ItemChatSpaBinding;
import com.transmedika.transmedikakitui.databinding.ItemDateHeaderBinding;
import com.transmedika.transmedikakitui.databinding.ItemKonsultasiBerakhirBinding;
import com.transmedika.transmedikakitui.databinding.ItemChatMeBinding;
import com.transmedika.transmedikakitui.databinding.ItemChatOtherBinding;
import com.transmedika.transmedikakitui.databinding.ItemChatImageMeBinding;
import com.transmedika.transmedikakitui.databinding.ItemChatImageOtherBinding;
import com.transmedika.transmedikakitui.databinding.ItemChatFileMeBinding;
import com.transmedika.transmedikakitui.databinding.ItemChatFileOtherBinding;
import com.transmedika.transmedikakitui.databinding.ItemChatVideoMeBinding;
import com.transmedika.transmedikakitui.databinding.ItemChatVideoOtherBinding;
import com.transmedika.transmedikakitui.databinding.ItemChatUrlMeBinding;
import com.transmedika.transmedikakitui.databinding.ItemChatUrlOtherBinding;
import com.transmedika.transmedikakitui.models.bean.json.CatatanDokter;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.ICD;
import com.transmedika.transmedikakitui.models.bean.json.Resep;
import com.transmedika.transmedikakitui.models.bean.json.ResepObat;
import com.transmedika.transmedikakitui.models.bean.parse.Message;
import com.transmedika.transmedikakitui.utils.DateUtil;
import com.transmedika.transmedikakitui.utils.ImageLoader;
import com.transmedika.transmedikakitui.utils.SpanUtils;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;
import com.transmedika.transmedikakitui.widget.NetkromTextView;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Message> mList;
    private final Context context;

    private static final int VIEW_TYPE_DATE_MESSAGE = 9;
    private static final int VIEW_TYPE_USER_MESSAGE_ME = 10;
    private static final int VIEW_TYPE_USER_MESSAGE_OTHER = 11;
    private static final int VIEW_TYPE_USER_MESSAGE_IMAGE_ME = 12;
    private static final int VIEW_TYPE_USER_MESSAGE_IMAGE_OTHER = 13;
    private static final int VIEW_TYPE_USER_MESSAGE_VIDEO_ME = 14;
    private static final int VIEW_TYPE_USER_MESSAGE_VIDEO_OTHER = 15;
    private static final int VIEW_TYPE_USER_MESSAGE_URL_ME = 16;
    private static final int VIEW_TYPE_USER_MESSAGE_URL_OTHER = 17;
    private static final int VIEW_TYPE_KONSULTASI_BERAKHIR = 18;
    private static final int VIEW_TYPE_KONSULTASI_RESEP = 19;
    private static final int VIEW_TYPE_KONSULTASI_SPA = 20;
    private static final int VIEW_TYPE_KONSULTASI_NOTE_NEXT_SCHEDULE = 21;
    private static final int VIEW_TYPE_USER_MESSAGE_FILE_ME = 22;
    private static final int VIEW_TYPE_USER_MESSAGE_FILE_OTHER = 23;
    private static final int VIEW_TYPE_PHR_REQ = 24;

    private ParseUser user;
    private OnItemClickListeners onItemClickListeners;
    private final Doctor dokter;

    public ConversationAdapter(Context mContext, List<Message> mList, ParseUser user, Doctor dokter) {
        this.user = user;
        this.mList = mList;
        this.context = mContext;
        this.dokter = dokter;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mList.get(position);
        if (Message.TEXT_TYPE.equalsIgnoreCase(message.getKind())) {
            if (user!=null && user.getObjectId().equals(message.getSenderId().getObjectId())) {
                return VIEW_TYPE_USER_MESSAGE_ME;
            } else {
                return VIEW_TYPE_USER_MESSAGE_OTHER;
            }
        }else if(Message.IMAGE_TYPE.equalsIgnoreCase(message.getKind())){
            if (user!=null && user.getObjectId().equals(message.getSenderId().getObjectId())) {
                return VIEW_TYPE_USER_MESSAGE_IMAGE_ME;
            } else {
                return VIEW_TYPE_USER_MESSAGE_IMAGE_OTHER;
            }
        }else if(Message.FILE_TYPE.equalsIgnoreCase(message.getKind())){
            if (user!=null && user.getObjectId().equals(message.getSenderId().getObjectId())) {
                return VIEW_TYPE_USER_MESSAGE_FILE_ME;
            } else {
                return VIEW_TYPE_USER_MESSAGE_FILE_OTHER;
            }
        }else if(Message.VIDEO_TYPE.equalsIgnoreCase(message.getKind())){
            if (user!=null && user.getObjectId().equals(message.getSenderId().getObjectId())) {
                return VIEW_TYPE_USER_MESSAGE_VIDEO_ME;
            } else {
                return VIEW_TYPE_USER_MESSAGE_VIDEO_OTHER;
            }
        }else if(Message.URL_TYPE.equalsIgnoreCase(message.getKind())){
            if (user!=null && user.getObjectId().equals(message.getSenderId().getObjectId())) {
                return VIEW_TYPE_USER_MESSAGE_URL_ME;
            } else {
                return VIEW_TYPE_USER_MESSAGE_URL_OTHER;
            }
        }else if(Message.DATE_TYPE.equalsIgnoreCase(message.getKind())) {
            return VIEW_TYPE_DATE_MESSAGE;
        }else if (Message.SESSION_END_TYPE.equalsIgnoreCase(message.getKind())) {
            return VIEW_TYPE_KONSULTASI_BERAKHIR;
        }else if (Message.RESEP_OBAT_TYPE.equalsIgnoreCase(message.getKind())) {
            return VIEW_TYPE_KONSULTASI_RESEP;
        }else if (Message.RESEP_OBAT_SPA.equalsIgnoreCase(message.getKind())) {
            return VIEW_TYPE_KONSULTASI_SPA;
        }else if (Message.RESEP_OBAT_CATATAN.equalsIgnoreCase(message.getKind())) {
            return VIEW_TYPE_KONSULTASI_NOTE_NEXT_SCHEDULE;
        }else if(Message.PHR_REQ_TYPE.equalsIgnoreCase(message.getKind())){
            return VIEW_TYPE_PHR_REQ;
        }else {
            return VIEW_TYPE_USER_MESSAGE_ME;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_USER_MESSAGE_ME) {
            return new ChatMeViewHolder(ItemChatMeBinding.inflate(inflater, parent, false));
        }else if(viewType == VIEW_TYPE_USER_MESSAGE_OTHER){
            return new ChatOtherHolder(ItemChatOtherBinding.inflate(inflater, parent, false));
        }else if(viewType == VIEW_TYPE_USER_MESSAGE_IMAGE_ME){
            return new ChatMeImageViewHolder(ItemChatImageMeBinding.inflate(inflater, parent, false));
        }else if(viewType == VIEW_TYPE_USER_MESSAGE_IMAGE_OTHER){
            return new ChatOtherImageHolder(ItemChatImageOtherBinding.inflate(inflater, parent, false));
        }else if(viewType == VIEW_TYPE_USER_MESSAGE_FILE_ME){
            return new ChatMeFileViewHolder(ItemChatFileMeBinding.inflate(inflater, parent, false));
        }else if(viewType == VIEW_TYPE_USER_MESSAGE_FILE_OTHER){
            return new ChatOtherFileViewHolder(ItemChatFileOtherBinding.inflate(inflater, parent, false));
        }else if(viewType == VIEW_TYPE_USER_MESSAGE_VIDEO_ME){
            return new ChatMeVideoViewHolder(ItemChatVideoMeBinding.inflate(inflater, parent, false));
        }else if(viewType == VIEW_TYPE_USER_MESSAGE_VIDEO_OTHER){
            return new ChatOtherVideoHolder(ItemChatVideoOtherBinding.inflate(inflater, parent, false));
        }else if(viewType == VIEW_TYPE_USER_MESSAGE_URL_ME){
            return new ChatMeUrlViewHolder(ItemChatUrlMeBinding.inflate(inflater, parent, false));
        }else if(viewType == VIEW_TYPE_USER_MESSAGE_URL_OTHER) {
            return new ChatOtherUrlHolder(ItemChatUrlOtherBinding.inflate(inflater, parent, false));
        }else if(viewType == VIEW_TYPE_KONSULTASI_BERAKHIR){
            return new KonsultasiBerakhirHolder(ItemKonsultasiBerakhirBinding.inflate(inflater, parent, false));
        }else if(viewType == VIEW_TYPE_KONSULTASI_RESEP){
            return new ResepHolder(ItemChatObatBinding.inflate(inflater, parent, false));
        }else if(viewType == VIEW_TYPE_KONSULTASI_SPA){
            return new SpaHolder(ItemChatSpaBinding.inflate(inflater, parent, false));
        }else if(viewType == VIEW_TYPE_KONSULTASI_NOTE_NEXT_SCHEDULE){
            return new JadwalHolder(ItemChatJadwalBinding.inflate(inflater, parent, false));
        }else if(viewType == VIEW_TYPE_PHR_REQ){
            return new ChatPhrHolder(ItemChatPhrBinding.inflate(inflater, parent, false));
        }else {
            return new ChatDateHolder(ItemDateHeaderBinding.inflate(inflater, parent, false));
        }
    }

    private void setStatusMessage(Message message, ImageView imgStatus){
        switch (message.getStatus()) {
            case Message.NOT_SEND_STATUS:
                imgStatus.setImageResource(R.drawable.not_sent);
                break;
            case Message.SENT_STATUS:
                imgStatus.setImageResource(R.drawable.sent);
                break;
            case Message.RECEIVED_STATUS:
                imgStatus.setImageResource(R.drawable.received);
                break;
            case Message.SEEN_STATUS:
                imgStatus.setImageResource(R.drawable.readed);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Message message = mList.get(holder.getAdapterPosition());
        if(holder instanceof ChatMeViewHolder) {
            ChatMeViewHolder cMViewHolder =  ((ChatMeViewHolder) holder);
            setStatusMessage(message, cMViewHolder.binding.imgStatus);
            cMViewHolder.binding.tvText.setText(message.getText());
            cMViewHolder.binding.tvDate.setText(DateUtil.HHmm(message.getDate()));
        }else if (holder instanceof ChatOtherHolder) {
            ChatOtherHolder cOHolder = ((ChatOtherHolder) holder);
            cOHolder.binding.tvText.setText(message.getText());
            cOHolder.binding.tvDate.setText(DateUtil.HHmm(message.getDate()));
        }else if (holder instanceof ChatPhrHolder) {
            ChatPhrHolder cPViewHolder = ((ChatPhrHolder) holder);
            cPViewHolder.binding.tvDate.setText(DateUtil.HHmm(message.getDate()));
            if(message.getText().equalsIgnoreCase(Message.PHR_REQ_STATUS_ALLOWED)){
                cPViewHolder.binding.llBtn.setVisibility(View.GONE);
                cPViewHolder.binding.tvJawaban.setText(R.string.anda_telah_memberi_izin);
                cPViewHolder.binding.tvJawaban.setVisibility(View.VISIBLE);
                cPViewHolder.binding.tvJawaban.setTextColor(ContextCompat.getColor(context, R.color.green));
            }else if(message.getText().equalsIgnoreCase(Message.PHR_REQ_STATUS_DENIED)) {
                cPViewHolder.binding.tvJawaban.setText(R.string.anda_telah_menolak);
                cPViewHolder.binding.tvJawaban.setVisibility(View.VISIBLE);
                cPViewHolder.binding.tvJawaban.setTextColor(ContextCompat.getColor(context, R.color.red));
                cPViewHolder.binding.llBtn.setVisibility(View.GONE);
            }else{
                cPViewHolder.binding.llBtn.setVisibility(View.VISIBLE);
                cPViewHolder.binding.tvJawaban.setVisibility(View.GONE);
            }
            cPViewHolder.binding.btnYa.setOnClickListener(v -> onItemClickListeners.onPhrYa(message, holder.getAdapterPosition()));
            cPViewHolder.binding.btnTidak.setOnClickListener(v -> onItemClickListeners.onPhrTidak(message, holder.getAdapterPosition()));
        }else if (holder instanceof ChatDateHolder) {
            ((ChatDateHolder) holder).binding.tvDateHeader.setText(DateUtil.ddMMMyyyy(message.getDate()));
        }else if(holder instanceof ChatMeImageViewHolder){
            ChatMeImageViewHolder cMIViewHolder = ((ChatMeImageViewHolder) holder);
            setStatusMessage(message, cMIViewHolder.binding.imgStatus);
            cMIViewHolder.binding.tvDate.setText(DateUtil.HHmm(message.getDate()));
            ImageLoader.loadResize(context, message.getText(),cMIViewHolder.binding.imageFile, R.color.line_light);
            if(message.getText().startsWith(context.getString(R.string.gagal_upload))){
                cMIViewHolder.binding.tvReupload.setVisibility(View.VISIBLE);
                cMIViewHolder.binding.imgErrorInfo.setVisibility(View.VISIBLE);
            }else {
                cMIViewHolder.binding.tvReupload.setVisibility(View.GONE);
                cMIViewHolder.binding.imgErrorInfo.setVisibility(View.GONE);
            }

            cMIViewHolder.binding.tvReupload.setOnClickListener(v -> {
                if(message.getFileLocal()!=null){
                    cMIViewHolder.binding.tvReupload.setVisibility(View.GONE);
                    cMIViewHolder.binding.imgErrorInfo.setVisibility(View.GONE);
                    message.setText(context.getString(R.string.uploading));
                }
                onItemClickListeners.uploadUlang(message, holder.getAdapterPosition());
            });

            cMIViewHolder.binding.imageFile.setOnClickListener(v -> {
                if(cMIViewHolder.binding.imgErrorInfo.getVisibility() == View.GONE)
                    onItemClickListeners.onImageClick(message, holder.getAdapterPosition());
            });

            cMIViewHolder.binding.imgErrorInfo.setOnClickListener(v -> onItemClickListeners.showInfoError(message, holder.getAdapterPosition()));

        }else if(holder instanceof ChatOtherImageHolder){
            ((ChatOtherImageHolder) holder).binding.tvDate.setText(DateUtil.HHmm(message.getDate()));
            ImageLoader.loadResize(context, message.getText(),((ChatOtherImageHolder) holder).binding.imageFile, R.color.line_light);
            ((ChatOtherImageHolder) holder).binding.imageFile.setOnClickListener(v -> onItemClickListeners.onImageClick(message, holder.getAdapterPosition()));
        }else if(holder instanceof ChatMeFileViewHolder){
            ChatMeFileViewHolder cMFViewHolder = ((ChatMeFileViewHolder) holder);
            setStatusMessage(message, cMFViewHolder.binding.imgStatus);
            cMFViewHolder.binding.tvDate.setText(DateUtil.HHmm(message.getDate()));
            cMFViewHolder.binding.tvFile.setText(TransmedikaUtils.getFilenameUrlExt(message.getText(),'/'));
            if(message.getFileLocal()!=null)
                cMFViewHolder.binding.tvText.setText(TransmedikaUtils.getFilenameUrlExt(message.getFileLocal(),'.'));
            if(message.getText().startsWith(context.getString(R.string.gagal_upload))){
                cMFViewHolder.binding.tvReupload.setVisibility(View.VISIBLE);
                cMFViewHolder.binding.imgErrorInfo.setVisibility(View.VISIBLE);
                cMFViewHolder.binding.tvFile.setText(context.getString(R.string.gagal_upload));
                cMFViewHolder.binding.tvFile.setVisibility(View.GONE);
                if(message.getFileLocal()!=null)
                    cMFViewHolder.binding.tvText.setText(TransmedikaUtils.getFilenameUrlExt(message.getFileLocal(),'.'));
            }else {
                cMFViewHolder.binding.tvReupload.setVisibility(View.GONE);
                cMFViewHolder.binding.imgErrorInfo.setVisibility(View.GONE);
                cMFViewHolder.binding.tvFile.setVisibility(View.VISIBLE);
            }

            cMFViewHolder.binding.tvReupload.setOnClickListener(v -> {
                if(message.getFileLocal()!=null) {
                    cMFViewHolder.binding.tvReupload.setVisibility(View.GONE);
                    cMFViewHolder.binding.imgErrorInfo.setVisibility(View.GONE);
                    cMFViewHolder.binding.tvFile.setVisibility(View.VISIBLE);
                    cMFViewHolder.binding.tvFile.setText(context.getString(R.string.uploading));
                    message.setText(context.getString(R.string.uploading));
                }
                onItemClickListeners.uploadUlang(message, holder.getAdapterPosition());
            });

            cMFViewHolder.binding.llDownload.setOnClickListener(v -> {
                if(cMFViewHolder.binding.imgErrorInfo.getVisibility() == View.GONE)
                    onItemClickListeners.onFileClick(message, holder.getAdapterPosition());
            });

            cMFViewHolder.binding.imgErrorInfo.setOnClickListener(v -> onItemClickListeners.showInfoError(message, holder.getAdapterPosition()));
        }else if(holder instanceof ChatOtherFileViewHolder){
            ChatOtherFileViewHolder cOFViewHolder = ((ChatOtherFileViewHolder) holder);
            cOFViewHolder.binding.tvDate.setText(DateUtil.HHmm(message.getDate()));
            cOFViewHolder.binding.tvFile.setText(TransmedikaUtils.getFilenameUrlExt(message.getText(),'/'));
            cOFViewHolder.binding.tvText.setText(TransmedikaUtils.getFilenameUrlExt(message.getText(),'.'));
            cOFViewHolder.binding.llDownload.setOnClickListener(v -> onItemClickListeners.onFileClick(message, holder.getAdapterPosition()));
        }else if(holder instanceof KonsultasiBerakhirHolder){
            ((KonsultasiBerakhirHolder) holder).binding.tvKonsultasiBerakhir.setText(message.getText());
        }else if(holder instanceof ResepHolder) {
            ResepHolder rViewHolder = ((ResepHolder) holder);
            if(message.getText()!=null){
                rViewHolder.binding.llObat.removeAllViews();
                Resep reseps = TransmedikaUtils.gsonBuilder().fromJson(message.getText(), Resep.class);
                if(reseps!=null && reseps.getReseps()!=null  && reseps.getReseps().size() > 0){
                    for (ResepObat resep : reseps.getReseps()) {
                        View viewV1 = View.inflate(context, R.layout.item_obat_obat, null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(TransmedikaUtils.dip2px(context, 0), 0, TransmedikaUtils.dip2px(context, 0), 0);
                        viewV1.setLayoutParams(params);
                        NetkromTextView mTvName = viewV1.findViewById(R.id.tv_name);
                        NetkromTextView mTvJumlah = viewV1.findViewById(R.id.tv_jumlah);
                        mTvName.setText(resep.getMedicinesName());
                        mTvJumlah.setText(String.valueOf(resep.getQty()).concat(" ").concat(resep.getUnit()));
                        rViewHolder.binding.llObat.addView(viewV1);
                    }
                    if(reseps.getExpires()!=null) {
                        rViewHolder.binding.tvExpires.setText(DateUtil.dateType9(reseps.getExpires()));
                    }
                }
                rViewHolder.binding.tvDate.setText(DateUtil.HHmm(message.getDate()));
                rViewHolder.binding.tvHeaderDokter.setText(dokter.getFullName().concat(" mengirimkan resep"));
                SpanUtils spanUtils = new SpanUtils(context);
                spanUtils.setSpan(rViewHolder.binding.tvDetail, context.getString(R.string.detail),context.getResources().getColor(R.color.textDefault),context.getString(R.string.detail));
                spanUtils.setSpanClickListener(() -> onItemClickListeners.onDetailObatClick(message, position));
                holder.itemView.setOnClickListener(v -> onItemClickListeners.onDetailObatClick(message, position));
            }
        }else if(holder instanceof SpaHolder){
            SpaHolder sPAViewHolder = ((SpaHolder) holder);
            sPAViewHolder.binding.tvSymptoms.setText(message.getSymtomps());
            sPAViewHolder.binding.tvPosibleDiagnosis.setText(message.getPossibleDiganosis());
            sPAViewHolder.binding.tvAdvice.setText(message.getAdvice());
            sPAViewHolder.binding.tvDate.setText(DateUtil.HHmm(message.getDate()));
            List<ICD> icdList = message.getSpa().getSpa().getIcd();
            if (icdList != null) {
                StringBuilder builder = new StringBuilder();
                int advLength = icdList.size();
                if (advLength > 0) {
                    sPAViewHolder.binding.tvLabelIcd.setVisibility(View.VISIBLE);
                    sPAViewHolder.binding.tvIcd.setVisibility(View.VISIBLE);
                    for (int i = 0; i < advLength ; i++) {
                        ICD icd = icdList.get(i);
                        if (i == advLength - 1) {
                            builder.append("- ".concat(icd.getName()));
                        }else {
                            builder.append("- ".concat(icd.getName().concat(System.lineSeparator())));
                        }
                    }
                    sPAViewHolder.binding.tvIcd.setText(builder.toString());
                } else {
                    sPAViewHolder.binding.tvLabelIcd.setVisibility(View.GONE);
                    sPAViewHolder.binding.tvIcd.setVisibility(View.GONE);
                }
            }
            SpanUtils spanUtils = new SpanUtils(context);
            spanUtils.setSpan(sPAViewHolder.binding.tvDetail, context.getString(R.string.detail),context.getResources().getColor(R.color.textDefault),context.getString(R.string.detail));
            spanUtils.setSpanClickListener(() -> onItemClickListeners.onDetailSpaClick(message, position));
            holder.itemView.setOnClickListener(v -> onItemClickListeners.onDetailSpaClick(message, position));
            sPAViewHolder.binding.tvHeaderDokter.setText(dokter.getFullName().concat(" mendiagnosa"));
        }else if(holder instanceof JadwalHolder) {
            JadwalHolder jViewHolder = ((JadwalHolder) holder);
            if (message.getText() != null) {
                CatatanDokter catatanDokter = TransmedikaUtils.gsonBuilder().fromJson(message.getText(), CatatanDokter.class);
                jViewHolder.binding.tvJadwalBerikutnya.setText(DateUtil.dateType9(catatanDokter.getDate()));
                jViewHolder.binding.tvDate.setText(DateUtil.HHmm(message.getDate()));
                jViewHolder.binding.tvHeaderDokter.setText(dokter.getFullName().concat(" mengirimkan catatan"));
                SpanUtils spanUtils = new SpanUtils(context);
                spanUtils.setSpan(jViewHolder.binding.tvDetail, context.getString(R.string.detail),context.getResources().getColor(R.color.textDefault),context.getString(R.string.detail));
                spanUtils.setSpanClickListener(() -> onItemClickListeners.onDetailNoteClick(message, position));
                holder.itemView.setOnClickListener(v -> onItemClickListeners.onDetailNoteClick(message, position));

                if(catatanDokter.getNote()!=null && catatanDokter.getNote().length() > 100) {
                    String text = catatanDokter.getNote();
                    String part = catatanDokter.getNote().substring(80, 100);
                    String partX = catatanDokter.getNote().substring(100, text.length());
                    String result =  text.replace(partX,"");
                    SpanUtils spanUtils2 = new SpanUtils(context);
                    spanUtils2.setSpanMore(jViewHolder.binding.tvCatatan, result, R.color.red, part);
                }else {
                    jViewHolder.binding.tvCatatan.setText(catatanDokter.getNote());
                }
            }
        }
    }

    public void setOnItemClickListeners(OnItemClickListeners onItemClickListeners) {
        this.onItemClickListeners = onItemClickListeners;
    }

    public void setUser(ParseUser user) {
        this.user = user;
    }

    public interface OnItemClickListeners{
        void onDetailObatClick(Message message, int pos);
        void onFileClick(Message message, int pos);
        void onImageClick(Message message, int pos);
        void onPhrYa(Message message, int pos);
        void onPhrTidak(Message message, int pos);
        void uploadUlang(Message message, int pos);
        void onDetailSpaClick(Message message, int pos);
        void onDetailNoteClick(Message message, int pos);
        void showInfoError(Message message, int pos);
    }

    public static class ChatMeViewHolder extends BaseBindingViewHolder<ItemChatMeBinding> {
        public ChatMeViewHolder(@NonNull ItemChatMeBinding binding) {
            super(binding);
        }
    }

    public static class ChatOtherHolder extends BaseBindingViewHolder<ItemChatOtherBinding> {
        public ChatOtherHolder(@NonNull ItemChatOtherBinding binding) {
            super(binding);
        }
    }

    public static class ChatPhrHolder extends BaseBindingViewHolder<ItemChatPhrBinding> {

        public ChatPhrHolder(@NonNull ItemChatPhrBinding binding) {
            super(binding);
        }
    }

    public static class ChatMeFileViewHolder extends BaseBindingViewHolder<ItemChatFileMeBinding> {

        public ChatMeFileViewHolder(@NonNull ItemChatFileMeBinding binding) {
            super(binding);
        }
    }

    public static class ChatOtherFileViewHolder extends BaseBindingViewHolder<ItemChatFileOtherBinding> {

        public ChatOtherFileViewHolder(@NonNull ItemChatFileOtherBinding binding) {
            super(binding);
        }
    }

    public static class ChatMeImageViewHolder extends BaseBindingViewHolder<ItemChatImageMeBinding> {

        public ChatMeImageViewHolder(@NonNull ItemChatImageMeBinding binding) {
            super(binding);
        }
    }

    public static class ChatOtherImageHolder extends BaseBindingViewHolder<ItemChatImageOtherBinding> {

        public ChatOtherImageHolder(@NonNull ItemChatImageOtherBinding binding) {
            super(binding);
        }
    }

    public static class ChatMeVideoViewHolder extends BaseBindingViewHolder<ItemChatVideoMeBinding> {


        public ChatMeVideoViewHolder(@NonNull ItemChatVideoMeBinding binding) {
            super(binding);
        }
    }

    public static class ChatOtherVideoHolder extends BaseBindingViewHolder<ItemChatVideoOtherBinding> {

        public ChatOtherVideoHolder(@NonNull ItemChatVideoOtherBinding binding) {
            super(binding);
        }
    }

    public static class ChatMeUrlViewHolder extends BaseBindingViewHolder<ItemChatUrlMeBinding> {

        public ChatMeUrlViewHolder(@NonNull ItemChatUrlMeBinding binding) {
            super(binding);
        }
    }

    public static class ChatOtherUrlHolder extends BaseBindingViewHolder<ItemChatUrlOtherBinding> {

        public ChatOtherUrlHolder(@NonNull ItemChatUrlOtherBinding binding) {
            super(binding);
        }
    }

    public static class ChatDateHolder extends BaseBindingViewHolder<ItemDateHeaderBinding> {

        public ChatDateHolder(@NonNull ItemDateHeaderBinding binding) {
            super(binding);
        }
    }

    public static class KonsultasiBerakhirHolder extends BaseBindingViewHolder<ItemKonsultasiBerakhirBinding> {

        public KonsultasiBerakhirHolder(@NonNull ItemKonsultasiBerakhirBinding binding) {
            super(binding);
        }
    }

    public static class ResepHolder extends BaseBindingViewHolder<ItemChatObatBinding> {

        public ResepHolder(@NonNull ItemChatObatBinding binding) {
            super(binding);
        }
    }

    public static class SpaHolder extends BaseBindingViewHolder<ItemChatSpaBinding> {

        public SpaHolder(@NonNull ItemChatSpaBinding binding) {
            super(binding);
        }
    }

    public static class JadwalHolder extends BaseBindingViewHolder<ItemChatJadwalBinding> {

        public JadwalHolder(@NonNull ItemChatJadwalBinding binding) {
            super(binding);
        }
    }

}
