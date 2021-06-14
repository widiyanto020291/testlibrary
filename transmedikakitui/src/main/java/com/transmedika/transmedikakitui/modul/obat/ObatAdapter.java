package com.transmedika.transmedikakitui.modul.obat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseRecyclerViewAdapterPageAdapter;
import com.transmedika.transmedikakitui.databinding.ItemObatObatObatBinding;
import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.ImageLoader;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.util.List;

public class ObatAdapter extends BaseRecyclerViewAdapterPageAdapter {

    private final List<Obat> mList;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public ObatAdapter(Context mContext, List<Obat> mList) {
        this.mList = mList;
        this.context = mContext;
    }

    @Override
    protected RecyclerView.ViewHolder contentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(ItemObatObatObatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    protected String conditionLoading(int position) {
        Obat obat = mList.get(position);
        return obat.getSlug();
    }

    @Override
    protected void onErrorClick(int position, int holderPosition) {
        onItemClickListener.onItemCobaLagiClick(mList.get(position), holderPosition);
    }

    public boolean isPositionFooter(int position) {
        Obat obat = mList.get(position);
        return position == getItemCount() - 1 &&
                (obat.getSlug().equals(Constants.LOADING) || obat.getSlug().equals(Constants.ERROR));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        Obat obat = mList.get(position);
        if (holder instanceof ContentViewHolder) {
            ContentViewHolder cViewHolder = ((ContentViewHolder) holder);
            cViewHolder.binding.btnTambah.setOnClickListener(v -> {
                obat.setJumlah(1);
                mList.set(position,obat);
                cViewHolder.binding.llPlusMin.setVisibility(View.VISIBLE);
                cViewHolder.binding.btnTambah.setVisibility(View.GONE);
                cViewHolder.binding.tvJumlah.setText(String.valueOf(1));
                onItemClickListener.onPlusItemClick(obat, holder.getAdapterPosition());
            });

            if(obat.getJumlah()!=null && obat.getJumlah() > 0){
                cViewHolder.binding.llPlusMin.setVisibility(View.VISIBLE);
                cViewHolder.binding.btnTambah.setVisibility(View.GONE);
                cViewHolder.binding.tvJumlah.setText(String.valueOf(obat.getJumlah()));
            }else {
                cViewHolder.binding.llPlusMin.setVisibility(View.GONE);
                cViewHolder.binding.btnTambah.setVisibility(View.VISIBLE);
            }

            cViewHolder.binding.imgbMin.setOnClickListener(v -> {
                int jmlObat = obat.getJumlah();
                jmlObat--;
                obat.setJumlah(jmlObat);
                mList.set(position,obat);
                if(jmlObat == 0){
                    cViewHolder.binding.llPlusMin.setVisibility(View.GONE);
                    cViewHolder.binding.btnTambah.setVisibility(View.VISIBLE);
                    cViewHolder.binding.tvJumlah.setText(String.valueOf(jmlObat));
                    onItemClickListener.onDelete(obat, holder.getAdapterPosition());
                }else {
                    cViewHolder.binding.llPlusMin.setVisibility(View.VISIBLE);
                    cViewHolder.binding.btnTambah.setVisibility(View.GONE);
                    cViewHolder.binding.tvJumlah.setText(String.valueOf(jmlObat));
                    onItemClickListener.onMinusItemClick(obat, holder.getAdapterPosition());
                }
            });

            cViewHolder.binding.imgbPlus.setOnClickListener(v -> {
                int jmlObat = obat.getJumlah();
                jmlObat++;
                obat.setJumlah(jmlObat);
                mList.set(position,obat);
                cViewHolder.binding.llPlusMin.setVisibility(View.VISIBLE);
                cViewHolder.binding.btnTambah.setVisibility(View.GONE);
                cViewHolder.binding.tvJumlah.setText(String.valueOf(jmlObat));
                onItemClickListener.onPlusItemClick(obat, holder.getAdapterPosition());
            });

            cViewHolder.binding.tvName.setText(obat.getName());
            cViewHolder.binding.tvRange.setText(
                    TransmedikaUtils.numberFormat().format(obat.getMinPrices()).concat(" - ").concat(
                            TransmedikaUtils.numberFormat().format(obat.getMaxPrices())));
            ViewCompat.setTransitionName(cViewHolder.binding.image, "posX".concat(String.valueOf(position)));
            ImageLoader.loadIcon(context, obat.getImage(), cViewHolder.binding.image, R.drawable.bg_img_rec);
            cViewHolder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(obat, holder.getAdapterPosition(),  cViewHolder.binding.image));
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Obat o, int pos, ImageView imageView);
        void onPlusItemClick(Obat o, int pos);
        void onMinusItemClick(Obat o, int pos);
        void onDelete(Obat o, int pos);
        void onItemCobaLagiClick(Obat o, int pos);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ItemObatObatObatBinding binding;
        public ContentViewHolder(ItemObatObatObatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
