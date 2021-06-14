package com.transmedika.transmedikakitui.modul;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.databinding.ItemSpecialistBinding;
import com.transmedika.transmedikakitui.models.bean.json.KategoriObat;
import com.transmedika.transmedikakitui.models.bean.json.Specialist;
import com.transmedika.transmedikakitui.utils.ImageLoader;

import java.util.List;


public class CommonCategoriesAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<T> mList;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public CommonCategoriesAdapter(Context mContext, List<T> mList) {
        this.mList = mList;
        this.context = mContext;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(ItemSpecialistBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ContentViewHolder) {
            if(mList.get(position) instanceof Specialist) {
                Specialist spesialis = (Specialist) mList.get(position);
                ((ContentViewHolder) holder).binding.tvName.setText(spesialis.getSpecialist());
                ImageLoader.loadIcon(context, spesialis.getImage(), ((ContentViewHolder) holder).binding.img, R.drawable.bg_circle_place_holder);
                holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(spesialis, holder.getAdapterPosition()));
            }else if(mList.get(position) instanceof KategoriObat) {
                KategoriObat kategoriObat = (KategoriObat) mList.get(position);
                ((ContentViewHolder) holder).binding.tvName.setText(kategoriObat.getName());
                ImageLoader.loadIcon(context, kategoriObat.getImage(), ((ContentViewHolder) holder).binding.img, R.drawable.bg_circle_place_holder);
                ((ContentViewHolder) holder).itemView.setOnClickListener(v -> onItemClickListener.onItemClick(kategoriObat, holder.getAdapterPosition()));
            }
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Object o, int pos);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ItemSpecialistBinding binding;
        public ContentViewHolder(ItemSpecialistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
