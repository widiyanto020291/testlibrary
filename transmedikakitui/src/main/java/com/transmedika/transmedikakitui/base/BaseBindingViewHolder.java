package com.transmedika.transmedikakitui.base;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public abstract class BaseBindingViewHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {
    public VB binding;
    public BaseBindingViewHolder(@NonNull VB binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
