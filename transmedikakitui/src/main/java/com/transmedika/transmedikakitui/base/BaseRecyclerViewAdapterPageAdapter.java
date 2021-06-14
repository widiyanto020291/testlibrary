package com.transmedika.transmedikakitui.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transmedika.transmedikakitui.databinding.ItemMoreBinding;
import com.transmedika.transmedikakitui.databinding.ItemRetryBinding;
import com.transmedika.transmedikakitui.utils.Constants;


public abstract class BaseRecyclerViewAdapterPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_LOADING = 10;
    private static final int VIEW_ERROR = 11;
    private static final int VIEW_CONTENT = 9;
    protected abstract RecyclerView.ViewHolder contentViewHolder(@NonNull ViewGroup parent, int viewType);
    protected abstract String conditionLoading(int position);
    protected abstract void onErrorClick(int position, int holderPosition);

    @Override
    public int getItemViewType(int position) {
        if (conditionLoading(position).equalsIgnoreCase(Constants.LOADING)) {
            return VIEW_LOADING;
        }else if (conditionLoading(position).equalsIgnoreCase(Constants.ERROR)) {
            return VIEW_ERROR;
        }else{
            return VIEW_CONTENT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_ERROR) {
            return new ErrorViewHolder(ItemRetryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }else if (viewType == VIEW_LOADING){
            return new LoadingViewHolder(ItemMoreBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }else {
            return contentViewHolder(parent,viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ErrorViewHolder) {
            ((ErrorViewHolder) holder).binding.rlMain.setOnClickListener(v -> onErrorClick(position, holder.getAdapterPosition()));
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ItemMoreBinding binding;
        public LoadingViewHolder(ItemMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class ErrorViewHolder extends RecyclerView.ViewHolder {
        ItemRetryBinding binding;
        public ErrorViewHolder(ItemRetryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
