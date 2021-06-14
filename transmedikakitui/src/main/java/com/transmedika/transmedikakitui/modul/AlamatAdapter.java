package com.transmedika.transmedikakitui.modul;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.databinding.ItemAlamatBinding;
import com.transmedika.transmedikakitui.models.bean.json.Alamat;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.TransmedikaSettings;

import java.util.List;


public class AlamatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Alamat> mList;
    private final Context context;
    private OnItemClickListener onItemClickListener;
    private final boolean flagSelected;
    private TransmedikaSettings transmedikaSettings;

    public AlamatAdapter(Context mContext, List<Alamat> mList, boolean flagSelected,
                         TransmedikaSettings transmedikaSettings) {
        this.flagSelected = flagSelected;
        this.mList = mList;
        this.context = mContext;
        this.transmedikaSettings = transmedikaSettings;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(ItemAlamatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ContentViewHolder) {
            Alamat alamat = mList.get(position);
            ContentViewHolder cViewHolder =  ((ContentViewHolder) holder);
            cViewHolder.binding.tvAlamat.setText(alamat.getAddress());
            cViewHolder.binding.tvTipeAlamat.setText(alamat.getAddressType());
            cViewHolder.binding.tvNote.setText(alamat.getNote());
            if(transmedikaSettings.getFontLight()!=null){
                cViewHolder.binding.tvNote.setCustomFont(context, transmedikaSettings.getFontLight());
            }

            switch (alamat.getAddressType()) {
                case Constants.ALAMAT_LAIN:
                    cViewHolder.binding.imgTipe.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_alamat_lain));
                    break;
                case Constants.ALAMAT_KANTOR:
                    cViewHolder.binding.imgTipe.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_alamat_kantor));
                    break;
                case Constants.ALAMAT_RUMAH:
                    cViewHolder.binding.imgTipe.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_alamat_rumah));
                    break;
            }

            cViewHolder.binding.imgTipe.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
            if(!flagSelected){
                cViewHolder.binding.imgEdit.setOnClickListener(v -> onItemClickListener.onEditClick(alamat, holder.getAdapterPosition()));
                cViewHolder.binding.imgHapus.setOnClickListener(v -> onItemClickListener.onHapusClick(alamat, holder.getAdapterPosition()));
                cViewHolder.binding.imgLove.setOnClickListener(v -> onItemClickListener.onLoveClick(alamat, holder.getAdapterPosition()));
                cViewHolder.binding.imgEdit.setVisibility(View.VISIBLE);
                cViewHolder.binding.imgHapus.setVisibility(View.VISIBLE);
                cViewHolder.binding.imgLove.setVisibility(View.INVISIBLE);
            }else {
                cViewHolder.binding.imgEdit.setVisibility(View.INVISIBLE);
                cViewHolder.binding.imgHapus.setVisibility(View.INVISIBLE);
                cViewHolder.binding.imgLove.setVisibility(View.INVISIBLE);
                cViewHolder.itemView.setOnClickListener(v -> onItemClickListener.onSelected(alamat, holder.getAdapterPosition()));
            }
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onEditClick(Alamat alamat, int pos);
        void onHapusClick(Alamat alamat, int pos);
        void onLoveClick(Alamat alamat, int pos);
        void onSelected(Alamat alamat, int pos);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ItemAlamatBinding binding;

        public ContentViewHolder(ItemAlamatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
