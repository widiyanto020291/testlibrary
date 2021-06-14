package com.transmedika.transmedikakitui.modul.consultation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transmedika.transmedikakitui.databinding.ItemFilterTopBinding;
import com.transmedika.transmedikakitui.models.bean.json.Filter;

import java.util.List;


public class FilterDoctorSelectedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Filter> mList;
    private OnItemClickListener onItemClickListener;

    public FilterDoctorSelectedAdapter(Context mContext, List<Filter> mList) {
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(ItemFilterTopBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Filter filter = mList.get(position);
        if (holder instanceof ContentViewHolder) {
            ((ContentViewHolder) holder).binding.tvTitle.setText(filter.getDescription());
            ((ContentViewHolder) holder).binding.imgClose.setOnClickListener(v -> onItemClickListener.onItemClick(filter, holder.getAdapterPosition()));
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Filter filter, int pos);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ItemFilterTopBinding binding;
        public ContentViewHolder(ItemFilterTopBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
