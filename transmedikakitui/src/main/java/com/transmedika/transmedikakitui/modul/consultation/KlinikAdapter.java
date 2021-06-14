package com.transmedika.transmedikakitui.modul.consultation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseRecyclerViewAdapterPageAdapter;
import com.transmedika.transmedikakitui.databinding.ItemKlinikBinding;
import com.transmedika.transmedikakitui.models.bean.json.Clinic;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.ImageLoader;

import java.util.List;


public class KlinikAdapter extends BaseRecyclerViewAdapterPageAdapter {

    private final List<Clinic> mList;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public KlinikAdapter(Context mContext, List<Clinic> mList) {
        this.mList = mList;
        this.context = mContext;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    protected RecyclerView.ViewHolder contentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(ItemKlinikBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    protected String conditionLoading(int position) {
        Clinic klinik = mList.get(position);
        return String.valueOf(klinik.getId());
    }

    @Override
    protected void onErrorClick(int position, int holderPosition) {
        onItemClickListener.onItemCobaLagiClick(mList.get(position),holderPosition);
    }

    public boolean isPositionFooter(int position) {
        Clinic klinik = mList.get(position);
        return position == getItemCount() - 1 &&
                (klinik.getId().equals(Constants.LOADING_LONG) || klinik.getId().equals(Constants.ERROR_LONG));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        Clinic klinik = mList.get(position);
        if (holder instanceof ContentViewHolder) {
            ContentViewHolder cViewHolder = ((ContentViewHolder) holder);
            cViewHolder.binding.tvTitle.setText(klinik.getName());
            ImageLoader.loadIcon(context, klinik.getImage(), cViewHolder.binding.img, R.color.line_light);
            cViewHolder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(klinik, holder.getAdapterPosition()));
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Clinic klinik, int pos);
        void onItemCobaLagiClick(Clinic klinik, int pos);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ItemKlinikBinding binding;
        public ContentViewHolder(ItemKlinikBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
