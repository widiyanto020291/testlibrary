package com.transmedika.transmedikakitui.presenter.consultation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transmedika.transmedikakitui.base.BaseBindingViewHolder;
import com.transmedika.transmedikakitui.databinding.ItemObatBinding;
import com.transmedika.transmedikakitui.models.bean.json.ResepObat;

import java.util.List;


public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.ContentViewHolder> {

    private final List<ResepObat> mList;

    public ResepAdapter(List<ResepObat> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(ItemObatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, final int position) {
        ResepObat resepObat = mList.get(position);
        holder.binding.tvNamaObat.setText(resepObat.getMedicinesName());
        holder.binding.tvJumlahObat.setText(String.valueOf(resepObat.getQty()).concat(" ".concat(resepObat.getUnit())));
        holder.binding.tvAturan.setText(resepObat.getRule());
        holder.binding.tvWaktu.setText(resepObat.getPeriod());
        holder.binding.tvCatatan.setText(resepObat.getNote());
    }


    public static class ContentViewHolder extends BaseBindingViewHolder<ItemObatBinding> {

        public ContentViewHolder(ItemObatBinding binding) {
            super(binding);
        }
    }
}
