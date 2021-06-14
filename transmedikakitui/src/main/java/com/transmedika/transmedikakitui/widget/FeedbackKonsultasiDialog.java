package com.transmedika.transmedikakitui.widget;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.databinding.FeedbackKonsultasiDialogBinding;
import com.transmedika.transmedikakitui.models.bean.json.param.FeedbackParam;

import java.util.ArrayList;
import java.util.List;


public class FeedbackKonsultasiDialog extends BottomSheetDialog implements View.OnClickListener {
    FeedbackKonsultasiDialogBinding binding;
    private DialogListener dialogListener;

    final private NetkromCheckBox[] stars;
    final private List<String> comment = new ArrayList<>();
    final private FeedbackParam param;

    public FeedbackKonsultasiDialog(@NonNull Context context) {
        super(context, R.style.AppBottomSheetDialogTheme);
        View view = View.inflate(context, R.layout.feedback_konsultasi_dialog, null);
        setContentView(view);
        binding = FeedbackKonsultasiDialogBinding.bind(view);
        stars = new NetkromCheckBox[]{binding.star1, binding.star2, binding.star3, binding.star4, binding.star5};
        for (NetkromCheckBox star : stars) {
            star.setOnClickListener(this);
        }
        param = new FeedbackParam();

        View.OnClickListener selected = v -> {
            NetkromTextView textView = ((NetkromTextView) v);
            int pos = comment.indexOf(textView.getText().toString());
            if (pos >= 0) {
                textView.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_alamat_default_2));
                textView.setTextColor(ContextCompat.getColor(context, R.color.textDefault));
                comment.remove(textView.getText().toString());
            } else {
                textView.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_alamat_selected));
                textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                comment.add(textView.getText().toString());
            }
            checkRequired();
        };

        binding.tvMudahcepat.setOnClickListener(selected);
        binding.tvBiayaTerjangkau.setOnClickListener(selected);
        binding.tvDokterProfesional.setOnClickListener(selected);

        binding.btnKonfirmasi.setOnClickListener(view1 -> {
            if (dialogListener != null) {
                param.setDescription(binding.edArea.getGetTextEditText());
                param.setComment(comment);
                dialogListener.onConfirmClick(param);
            }
        });
    }

    private void checkRequired() {
        binding.btnKonfirmasi.setEnabled(param.getRating() > 0 && comment.size() > 0);
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void onClick(View view) {
        boolean isCheck = true;
        for (int i = 0; i < stars.length; i++) {
            stars[i].setChecked(isCheck);
            if (stars[i].getId() == view.getId()) {
                param.setRating(i+1);
                isCheck = false;
            }
        }
        checkRequired();
    }

    public FeedbackParam getParam() {
        return param;
    }

    public interface DialogListener {
        void onConfirmClick(FeedbackParam feedbackParam);
    }
}
