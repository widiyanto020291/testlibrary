package com.transmedika.transmedikakitui.modul.obat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.databinding.ItemCariObatBinding;
import com.transmedika.transmedikakitui.models.bean.json.Obat2;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import java.util.List;


public class HasilCariObatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Obat2> mList;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public HasilCariObatAdapter(Context mContext, List<Obat2> mList) {
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
        return new ContentViewHolder(ItemCariObatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ContentViewHolder) {
            Obat2 obat = mList.get(position);
            ContentViewHolder ccViewHolder = ((ContentViewHolder) holder);
            ccViewHolder.binding.llPlusMin.setVisibility(View.VISIBLE);
            ccViewHolder.binding.tvJumlah.setText(String.valueOf(obat.getJumlah()));

            ccViewHolder.binding.imgbMin.setOnClickListener(v -> {
                int jmlObat = obat.getJumlah();
                jmlObat--;
                obat.setJumlah(jmlObat);
                mList.set(holder.getAdapterPosition(),obat);
                if(jmlObat == 0){
                    ccViewHolder.binding.llPlusMin.setVisibility(View.GONE);
                    ccViewHolder.binding.tvJumlah.setText(String.valueOf(jmlObat));
                    onItemClickListener.onDelete(obat, holder.getAdapterPosition());
                }else {
                    ccViewHolder.binding.llPlusMin.setVisibility(View.VISIBLE);
                    ccViewHolder.binding.tvJumlah.setText(String.valueOf(jmlObat));
                    onItemClickListener.onMinusItemClick(obat, holder.getAdapterPosition());
                }
                ccViewHolder.binding.tvTotal.setText(TransmedikaUtils.numberFormat().format(obat.getPrice() * jmlObat));

                if(obat.getJumlah()!=0 && (obat.getJumlah() <= obat.getStock())){
                    obat.setPerbaharui(false);
                    onItemClickListener.onPerbaharuiMinus(obat, holder.getAdapterPosition());
                    ccViewHolder.binding.tvStok.setVisibility(View.GONE);
                }
            });

            ccViewHolder.binding.imgbPlus.setOnClickListener(v -> {
                int jmlObat = obat.getJumlah();
                jmlObat++;
                obat.setJumlah(jmlObat);
                mList.set(holder.getAdapterPosition(),obat);
                ccViewHolder.binding.llPlusMin.setVisibility(View.VISIBLE);
                ccViewHolder.binding.tvJumlah.setText(String.valueOf(jmlObat));
                onItemClickListener.onPlusItemClick(obat, holder.getAdapterPosition());
                ccViewHolder.binding.tvTotal.setText(TransmedikaUtils.numberFormat().format(obat.getPrice() * jmlObat));

                if(obat.getJumlah() > obat.getStock()){
                    onItemClickListener.onPerbaharuiPlus(obat, holder.getAdapterPosition());
                    ccViewHolder.binding.tvStok.setVisibility(View.VISIBLE);
                    ccViewHolder.binding.tvStok.setText(context.getString(R.string.stok).concat(String.valueOf(obat.getStock())));
                }
            });

            if(obat.getAvailable()) {
                if (obat.getStatus()!=null && !obat.getStatus()) {
                    ccViewHolder.binding.llPlusMin.setVisibility(View.GONE);
                    ccViewHolder.binding.tvTidakTersedia.setVisibility(View.VISIBLE);
                    ccViewHolder.binding.tvTidakTersedia.setText(R.string.resep_sudah_pernah_dibeli);
                    ccViewHolder.binding.tvName.setTextColor(ContextCompat.getColor(context,R.color.gray2));
                    ccViewHolder.binding.tvName.setText(obat.getName());
                    ccViewHolder.binding.imgClose.setVisibility(View.VISIBLE);
                    ccViewHolder.binding.tvTotal.setVisibility(View.GONE);
                } else {
                    ccViewHolder.binding.llPlusMin.setVisibility(View.VISIBLE);
                    ccViewHolder.binding.tvTidakTersedia.setVisibility(View.GONE);
                    ccViewHolder.binding.imgClose.setVisibility(View.GONE);
                    ccViewHolder.binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.textDefault));
                    ccViewHolder.binding.tvName.setText(obat.getName().concat(" @ ").concat(TransmedikaUtils.numberFormat().format(obat.getPrice())));
                    ccViewHolder.binding.tvTotal.setText(TransmedikaUtils.numberFormat().format(obat.getPrice() * obat.getJumlah()));
                    ccViewHolder.binding.tvTotal.setVisibility(View.VISIBLE);
                    if(obat.getPerbaharui()!=null){
                        if(obat.getPerbaharui()){
                            ccViewHolder.binding.tvStok.setVisibility(View.VISIBLE);
                            ccViewHolder.binding.tvStok.setText(context.getString(R.string.stok).concat(String.valueOf(obat.getStock())));
                        }else {
                            ccViewHolder.binding.tvStok.setVisibility(View.GONE);
                        }
                    }else {
                        ccViewHolder.binding.tvStok.setVisibility(View.GONE);
                    }
                }
            }else {
                ccViewHolder.binding.llPlusMin.setVisibility(View.GONE);
                ccViewHolder.binding.tvTidakTersedia.setVisibility(View.VISIBLE);
                ccViewHolder.binding.imgClose.setVisibility(View.VISIBLE);
                ccViewHolder.binding.tvTidakTersedia.setText(context.getString(R.string.tidak_tersedia));
                ccViewHolder.binding.tvName.setTextColor(ContextCompat.getColor(context,R.color.gray2));
                ccViewHolder.binding.tvName.setText(obat.getName());
                ccViewHolder.binding.tvTotal.setVisibility(View.GONE);
                ccViewHolder.binding.tvStok.setVisibility(View.GONE);
                ccViewHolder.binding.imgClose.setOnClickListener(v -> {
                    onItemClickListener.onDeleteTemp(obat, holder.getAdapterPosition());
                });
            }

            ccViewHolder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(obat, holder.getAdapterPosition()));
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Obat2 o, int pos);
        void onPlusItemClick(Obat2 o, int pos);
        void onMinusItemClick(Obat2 o, int pos);
        void onDelete(Obat2 o, int pos);
        void onPerbaharuiPlus(Obat2 o, int pos);
        void onPerbaharuiMinus(Obat2 o, int pos);
        void onDeleteTemp(Obat2 o, int pos);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ItemCariObatBinding binding;
        public ContentViewHolder(ItemCariObatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
