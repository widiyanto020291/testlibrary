package com.transmedika.transmedikakitui.modul;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.databinding.ItemJenisAlamatBinding;
import com.transmedika.transmedikakitui.models.bean.BaseDropDown;

import java.util.List;


public class TambahAlamatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<BaseDropDown> mList;
    private final Context context;
    private OnItemClickListener onItemClickListener;
    private int selected=0;

    public TambahAlamatAdapter(Context mContext, List<BaseDropDown> mList) {
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
        return new ContentViewHolder(ItemJenisAlamatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ContentViewHolder) {
            BaseDropDown baseDropDown = mList.get(position);
            ContentViewHolder cViewHolder = ((ContentViewHolder) holder);
            cViewHolder.binding.tvNama.setText(baseDropDown.getName());
            cViewHolder.binding.img.setImageResource(baseDropDown.getImg());

            if(position==selected){
                cViewHolder.binding.llMain.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_alamat_selected));
                cViewHolder.binding.tvNama.setTextColor(ContextCompat.getColor(context,R.color.white));
                cViewHolder.binding.img.setColorFilter(ContextCompat.getColor(context,R.color.white));
            }else {
                cViewHolder.binding.llMain.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_alamat_default));
                cViewHolder.binding.tvNama.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                cViewHolder.binding.img.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
            }

            holder.itemView.setOnClickListener(v -> {
                if(selected==position) return;
                int previous = selected;
                selected=position;
                notifyItemChanged(previous);
                notifyItemChanged(selected);
                onItemClickListener.onItemClick(baseDropDown);
            });
        }
    }


    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(BaseDropDown b);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ItemJenisAlamatBinding binding;
        public ContentViewHolder(ItemJenisAlamatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
