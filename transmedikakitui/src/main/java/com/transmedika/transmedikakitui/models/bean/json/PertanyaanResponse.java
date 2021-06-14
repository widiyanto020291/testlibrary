package com.transmedika.transmedikakitui.models.bean.json;

import android.text.InputType;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Objects;

public class PertanyaanResponse {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TYPE_EDITTEXT, TYPE_RADIO_GROUP, TYPE_CHECK_GROUP, TYPE_TEXTAREA})
    public @interface TYPE_VIEW { }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_ANSWER_SINGLE, TYPE_ANSWER_MULTIPLE})
    public @interface TYPE_ANSWER { }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_REQUIRED_TRUE, TYPE_REQUIRED_FALSE})
    public @interface TYPE_REQUIRED { }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TYPE_INPUT_TEXT, TYPE_INPUT_DATE, TYPE_INPUT_NUMBER, TYPE_INPUT_EMAIL,
            TYPE_INPUT_PHONE, TYPE_INPUT_TIME, TYPE_INPUT_DATETIME, TYPE_INPUT_DECIMAL})
    public @interface TYPE_INPUT { }

    //Type View
    public static final String TYPE_EDITTEXT = "Edittext";
    public static final String TYPE_RADIO_GROUP = "RadioGroup";
    public static final String TYPE_CHECK_GROUP = "CheckGroup";
    public static final String TYPE_TEXTAREA = "Textarea";

    //Type Jawaban
    public static final int TYPE_ANSWER_SINGLE = 1;
    public static final int TYPE_ANSWER_MULTIPLE = 0;

    //Requeired
    public static final int TYPE_REQUIRED_TRUE = 1;
    public static final int TYPE_REQUIRED_FALSE = 0;

    //Type Input
    public static final String TYPE_INPUT_TEXT = "text";
    public static final String TYPE_INPUT_EMAIL = "email";
    public static final String TYPE_INPUT_PHONE = "phone";
    public static final String TYPE_INPUT_DATE = "date";
    public static final String TYPE_INPUT_TIME = "time";
    public static final String TYPE_INPUT_DATETIME = "datetime";
    public static final String TYPE_INPUT_DECIMAL = "decimal";
    public static final String TYPE_INPUT_NUMBER = "number";

    @SerializedName("id") private Long idPertanyaan;
    @SerializedName("question_text") private String pertanyaan;
    @SerializedName("component_type") private @TYPE_VIEW String tipeView;
    @SerializedName("detail") private List<Jawaban> jawabans;
    @SerializedName("required") private @TYPE_REQUIRED Integer mandatori;
    @SerializedName("is_single") private @TYPE_ANSWER Integer typeAnswer;
    @SerializedName("input_type") private @TYPE_INPUT String typeInput;
    @SerializedName("hint") private String hint;
    @SerializedName("max_length") private Integer maxLength;
    @SerializedName("format") private String format; //only datetime/time/date

    public Long getIdPertanyaan() {
        return idPertanyaan;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public @TYPE_VIEW String getTipeView() {
        return tipeView;
    }

    public List<Jawaban> getJawabans() {
        return jawabans;
    }

    public @TYPE_REQUIRED Integer getMandatori() {
        return mandatori;
    }

    public void setMandatori(Integer mandatori) {
        this.mandatori = mandatori;
    }

    public @TYPE_ANSWER Integer getTypeAnswer() {
        return typeAnswer;
    }

    public void setTypeAnswer(Integer typeAnswer) {
        this.typeAnswer = typeAnswer;
    }

    public @TYPE_INPUT String getTypeInput() {
        return typeInput;
    }

    public void setTypeInput(String typeInput) {
        this.typeInput = typeInput;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setIdPertanyaan(Long idPertanyaan) {
        this.idPertanyaan = idPertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public void setTipeView(String tipeView) {
        this.tipeView = tipeView;
    }

    public void setJawabans(List<Jawaban> jawabans) {
        this.jawabans = jawabans;
    }

    public int getInputTypeAndroid(){
        switch (typeInput) {
            case TYPE_INPUT_DATE:
            case TYPE_INPUT_DATETIME:
            case TYPE_INPUT_TIME:
                return InputType.TYPE_CLASS_DATETIME;
            case TYPE_INPUT_EMAIL:
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
            case TYPE_INPUT_NUMBER:
                return InputType.TYPE_CLASS_NUMBER;
            case TYPE_INPUT_DECIMAL:
                return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
            case TYPE_INPUT_PHONE:
                return InputType.TYPE_CLASS_PHONE;
            default:
                return InputType.TYPE_CLASS_TEXT;
        }
    }

    public boolean getMandadori(){
        return mandatori.equals(TYPE_REQUIRED_TRUE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PertanyaanResponse)) return false;
        PertanyaanResponse that = (PertanyaanResponse) o;
        return getIdPertanyaan().equals(that.getIdPertanyaan());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdPertanyaan());
    }

    public static class Jawaban{
        @SerializedName("id") private Long idJawaban;
        @SerializedName("label") private String jawaban;

        public Long getIdJawaban() {
            return idJawaban;
        }

        public void setIdJawaban(Long idJawaban) {
            this.idJawaban = idJawaban;
        }

        public String getJawaban() {
            return jawaban;
        }

        public void setJawaban(String jawaban) {
            this.jawaban = jawaban;
        }

        @Override
        public String toString() {
            return  jawaban;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Jawaban)) return false;
            Jawaban jawaban = (Jawaban) o;
            return getIdJawaban().equals(jawaban.getIdJawaban());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getIdJawaban());
        }
    }


}
