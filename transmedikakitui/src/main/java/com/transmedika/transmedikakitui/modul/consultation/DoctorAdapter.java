package com.transmedika.transmedikakitui.modul.consultation;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseRecyclerViewAdapterPageAdapter;
import com.transmedika.transmedikakitui.databinding.ItemDoctorBinding;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.ImageLoader;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.util.List;


public class DoctorAdapter extends BaseRecyclerViewAdapterPageAdapter {

    private final List<Doctor> mList;
    private final Context context;
    private OnItemClickListener onItemClickListener;
    private final TransmedikaSettings transmedikaSettings;

    public DoctorAdapter(Context mContext, List<Doctor> mList, TransmedikaSettings transmedikaSettings) {
        this.mList = mList;
        this.context = mContext;
        this.transmedikaSettings = transmedikaSettings;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    protected RecyclerView.ViewHolder contentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(ItemDoctorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    protected String conditionLoading(int position) {
        Doctor dokter = mList.get(position);
        return dokter.getUuid();
    }

    @Override
    protected void onErrorClick(int position, int holderPosition) {
        onItemClickListener.onItemCobaLagiClick(mList.get(position),holderPosition);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        Doctor dokter = mList.get(position);
        if (holder instanceof ContentViewHolder) {
            ContentViewHolder cViewHolder = ((ContentViewHolder) holder);
            cViewHolder.binding.tvNama.setText(dokter.getFullName());
            cViewHolder.binding.tvSpesialis.setText(dokter.getSpecialist());
            cViewHolder.binding.tvDesc.setText(dokter.getDescription());

            cViewHolder.binding.tvHarga.setText(TransmedikaUtils.numberFormat().format(dokter.getRates()));
            cViewHolder.binding.tvHargaDiskon.setPaintFlags( cViewHolder.binding.tvHargaDiskon.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            cViewHolder.binding.tvPengalaman.setText(dokter.getExperience());
            cViewHolder.binding.tvRating.setText(dokter.getRating() != null && !dokter.getRating().equals("0") ? dokter.getRating(): "-");

            /*if(dokter.getVoucher()!=null && dokter.getVoucher()!=0){
                cViewHolder.binding.mTvHargaDisc.setVisibility(View.VISIBLE);
                cViewHolder.binding.mTvHargaDisc.setText(SystemUtil.numberFormat().format(dokter.getVoucher()));
            }else {
                cViewHolder.binding.mTvHargaDisc.setVisibility(View.INVISIBLE);
            }*/

            if(transmedikaSettings.getDoctorItemButtonChatBackground()!=null){
                cViewHolder.binding.btnChat.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                        TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorItemButtonChatBackground()),null));
            }

            if(dokter.getStatusDocter()!=null) {
                if (dokter.getStatusDocter().equals(Constants.ONLINE)) {
                    if(transmedikaSettings.getDoctorOnlineBackground()!=null){
                        cViewHolder.binding.imgStatus.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                                TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorOnlineBackground()),null));
                    }else {
                        cViewHolder.binding.imgStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_online));
                    }
                    cViewHolder.binding.btnChat.setEnabled(true);
                    cViewHolder.binding.tvDesc.setTextColor(ContextCompat.getColor(context, R.color.textDefault));
                } else if (dokter.getStatusDocter().equals(Constants.OFFLINE)) {
                    if(transmedikaSettings.getDoctorOfflineBackground()!=null){
                        cViewHolder.binding.imgStatus.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                                TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorOfflineBackground()),null));
                    }else {
                        cViewHolder.binding.imgStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_offline));
                    }
                    cViewHolder.binding.btnChat.setEnabled(false);
                    cViewHolder.binding.tvDesc.setTextColor(ContextCompat.getColor(context, R.color.gray3));
                } else {
                    if(transmedikaSettings.getDoctorBusyBackground()!=null){
                        cViewHolder.binding.imgStatus.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                                TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorBusyBackground()),null));
                    }else {
                        cViewHolder.binding.imgStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_busy));
                    }
                    cViewHolder.binding.btnChat.setEnabled(false);
                    cViewHolder.binding.tvDesc.setTextColor(ContextCompat.getColor(context, R.color.gray3));
                }
            }

            if(transmedikaSettings.getDoctorStatusIcon()!=null)
                cViewHolder.binding.imgStatus1.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                        TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorStatusIcon()),null));
            if(transmedikaSettings.getDoctorPriceIcon()!=null)
                cViewHolder.binding.imgPrice.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                        TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorPriceIcon()),null));
            if(transmedikaSettings.getDoctorRateIcon()!=null)
                cViewHolder.binding.imgRate.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                        TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorRateIcon()),null));
            if(transmedikaSettings.getDoctorExperienceIcon()!=null)
                cViewHolder.binding.imgPengalaman.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                        TransmedikaUtils.setDrawable(context,transmedikaSettings.getDoctorExperienceIcon()),null));

            ImageLoader.loadIcon(context, dokter.getProfileDoctor(),cViewHolder.binding.imgProfile, R.drawable.bg_circle_place_holder);

            cViewHolder.binding.imgProfile.setOnClickListener(v -> onItemClickListener.onProfileClick(dokter, holder.getAdapterPosition(),cViewHolder.binding.imgProfile));
            cViewHolder.binding.btnChat.setOnClickListener(v -> onItemClickListener.onItemClick(dokter, holder.getAdapterPosition(),cViewHolder.binding.imgProfile));
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Doctor dokter, int pos, ImageView imageView);
        void onProfileClick(Doctor dokter, int pos, ImageView imageView);
        void onItemCobaLagiClick(Doctor dokter, int pos);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ItemDoctorBinding binding;
        public ContentViewHolder(ItemDoctorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
