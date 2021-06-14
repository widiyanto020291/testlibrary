package com.transmedika.transmedikakitui.modul.obat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseBindingFragment;
import com.transmedika.transmedikakitui.component.RxBus;
import com.transmedika.transmedikakitui.contract.obat.DetailObatContract;
import com.transmedika.transmedikakitui.databinding.FragmentDetailObatBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.json.KategoriObat;
import com.transmedika.transmedikakitui.models.bean.json.Obat;
import com.transmedika.transmedikakitui.models.events.BroadcastEvents;
import com.transmedika.transmedikakitui.presenter.obat.DetailObatPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.ImageLoader;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;
import com.transmedika.transmedikakitui.widget.NetkromTextView;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DetailObatFragment extends BaseBindingFragment<FragmentDetailObatBinding, DetailObatContract.View, DetailObatPresenter>
    implements DetailObatContract.View{

    private IObat iObat;
    private Obat obat;
    private KategoriObat kategoriObat;

    public static DetailObatFragment newInstance(Obat obat, KategoriObat kategoriObat) {

        Bundle args = new Bundle();

        DetailObatFragment fragment = new DetailObatFragment();
        args.putParcelable(Constants.DATA, obat);
        args.putParcelable(Constants.KATEGORI_OBAT, kategoriObat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        iObat = (IObat) context;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new DetailObatPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    protected DetailObatContract.View getBaseView() {
        return this;
    }

    @Override
    protected FragmentDetailObatBinding getViewBinding(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return FragmentDetailObatBinding.inflate(inflater);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        if(getArguments()!=null) {
            this.obat = getArguments().getParcelable(Constants.DATA);
            this.kategoriObat = getArguments().getParcelable(Constants.KATEGORI_OBAT);
            setData(this.obat);
            setToolBar();
            setUpListener();

            if(transmedikaSettings.getFontBold()!=null){
                binding.tvNamaObat.setCustomFont(mContext, transmedikaSettings.getFontBold());
            }
        }
    }

    private void setUpListener(){
        binding.imgbMin.setOnClickListener(v -> {
            int jmlObat = obat.getJumlah();
            jmlObat--;
            obat.setJumlah(jmlObat);
            if(jmlObat == 0){
                mPresenter.deleteObat(obat.getSlug());
                binding.imgbMin.setEnabled(false);
            }else {
                mPresenter.insertObat(obat);
                binding.imgbMin.setEnabled(true);
            }
            sendBroadcast(obat);
            binding.tvJumlah.setText(String.valueOf(jmlObat));
        });

        binding.imgbPlus.setOnClickListener(v -> {
            if(obat.getJumlah() >= 0) {
                int jmlObat = obat.getJumlah();
                jmlObat++;
                obat.setJumlah(jmlObat);
                mPresenter.insertObat(obat);
                binding.tvJumlah.setText(String.valueOf(jmlObat));
                sendBroadcast(obat);
            }
        });
    }

    private void sendBroadcast(Obat obat){
        BroadcastEvents.Event event = new BroadcastEvents.Event();
        event.setInitString(Constants.UPDATE_OBAT);
        event.setObject(obat);
        RxBus.getDefault().post(new BroadcastEvents(event));
        iObat.calculateObat();
    }

    private void setToolBar(){
        binding.toolbar.setNavigationOnClickListener(v -> mActivity.onBackPressed());
        binding.toolbar.setTitle(kategoriObat.getName()!=null ? kategoriObat.getName() : getString(R.string.detail_obat));
    }

    private void setData(Obat obat){
        binding.tvHarga.setText( TransmedikaUtils.numberFormat().format(obat.getMaxPrices()));
        binding.tvNamaObat.setText(obat.getName());
        if(obat.getJumlah()==null) {
            obat.setJumlah(0);
        }
        binding.tvJumlah.setText(String.valueOf(obat.getJumlah()));

        ImageLoader.load(mContext, obat.getImage(), binding.imgObat, R.color.line_light);

        for (Map.Entry<String, String> pair : obat.getDescription().entrySet()) {
            View viewV1 = View.inflate(mContext, R.layout.item_keterangan, null);
            NetkromTextView tvTitle = viewV1.findViewById(R.id.tv_title);
            NetkromTextView tvDesc = viewV1.findViewById(R.id.tv_desc);
            String key = pair.getKey();
            tvTitle.setText(key.substring(0, 1).toUpperCase().concat(key.substring(1)).replace("_", " "));
            tvDesc.setText(pair.getValue()!=null ? pair.getValue() : getString(R.string.strip));
            binding.llKet.addView(viewV1);
        }
    }
}
