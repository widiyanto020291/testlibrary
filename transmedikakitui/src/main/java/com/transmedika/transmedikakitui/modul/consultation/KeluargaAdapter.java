package com.transmedika.transmedikakitui.modul.consultation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.databinding.ItemCommonBinding;
import com.transmedika.transmedikakitui.models.bean.json.Profile;

import java.util.List;


import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class KeluargaAdapter extends BaseAdapter {

    private final Context context;
    private final List<Profile> mList;

    public KeluargaAdapter(Context context, List<Profile> list) {
        this.context = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return (mList == null) ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ItemCommonBinding binding = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                binding = ItemCommonBinding.bind(inflater.inflate(R.layout.item_common, viewGroup, false));
            }
        } else {
            binding = ItemCommonBinding.bind(view);
        }

        if (binding != null) {
            binding.rlMain.setFocusable(false);
            binding.rlMain.setClickable(false);
            binding.tvName.setText(mList.get(position).getFullName());

            if (mList.get(position).getFullName() == null) {
                binding.tvName.setTextColor(context.getResources().getColor(R.color.gray1));
            } else {
                binding.tvName.setTextColor(context.getResources().getColor(R.color.textDefault));
            }
            view = binding.getRoot();
        }
        return view;
    }
}
