package com.transmedika.transmedikakitui.modul.consultation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.SimpleBindingActivity;
import com.transmedika.transmedikakitui.databinding.ActivityDetailSpaBinding;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.ICD;
import com.transmedika.transmedikakitui.models.bean.json.SpaPasien;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.DateUtil;
import com.transmedika.transmedikakitui.utils.ImageLoader;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

public class DetailSpaActivity extends SimpleBindingActivity<ActivityDetailSpaBinding> {
    @Override
    protected ActivityDetailSpaBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivityDetailSpaBinding.inflate(inflater);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        Bundle b = getIntent().getBundleExtra(Constants.DATA_USER);
        if(b!=null) {
            Doctor dokter = Objects.requireNonNull(b).getParcelable(Constants.DATA);
            //Konsultasi konsultasi = Objects.requireNonNull(b).getParcelable(Constants.DATA_KONSULTASI);
            //Profil profil = Objects.requireNonNull(b).getParcelable(Constants.DATA_PROFILE);
            String message = Objects.requireNonNull(b).getString(Constants.DATA_MSG);
            Date createdDate = (Date) b.getSerializable(Constants.DATA_DATE);


            binding.tvDokterNama.setText(dokter.getFullName());
            binding.tvDokterSpesialis.setText(dokter.getSpecialist());
            binding.tvDokterNoStr.setText(dokter.getNoStr());
            if (createdDate != null)
                binding.tvTglResep.setText(DateUtil.ddMMMyyyy(createdDate));
            ImageLoader.loadAll(mContext, dokter.getProfileDoctor(), binding.cvDokterProfile);
            if (message != null) {
                StringTokenizer st;
                int jml;
                SpaPasien spa = TransmedikaUtils.gsonBuilder().fromJson(message, SpaPasien.class);
                if (spa.getSpa().getSymptoms() != null) {
                    st = new StringTokenizer(spa.getSpa().getSymptoms(), "|");
                    jml = st.countTokens();
                    StringBuilder builder = new StringBuilder();
                    if (jml > 0) {
                        for (int i = 0; i < jml; i++) {
                            if (i == jml - 1) {
                                builder.append("- ".concat(st.nextToken()));
                            } else {
                                builder.append("- ".concat(st.nextToken().concat(System.lineSeparator())));
                            }
                        }

                        binding.tvSymptoms.setText(builder.toString());
                    }
                }
                if (spa.getSpa().getPossibleDiagnosis() != null) {
                    st = new StringTokenizer(spa.getSpa().getPossibleDiagnosis(), "|");
                    jml = st.countTokens();
                    StringBuilder builder = new StringBuilder();
                    if (jml > 0) {
                        for (int i = 0; i < jml; i++) {
                            if (i == jml - 1) {
                                builder.append("- ".concat(st.nextToken()));
                            } else {
                                builder.append("- ".concat(st.nextToken().concat(System.lineSeparator())));
                            }
                        }

                        binding.tvPosibleDiagnosis.setText(builder.toString());
                    }
                }
                if (spa.getSpa().getAdvice() != null) {
                    st = new StringTokenizer(spa.getSpa().getAdvice(), "|");
                    jml = st.countTokens();
                    StringBuilder builder = new StringBuilder();
                    if (jml > 0) {
                        for (int i = 0; i < jml; i++) {
                            if (i == jml - 1) {
                                builder.append("- ".concat(st.nextToken()));
                            } else {
                                builder.append("- ".concat(st.nextToken().concat(System.lineSeparator())));
                            }
                        }

                        binding.tvAdvice.setText(builder.toString());
                    }
                }
                List<ICD> icdList = spa.getSpa().getIcd();
                if (spa.getSpa().getIcd() != null) {
                    StringBuilder builder = new StringBuilder();
                    int advLength = icdList.size();
                    if (advLength > 0) {
                        binding.tvIcd.setVisibility(View.VISIBLE);
                        for (int i = 0; i < advLength ; i++) {
                            ICD icd = icdList.get(i);
                            if (i == advLength - 1) {
                                builder.append("- ".concat(icd.getName()));
                            }else {
                                builder.append("- ".concat(icd.getName().concat(System.lineSeparator())));
                            }
                        }
                        binding.tvIcd.setText(builder.toString());
                    } else {
                        binding.tvIcd.setVisibility(View.GONE);
                    }
                }
            }
        }
        setToolBar();
    }
    private void setToolBar(){
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.toolbar.setTitle(R.string.catatan_dokter);
    }
}
