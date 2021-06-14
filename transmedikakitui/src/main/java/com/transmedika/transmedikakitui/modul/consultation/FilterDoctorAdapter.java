package com.transmedika.transmedikakitui.modul.consultation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transmedika.transmedikakitui.databinding.ItemFilterDialogBinding;
import com.transmedika.transmedikakitui.databinding.ItemHeaderFilterBinding;
import com.transmedika.transmedikakitui.models.bean.json.Filter;
import com.transmedika.transmedikakitui.utils.Constants;

import java.util.List;


public class FilterDoctorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Filter> mList;
    private OnItemCheckedChangeListener onItemCheckedChangeListener;

    private static final int VIEW_CONTENT = 9;
    private static final int VIEW_HEADER = 10;

    public FilterDoctorAdapter(List<Filter> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Filter filter = mList.get(position);
        if (filter.getValue().equalsIgnoreCase(Constants.HEADER_FILTER)) {
            return VIEW_HEADER;
        }else{
            return VIEW_CONTENT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_CONTENT) {
            return new ContentViewHolder(ItemFilterDialogBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }else {
            return new HeaderViewHolder(ItemHeaderFilterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Filter filter = mList.get(position);
        if (holder instanceof ContentViewHolder) {
            ((ContentViewHolder) holder).binding.tvTitle.setText(filter.getDescription());
            ((ContentViewHolder) holder).binding.cb.setChecked(filter.getChecked() != null && filter.getChecked());
            ((ContentViewHolder) holder).binding.cb.setOnCheckedChangeListener((buttonView, isChecked) -> onItemCheckedChangeListener.onCheckedChanged(filter, holder.getAdapterPosition(), isChecked));
        }else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).binding.tvTitle.setText(filter.getSymbol());
        }
    }


    public void setOnItemCheckedChangeListener(OnItemCheckedChangeListener onItemCheckedChangeListener) {
        this.onItemCheckedChangeListener = onItemCheckedChangeListener;
    }

    public interface OnItemCheckedChangeListener {
        void onCheckedChanged(Filter filter, int pos, boolean checked);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ItemFilterDialogBinding binding;
        public ContentViewHolder(ItemFilterDialogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        ItemHeaderFilterBinding binding;
        public HeaderViewHolder(ItemHeaderFilterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
